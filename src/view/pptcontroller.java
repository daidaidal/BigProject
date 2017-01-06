package view;

import java.io.File;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;

import control.MainApp;
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
import task.FileKeepTask;

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
	private MainApp mainApp;
	private String hisid;
	private boolean judge=false;
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
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
					File file = new File("./src/record/1.mp3");
					filesender(file, 1,"0");
					File f = new File("./src/record/1.mp3");  // 输入要删除的文件位置
	            	if(f.exists())
	            	    f.delete();
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
            	{	
	            	count++;
	            	image = new Image("/image/"+String.valueOf(count)+".png");
	            	iv.setImage(image);
	            	pptpane.getChildren().remove(iv);
	            	pptpane.getChildren().add(iv);           	
	              	File file = new File("./src/image/"+String.valueOf(count)+".png");
	            	filesender(file,0,String.valueOf(count));      	
            	//pptStage.setScene(scene);
            	}
            }
            else if(me.getX()<300)
            {
            	if(count>1) {
	            	count--;
	            	image = new Image("/image/"+String.valueOf(count)+".png");
	            	iv.setImage(image);
	            	pptpane.getChildren().remove(iv);
	            	pptpane.getChildren().add(iv);
	            	pptStage.setScene(scene);
	            	File file = new File("./src/image/"+String.valueOf(count)+".png");
	            	filesender(file,0,String.valueOf(count));
            	}
            }
        }
    };
    private void filesender(File file,int voi,String count){
    	Socket fSocket = mainApp.getfSocket();
    	mainApp.getfTask().setSwitcher(0);
		ArrayList<Object> pack0 = new ArrayList<>();
		pack0.add(2);
		pack0.add(mainApp.getPerson().getId());
		pack0.add(hisid);
		pack0.add(count);
		pack0.add(file.getName());
		pack0.add(voi);
		try {
			ObjectOutputStream out0 = new ObjectOutputStream(fSocket.getOutputStream());
			out0.writeObject(pack0);
			out0.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte [] b = new byte[40960]; 
		long times = file.length() / 40960;
		long left = file.length() % 40960;
		byte [] b_left = new byte[(int)left+10];
		if (left != 0)
			times++;	
		try {
			RandomAccessFile ra = new RandomAccessFile(file, "r");
			for (int i = 0; i < times;i++){
				if (i != times - 1){
					ra.seek(i * 40960);
					ra.read(b);
					BufferedOutputStream outputStream = new BufferedOutputStream(fSocket.getOutputStream());
					outputStream.write(b);
					outputStream.flush();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}									
				else {
					ra.seek(i * 40960);
					int temp = ra.read(b_left);
					byte c = 'a';
					for (int j = 0;j < 10;j++){
						b_left[temp + j] = c;
						c++;
					}
					BufferedOutputStream outputStream = new BufferedOutputStream(fSocket.getOutputStream());
					outputStream.write(b_left);
					outputStream.flush();
				}	
			}
			ra.close();
			System.out.println("file out");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileKeepTask fTask = new FileKeepTask(fSocket);
			mainApp.setfTask(fTask);
			mainApp.getCachedThreadPool().execute(fTask);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void setpptpicture(String count)
    {
    	System.out.println("count:"+count);
    	image = new Image("/image/"+count+".png");
    	pptpane.getChildren().remove(iv);
    	iv.setImage(image);
    	pptpane.getChildren().add(iv);
    }
	public void init(String path,int number,StackPane pptpane,Stage pptStage,String hisid,boolean judge)
	{
		//调试ppt
		System.out.println("init");
		this.pptpane=pptpane;
		this.pptStage=pptStage;
		this.hisid = hisid;
		image = new Image("/image/1.png");
	    iv = new ImageView();
        iv.setImage(image);
		this.number=number;
		File file = new File("./src/image/1.png");
		filesender(file,0,"1");

		pptpane.getChildren().add(iv);
		if(judge==false)
			pptpane.addEventHandler(MouseEvent.MOUSE_CLICKED, change);
		//pptpane.addEventHandler(eventType, eventHandler);
		scene = new Scene(pptpane);
		if(judge==false)
		{
			scene.addEventHandler(KeyEvent.KEY_TYPED, voice);
			scene.addEventHandler(KeyEvent.KEY_TYPED, play);
		}
		pptStage.setScene(scene);
		pptStage.show();
		//调试ppt
		System.out.println("after init");
	}
}
