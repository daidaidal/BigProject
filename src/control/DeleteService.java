package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteService {
	public void delete(String myid,String id,Boolean judge)
	{	
		String members=null;
		String table=null;
		String friend_member=null;
		if(judge==true)
		{
			friend_member="membersid";
			table="qun";
		}
		else if(judge==false)
		{
			friend_member="friendsid";
			table="person";
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
      try{   
    	  //con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bookdb", "root", "daidai");
    	  Connection con = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb","bp_user","123456");
          Statement stmt=con.createStatement();  
          //id时当前账户的id，idjoin是搜索后获得的id，加好友或者加群都要同时修改两张表(暂时无验证）
          //把对方从我的表删除
          ResultSet rs=stmt.executeQuery("select * from person where id='"+myid+"'");
          if(rs.next())
        	  members=rs.getString("friendsid");
          
          if(judge==true)
        	  members=members+id+"##";
          else 
        	  members="##"+id+members;
          
          String sql1="update person set friendsid='"+members+"' where id='"+myid+"'";
          //把我从对方的表删除
          ResultSet rs2=stmt.executeQuery("select * from "+table+" where id='"+id+"'");
          if(rs2.next())
        	  members=rs2.getString(friend_member);
          members="##"+id+members;
          String sql2="update "+table+" set "+friend_member+"='"+members+"' where id='"+id+"'";
          
          stmt.executeUpdate(sql1);
          stmt.executeUpdate(sql2);
          con.close();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
