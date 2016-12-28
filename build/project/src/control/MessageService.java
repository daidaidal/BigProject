package control;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.messagecontroller;

public class MessageService {
	public void showmessage(String message)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MessageService.class.getResource("/view/message.fxml"));
			AnchorPane m = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage ms = new Stage();
			ms.setTitle("message");
			Scene scene = new Scene(m);
			ms.setScene(scene);
			// Set the person into the controller.
			messagecontroller controller = loader.getController();
			controller.setMessage(message);
			// Show the dialog and wait until the user closes it
			ms.show();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}
