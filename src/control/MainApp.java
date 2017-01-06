package control;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ChoiceHolder;
import model.Person;
import task.DrawKeepTask;
import task.DrawReceiveTask;
import task.FileKeepTask;
import task.FileReceiveTask;
import task.KeepTask;
import task.SingalTask;
import view.addcontroller;
import view.documentcontroller;
import view.drawcontroller;
import view.editcontroller;
import view.jianquncontroller;
import view.logincontroller;
import view.mainviewcontroller;
import view.signincontroller;

public class MainApp extends Application {

	private FileKeepTask fTask;
	private String index;
	private int filenum = 0;
	private HashMap<String, File> fileMap = new HashMap<>();
	private Message2Service m;
	private drawcontroller controller;
	private Socket mSocket;
	private Socket dSocket;
	private Socket fSocket;
    private Stage primaryStage;
    private Pane rootLayout;
    private Person person;
    private ExecutorService cachedThreadPool;
    private ObservableList<Person> friendsData = FXCollections.observableArrayList();
    
    public void setFriendsData(ObservableList<Person> friendsData){
    	this.friendsData=friendsData;
    }
    public ObservableList<Person> getFriendsData() {
    	return friendsData;
    }
    public MainApp()
    {
    }
    
	public Socket getfSocket() {
		return fSocket;
	}
	public ExecutorService getCachedThreadPool() {
		return cachedThreadPool;
	}
	public void setfTask(FileKeepTask fTask) {
		this.fTask = fTask;
	}
	public FileKeepTask getfTask() {
		return fTask;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public Person getPerson() {
		return person;
	}
	public HashMap<String, File> getFileMap() {
		return fileMap;
	}
	public void setFile(String index, File file) {
		fileMap.put(index, file);
	}
	public int getFilenum() {
		return filenum;
	}
	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}
	public drawcontroller getController() {
		return controller;
	}
	public void setController(drawcontroller controller) {
		this.controller = controller;
	}

	private mainviewcontroller control_main_in_edit;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("登录");

        initRootLayout();

    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/login.fxml"));
            rootLayout = (Pane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            logincontroller controller=loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showsignin()
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/signin.fxml"));
			AnchorPane signpane = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage signStage = new Stage();
			signStage.setTitle("signin");
			signStage.initModality(Modality.WINDOW_MODAL);
			signStage.initOwner(primaryStage);
			Scene scene = new Scene(signpane);
			signStage.setScene(scene);
			// Set the person into the controller.
			signincontroller controller = loader.getController();
			controller.setStage(signStage);
			// Show the dialog and wait until the user closes it
			signStage.showAndWait();;
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showmainview(Person person)
    {
    	this.person=person;
    	try {
    		primaryStage.close();
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/mainview.fxml"));
            AnchorPane mainpane = (AnchorPane) loader.load();

            // Create the dialog Stage.
            //Stage primaryStage = new Stage();
            primaryStage.setTitle("main");
            Scene scene = new Scene(mainpane);
            primaryStage.setScene(scene);

            // Set the person into the controller.
            mainviewcontroller controller = loader.getController();
            controller.setMainStage(primaryStage);
            controller.setPerson(person);
            controller.setMainApp(this);
            control_main_in_edit=controller;
            try {
				mSocket = new Socket("115.28.67.141", 10240);
				controller.setSocket(mSocket);				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String idd=person.getId();
            cachedThreadPool = Executors.newCachedThreadPool();
            SingalTask st = new SingalTask(mSocket, controller,idd);
            KeepTask kt = new KeepTask(mSocket);
            cachedThreadPool.execute(st);
            cachedThreadPool.execute(kt);
            try {
            	dSocket = new Socket("115.28.67.141", 10241);			
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            ArrayList<Object> pack = new ArrayList<>();
            pack.add(person.getId());
            ObjectOutputStream out = new ObjectOutputStream(dSocket.getOutputStream());
            out.writeObject(pack);
            out.flush();
			DrawKeepTask dkp = new DrawKeepTask(dSocket);
			ChoiceHolder ch = new ChoiceHolder();
			Message3Service ms = new Message3Service(ch);
			m = new Message2Service();
			DrawReceiveTask drt = new DrawReceiveTask(dSocket, this, person.getId(), m, ms,ch);
			cachedThreadPool.execute(dkp);
			cachedThreadPool.execute(drt);
            /*
             * 
             */
			//fileMap.put(person.getId()+, value)
			try {
            	fSocket = new Socket("115.28.67.141", 10242);	
            	controller.setfSocket(fSocket);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            ArrayList<Object> pack1 = new ArrayList<>();
            pack1.add(person.getId());
            ObjectOutputStream out1 = new ObjectOutputStream(fSocket.getOutputStream());
            out1.writeObject(pack1);
            out1.flush();
			fTask = new FileKeepTask(fSocket);
			ChoiceHolder ch1 = new ChoiceHolder();
			Message3Service ms1 = new Message3Service(ch1);
			FileReceiveTask frt =  new FileReceiveTask(fileMap, fSocket, this, person.getId(), ms1, ch1);
			cachedThreadPool.execute(fTask);
			cachedThreadPool.execute(frt);
            // Show the dialog and wait until the user closes it
            primaryStage.show();
            
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	try {
    					Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
    					Statement stmt = connect.createStatement();
    					stmt.executeUpdate("update tongxun set online='offline' where id='"+idd+"'");
    					connect.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                	mSocket=null;
                	cachedThreadPool.shutdownNow();
                }});
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showqundocument()
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/document.fxml"));
			AnchorPane documentpane = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage documentStage = new Stage();
			documentStage.setTitle("群文件");
			Scene scene = new Scene(documentpane);
			documentStage.setScene(scene);
			// Set the person into the controller.
			documentcontroller controller = loader.getController();
			// Show the dialog and wait until the user closes it
			documentStage.showAndWait();;
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void filepre(String hisid)
    {
    	try {
			ArrayList<Object> data = new ArrayList<>();
			ObjectOutputStream outputStream = new ObjectOutputStream(fSocket.getOutputStream());
			data.add(-1);
			data.add(person.getId());
			data.add(hisid);
			outputStream.writeObject(data);
			outputStream.flush();
			System.out.println("-1 out");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    public void showdrawpre(String hisid)
    {
    	m.set("正在等待对方确认...");
    	try {
			ArrayList<Object> data = new ArrayList<>();
			ObjectOutputStream outputStream = new ObjectOutputStream(dSocket.getOutputStream());
			data.add(-1);
			data.add(person.getId());
			data.add(hisid);
			outputStream.writeObject(data);
			outputStream.flush();
			System.out.println("-1 out");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    
    public void showdraw(String hisid)
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/draw.fxml"));
			StackPane mainpane = (StackPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage drawStage = new Stage();

			// Set the person into the controller.
			controller = loader.getController();
			controller.setdSocket(dSocket);
			controller.setId(person.getId());
			controller.setHisid(hisid);
			controller.drawinit(drawStage,mainpane);
			// Show the dialog and wait until the user closes it
			
			drawStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					dSocket = null;
				}	
			});
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showeditview(Person person)
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/editinformation.fxml"));
			AnchorPane editpane = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage editStage = new Stage();
			editStage.setTitle("edit");
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(primaryStage);
			Scene scene = new Scene(editpane);
			editStage.setScene(scene);
			// Set the person into the controller.
			editcontroller controller = loader.getController();
			controller.setStage(editStage);
			controller.setPerson(person);
			controller.setMainApp(this);
			controller.getMainviewController(control_main_in_edit);
			// Show the dialog and wait until the user closes it
			editStage.showAndWait();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    	
    }
    public void showjianqunview(String id)
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/jianqun.fxml"));
			AnchorPane editpane = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage jianqunStage = new Stage();
			jianqunStage.setTitle("建群");
			Scene scene = new Scene(editpane);
			jianqunStage.setScene(scene);
			// Set the person into the controller.
			jianquncontroller controller = loader.getController();
			controller.setMainApp(this);
			controller.setId(person.getId());
			controller.setStage(jianqunStage);
			// Show the dialog and wait until the user closes it
			jianqunStage.show();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showaddview(Person person)
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/add.fxml"));
			AnchorPane addpane = (AnchorPane) loader.load();
			// Create the dialog Stage.
			//Stage primaryStage = new Stage();
			Stage addStage = new Stage();
			addStage.setTitle("add");
			addStage.initModality(Modality.WINDOW_MODAL);
			addStage.initOwner(primaryStage);
			Scene scene = new Scene(addpane);
			addStage.setScene(scene);
			// Set the person into the controller.
			addcontroller controller = loader.getController();
			controller.setPerson(person);
			controller.setMainApp(this);
			// Show the dialog and wait until the user closes it
			addStage.show();;
			
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void setPerson(Person person)
    {
    	this.person=person;
    }
}