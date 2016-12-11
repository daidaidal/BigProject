package control;

import javafx.collections.ObservableList;
import model.Person;

public class ShowMessageService {
	public void show(String id,String name,String message,MainApp mainapp)
	{
		ObservableList<Person> friendsData=mainapp.getFriendsData();
		int len = friendsData.size();
		for(int i=0;i<len;i++)
		{
			if(friendsData.get(i).getId().equals(id))
			{
				String before=friendsData.get(i).getShowtext();
				if(name!=null)
					before=before+friendsData.get(i).getName()+":"+"\n"+message+"\n";
				else 
					before=before+name+":"+"\n"+message+"\n";
				System.out.println("before:"+before);
				friendsData.get(i).setShowtext(before);
				mainapp.setFriendsData(friendsData);
				return;
			}
		}
	}
}
