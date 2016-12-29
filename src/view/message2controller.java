package view;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class message2controller {
	@FXML
	private Text message;
	@FXML
	private void initialize() {
    }


	public void setMessage(String message)
	{
		this.message.setText(message);
	}

}
