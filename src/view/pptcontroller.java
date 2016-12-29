package view;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class pptcontroller {
	@FXML
	private StackPane pptpane;
	private Stage pptStage;
	private int number;
	private int count=1;
	private Image image;
	@FXML
	private void initialize() {
    }
	
	public pptcontroller(){
	}
	public EventHandler<MouseEvent> change=new EventHandler<MouseEvent>() {  
        @Override  
        public void handle(MouseEvent me) {  
            if(me.getX()>300)
            {
            	count++;
            }
            
            
        }  
    };
	public void init(String path,int number,StackPane pptpane,Stage pptStage)
	{
		this.pptpane=pptpane;
		this.pptStage=pptStage;
		
		image = new Image("/image/1.png");
		ImageView iv = new ImageView();
        iv.setImage(image);
		this.number=number;
		
		pptpane.getChildren().add(iv);
		//pptpane.addEventHandler(eventType, eventHandler);
		Scene scene = new Scene(pptpane);
		pptStage.setScene(scene);
		pptStage.show();
	}
}
