package control;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginService {
	public boolean login(String id,String password)
	{
		String judge=null;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
		try {
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/bpdb", "bp_user", "123456");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from person where id='" + id + "'");
		if (rs.next()) {
		judge = rs.getString("password");
		}
		connect.close();
		} catch (SQLException e) {
		e.printStackTrace();
		}
		if (password.equals(judge))
			return true;
		else return false;
	}
}
