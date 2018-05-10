package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.jboss.logmanager.Level;

public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createUserButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField emailTextField;
    
    @FXML
    private Button engButton;
    
    @FXML
    private Button skButton;

    private Logger log = Logger.getLogger(LoginController.class.getName());
    
    public static Locale language = Locale.getDefault();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	
    	/***
    	 * prehodi sa na scenu ktora poskytne vytvorenie noveho uctu
    	 */
    	createUserButton.setOnAction(event->{
    		
    		 Stage stage=(Stage) createUserButton.getScene().getWindow();
             try {
                 Parent root;
                 ResourceBundle resource = ResourceBundle.getBundle("multilang",language);
                 root = FXMLLoader.load(getClass().getResource("/view/CreateNewAccountFXML.fxml"),resource);
                 Scene scene = new Scene(root);
                 stage.setScene(scene);
                 stage.show();
             } catch(Exception e) {
            	 log.log(Level.SEVERE, "SCENE CRASH", e);
             }
    		
    	});
    	
    	/***
    	 * po stlaceni sa zisti ci sa dane udaje(email,heslo) nachadzaju v databaze ak ano pouzivatela presunia na novu scenu
    	 */
    	loginButton.setOnAction(event->{
    		boolean found = false;
    		if(passwordTextField.getText().equals("") || emailTextField.getText().equals("")){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Required fields are not filled");
                alert.showAndWait();
            }else{
            	found = Main.remoteUser.checkCredentials(emailTextField.getText(), passwordTextField.getText());
                

            }
            if(found) {
            	User user;
            	user = Main.remoteUser.getUserInfo(emailTextField.getText(),passwordTextField.getText());
            	User.getInstance().setId(user.getId());
            	User.getInstance().setName(user.getName());
            	User.getInstance().setEmail(user.getEmail());
            	User.getInstance().setDob(user.getDob());
            	User.getInstance().setPassword(user.getPassword());
                Stage stage=(Stage) loginButton.getScene().getWindow();
                try {
                    Parent root;
                    ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),language);
                    root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("profileFXML")),resource);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch(Exception e) {
                	log.log(Level.SEVERE, "SCENE CRASH", e);
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Email/Password is incorrect");
                alert.showAndWait();

            }



    		
    	});
    	/***
    	 * zmeni local na anglicky a nastavi aktualnu scenu na anglicku
    	 */
    	engButton.setOnAction(event->{
    		language = new Locale("en","US");
    		ResourceBundle tempResource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),language);
    		loginButton.setText(tempResource.getString("loginLabel"));
    		passwordLabel.setText(tempResource.getString("passwordLabel"));
    		createUserButton.setText(tempResource.getString("createAccLabel"));
    	});
    	/***
    	 * zmeni local na slovensky a nastavi aktualnu scenu na slovensku
    	 */
    	skButton.setOnAction(event->{
    		language = new Locale("sk","SK");
      		ResourceBundle tempResource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),language);
    		loginButton.setText(tempResource.getString("loginLabel"));
    		passwordLabel.setText(tempResource.getString("passwordLabel"));
    		createUserButton.setText(tempResource.getString("createAccLabel"));
  
    	});
    		


    }
}