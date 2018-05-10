package controllers;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import models.Category;
import models.Event;
import models.User;


public class FindEventsController implements Initializable{

	@FXML
	private TableView<Event> findEventsTableView;
	
    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event,String> nameColumn;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> categoryColumn;

    @FXML
    private TableColumn<Event, String> userColumn;
    
    @FXML
    private Button followButton;
    
    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Event, Integer> entryColumn;
    
    @FXML
    private TextField locationTextField;
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private ComboBox<Category> categoryComboBox;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button applyButton;
    
    private Logger log = Logger.getLogger(FindEventsController.class.getName());
    
    private ObservableList<Event> events;

    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	
    	ArrayList<String> filter = new ArrayList<>();
    	
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("nameColumnProperty")));
    	locationColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("locationColumnProperty")));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("categoryColumnProperty")));
    	entryColumn.setCellValueFactory(new PropertyValueFactory<Event,Integer>(Main.clientProperties.getProperty("entryColumnProperty")));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("dateColumnProperty")));
    	userColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("userColumnProperty")));
    	ObservableList<Category> categories = FXCollections.observableArrayList(Main.remoteCategory.getCategories());
    	categoryComboBox.getItems().addAll(categories);
    	events = FXCollections.observableArrayList(Main.remoteEvent.getNewEvents(User.getInstance().getId(),filter));
    	findEventsTableView.setItems(events);
    	
    	/***
    	 * prida event do zoznamu lajkovanych/followovanych pre daneho pouzivatela
    	 */
    	followButton.setOnAction(event->{
    		Event selectedEvent = findEventsTableView.getSelectionModel().getSelectedItem();
    		int index = findEventsTableView.getSelectionModel().getSelectedIndex();
    		if(selectedEvent != null) {
    			events.remove(index);
    			findEventsTableView.setItems(events);
    			Main.remoteEvent.setLikedEvent(selectedEvent.getId(), User.getInstance().getId());
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
                 log.log(Level.SEVERE, "Scene crash", e);
             }
    	});
    	
    	/***
    	 * po stlaceni sa zoberu vsetky kriteria podla ktorych chce uzivatel filtrovat a zavola sa metoda ktora vrati dane eventy podla filtra
    	 */
    	applyButton.setOnAction(event->{
    		filter.clear();
    		if(!locationTextField.getText().isEmpty()) {
    			filter.add("AND ev.location = '"+locationTextField.getText()+"' ");
    		}
    		if(datePicker.getValue()!=null) {
    			filter.add("AND ev.date_at = '"+datePicker.getValue().toString()+"' ");
    		}
    		if(!nameTextField.getText().isEmpty()) {
    			filter.add("AND ev.name = '"+nameTextField.getText()+"' ");
    		}
    		if(categoryComboBox.getSelectionModel().getSelectedItem()!=null) {
    			filter.add("AND ev.category_id = "+categoryComboBox.getSelectionModel().getSelectedItem().getId()+" ");
    		}
    		ObservableList<Event> filteredEvents = FXCollections.observableArrayList(Main.remoteEvent.getNewEvents(User.getInstance().getId(), filter));
    		findEventsTableView.setItems(filteredEvents);
    		events = filteredEvents;
    	});
    }

}
