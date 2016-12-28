package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JoinService {
	public String join(boolean judge,String id,String idjoin)
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
          //把对方加入我的表
          ResultSet rs=stmt.executeQuery("select * from person where id='"+id+"'");
          if(rs.next())
        	  members=rs.getString("friendsid");
          if(members.contains(idjoin))
        	  return "已经加过好友/群了";
          if(judge==true)
        	  members=members+idjoin+"##";
          else 
        	  members="##"+idjoin+members;
          String sql1="update person set friendsid='"+members+"' where id='"+id+"'";
          //把我加入对方的表
          ResultSet rs2=stmt.executeQuery("select * from "+table+" where id='"+idjoin+"'");
          if(rs2.next())
        	  members=rs2.getString(friend_member);
          members="##"+id+members;
          String sql2="update "+table+" set "+friend_member+"='"+members+"' where id='"+idjoin+"'";
          
          stmt.executeUpdate(sql1);
          stmt.executeUpdate(sql2);
          con.close();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	return "操作成功";
	}
}
