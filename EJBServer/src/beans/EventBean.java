package beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.*;


import models.Event;

/**
 * Session Bean implementation class EventBean
 */
@Stateless
public class EventBean implements EventBeanRemote {

	/***
    *@param user_id hodnota idcka prihlaseneho pouzivatela v databaze 
    *@return vracia eventy ktore dany user likuje
    */
	public List<Event> getUserLikedEvents(int user_id){
		List<Event> events = new ArrayList<Event>();
		try {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
		Connection conn = ds.getConnection();
		Statement stm = conn.createStatement();
		stm.executeQuery("SELECT ev.id,ev.name,ev.date_at,ev.entry,ev.location,cat.name as category,usr.name as user_name FROM liked_events lv " + 
				"JOIN events ev ON (ev.id = lv.event_id) " + 
				"JOIN category cat ON (ev.category_id = cat.id) " + 
				"JOIN users usr ON (ev.user_id = usr.id) " + 
				"WHERE lv.user_id = "+user_id+"");
		ResultSet rs = stm.getResultSet();
		while(rs.next()) {
			Event event = new Event();
			event.setId(rs.getInt("id"));
			event.setName(rs.getString("name"));
			event.setLocation(rs.getString("location"));
			event.setEntry(rs.getInt("entry"));
			event.setCategory(rs.getString("category"));
			event.setUserFullName(rs.getString("user_name"));
			event.setDate(rs.getString("date_at"));
			events.add(event);
		}
		conn.close();
		stm.close();
		rs.close();
		}catch(Exception e) {
			
			
		}
		return events;
	}
	/***
	 * @param user_id id prihlaseneho pouzivatela v databaze
	 * @return vracia eventy ktore pouzivatel vytvoril
	 */
	public List<Event> getUserEvents(int user_id){
		List<Event> events = new ArrayList<Event>();
		try {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			
			stm.executeQuery("SELECT ev.id,ev.name,ev.date_at,ev.location,ev.entry, cat.name as category FROM events ev " + 
					"JOIN category cat ON (cat.id = ev.category_id) " + 
					"WHERE ev.user_id = "+user_id+"");
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				
				Event event = new Event();
				event.setId(rs.getInt("id"));
				event.setName(rs.getString("name"));
				event.setLocation(rs.getString("location"));
				event.setEntry(rs.getInt("entry"));
				event.setCategory(rs.getString("category"));
				event.setDate(rs.getString("date_at"));
				events.add(event);
				
			}
			conn.close();
			rs.close();
			stm.close();
		}catch(Exception e) {
			
		}
		
		
		return events;
	}

	/***
	 * @param name nazov eventu
	 * @param location miesto konania eventu
	 * @param date datum konania eventu
	 * @param category kategoria v ktorej je dany event zaregistrovany
	 * @param entry vyska vstupneho 
	 * @param user_id id prihlaseneho pouzivatela v databaze
	 * 
	 * Tato metoda zoberie udaje o novom evente ktory vytvoril prihlaseny pouzivatel a vlozi informacie do databazy
	 */
	@Override
	public void addEvent(String name, String location, String date, int category, int entry, int user_id) {
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
				Connection conn = ds.getConnection();
				Statement stm = conn.createStatement();
				stm.executeQuery("INSERT INTO events (name,location,date_at,category_id,entry,user_id) VALUES ('"+name+"','"+location+"',"
						+ "'"+date+"',"+category+","+entry+","+user_id+")");
				conn.close();
				stm.close();
				
			}catch(Exception e) {
				
				
			}
	}
	
	/***
	 * @param eventId id eventu ktory chce user vymazat
	 * 
	 * Tato metoda vymaze event z databazy
	 */
	@Override
	public void removeEvent(int eventId) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("DELETE FROM events WHERE id = "+eventId+"");
			conn.close();
			stm.close();
		}
		catch(Exception e) {
			
		}
	}

	/***
	 * @param id id eventu ktory chce user modifikovat
	 * @param name nazov eventu po modifikacii
	 * @param location miesto konania po modifikacii
	 * @param date datum konania po modifikacii
	 * @param category id kategorie po modifikacii
	 * @param entry vyska vstupneho po modifikacii
	 */
	@Override
	public void updateEvent(int id, String name, String location, String date, int category, int entry) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("UPDATE events SET name = '"+name+"',location = '"+location+"',date_at = '"+date+"',category_id = "+category+",entry = "+entry+""
					+ "WHERE id = "+id+"");
			conn.close();
			stm.close();
		}
		catch(Exception e) {
			
		}
		
	}
	/***
	 * @param id id prihlasaneho pouzivatela v databaze
	 * @param filter zoznam stringov ktore pridavam na koniec query ak sa filtruje podla nejakych kriterii
	 * @return vrati zoznam eventov po filtrovani
	 */
	@Override
	public List<Event> getNewEvents(int id,ArrayList<String> filter) {
		List<Event> events = new ArrayList<Event>();
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			String query = "SELECT ev.id,ev.name,ev.date_at,ev.location,ev.entry, u.name as username,cat.name as category from events ev\r\n" + 
					"JOIN users u ON ev.user_id = u.id\r\n" + 
					"JOIN category cat ON ev.category_id = cat.id\r\n" + 
					"where ev.name NOT IN (\r\n" + 
					"  select ev.name from events ev\r\n" + 
					"    LEFT JOIN liked_events lev ON (lev.event_id = ev.id)\r\n" + 
					"  WHERE ev.user_id = "+id+" OR lev.user_id = "+id+"\r\n" + 
					") ";
			for(String str:filter) {
				query = query.concat(str);
			}
			stm.executeQuery(query);
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				
				Event event = new Event();
				event.setId(rs.getInt("id"));
				event.setName(rs.getString("name"));
				event.setCategory(rs.getString("category"));
				event.setDate(rs.getString("date_at"));
				event.setEntry(rs.getInt("entry"));
				event.setLocation(rs.getString("location"));
				event.setUserFullName(rs.getString("username"));
				events.add(event);
				
			}
			rs.close();
			conn.close();
			stm.close();
		}
		catch(Exception e) {
			
		}
		return events;
	}
	/***
	 * @param eventId id eventu v databaze
	 * @param userId id pouzivatela v databaze
	 * 
	 * Tato metoda len prida do vazobnej tabulky vztah medzi pouzivatelom a eventom a teda ho pouzivatel lajkuk/followuje
	 */
	@Override
	public void setLikedEvent(int eventId, int userId) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("INSERT INTO liked_events (event_id,user_id) VALUES ("+eventId+","+userId+")");
			conn.close();
			stm.close();
		}
		catch(Exception e) {
			
		}
		
		
	}

}
