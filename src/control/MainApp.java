package control;
import view.editcontroller;
import view.logincontroller;
import view.mainviewcontroller;
import view.signincontroller;
import model.Person;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private Pane rootLayout;
    private Person person;
    
    private ObservableList<Person> friendsData = FXCollections.observableArrayList();
    public ObservableList<Person> getFriendsData() {
    	return friendsData;
    }
    public MainApp()
    {
    	//id name company email
    	friendsData.add(new Person("2","f","apple","a"));
    	friendsData.add(new Person("3","s","google","g"));
    	friendsData.add(new Person("4","t","microsoft","m"));
    	friendsData.add(new Person("5","f","sansung","s"));
    	friendsData.add(new Person("6","f","sony","s"));
    }
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

            // Show the dialog and wait until the user closes it
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showeditview(Person person)
    {
    	try {
			FXMLLoader loader = new FXMLLoader();
			System.out.println(MainApp.class.getResource("../view/editinformation.fxml"));
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
			// Show the dialog and wait until the user closes it
			editStage.showAndWait();;
			
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