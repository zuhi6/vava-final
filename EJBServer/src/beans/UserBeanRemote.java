package beans;

import java.sql.SQLException;

import javax.ejb.Remote;


import models.User;


@Remote
public interface UserBeanRemote {

	public boolean checkCredentials(String email,String password);
	public User getUserInfo(String email,String password);
	public void addNewUser(String email,String password,String fullName,String dob);
}
