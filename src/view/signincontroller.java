package view;

import control.SigninService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class signincontroller {
	@FXML
	private TextField nameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label idLabel;
	
	private SigninService sign=null;
	private Stage signStage;
	private String id;
	public signincontroller(){
	}
	public void setStage(Stage signStage)
	{
		this.signStage=signStage;
	}
	@FXML
    private void initialize() {
		sign=new SigninService();
		id=sign.signin();
		idLabel.setText(id);
    }
	@FXML
	private void handleOk()
	{
		String name=nameField.getText();
		String password=passwordField.getText();
		sign.afterSignin(name, password);
		signStage.close();
	}
	
}
