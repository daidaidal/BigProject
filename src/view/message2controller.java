package view;


import javafx.fxml.FXML;
import javafx.scene.text.Text;

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
