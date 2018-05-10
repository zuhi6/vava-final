package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;


public class CreateNewAccountController implements Initializable{

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private PasswordField passwordField;
    
    private Logger log = Logger.getLogger(CreateNewAccountController.class.getName());

    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	
    	
    	/***
    	 * vrati sa na predoslu scenu
    	 */
    	backButton.setOnAction(event->{
    		
   		 Stage stage=(Stage) backButton.getScene().getWindow();
            try {
                Parent root;
                ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("loginFXML")),resource);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
                log.log(Level.SEVERE, "SCENE CRASH", e);
            }
    	});
    	
    	/***
    	 * ak su vsetky informacie o uzivatelovi vyplnene a neexistuje uzivatel s rovnakymi udami(email,heslo) zavola sa metoda na serveri a vlozia sa udaje
    	 * do databazy
    	 */
    	createButton.setOnAction(event->{
    		
    		boolean available;
            if(fullNameTextField.getText().equals("") || emailTextField.getText().equals("") || passwordField.getText().equals("") || dobDatePicker.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Required fields are not filled");
                alert.showAndWait();

            }
            else{
            	
                try {
                    available = Main.remoteUser.checkCredentials(emailTextField.getText(),passwordField.getText()); 
                    if (!available){

                        Main.remoteUser.addNewUser(emailTextField.getText(),passwordField.getText(),fullNameTextField.getText(),dobDatePicker.getValue().toString());

                        backButton.fire();



                    }else{

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Username/Email is already in use");
                        alert.showAndWait();

                    }
                }
                catch(Exception e){
                	log.log(Level.SEVERE, "Check Credentials Crash", e);

                }
            }

    	});
    	
    }
    
    
    
       
}