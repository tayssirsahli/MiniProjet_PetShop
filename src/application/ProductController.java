package application;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.beans.Produit;
import connexion.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductController implements Initializable {

	  @FXML
	    private TextField ProdId;

	    @FXML
	    private TextField ProdName;

	    @FXML
	    private TextField Prodquant;

	    @FXML
	    private Button AddBtn;

	    @FXML
	    private Button UpdateBtn;

	    @FXML
	    private Button DeleteBtn;

	    @FXML
	    private TextField ProdPrice;

	    @FXML
	    private ComboBox<String> comboCategorie;
	    
	    @FXML
	    private TableColumn<Produit, Integer> TProdCategory;
	    
	    @FXML
	    private TextField searchLabel;

	    @FXML
	    private TableView<Produit> TableProds;

	    @FXML
	    private TableColumn<Produit,Integer> TProdId;

	    @FXML
	    private TableColumn<Produit, String> TProdName;

	    @FXML
	    private TableColumn<Produit,Integer> TProdquan;

	    @FXML
	    private TableColumn<Produit, Double> TProdPrice;

	    @FXML
	    void AddProd(ActionEvent event) {

	    }

	    @FXML
	    void DeleteProd(ActionEvent event) {

	    }

	    @FXML
	    void UpdateProd(ActionEvent event) {

	    }
	ObservableList<Produit> list1 = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<String> list = new ArrayList<String>();
		

		String query = "select nom from categorie";
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = Connexion.getConn().createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {

				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ObservableList<String> cb = FXCollections.observableArrayList(list);
		comboCategorie.setItems(cb);
		TProdId.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("id"));
		TProdName.setCellValueFactory(new PropertyValueFactory<Produit, String>("nom"));
		TProdquan.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("quantite"));
		TProdPrice.setCellValueFactory(new PropertyValueFactory<Produit, Double>("prix"));
		TProdCategory.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("categorie"));

		

		TableProds.setItems(list1);
		
	}

}
