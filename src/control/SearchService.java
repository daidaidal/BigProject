package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Person;

public class SearchService {
	private boolean judge; //false是人。true是群
	private Person person=new Person();
	public boolean getJudge()
	{
		return judge;
	}
	public  Person SearchPerson(String id)
	{
		judge=true;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from person where id='" + id + "'");
		if (rs.next()) {
			 judge=false;
			 String name=rs.getString("name");
			 String company=rs.getString("company");
			 String email=rs.getString("email");
			 person.setName(name);
			 person.setCompany(company);
			 person.setEmail(email);	
			 person.setId(id);
		}
		if(judge==true)
		{
			ResultSet rs2 = stmt.executeQuery("select * from qun where id='" + id + "'");
			if (rs2.next()) {
				 judge=true;
				 String name=rs2.getString("name");
				 String company=rs2.getString("company");
				 String email=rs2.getString("email");
				 person.setName(name);
				 person.setCompany(company);
				 person.setEmail(email);	
				 person.setId(id);
			}
		}
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return person;
	}
	
}
