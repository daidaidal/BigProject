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
	private int choice = -1;
	
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public void setStage(Stage ms)
	{
		this.ms=ms;
	}
	public void setJudge(int judge)
	{
		this.judge=judge;
		if(judge==1)
			message.setText("密码错误");
		else if(judge == 2){
			message.setText("白板请求");
		}
	}
	@FXML
	private void handleok()
	{
		if(judge==1)
			ms.close();
		else if(judge==2){
			setChoice(1);
			ms.close();
		}
			
	}
	@FXML
	private void handlecancle()
	{
		if(judge==1)
			ms.close();
		else if (judge==2){
			setChoice(-1);
			ms.close();
		}
	}

}
