package view;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javafx.scene.input.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import control.MyRecord;

public class drawcontroller {
	@FXML
	private Button save;
	@FXML
	private Button huanyuan;
	@FXML
	private Button erazer;
	@FXML
	private StackPane drawpane;

	private boolean voicejudge;
	private Stage drawStage;
	private MyRecord cord;

	private static final int WIDTH = 600;
    private static final int HEIGHT = 350;
    private int brushSize = 8;
    private Color color = Color.BLACK;

    private Canvas canvas = new Canvas(WIDTH, HEIGHT);


    private GraphicsContext gc = canvas.getGraphicsContext2D(); 

    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y=new ArrayList<>();
    private int[] temp1={0,1,1,brushSize};

    
    private int judge=-1;
    private int ca_xie=1;
    
    private Socket dSocket;
    private String id;
    private String hisid;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHisid() {
		return hisid;
	}
	public void setHisid(String hisid) {
		this.hisid = hisid;
	}
	public Socket getdSocket() {
		return dSocket;
	}
	public void setdSocket(Socket dSocket) {
		this.dSocket = dSocket;
	}

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
        	try {
				ObjectOutputStream out = new ObjectOutputStream(dSocket.getOutputStream());
				ArrayList<Object> pack = new ArrayList<>();
				pack.add(1);
				pack.add(id);
				pack.add(hisid);
				pack.add(x);
				pack.add(y);
				pack.add(temp1);
				out.writeObject(pack);
				out.flush();
				System.out.println("1 out");
				x.clear();
				y.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
    };

    public EventHandler<KeyEvent> voice=new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
        	if(ke.getCharacter()=="v")
        		if(voicejudge==false)
        			{
								cord.capture();
								voicejudge=true;
							}
        		else if(voicejudge==true)
        			{
								cord.stop();
								cord.save();
								voicejudge=false;
							}
        }
    };
    public void get(List<Double> tx,List<Double> ty,int temp[])
    {
    	//temp 鏁扮粍 double绫诲瀷
    	//temp[0] 鏄惁鍏ㄩ儴娓呴櫎 y:1 n:0
    	//temp[1] 鏄啓杩樻槸鎿﹂櫎 鍐�:1 鎿�:0
    	//temp[2] 棰滆壊 1 2 3 4 5 6 鍏棰滆壊
    	//temp[3] 绮楃粏 double 绫诲瀷
    	if(temp[0]==1)
    		gc.clearRect(0,0,1000,1000);
    	else
    	{
    		if(temp[1]==1) //鍐�
    		{
    			Color colort = null;
    			if(temp[2]==1)
    				colort=Color.rgb(0, 0, 0, 1);
    			else if (temp[2]==2)
    				colort=Color.rgb(135, 206, 250, 1);
    			else if (temp[2]==3)
    				colort=Color.rgb(154, 255, 154, 1);
    			else if (temp[2]==4)
    				colort=Color.rgb(255, 106, 106, 1);
    			else if (temp[2]==5)
    				colort=Color.rgb(238, 130, 238, 1);
    			else if (temp[2]==6)
    				colort=Color.rgb(255, 255, 0, 1);
    			for(int i=0;i<tx.size();i++)
    			{
    				gc.setFill(colort);
    	            gc.fillOval(tx.get(i), ty.get(i),temp[3],temp[3]);
    			}
    		}
    		else if(temp[1]==0) //鎿�
    		{
    			for(int i=0;i<tx.size();i++)
    	            gc.clearRect(tx.get(i), ty.get(i),temp[3],temp[3]);
    		}
    	}

    }
	public void drawinit(Stage drawStage,StackPane mainpane){
		this.drawStage=drawStage;
		//Canvas canvas = new Canvas(WIDTH, HEIGHT);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        //canvas.setOnMouseDragged(paint1);
		voicejudge=false;
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,paint1);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,send);
        canvas.addEventHandler(KeyEvent.KEY_TYPED, voice);
      //鎸変綇A閿拰F閿垎鍒缉灏忓拰澧炲ぇ鐢荤瑪灏哄
        mainpane.getChildren().add(canvas);
        Scene scene = new Scene(mainpane);
        drawStage.setTitle("鐢绘澘");
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
		gc.clearRect(0,0,1000,1000);
		temp1[0]=1;
	}
	@FXML
	private void cachu()
	{
		if(ca_xie==1)
		{
			canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED,paint1);
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,erazer1);
			ca_xie=-1;
			temp1[1]=0;
		}
		else if(ca_xie==-1)
		{
			canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED,erazer1);
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,paint1);
			ca_xie=1;
			temp1[1]=1;
		}
	}
	@FXML
	private void handlecu()
	{
		brushSize++;
		temp1[3]=brushSize;
	}
	@FXML
	private void handlexi()
	{
		if(brushSize>0)
		brushSize--;
		temp1[3]=brushSize;
	}
	@FXML
	private void colour1() //榛戣壊
	{
        color = Color.rgb(0, 0, 0, 1);
        temp1[2]=1;
	}

	@FXML
	private void colour2() //LightSkyBlue	135 206 250
	{
        color = Color.rgb(135, 206, 250, 1);
        temp1[2]=2;
	}

	@FXML
	private void colour3() //PaleGreen1	154 255 154
	{
        color = Color.rgb(154, 255, 154, 1);
        temp1[2]=3;
	}

	@FXML
	private void colour4() //IndianRed1	255 106 106
	{
        color = Color.rgb(255, 106, 106, 1);
        temp1[2]=4;
	}

	@FXML
	private void colour5() //Violet	238 130 238
	{
        color = Color.rgb(238, 130, 238, 1);
        temp1[2]=5;
	}


	@FXML
	private void colour6() //Yellow	255 255 0
	{
        color = Color.rgb(255, 255, 0, 1);
        temp1[2]=6;
	}


}
