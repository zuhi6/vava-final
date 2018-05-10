package models;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String name;
	private String email;
	private String dob;
	private String password;
	
	 private static User instance = null;

	   	public User() {}

	    public static User getInstance(){
	        if(instance == null){
	            instance = new User();
	        }
	        return instance;
	    }
	    
	 public void setId(int id) {
		 this.id = id;
	 }
	 public int getId() {
		 return this.id;
	 }
	 public void setName(String name) {
		 this.name = name;
	 }
	 public String getName() {
		 return this.name;
	 }
	 public void setEmail(String email) {
		 this.email = email;
	 }
	 public String getEmail() {
		 return this.email;
	 }
	 public void setDob(String dob) {
		 this.dob = dob;
	 }
	 public String getDob() {
		 return this.dob;
	 }
	 public void setPassword(String password) {
		 this.password = password;
	 }
	 public String getPassword() {
		 return this.password;
	 }
}
