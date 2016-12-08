package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Person;

public class GetFriends {
	public String getfriends(String myid)
	{
		String friendsid=null;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from person where id='" + myid + "'");
		if (rs.next()) {
			friendsid= rs.getString("friendsid");
		}
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return friendsid;
	}
	
	public Person Add_Friends_info(String friendid,boolean judge)
	{
		Person person=new Person();
		String sql=null;
		if(judge==true)
			sql="select * from qun where id='" + friendid + "'";
		else if(judge==false)
			sql="select * from person where id='" + friendid + "'";
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			String id = rs.getString("id");
			String name=rs.getString("name");
			String company=rs.getString("company");
			String email=rs.getString("email");
			String mem=null;
			if(judge==true)
			{
				mem=rs.getString("membersid");
				person.setMembersid(mem);
			}
			else 
			{
				mem=rs.getString("friendsid");
				person.setFriendsid(mem);
			}
			person.setId(id);
			person.setName(name);
			person.setCompany(company);
			person.setEmail(email);	
		}
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return person;
	}
}
