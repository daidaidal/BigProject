package task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import control.MainApp;
import control.Message2Service;
import control.Message3Service;
import javafx.application.Platform;
import model.ChoiceHolder;

public class DrawReceiveTask implements Runnable {
	private Socket socket;
	private MainApp mApp;
	private String id;
	private Message2Service m;
	private Message3Service ms;
	private ChoiceHolder ch;

	public DrawReceiveTask(Socket socket, MainApp mApp, String id, Message2Service m, Message3Service ms,
			ChoiceHolder ch) {
		super();
		this.socket = socket;
		this.mApp = mApp;
		this.id = id;
		this.m = m;
		this.ms = ms;
		this.ch = ch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectInputStream inputStream = null;
		while(socket != null){
			try {
				socket.setSoTimeout(100);
				inputStream = new ObjectInputStream(socket.getInputStream());
				ArrayList<Object> data = (ArrayList<Object>) inputStream.readObject();
				Integer mode = (Integer) data.get(0);
				if (mode == -1) {			
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {					    	
							ms.set(2);
					    }
					});
					int choice = ch.get();
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
						Platform.runLater(new Runnable() {
						    @Override
						    public void run() {
								mApp.showdraw((String)data.get(1));
						    }
						});	
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
						Platform.runLater(new Runnable() {
						    @Override
						    public void run() {
						    	m.close();
								mApp.showdraw((String)data.get(1));
						    }
						});						
					}
					else if(choice.equals("n")){
						
					}
					
				}
				else if(mode == 1) {
					mApp.getController().get(
							(ArrayList<Double>)data.get(3), 
							(ArrayList<Double>)data.get(4), 
							(int[])data.get(5));
				}else if(mode == 2){
					
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
			if (Thread.interrupted()) break;
		}
	}

}
