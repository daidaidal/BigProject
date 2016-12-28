package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Person;

public class SigninService {
	private String maxid;
	public void setMaxId(String maxid)
	{
		this.maxid=maxid;
	}
	public String signin()
	{
		String maxid=null;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from maxid");
		if (rs.next()) {
		maxid = rs.getString("id");
		}
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
		setMaxId(maxid);
		return maxid;
	}
	public void afterSignin(String name,String password,Person person)
	{
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		int id=Integer.parseInt(maxid)+1;
		String sql="update maxid set id='"+id+"' where id='"+maxid+"'";
		String sql2=null;
		String sql3=null;
		System.out.println(name);
		if(person==null)
			sql2="insert into person (name,id,password,company,email,friendsid) values ('"+name+"','"+maxid+"','"+password+"','','','##@.@##')";
		else 
			sql2="insert into qun (name,id,company,email,membersid) values ('"+person.getName()+"','"+maxid+"','"+person.getCompany()+"','"+person.getEmail()+"','')";
		stmt.executeUpdate(sql);
		stmt.executeUpdate(sql2);
		if(person==null){
			sql3="insert into tongxun (id,online,ip,message) values ('"+maxid+"','"+"','"+"','')";
		stmt.executeUpdate(sql3);
		}
			
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
	}

}
