package controllers;

import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import models.Event;
import models.User;

public class MyEventsController implements Initializable {

    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event, String> nameColumn;

    @FXML
    private Button removeEventButton;

    @FXML
    private Button modifyEventButton;

    @FXML
    private TableView<Event> myEventsTableView;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> categoryTableColumn;

    @FXML
    private Button createEventButton;

    @FXML
    private TableColumn<Event, Integer> entryColumn;
    
    @FXML
    private Button backButton;
   
    private Logger log = Logger.getLogger(MyEventsController.class.getName());
    
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("nameColumnProperty")));
    	locationColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("locationColumnProperty")));
    	categoryTableColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("categoryColumnProperty")));
    	entryColumn.setCellValueFactory(new PropertyValueFactory<Event,Integer>(Main.clientProperties.getProperty("entryColumnProperty")));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("dateColumnProperty")));
    	ObservableList<Event> events = FXCollections.observableArrayList(Main.remoteEvent.getUserEvents(User.getInstance().getId()));
    	myEventsTableView.setItems(events);
    	
    	/***
    	 * prehodi pouzivatela na scenu s moznostou vytvorenia eventu
    	 */
    	createEventButton.setOnAction(event->{
    		
    		 Stage stage=(Stage) createEventButton.getScene().getWindow();
             try {
                 Parent root;
                 ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                 root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("createEventFXML")),resource);
                 Scene scene = new Scene(root);
                 stage.setScene(scene);
                 stage.show();
             } catch(Exception e) {
            	 log.log(Level.SEVERE, "Scene Crash", e);
             }
    		
    	});
    	/***
    	 * vymaze event ktory pouzivatel vytvoril
    	 */
    	removeEventButton.setOnAction(event->{
    		Event selectedEvent = myEventsTableView.getSelectionModel().getSelectedItem();
    		int index = myEventsTableView.getSelectionModel().getSelectedIndex();
    		try {
    		if(selectedEvent != null) {
    			events.remove(index);
    			myEventsTableView.setItems(events);
    			Main.remoteEvent.removeEvent(selectedEvent.getId());
    			
    		}
    		else {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setContentText("You haven't selected any event");
    			alert.showAndWait();
    		}
    		}
    		catch(Exception e) {
    			log.log(Level.SEVERE, "removeEvent Crash", e);
    		}
    	});
    	/***
    	 * otvori scenu s moznostou modifikacie vybraneho eventu
    	 */
    	modifyEventButton.setOnAction(event->{
    		Main.selectedEvent = myEventsTableView.getSelectionModel().getSelectedItem();
    		if(Main.selectedEvent != null) {
    			 Stage stage=(Stage) modifyEventButton.getScene().getWindow();
                 try {
                     Parent root;
                     ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                     root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("modifyEventFXML")),resource);
                     Scene scene = new Scene(root);
                     stage.setScene(scene);
                     stage.show();
                 } catch(Exception e) {
                	 log.log(Level.SEVERE, "Scene Crash", e);
                 }
        		
    			
    		}
    		else {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setContentText("You haven't selected any event");
    			alert.showAndWait();
    		}
    	});
    	/***
    	 * vrati sa na predoslu scenu
    	 */
    	backButton.setOnAction(event->{
    		
    		 Stage stage=(Stage) backButton.getScene().getWindow();
             try {
                 Parent root;
                 ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                 root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("profileFXML")),resource);
                 Scene scene = new Scene(root);
                 stage.setScene(scene);
                 stage.show();
             } catch(Exception e) {
            	 log.log(Level.SEVERE, "Scene Crash", e);
             }
    		
    		
    	});
    	
    	
    }

}
