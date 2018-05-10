package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.jboss.logmanager.Level;

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

public class ModifyEventController implements Initializable {

    @FXML
    private TextField eventNameTextField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField entryFeeTextField;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button saveButton;

    @FXML
    private TextField locationTextField;

    private Logger log = Logger.getLogger(ModifyEventController.class.getName());
    
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	eventNameTextField.setText(Main.selectedEvent.getName());
    	entryFeeTextField.setText(Integer.toString(Main.selectedEvent.getEntry()));
    	LocalDate localDate = LocalDate.parse(Main.selectedEvent.getDate());
    	datePicker.setValue(localDate);
    	locationTextField.setText(Main.selectedEvent.getLocation());
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
            	 log.log(Level.SEVERE, "Scene Crash", e);
             }
    	});
    	/***
    	 * ak su vsetky udaje vyplnene zavola metodu ktora updatne zaznam v databaze a vrati sa na predoslu scenu
    	 */
    	saveButton.setOnAction(event->{
    		
    		LocalDate date = datePicker.getValue();
    		String name = eventNameTextField.getText();
    		String location = locationTextField.getText();
    		try {
    		if (date != null && !name.equals("") && !location.equals("") && !entryFeeTextField.getText().equals("") && categoryComboBox.getSelectionModel().getSelectedItem() != null) {
    			int entry = Integer.parseInt(entryFeeTextField.getText());
    			int category = categoryComboBox.getSelectionModel().getSelectedItem().getId();
    			
    			Main.remoteEvent.updateEvent(Main.selectedEvent.getId(),name,location,date.toString(),category,entry);
    			backButton.fire();
    		}else {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setContentText("Please fill all fields");
    			alert.showAndWait();
    		}
    		
    		}
    		catch(Exception e) {
    			log.log(Level.SEVERE, "updateEvent Crash", e);
    		}
    	});
    	
    	
    }
}