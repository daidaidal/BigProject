package view;

import java.util.ArrayList;
import java.util.List;

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
	private Button erazer;
	@FXML
	private StackPane drawpane;

	
	private Stage drawStage;
	
	private static final int WIDTH = 400;  
    private static final int HEIGHT = 200;  
    private int brushSize = 8;  
    private Color color = Color.BLACK; 
    
    private Canvas canvas = new Canvas(WIDTH, HEIGHT);  
    private GraphicsContext gc = canvas.getGraphicsContext2D(); 
    private List<Double> x = new ArrayList<>();
    private List<Double> y=new ArrayList<>();
    private int judge=-1;
    
    public EventHandler<MouseEvent> paint1=new EventHandler<MouseEvent>() {  
        @Override  
        public void handle(MouseEvent me) {  
            double px = me.getX() - brushSize / 2;  
            double py = me.getY() - brushSize / 2;  
            double pw = brushSize;  
            double ph = brushSize;  
            //gc.save();  
            gc.setFill(color);  
            gc.fillOval(px, py, pw, ph);  
            //gc.restore();
            if(judge==-1)
            {
            	x.add(px);
            	y.add(py);
            }
           
        }  
    };
    
    public EventHandler<MouseEvent> erazer1=new EventHandler<MouseEvent>() {  
        @Override  
        public void handle(MouseEvent me) {  
            double px = me.getX() - brushSize / 2;  
            double py = me.getY() - brushSize / 2;  
            double pw = brushSize;  
            double ph = brushSize;  
            //gc.save();   
            gc.clearRect(px,py,pw,ph);
            //gc.restore();
        }  
    };
    
    public EventHandler<MouseEvent> send=new EventHandler<MouseEvent>() {  
        @Override  
        public void handle(MouseEvent me) {  
        	gc.clearRect(me.getX(),me.getY(),400,200);}
    };
    
    
	public void drawinit(Stage drawStage,StackPane mainpane){
		this.drawStage=drawStage;
		//Canvas canvas = new Canvas(WIDTH, HEIGHT);  
        //GraphicsContext gc = canvas.getGraphicsContext2D();  
        //canvas.setOnMouseDragged(paint1); 
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,paint1);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,send);
        
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
		if(judge==-1)
			judge=1;
		else if(judge==1)
			judge=-1;
	}
	
	@FXML
	private void handlehuanyuan()
	{
		for(int i=0;i<x.size();i++)
		{
			gc.setFill(color);  
            gc.fillOval(x.get(i), y.get(i),brushSize,brushSize); 
		}
	}
	@FXML
	private void deleteall()
	{
		gc.clearRect(200,100,400,200);
	}
	@FXML
	private void cachu()
	{
		canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED,paint1);
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,erazer1);
	}
	
	
	

}
