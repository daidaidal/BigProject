package view;

import control.JoinService;
import control.MainApp;
import control.SearchService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Person;

public class addcontroller {
	@FXML
	TextField idField;
	@FXML
	Label ren_qunLabel;
	@FXML
	Label nameLabel;
	@FXML
	Label companyLabel;
	@FXML
	Label emailLabel;
	@FXML
	Label messageLabel;
	
	private Person person;
	private Person showperson;//搜索后显示的人或者群
	private boolean judge;
	private MainApp mainapp;
	
	public addcontroller(){
	}
	public void setPerson(Person person)
	{
		this.person=person;
	}
	public void setMainApp(MainApp mainapp)
	{
		this.mainapp=mainapp;
	}
	@FXML
    private void initialize() {
    }
	
	@FXML
	private void handleSearch()
	{
		String id=idField.getText();
		SearchService search=new SearchService();
		showperson=search.SearchPerson(id);
		if(search.getJudge()==true){
			judge=true;
			ren_qunLabel.setText("群");
		}
		else if(search.getJudge()==false){
			judge=false;
			ren_qunLabel.setText("人");
		}
		nameLabel.setText(showperson.getName());
		companyLabel.setText(showperson.getCompany());
		emailLabel.setText(showperson.getEmail());	
	}
	
	@FXML
	private void handleJoin()
	{
		JoinService join=new JoinService();
		String message=join.join(judge,person.getId(), showperson.getId());
		messageLabel.setText(message);
		
		if(!message.equals("已经加过好友/群了"))
		{
			ObservableList<Person> friendsData=mainapp.getFriendsData();
			friendsData.add(showperson);
			mainapp.setFriendsData(friendsData);
		}
	}
	
	@FXML
	private void handleJianqun()
	{
		mainapp.showjianqunview(person.getId());
	}
	

}
