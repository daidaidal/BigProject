package task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import view.mainviewcontroller;

public class SingalTask implements Runnable {
	private Socket socket;
	private mainviewcontroller controller;
	private String idd;
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
			DataInputStream inputStream = null;
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(idd);
			outputStream.flush();
			while (socket != null)
			{
				try {
					socket.setSoTimeout(100);
					inputStream = new DataInputStream(socket.getInputStream());
					String getmessage=inputStream.readUTF();		
					controller.setGetmessage(getmessage);
				} catch (SocketTimeoutException e) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
				if (Thread.interrupted()) break;
			}		
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
}
