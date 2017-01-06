package task;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class FileKeepTask implements Runnable {
	private Socket socket;
	private int switcher;
	
	public int getSwitcher() {
		return switcher;
	}

	public void setSwitcher(int switcher) {
		this.switcher = switcher;
	}

	public FileKeepTask(Socket socket) {
		super();
		this.socket = socket;
		switcher = 1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ObjectOutputStream outputStream;
		while (switcher == 1)
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
