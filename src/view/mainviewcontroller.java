package view;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import control.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Person;

public class mainviewcontroller {
	@FXML
    private TableView<Person> friendsTable;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> companyColumn;
    
	@FXML
	private Text nameText;
	@FXML
	private Text companyText;
	@FXML
	private Text emailText;
	
	@FXML
	private Label friendsnameLabel;
	@FXML
	private Label friendscompanyLabel;
	@FXML
	private Label friendsemailLabel;
	@FXML
	private Label friendsidLabel;
	@FXML
	private TextArea sendArea;
	@FXML
	private TextArea showArea;
	
	private Person person;
	private Stage mainStage;
	private MainApp mainapp;
	private Socket socket;
	
	public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
       
    }
	public void setSocket(Socket s)
	{
		this.socket=s;
	}
	public void setPerson(Person person) {  
		this.person=person;
		nameText.setText(person.getName());
		companyText.setText(person.getCompany());
		emailText.setText(person.getEmail());
	}
	
	public void setMainApp(MainApp mainapp)
	{
		this.mainapp=mainapp;
	     // Add observable list data to the table
        friendsTable.setItems(mainapp.getFriendsData());
	}
	@FXML
	private void initialize(){
		// Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
        companyColumn.setCellValueFactory(
        		cellData -> cellData.getValue().companyProperty());
        
        // Clear person details.
        showFriendsDetails(null);

        // Listen for selection changes and show the person details when changed.
		friendsTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showFriendsDetails(newValue));
		
	}
	
	private void showFriendsDetails(Person person) {
    	if (person != null) {
    		// Fill the labels with info from the person object.
    		String name=person.getName();
    		String company=person.getCompany();
    		String email=person.getEmail();
    		String id=person.getId();
    		friendsnameLabel.setText(name);
    		friendscompanyLabel.setText(company);
    		friendsemailLabel.setText(email);
    		friendsidLabel.setText(id);
    	
    		
    	} else {
    		// Person is null, remove all the text.
    		friendsnameLabel.setText("");
    		friendscompanyLabel.setText("");
    		friendsemailLabel.setText("");
    	}
    }
	public void setGetmessage(String getmessage)
	{
		showArea.appendText(getmessage+"/n");
	}
	
	@FXML
	private void handleEdit()
	{
		mainapp.showeditview(person);
	}
	@FXML
	private void handleAdd()
	{
		mainapp.showaddview(person);
	}
	@FXML
	private void handlesend()
	{
		String ip=null;
		System.out.println();
			try {
				Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
				Statement stmt = connect.createStatement();
				ResultSet rs = stmt.executeQuery("select * from tongxun where id='" + friendsidLabel.getText() + "'");
				if (rs.next()) {
					 ip=rs.getString("ip");
				}
				connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String send=null;
		send=ip+"^&^"+sendArea.getText();
		sendArea.setText("");
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(send);
			out.flush();
			out.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showArea.appendText(send+"\n");
		
		
		
	}
	
}
