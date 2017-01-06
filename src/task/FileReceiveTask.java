package task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import control.MainApp;
import control.Message3Service;
import javafx.application.Platform;
import model.ChoiceHolder;
import model.FileData;

public class FileReceiveTask implements Runnable {
	private HashMap<String, FileData> fdMap = new HashMap<>();//file received from others
	private HashMap<String, File> fileMap;//file send to others
	private Socket socket;
	private MainApp mApp;
	private String id;
	private Message3Service ms;
	private ChoiceHolder ch;
	
	public FileReceiveTask(HashMap<String, File> fileMap, Socket socket, MainApp mApp, String id, Message3Service ms,
			ChoiceHolder ch) {
		super();
		this.fileMap = fileMap;
		this.socket = socket;
		this.mApp = mApp;
		this.id = id;
		this.ms = ms;
		this.ch = ch;
	}
	private void filereader(String name,String index){
		BufferedInputStream bufferinput = null;
		byte[] b = new byte[81920*4];
		String path = "c://bigproject" + "//" + index + "//";
		File f = new File(path);
		if (!f.exists()) f.mkdirs();
		path += name;
		RandomAccessFile rs = null;
		int haveread = 0;
		byte c = 'a';
		boolean isend = false;
		try {
			bufferinput = new BufferedInputStream(socket.getInputStream());	
			rs = new RandomAccessFile(path, "rw");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true){
			try {
				int readlen = bufferinput.read(b, 0, b.length);		
				if (readlen > 0){
					for (int i = 0;i < 10;i++){
						if (b[readlen-10+i] != c){ 
							c = 'a';
							break;
						}
						else{
							c++;
						}
						if (c == 'k')
							isend = true;
					}
					rs.seek(haveread);
					if (isend) readlen-=10;
					rs.write(b, 0, readlen);
				}				
				haveread += readlen;
				if (isend)					
					break;				
			} catch (SocketTimeoutException E){
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("file get");
	}
	@SuppressWarnings("unchecked")
	private void reader(ObjectInputStream inputStream){
		try {
			ArrayList<Object> data = (ArrayList<Object>) inputStream.readObject();	
			Integer mode = (Integer) data.get(0);
			if (mode == -1) {	
				System.out.println("-1 get");
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {					    	
						ms.set(3);
				    }
				});
				int choice = ch.get();
				ch = new ChoiceHolder();
				ms = new Message3Service(ch);
				System.out.println("choice get");
				ArrayList<Object> pack = new ArrayList<>();
				pack.add(-2);
				pack.add(id);
				pack.add(data.get(1));
				if (choice == 1){						
					pack.add("y");
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(pack);
					out.flush();
					System.out.println("-2 sent");
				}
				else if(choice == 0){
					pack.add("n");
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(pack);
					out.flush();
				}
			}
			else if(mode == -2){
				System.out.println("-2 get");
				String choice = (String)data.get(3);
				if (choice.equals("y")){
					mApp.getfTask().setSwitcher(0);
					String index = mApp.getIndex();
					File file = fileMap.get(index);
					ArrayList<Object> pack0 = new ArrayList<>();
					pack0.add(1);
					pack0.add(id);
					pack0.add(data.get(1));
					pack0.add(index);
					pack0.add(file.getName());
					pack0.add(file.length());
					pack0.add(1);
					try {
						ObjectOutputStream out0 = new ObjectOutputStream(socket.getOutputStream());
						out0.writeObject(pack0);
						out0.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte [] b = new byte[40960]; 
					long times = file.length() / 40960;
					long left = file.length() % 40960;
					byte [] b_left = new byte[(int)left+10];
					if (left != 0)
						times++;	
					try {
						RandomAccessFile ra = new RandomAccessFile(file, "r");
						for (int i = 0; i < times;i++){
							if (i != times - 1){
								ra.seek(i * 40960);
								ra.read(b);
								BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
								outputStream.write(b);
								outputStream.flush();
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}									
							else {
								ra.seek(i * 40960);
								int temp = ra.read(b_left);
								byte c = 'a';
								for (int j = 0;j < 10;j++){
									b_left[temp + j] = c;
									c++;
								}
								BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
								outputStream.write(b_left);
								outputStream.flush();
							}	
						}
						ra.close();
						System.out.println("file out");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						FileKeepTask fTask = new FileKeepTask(socket);
						mApp.setfTask(fTask);
						mApp.getCachedThreadPool().execute(fTask);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(choice.equals("n")){
					
				}
				
			}
			else if(mode == 1) {
				String index = (String) data.get(3);
				String name = (String) data.get(4);
				long flen = (long)data.get(5);
				FileData fd = new FileData(name, flen, false, 
						(String)data.get(1) + "to" + (String)data.get(2), null);
				fdMap.put(index, fd);
				System.out.println("1 get");
				filereader(name, index);
			}
			else if(mode == 2){
				System.out.println("2 get");
				String index = (String) data.get(3);
				int order = (int) data.get(4);
				byte [] b = (byte[]) data.get(5);
				FileData fd = fdMap.get(index);
				fd.setCreated(true);
				String path = "c://bigproject" + "//" + index + "//";
				File f = new File(path);
				if (!f.exists()) f.mkdirs();
				path += fd.getFilename();
				RandomAccessFile rs = new RandomAccessFile(path, "rw");
				rs.seek(order * 40960);
				rs.write(b);
				rs.close();
			}
			else if(mode == 3){
				
			}
		} catch (SocketTimeoutException e){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectInputStream inputStream;
		while(socket != null){			
			try {
				socket.setSoTimeout(100);
				inputStream = new ObjectInputStream(socket.getInputStream());
				reader(inputStream);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Thread.interrupted()) break;
		}
	}

}
