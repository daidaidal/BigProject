package control;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import model.Person;
import task.DrawKeepTask;
import task.KeepTask;
import task.SingalTask;
import view.addcontroller;
import view.drawcontroller;
import view.editcontroller;
import view.jianquncontroller;
import view.logincontroller;
import view.mainviewcontroller;
import view.signincontroller;

public class MainApp extends Application {

	private Socket mSocket;
	private Socket dSocket;
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
    public void showdrawpre(String hisid)
    {
    	Message2Service m=new Message2Service();
    	m.set("正在等待对方确认...");
    	try {
			dSocket = new Socket("115.28.67.141", 10241);
			DrawKeepTask dkp = new DrawKeepTask(dSocket, person.getId());
			cachedThreadPool.execute(dkp);
			ArrayList<Object> data = new ArrayList<>();
			ObjectOutputStream outputStream = new ObjectOutputStream(dSocket.getOutputStream());
			data.add(-1);
			data.add(person.getId());
			data.add(hisid);
			outputStream.writeObject(data);
			outputStream.flush();
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
			drawcontroller controller = loader.getController();
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