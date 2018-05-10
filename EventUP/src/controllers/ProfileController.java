package controllers;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import models.Category;
import models.Event;
import models.User;

public class ProfileController implements Initializable {

    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event, String> nameColumn;

    @FXML
    private Button eventsButton;
    
    @FXML
    private Button myEventsButton;
    
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Event> myEventsTableView;

    @FXML
    private Label userFullNameLabel;

    @FXML
    private TableColumn<Event, Integer> entryColumn;
    
    @FXML
    private TableColumn<Event, String> categoryTableColumn;
    
    @FXML
    private TableColumn<Event, String> dateColumn;
    
    @FXML
    private Button pdfButton;
    
    private Logger log = Logger.getLogger(ProfileController.class.getName());
    
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
    	userFullNameLabel.setText(User.getInstance().getName());
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("nameColumnProperty")));
    	locationColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("locationColumnProperty")));
    	categoryTableColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("categoryColumnProperty")));
    	entryColumn.setCellValueFactory(new PropertyValueFactory<Event,Integer>(Main.clientProperties.getProperty("entryColumnProperty")));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<Event,String>(Main.clientProperties.getProperty("dateColumnProperty")));
    	ObservableList<Event> events = FXCollections.observableArrayList(Main.remoteEvent.getUserLikedEvents(User.getInstance().getId()));
    	myEventsTableView.setItems(events);
    	
    	/***
    	 * otvori scenu s eventami ktore vytvoril prihlasny pouzivatel
    	 */
    	myEventsButton.setOnAction(event->{
    		
    		 Stage stage=(Stage) myEventsButton.getScene().getWindow();
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
    	 * otvori scenu kde si moze prezerat eventy inych pouzivatelov
    	 */
    	eventsButton.setOnAction(event->{
    		 Stage stage=(Stage) eventsButton.getScene().getWindow();
             try {
                 Parent root;
                 ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                 root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("findEventsFXML")),resource);
                 Scene scene = new Scene(root);
                 stage.setScene(scene);
                 stage.show();
             } catch(Exception e) {
            	 log.log(Level.SEVERE, "Scene Crash", e);
             }
    		
    	});
    	/***
    	 * odhlasi pouzivatela a vrati sa na scenu s prihlasenim
    	 */
    	logoutButton.setOnAction(event->{
    		
    		Stage stage=(Stage) logoutButton.getScene().getWindow();
            try {
                Parent root;
                ResourceBundle resource = ResourceBundle.getBundle(Main.clientProperties.getProperty("languagePrefix"),LoginController.language);
                root = FXMLLoader.load(getClass().getResource(Main.clientProperties.getProperty("loginFXML")),resource);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
            	log.log(Level.SEVERE, "Scene Crash", e);
            }
    		
    	});
    	/***
    	 * vytvori pdf dokument v adresari aplikacie vybraneho eventu
    	 */
    	pdfButton.setOnAction(event->{
    		
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Event selectedEvent = myEventsTableView.getSelectionModel().getSelectedItem();
			if (selectedEvent != null) {
				Document document = new Document();
				try {
					PdfWriter writer = PdfWriter.getInstance(document,
							new FileOutputStream(selectedEvent.getName() + "_" + User.getInstance().getName() + ".pdf"));
					document.open();
					document.add(new Paragraph("EVENT INFO"));
					document.add(new Paragraph("\n"));
					document.add(new Paragraph("EVENT NAME: " + selectedEvent.getName()));
					document.add(new Paragraph("EVENT LOCATION: " + selectedEvent.getLocation()));
					document.add(new Paragraph("EVENT DATE: " + selectedEvent.getDate()));
					document.add(new Paragraph("EVENT CATEGORY: " + selectedEvent.getCategory()));
					document.add(new Paragraph("EVENT ENTRY: " + selectedEvent.getEntry()));
					document.add(new Paragraph("EVENT OWNER: " + selectedEvent.getUserFullName()));
					document.close();
					writer.close();
					alert.setContentText("PDF successfully created in app folder");
					alert.showAndWait();
				} catch (Exception e) {
					log.log(Level.SEVERE, "PDF CRASH", e);
				}
			} else {
				alert.setContentText("You haven't selected any event");
				alert.showAndWait();
				
			}
    		
    	});
    	
    	
    	
    	
    }
}

