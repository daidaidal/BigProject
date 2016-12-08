package view;
import model.Person;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.controlsfx.dialog.Dialogs;

import control.GetFriends;
import control.LoginService;
import control.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class logincontroller {
	@FXML
	private TextField idField;
	@FXML
	private TextField passwordField;
	
	private MainApp mainapp;
	
	public logincontroller(){
	}
	
	@FXML
    private void initialize() {
    }
	
	@FXML
	private void handlesignin()
	{
		mainapp.showsignin();
	}
	@FXML
	private void handlelogin()
	{
		LoginService log=new LoginService();
		if(passwordField.getText()==null)
			System.out.println("null");
		else if(passwordField.getText()=="")
			System.out.println("not null");
		boolean judge=log.login(idField.getText(),passwordField.getText());
		if(judge==true)
		{
			try {
			    Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
			}
			try {
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/bpdb", "bp_user", "123456");
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("select * from person where id='" + idField.getText() + "'");
			if (rs.next()) {
			 String id=rs.getString("id");
			 String name = rs.getString("name");
			 String email = rs.getString("email");
			 String company = rs.getString("company");	
			 String password = rs.getString("password");
			 String friendsid=rs.getString("friendsid");
			 Person person=new Person();
			 person.setId(id);
			 person.setName(name);
			 person.setEmail(email);
			 person.setCompany(company);
			 person.setPassword(password);
			 person.setFriendsid(friendsid);
			 
			 ObservableList<Person> friendsData=mainapp.getFriendsData();
			 String[] friends=friendsid.split("@.@")[0].split("##"); //friends从0开始，qun从1开始
			 if(friends.length>0)
				 for(int i=1;i<friends.length;i++)
				 {
					 GetFriends f=new GetFriends();
					 friendsData.add(f.Add_Friends_info(friends[i], false));
				 }
			 
			 
			 String[] quns=friendsid.split("@.@")[1].split("##");
			 
			 for(int i=1;i<quns.length;i++)
			 {
				 GetFriends f = new GetFriends();
				 friendsData.add(f.Add_Friends_info(quns[i], true));
			 }
			 
			 //加载好友或者群的信息
			 
			 mainapp.setFriendsData(friendsData);
			 mainapp.showmainview(person);
			}
			connect.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
		}
		/*
		if(judge==false)
		{
			Dialogs.create()
		        .title("11")
		        .masthead("11")
		        .message("111")
		        .showWarning();
		}
		*/
		
	}
	
	public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;
    }
	
	
}
