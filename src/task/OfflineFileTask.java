package task;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;

import control.MainApp;
import view.mainviewcontroller;

public class OfflineFileTask implements Runnable {
	private File file;
	private Socket fSocket;
	private MainApp mainapp;
	private mainviewcontroller mainviewcontroller;
	
	public OfflineFileTask(File file, Socket fSocket, MainApp mainapp, view.mainviewcontroller mainviewcontroller) {
		super();
		this.file = file;
		this.fSocket = fSocket;
		this.mainapp = mainapp;
		this.mainviewcontroller = mainviewcontroller;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (file == null) return;
		String id = mainapp.getPerson().getId();
		String hisid = mainviewcontroller.getFriendsidLabel().getText();
		String online = mainviewcontroller.getFriendsonlineLabel().getText();
		String index = id + "to" + hisid + "file" + mainapp.getFilenum();
		if (online.equals("online")){
			mainapp.filepre(hisid);				
			mainapp.setIndex(index);
			mainapp.setFilenum(mainapp.getFilenum() + 1);
			mainapp.setFile(index, file);
		}
		else if(online.equals("offline")){
			mainapp.getfTask().setSwitcher(0);
			ArrayList<Object> pack0 = new ArrayList<>();
			pack0.add(1);
			pack0.add(id);
			pack0.add(hisid);
			pack0.add(index);
			pack0.add(file.getName());
			pack0.add(file.length());
			pack0.add(0);
			try {
				ObjectOutputStream out0 = new ObjectOutputStream(fSocket.getOutputStream());
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
						BufferedOutputStream outputStream = new BufferedOutputStream(fSocket.getOutputStream());
						outputStream.write(b);
						outputStream.flush();
						try {
							Thread.sleep(100);
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
						BufferedOutputStream outputStream = new BufferedOutputStream(fSocket.getOutputStream());
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
				FileKeepTask fTask = new FileKeepTask(fSocket);
				mainapp.setfTask(fTask);
				mainapp.getCachedThreadPool().execute(fTask);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
