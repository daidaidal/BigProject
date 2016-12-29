package control;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.messagecontroller;

public class MessageService {
	public void set(int judge){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MessageService.class.getResource("/view/message.fxml"));
			AnchorPane m = (AnchorPane) loader.load();
			
			Stage ms = new Stage();
			ms.setTitle("message");
			Scene scene = new Scene(m);
			ms.setScene(scene);
			
			
			messagecontroller controller = loader.getController();
			//密码错误 1
			controller.setStage(ms);
			controller.setJudge(judge);
			ms.show();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

}
