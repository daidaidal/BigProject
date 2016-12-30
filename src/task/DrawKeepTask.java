package task;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DrawKeepTask implements Runnable {
	private Socket socket;
	private String id;
	
	public DrawKeepTask(Socket socket, String id) {
		super();
		this.socket = socket;
		this.id = id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectOutputStream outputStream;
		while (socket != null)
		{
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList<Object> data = new ArrayList<>();
				data.add(0);
				data.add(id);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(data);
				outputStream.flush();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Thread.interrupted()) break;
		}
	}

}
