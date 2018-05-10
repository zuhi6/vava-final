package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import models.User;

/**
 * Session Bean implementation class TestBean
 */
@Stateless
public class UserBean implements UserBeanRemote,Serializable  {
	

	/***
	 * @param email email pouzivatela ktory sa snazi prihlasit
	 * @param password heslo pouzivatela ktory sa snazi prihlasit
	 * @return vracia true alebo false podla toho ci sa uzivatel nachadza v databaze
	 */
	public boolean checkCredentials(String email,String password) {
		
		boolean found = false;
		
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("SELECT count(*) FROM users WHERE email = '"+email+"' AND password = '"+password+"'");
			ResultSet rs = stm.getResultSet();
			rs.next();
			
		if(rs.getInt(1) == 1) {
			found = true;
		}
		
		conn.close();
		stm.close();
		rs.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return found;
	}
	
	/***
	 * @param email email pouzivatela ktory sa nachadza v nasej databaze
	 * @param password heslo pouzivatela ktory sa nachadza v nasej databaze
	 * @return vrati instanciu triedy user teda pouzivatela ktory sa uspesne prihlasil
	 */
	public User getUserInfo(String email,String password) {
		User user = User.getInstance();
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("SELECT * FROM users WHERE email = '"+email+"' AND password = '"+password+"'");
			ResultSet rs = stm.getResultSet();
			rs.next();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setDob(rs.getString("dob"));
			user.setPassword(rs.getString("password"));
			conn.close();
			stm.close();
			rs.close();
		}catch(Exception e) {
			
		}
		return user;
	}
	/***
	 * @param email email noveho pouzivatela ktory sa registruje
	 * @param password heslo pouzivatela ktory sa registruje
	 * @param fullName cele meno pouzivatela pri registracii
	 * @param dob datum narodenia novo registrovaneho pouzivatela
	 */
	public void addNewUser(String email,String password,String fullName,String dob) {
		
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("INSERT INTO users (name,email,dob,password) VALUES ('"+fullName+"','"+email+"','"+dob+"','"+password+"')");
			conn.close();
			stm.close();
		
		}catch(Exception e) {
			
		}
	}

}
