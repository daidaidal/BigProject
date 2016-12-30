package task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import view.drawcontroller;

public class DrawReceiveTask implements Runnable {
	private Socket socket;
	private drawcontroller controller;
	
	public DrawReceiveTask(Socket socket, drawcontroller controller) {
		super();
		this.socket = socket;
		this.controller = controller;
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
					
				}
				else if(mode == 1) {
					controller.get(
							(ArrayList<Double>)data.get(3), 
							(ArrayList<Double>)data.get(4), 
							(int[])data.get(5));
				}else if(mode == 2){
					
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
