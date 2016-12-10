package task;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import view.mainviewcontroller;

public class SingalTask implements Runnable {
	private Socket socket;
	private mainviewcontroller controller;

	public SingalTask(Socket socket, mainviewcontroller controller) {
		super();
		this.socket = socket;
		this.controller = controller;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream inputStream = null;
			while (true)
			{
				socket.setSoTimeout(100);
				inputStream = new DataInputStream(socket.getInputStream());
				String getmessage=inputStream.readUTF();		
				controller.setGetmessage(getmessage);
			}		
		} catch (SocketTimeoutException e) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		
	}

}
