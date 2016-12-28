package view;
import model.Person;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import view.messagecontroller;
import control.GetFriends;
import control.LoginService;
import control.MainApp;
import control.MessageService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class logincontroller {
	@FXML
	private TextField idField;
	@FXML
	private TextField passwordField;
	
	private MainApp mainapp;
	
	public logincontroller(){
	}
	
	@FXML
    private void initialize() {
    }
	
	@FXML
	private void handlesignin()
	{
		mainapp.showsignin();
	}
	@FXML
	private void handlelogin()
	{
		Person person=new Person();
		LoginService log=new LoginService();
		if(passwordField.getText()==null)
			System.out.println("null");
		else if(passwordField.getText()=="")
			System.out.println("not null");
		boolean judge=log.login(idField.getText(),passwordField.getText());
		if(judge==false)
		{
			MessageService m=new MessageService();
			m.set(1);	
		}
		if(judge==true)
		{
			try {
			    Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
			}
			try {
			Connection connect = DriverManager.getConnection("jdbc:mysql://115.28.67.141:3306/bpdb", "bp_user", "123456");
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("select * from person where id='" + idField.getText() + "'");
			if (rs.next()) {
			 String id=rs.getString("id");
			 String name = rs.getString("name");
			 String email = rs.getString("email");
			 String company = rs.getString("company");	
			 String password = rs.getString("password");
			 String friendsid=rs.getString("friendsid");
			 person.setId(id);
			 person.setName(name);
			 person.setEmail(email);
			 person.setCompany(company);
			 person.setPassword(password);
			 person.setFriendsid(friendsid);
			}
			//获取ip地址
			/*
			InetAddress ip;
			String localip=null;
			try {
				ip=InetAddress.getLocalHost();
				localip=ip.getHostAddress();
				person.setMyip(localip);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println(localip);
			String sqltongxun="update tongxun set ip='"+localip+"' where id='"+person.getId()+"'";
			stmt.executeUpdate(sqltongxun);
			*/
			
			int count=1;
			int maxcount=0;
			//离线消息集
			String[] lixian=null;
			ResultSet rs2=null;
			rs2=stmt.executeQuery("select * from tongxun where id='"+person.getId()+"'");
			if(rs2.next())
			{
				lixian=rs2.getString("message").split("@.@");
				maxcount=lixian.length;
			}
			//加载朋友列表和朋友信息
			String friendsid=person.getFriendsid();
			 ObservableList<Person> friendsData=mainapp.getFriendsData();
			 String[] friends=friendsid.split("@.@")[0].split("##"); //friends从0开始，qun从1开始
			 if(friends.length>0)
				 for(int i=1;i<friends.length;i++)
				 {
					 String online=null;
					 GetFriends f=new GetFriends();
					 Person p=new Person();
					 p=f.Add_Friends_info(friends[i], false);
					 p.setJudge(false);
					 String id=p.getId();
					 /*
					 ResultSet rs3 =stmt.executeQuery("select * from tongxun where id='"+id+"'");
					 if(rs3.next())
						 online=rs3.getString("online");
					 if(online.equals("yes"))
						 p.setOnline("是");
					 else if(online.equals("no"))
						 p.setOnline("否");
						 */
					 if(count!=-1)
					 {
						 for(i=count;i<maxcount;i++)
						 {
							 String[] message=lixian[count].split("###");
							 String iddd=message[0];
							 if(id.equals(iddd))
							 {
								 String before=p.getShowtext();
								 before=before+p.getName()+":"+"\n"+message[1]+"\n";
								 p.setShowtext(before);
								 count++;
								 if(count==maxcount)
									 count=-1; 
							 }
						 }
					 }
					 friendsData.add(p);
				 }
			 
			 
			 String[] quns=friendsid.split("@.@")[1].split("##");
			 if(quns.length>0)
				 for(int i=1;i<quns.length;i++)
				 {
					 GetFriends f = new GetFriends();
					 Person p= new Person();
					 p=f.Add_Friends_info(quns[i], true);
					 p.setJudge(true);
					 String id=p.getId();
					 if(count!=-1)
					 {
						 for(i=count;i<maxcount;i++)
						 {
							 String[] message=lixian[count].split("###");
							 String iddd=message[0];
							 if(id.equals(iddd))
							 {
								 String before=p.getShowtext();
								 before=before+message[1]+":"+"\n"+message[2]+"\n";
								 p.setShowtext(before);
								 count++;
								 if(count==maxcount)
									 count=-1; 
							 }
						 }
					 }
					 friendsData.add(p);
				 }
			 
			 //加载好友或者群的信息
			 //在这里加载每个人的离线信息
			 stmt.executeUpdate("update tongxun set message='"+"',online='online' where id='"+person.getId()+"'");
			 mainapp.setFriendsData(friendsData);
			 mainapp.showmainview(person);
			connect.close();
			} catch (SQLException e) {
			e.printStackTrace();
			}
		}
		/*
		if(judge==false)
		{
			Dialogs.create()
		        .title("11")
		        .masthead("11")
		        .message("111")
		        .showWarning();
		}
		*/
		
	}
	
	public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;
    }
	
	
}
