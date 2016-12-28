package task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import model.DataPack;
import view.mainviewcontroller;

public class SingalTask implements Runnable {
	private Socket socket;
	private mainviewcontroller controller;
	private String idd;
	private DataPack dp;
	public SingalTask(Socket socket, mainviewcontroller controller, String idd) {
		super();
		this.socket = socket;
		this.controller = controller;
		this.idd = idd;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream inputStream = null;
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(idd);
			outputStream.flush();
			while (true)
			{
				try {
					socket.setSoTimeout(100);
					inputStream = new ObjectInputStream(socket.getInputStream());
					try {
						dp = (DataPack) inputStream.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//panduan
					controller.setGetmessage(dp.getMsg());
				} catch (SocketTimeoutException e) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}				
			}		
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
}
