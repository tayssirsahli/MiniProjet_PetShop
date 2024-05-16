package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;



public class CrudPetsController implements Initializable{
	
	
	
	

	    @FXML
	    private TextField PetId;

	    @FXML
	    private TextField PetName;

	    @FXML
	    private TextField PetRaice;

	    @FXML
	    private TextField PetAge;

	    @FXML
	    private Button AddBtn;

	    @FXML
	    private Button UpdateBtn;

	    @FXML
	    private Button DeleteBtn;

	    @FXML
	    private TextField PetPrice;

	    @FXML
	    private TextField searchLabel;

	    @FXML
	    private TableView<?> TablePets;

	    @FXML
	    private TableColumn<?, ?> TPetId;

	    @FXML
	    private TableColumn<?, ?> TPetName;

	    @FXML
	    private TableColumn<?, ?> TPetRace;

	    @FXML
	    private TableColumn<?, ?> TPetAge;

	    @FXML
	    private TableColumn<?, ?> TPetPrice;

	    @FXML
	    void AddPet(ActionEvent event) {

	    }

	    @FXML
	    void DeletePet(ActionEvent event) {

	    }

	    @FXML
	    void UpdatePet(ActionEvent event) {

	    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
