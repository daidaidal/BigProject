package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class drawcontroller {
	@FXML
	private Button save;
	@FXML
	private Button huanyuan;
	@FXML
	private StackPane drawpane;
	
	private Stage drawStage;
	
	private static final int WIDTH = 598;  
    private static final int HEIGHT = 336;  
    private int brushSize = 8;  
    private Color color = Color.BLACK; 
    
    private Canvas canvas = new Canvas(WIDTH, HEIGHT);  
    private GraphicsContext gc = canvas.getGraphicsContext2D(); 
    private GraphicsContext gcs = gc;
    public EventHandler<MouseEvent> e=new EventHandler<MouseEvent>() {  
        @Override  
        public void handle(MouseEvent me) {  
            double px = me.getX() - brushSize / 2;  
            double py = me.getY() - brushSize / 2;  
            double pw = brushSize;  
            double ph = brushSize;  
            gc.save();  
            gc.setFill(color);  
            gc.fillOval(px, py, pw, ph);  
            gc.restore();
        }  
    };
	//public void setStage(Stage drawStage)
	//{
	//	this.drawStage=drawStage;
	//}
    
	public void drawinit(Stage drawStage,StackPane mainpane){
		this.drawStage=drawStage;
		//Canvas canvas = new Canvas(WIDTH, HEIGHT);  
        //GraphicsContext gc = canvas.getGraphicsContext2D();  
        canvas.setOnMouseDragged(e); 
        
        mainpane.getChildren().add(canvas);  
        Scene scene = new Scene(mainpane);
        drawStage.setTitle("画板");  
        drawStage.setScene(scene);  
        drawStage.show();  
        
	}
	
	@FXML
    private void initialize() {
    }
	
	@FXML
	private void handlesave()
	{
		gcs.save();
		gcs=gc;
		gcs.save();
	}
	
	@FXML
	private void handlehuanyuan()
	{
		gc.save();
		gc=gcs;
		gc.restore();
	}
	
	
	

}
