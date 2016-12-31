package control;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ChoiceHolder;
import view.messagecontroller;

public class Message3Service {
	private messagecontroller controller;
	private ChoiceHolder ch;
		
	public Message3Service(ChoiceHolder ch) {
		super();
		this.ch = ch;
	}

	public messagecontroller getController() {
		return controller;
	}

	public void setController(messagecontroller controller) {
		this.controller = controller;
	}

	public void set(int judge){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MessageService.class.getResource("/view/message.fxml"));
			AnchorPane m = (AnchorPane) loader.load();
			
			Stage ms = new Stage();
			ms.setTitle("message");
			Scene scene = new Scene(m);
			ms.setScene(scene);
			
			
			controller = loader.getController();
			//密码错误 1
			controller.setStage(ms);
			controller.setJudge(judge);
			controller.setCh(ch);
			ms.show();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}
