package beans;



import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;


import models.Event;

@Remote
public interface EventBeanRemote {

	public List<Event> getUserLikedEvents(int user_id);
	public List<Event> getUserEvents(int user_id);
	public void addEvent(String name, String location, String date, int category, int entry,int user_id);
	public void removeEvent(int eventId);
	public void updateEvent(int id, String name, String location, String date, int category, int entry);
	public List<Event> getNewEvents(int id,ArrayList<String> filter);
	public void setLikedEvent(int eventId, int userId);
	
}
