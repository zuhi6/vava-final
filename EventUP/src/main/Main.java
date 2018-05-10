package main;

import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import beans.CategoryBeanRemote;
import beans.EventBeanRemote;
import beans.UserBeanRemote;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Event;

public class Main extends Application {
	
	public static UserBeanRemote remoteUser;
	public static EventBeanRemote remoteEvent;
	public static CategoryBeanRemote remoteCategory;
	public static Event selectedEvent;
	private static Logger log = Logger.getLogger(Main.class.getName());
	public static Properties clientProperties;
	
	   public void start(Stage primaryStage) {

	        try {
	        	ResourceBundle resources = ResourceBundle.getBundle(clientProperties.getProperty("languagePrefix"),Locale.getDefault());
	            Pane page = (Pane) FXMLLoader.load(Main.class.getResource(clientProperties.getProperty("loginFXML")),resources);
	            Scene scene = new Scene(page);
	            primaryStage.setScene(scene);
	            primaryStage.setTitle(clientProperties.getProperty("title"));
	            primaryStage.show();
	        } catch(Exception e) {
	            log.log(Level.SEVERE, "START CRASHED", e);
	        }
	    }

	
	
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
		clientProperties = new Properties();
		clientProperties.load(new FileInputStream("config/configuration.properties"));
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote://localhost:4447"); 
        props.put(Context.SECURITY_PRINCIPAL, "zuhi");
        props.put(Context.SECURITY_CREDENTIALS, "afoj");
        
		Context ctx = new InitialContext(props);
		remoteUser = (UserBeanRemote)ctx.lookup("/EJBServer/UserBean!beans.UserBeanRemote");
		remoteEvent = (EventBeanRemote)ctx.lookup("/EJBServer/EventBean!beans.EventBeanRemote");
		remoteCategory = (CategoryBeanRemote)ctx.lookup("/EJBServer/CategoryBean!beans.CategoryBeanRemote");
		launch(args);
		}
		catch(Exception e) {
			log.log(Level.SEVERE, "ERROR", e);
		}
		
	}

}  
