package view;

import java.io.File;

import control.MyRecord;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
	private ImageView iv;
	private Scene scene;
	private boolean voicejudge=false;
	private MyRecord cord;
	
	public EventHandler<KeyEvent> play=new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
        	System.out.println("start");
        	if(ke.getCharacter().equals("p"))
        	{
        		MyRecord cord1=new MyRecord();
        		cord1.play();
        	}
        		
        }
    };
    
	public EventHandler<KeyEvent> voice=new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
        	System.out.println("start");
        	if(ke.getCharacter().equals("v"))
        		if(voicejudge==false)
        			{
        						System.out.println("start");
        						cord=new MyRecord();
								cord.capture();
								voicejudge=true;
        			}
        		else if(voicejudge==true)
    			{
					System.out.println("stop");
					cord.stop();
					cord.save();
					voicejudge=false;
		}
        }
    };
    
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
            	if(count<number)
            	{	count++;
            	image = new Image("/image/"+String.valueOf(count)+".png");
            	iv.setImage(image);
            	pptpane.getChildren().remove(iv);
            	pptpane.getChildren().add(iv);
            	/*File f = new File("./src/image/"+String.valueOf(count)+".png");  // 输入要删除的文件位置
            	if(f.exists())
            	    f.delete();
            	    */
            	//pptStage.setScene(scene);
            	}
            }
            else if(me.getX()<300)
            {
            	if(count>1)
            		{count--;
            	image = new Image("/image/"+String.valueOf(count)+".png");
            	iv.setImage(image);
            	pptpane.getChildren().remove(iv);
            	pptpane.getChildren().add(iv);
            	pptStage.setScene(scene);}
            }
            }
    };
	public void init(String path,int number,StackPane pptpane,Stage pptStage)
	{
		this.pptpane=pptpane;
		this.pptStage=pptStage;
		
		image = new Image("/image/1.png");
	    iv = new ImageView();
        iv.setImage(image);
		this.number=number;
		
		pptpane.getChildren().add(iv);
		pptpane.addEventHandler(MouseEvent.MOUSE_CLICKED, change);
		//pptpane.addEventHandler(eventType, eventHandler);
		scene = new Scene(pptpane);
		scene.addEventHandler(KeyEvent.KEY_TYPED, voice);
		scene.addEventHandler(KeyEvent.KEY_TYPED, play);
		pptStage.setScene(scene);
		pptStage.show();
	}
}
