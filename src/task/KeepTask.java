package task;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class KeepTask implements Runnable {
	private Socket socket;
	
	public KeepTask(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataOutputStream outputStream;
		while (true)
		{
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				outputStream = new DataOutputStream(socket.getOutputStream());
				outputStream.writeUTF("");
				outputStream.flush();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

}
