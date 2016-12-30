package view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class messagecontroller {
	@FXML
	private Text message;
	@FXML
	private void initialize() {
    }
	private  int judge;
	private Stage ms;
	public void setStage(Stage ms)
	{
		this.ms=ms;
	}
	public void setJudge(int judge)
	{
		this.judge=judge;
		if(judge==1)
			message.setText("密码错误");
	}
	@FXML
	private void handleok()
	{
		if(judge==1)
			ms.close();
	}
	@FXML
	private void handlecancle()
	{
		if(judge==1)
			ms.close();
	}

}
