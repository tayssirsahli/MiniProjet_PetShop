package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class dashboardController implements Initializable {


	 @FXML
	    private Button closeBtn;

	    @FXML
	    private Button HomeId;

	    @FXML
	    private Button AddPets;

	    @FXML
	    private Button PurchaseId;

	    @FXML
	    private Button Productsid;
	    @FXML
	    private BorderPane bp;
	    @FXML
	    private AnchorPane ap;
	    
    private FXMLLoader loader;

    

    @FXML
    void close() {
		System.exit(0);    
		}
    
    
    @FXML
    void AddPets(MouseEvent event) {
    	loadPage2("bodyAdd");
    }

    @FXML
    void DashboardWindow(MouseEvent event) {
    	loadPage2("bodyDashboard");


    }

    @FXML
    void ProductsWindow(MouseEvent event) {
        loadPage2("Products");
    }


	
	  @FXML 
	  void PurchaseWindow(MouseEvent event) { 
	    	loadPage2("Purchase");

		  }
	  
	  
	  @FXML
	  void hideContent(MouseEvent event) {
	      bp.setCenter(null);
	  }
	 
    @FXML
    void logout(MouseEvent event) {
    }
    public void loadPage(String page) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(page + ".fxml"));
		fxmlLoader.setRoot(bp); 
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        bp.setCenter(root);
    }
    
    public void loadPage2(String page) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(page + ".fxml"));
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        bp.setCenter(root);
    }

    
   
	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }
}
