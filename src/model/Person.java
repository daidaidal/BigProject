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
	public Person(){
		this("","","","");
	}
	public Person(String id,String name,String company,String email) {
		this.id = new SimpleStringProperty(id);
		this.password = new SimpleStringProperty("");
		this.name = new SimpleStringProperty(name);
		this.company = new SimpleStringProperty(company);
		this.email = new SimpleStringProperty(email);
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