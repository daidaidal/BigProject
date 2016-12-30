package view;
import java.awt.Desktop;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import control.DeleteService;
import control.MainApp;
import control.PptService;
import control.MyRecord;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	private TextArea friendsshowArea;
	@FXML
	private Label friendsjudgeLabel;
	@FXML
	private ContextMenu cm;
	@FXML
	private Label friendsonlineLabel;

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
	public void setFriendsTable()
	{
		friendsTable.setItems(mainapp.getFriendsData());
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
    		String showtext=person.getShowtext();
    		String judge=null;
    		String online=null;
    		friendsnameLabel.setText(name);
    		friendscompanyLabel.setText(company);
    		friendsemailLabel.setText(email);
    		friendsidLabel.setText(id);
    		friendsshowArea.setText(showtext);
    		if(person.getJudge()==true)
    			judge="群";
    		else
    			judge="人";
    		friendsjudgeLabel.setText(judge);

    		if(person.getJudge()==false)
	    		try {
					Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
					Statement stmt = connect.createStatement();
					ResultSet rs = stmt.executeQuery("select * from tongxun where id='" + id + "'");
					if (rs.next()) {
						 online=rs.getString("online");
					}
					connect.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		friendsonlineLabel.setText(online);


    	} else {
    		// Person is null, remove all the text.
    		friendsnameLabel.setText("");
    		friendscompanyLabel.setText("");
    		friendsemailLabel.setText("");
    		friendsidLabel.setText("");
    		//friendsshowArea.setText("");
    	}
    }
	public void setGetmessage(String getmessage)
	{
		if(getmessage!=null)
		{
			System.out.println(getmessage);
		}
		System.out.println(getmessage);
		String id1=null;
		String name=null;
		String message=null;
		String[] s=getmessage.split("###");
		if(s.length==2)
		{
			id1=s[0];
			message=s[1];
		}
		else if (s.length==3)
		{
			id1=s[0];
			name=s[1];
			message=s[2];
		}
		//sh.show(id1,name, message, mainapp);
		ObservableList<Person> friendsData=mainapp.getFriendsData();
		int len = friendsData.size();
		for(int i=0;i<len;i++)
		{
			if(friendsData.get(i).getId().equals(id1))
			{
				String before=friendsData.get(i).getShowtext();
				System.out.println(friendsData.get(i).getId());
				if(before==null)
					before="";
				if(name!=null)
					before=before+name+":"+"\n"+message+"\n";
				else
					before=before+friendsData.get(i).getName()+":"+"\n"+message+"\n";
				System.out.println("before:"+before);
				friendsData.get(i).setShowtext(before);
				mainapp.setFriendsData(friendsData);
				break;
			}
		}
		if(id1.equals(friendsidLabel.getText()))
		{
			if(friendsjudgeLabel.getText().equals("人"))
				friendsshowArea.appendText(friendsnameLabel.getText()+":"+"\n"+message+"\n");
			else if(friendsjudgeLabel.getText().equals("群"))
				friendsshowArea.appendText(name+":"+"\n"+message+"\n");
		}
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
	private void handle_cm_delete()
	{
		String id=friendsidLabel.getText();
		DeleteService d=new DeleteService();
		Boolean judge;
		if(friendsjudgeLabel.getText().toString().equals("群"))
				judge=true;
		else
			judge=false;
		d.delete(person.getId(), id,judge);

		ObservableList<Person> friendsData=mainapp.getFriendsData();
		int len = friendsData.size();
		for(int i=0;i<len;i++)
		{
			if(friendsData.get(i).getId().equals(id))
			{
				Person p=friendsData.remove(i);
				mainapp.setFriendsData(friendsData);
				break;
			}
		}
	}
	@FXML
	private void handlesend()
	{
		String ip=null;
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
		if(friendsjudgeLabel.getText().toString().equals("人"))
			send=person.getId()+"^&^"+friendsidLabel.getText()+"^&^"+sendArea.getText();
		else
			send="qun^&^"+person.getId()+"^&^"+person.getName()+"^&^"+friendsidLabel.getText()+"^&^"+sendArea.getText();
		String show = sendArea.getText();
		sendArea.setText("");
		try {
			if(socket==null)
				System.out.println("socket is null");
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(send);
			out.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//这么加来回切换会有bug
		System.out.println("show:"+show);
		String id1=friendsidLabel.getText();
		String name=person.getName();
		String message=show;
		friendsshowArea.appendText(name+":"+"\n"+show+"\n");
		ObservableList<Person> friendsData=mainapp.getFriendsData();
		int len = friendsData.size();
		for(int i=0;i<len;i++)
		{
			if(friendsData.get(i).getId().equals(id1))
			{
				String before=friendsData.get(i).getShowtext();
				System.out.println(friendsData.get(i).getId());
				if(before==null)
					before="";
				if(name!=null)
					before=before+name+":"+"\n"+message+"\n";
				else
					before=before+friendsData.get(i).getName()+":"+"\n"+message+"\n";
				System.out.println("before:"+before);
				friendsData.get(i).setShowtext(before);
				mainapp.setFriendsData(friendsData);
				break;
			}
		}

		//friendsshowArea.appendText(person.getName()+":"+"\n"+show+"\n");
		//friendsshowArea.appendText(person.getName()+":"+"\n"+message+"\n");



	}
	@FXML
	private void handledraw()
	{
		mainapp.showdraw();
	}
	@FXML
	private void handleshowppt()
	{
		int number=0;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择ppt");
		File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
        	System.out.println(file);
        	PptService p = new PptService();
        	number=p.translate(file.toString());
        	try {
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(mainviewcontroller.class.getResource("/view/ppt.fxml"));
    			StackPane pptpane = (StackPane) loader.load();
    			// Create the dialog Stage.
    			//Stage primaryStage = new Stage();
    			Stage pptStage = new Stage();
    			pptStage.setTitle("edit");
    			pptStage.initModality(Modality.WINDOW_MODAL);

    			// Set the person into the controller.
    			pptcontroller controller = loader.getController();
    			controller.init("1",number,pptpane,pptStage);

    		} catch (IOException e) {
                e.printStackTrace();
            }
            //openFile(file);
        }
	}
	@FXML
	private void handledocument()
	{
		if(friendsjudgeLabel.getText().equals("人"))
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("选择ppt");
			File file = fileChooser.showOpenDialog(mainStage);
		}
		else if(friendsjudgeLabel.getText().equals("群"))
		{

		}
	}
	private void openFile(File file) {
		Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
            		mainviewcontroller.class.getName()).log(
                        Level.SEVERE, null, ex
                    );
        }
    }


}
