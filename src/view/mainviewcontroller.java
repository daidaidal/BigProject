package view;

import control.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	
	private Person solo;
	private Person person;
	private Stage mainStage;
	private MainApp mainapp;
	
	public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
       
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
    		friendsnameLabel.setText(name);
    		friendscompanyLabel.setText(company);
    		friendsemailLabel.setText(email);
    		
    		//1对1聊天
    		solo.setName(name);
    		solo.setId(person.getId());
    		solo.setCompany(company);
    		solo.setEmail(email);
    		
    	} else {
    		// Person is null, remove all the text.
    		friendsnameLabel.setText("");
    		friendscompanyLabel.setText("");
    		friendsemailLabel.setText("");
    	}
    }
	
	@FXML
	private void handleEdit()
	{
		mainapp.showeditview(person);
	}
	
}
