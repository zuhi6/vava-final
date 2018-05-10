package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import models.Category;
import models.User;

public class CreateEventController implements Initializable {

    @FXML
    private TextField eventNameTextField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private TextField entryTextField;

    @FXML
    private TextField eventLocationTextField;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button backButton;
    
    private Logger log = Logger.getLogger(CreateEventController.class.getName());
    
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	

    	ObservableList<Category> categories = FXCollections.observableArrayList(Main.remoteCategory.getCategories());
    	categoryComboBox.getItems().addAll(categories);
    	
    	/***
    	 * vrati sa na predoslu scenu
    	 */
    	backButton.setOnAction(event->{
    		Stage stage=(Stage) backButton.getScene().getWindow();
            try {
                Parent root;
                ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("myEventsFXML")),resource);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
            	log.log(Level.SEVERE, "SCENE CRASH", e);
            }
    		
    	});
    	
    	/***
    	 * pokial su vsetky udaje pri vytvoreni eventu zadane zavola metodu ktora vlozi event do databazy a vrati sa na predoslu scenu
    	 */
    	createButton.setOnAction(event->{
    		LocalDate date = dateDatePicker.getValue();
    		String name = eventNameTextField.getText();
    		String location = eventLocationTextField.getText();
    		
    		if (date != null && !name.equals("") && !location.equals("") && !entryTextField.getText().equals("") && categoryComboBox.getSelectionModel().getSelectedItem() != null) {
    			int entry = Integer.parseInt(entryTextField.getText());
    			int category = categoryComboBox.getSelectionModel().getSelectedItem().getId();
    			
    			Main.remoteEvent.addEvent(name,location,date.toString(),category,entry,User.getInstance().getId());
    			backButton.fire();
    		}else {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setContentText("Please fill all fields");
    			alert.showAndWait();
    		}
    	});
    	
    }

}
