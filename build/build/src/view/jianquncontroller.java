package view;

import control.JoinService;
import control.MainApp;
import control.SigninService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;

public class jianquncontroller {
	@FXML
	TextField nameField;
	@FXML
	TextField companyField;
	@FXML
	TextField emailField;
	@FXML
	Label idLabel;
	private MainApp mainapp;
	private String id;
	private Stage jianqunStage;
	private  SigninService s=null;
	public void setMainApp(MainApp mainapp)
	{
		this.mainapp=mainapp;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public void setStage(Stage jianqunStage)
	{
		this.jianqunStage=jianqunStage;
	}
	public jianquncontroller(){
	}
	
	@FXML
    private void initialize() {
		s=new SigninService();
		String maxid=s.signin();
		idLabel.setText(maxid);
    }
	
	@FXML
	private void handleOk(){
		String name=nameField.getText();
		String company=companyField.getText();
		String email=emailField.getText();
		Person person=new Person();
		
		person.setName(name);
		person.setEmail(email);
		person.setCompany(company);
		person.setId(idLabel.getText());
		person.setJudge(true);
		s.afterSignin("","", person);
		
		JoinService join=new JoinService();
		join.join(true, id, person.getId());
		
		ObservableList<Person> friendsData=mainapp.getFriendsData();
		friendsData.add(person);
		mainapp.setFriendsData(friendsData);
		jianqunStage.close();
	}
	
}
