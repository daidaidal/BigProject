package control;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.message2controller;

public class Message2Service {
	private Stage ms;
	public void set(String message)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Message2Service.class.getResource("/view/message2.fxml"));
			StackPane m = (StackPane) loader.load();
					
			ms = new Stage();
			ms.setTitle("message");
			Scene scene = new Scene(m);
			ms.setScene(scene);
					
					
			message2controller controller = loader.getController();
			controller.setMessage(message);
			ms.show();
		}
		catch (IOException e) {
            e.printStackTrace();
        }
	}
	public void close()
	{
		ms.close();
	}
}
