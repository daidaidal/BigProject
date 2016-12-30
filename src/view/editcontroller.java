package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import control.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;


public class editcontroller {
	@FXML
	private TextField nameField;
	@FXML
	private TextField companyField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	
	private MainApp mainapp;
	private Stage editStage;
	private Person person;
	private mainviewcontroller controller;
	
	public editcontroller(){
	}
	public void getMainviewController(mainviewcontroller controller)
	{
		this.controller=controller;
	}
	
	public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;
    }
	public void setStage(Stage editStage)
	{
		this.editStage=editStage;
	}
	@FXML
    private void initialize() {
    }
	
	public void setPerson(Person person)
	{
		this.person=person;
		nameField.setText(person.getName());
		companyField.setText(person.getCompany());
		emailField.setText(person.getEmail());
		passwordField.setText(person.getPassword());
	}
	
	@FXML
	private void handleOk()
	{
		String name=nameField.getText();
		String company=companyField.getText();
		String email=emailField.getText();
		String password=passwordField.getText();
		
		person.setName(name);
		person.setCompany(company);
		person.setEmail(email);
		person.setPassword(password);
		
		String sql="update person set name='"+name+"',"
									+"company='"+company+"',"
									+"email='"+email+"',"
									+"password='"+password+"' "
									+"where id='"+person.getId()+"'";
									 
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
      try{   
    	  //con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bookdb", "root", "daidai");
    	  Connection con = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb","bp_user","123456");
          Statement stmt=con.createStatement();  
          //int i=
          stmt.executeUpdate(sql);
          con.close();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        controller.setPerson(person);
        editStage.close();
	}
	
	@FXML
	private void handleCancle()
	{
		editStage.close();
	}
	
	
	

}
