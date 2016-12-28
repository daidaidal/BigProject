package task;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.DataPack;

public class KeepTask implements Runnable {
	private Socket socket;
	
	public KeepTask(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectOutputStream outputStream;
		while (true)
		{
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataPack dp = new DataPack(0, "", null, null);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(dp);
				outputStream.flush();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

}
