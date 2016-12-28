package view;

import control.SigninService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class messagecontroller {
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
