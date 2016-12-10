package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Person.
 *
 * @author Marco Jakob
 */
public class Person {
	private  StringProperty id;
	private  StringProperty name;
	private  StringProperty company;
	private  StringProperty email;
	private  StringProperty password;
	private  StringProperty friendsid;
	private  StringProperty membersid;
	private  StringProperty myip;
	
	public Person() {
		this.id = new SimpleStringProperty("");
		this.password = new SimpleStringProperty("");
		this.name = new SimpleStringProperty("");
		this.company = new SimpleStringProperty("");
		this.email = new SimpleStringProperty("");
		this.friendsid = new SimpleStringProperty("");
		this.membersid = new SimpleStringProperty("");
		this.myip = new SimpleStringProperty("");
	}
	public String getMyip() {
		return myip.get();
	}

	public void setMyip(String myip) {
		this.myip.set(myip);
	}
	
	public StringProperty myipProperty() {
		return myip;
	}
	
	
	public String getFriendsid() {
		return friendsid.get();
	}

	public void setFriendsid(String friendsid) {
		this.friendsid.set(friendsid);
	}
	
	public StringProperty friendsidProperty() {
		return friendsid;
	}
	
	public String getMembersid() {
		return membersid.get();
	}

	public void setMembersid(String membersid) {
		this.membersid.set(membersid);
	}
	
	public StringProperty membersidProperty() {
		return membersid;
	}
	
	
	public String getId() {
		return id.get();
	}

	public void setId(String id) {
		this.id.set(id);
	}
	
	public StringProperty idProperty() {
		return id;
	}
	
	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}
	
	public StringProperty passwordProperty() {
		return password;
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return name;
	}

	public String getCompany() {
		return company.get();
	}

	public void setCompany(String company) {
		this.company.set(company);
	}
	
	public StringProperty companyProperty() {
		return company;
	}
	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}
	
	public StringProperty emailProperty() {
		return email;	
	}
}