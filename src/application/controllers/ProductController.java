package application.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.beans.Article;
import application.beans.Produit;
import connexion.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	private TableColumn<Article, String> TProdName;

	@FXML
	private TableColumn<Article, Integer> TProdquan;

	@FXML
	private TableColumn<Article, Double> TProdPrice;

	@FXML
	void AddProd(ActionEvent event) {
		String nom = ProdName.getText();
		int quantite = Integer.parseInt(Prodquant.getText());
		double prix = Double.parseDouble(ProdPrice.getText());
		String categorie = comboCategorie.getValue();

		if (nom.isEmpty() || categorie.isEmpty()) {
			return;
		}

		String query = "select id from categorie where nom = '" + categorie + "'";
		ResultSet rs = null;
		Statement stmt = null;
		int id = 0;
		try {
			stmt = Connexion.getConn().createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement st;
		Produit nouveauProduit = new Produit(nom, quantite, prix, id);

		try {
			st = Connexion.getConn().prepareStatement("INSERT INTO article ( nom, quantité, prix) VALUES (?, ?, ?)");
			st.setString(1, nom);
			st.setInt(2, quantite);
			st.setDouble(3, prix);

			int count = st.executeUpdate();

			st = Connexion.getConn().prepareStatement("INSERT INTO produit (categorie) VALUES (?)");
			st.setInt(1, id);
			int countP = st.executeUpdate();

			if (count > 0 && countP > 0)
				list1.add(nouveauProduit);

			else
				showAlert(AlertType.ERROR, "Insert Error!", "An error occurred while adding product");

		} catch (SQLException e) {

			e.printStackTrace();

		}

		clearFields();
	}

	private void clearFields() {
		ProdName.clear();
		Prodquant.clear();
		ProdPrice.clear();
		comboCategorie.getSelectionModel().clearSelection();
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.show();
	}

	@FXML
	void DeleteProd(ActionEvent event) {
		Produit produitSelectionne = TableProds.getSelectionModel().getSelectedItem();
		if (produitSelectionne == null) {
			showAlert(AlertType.ERROR, "Aucun produit sélectionné", "Veuillez sélectionner un produit à supprimer.");
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation de suppression");
		alert.setHeaderText(null);
		alert.setContentText("Êtes-vous sûr de vouloir supprimer le produit sélectionné ?");
		if (alert.showAndWait().get() != ButtonType.OK) {
			return;
		}
		String query = "SELECT id FROM article WHERE nom = ?";
		int Id = 0;
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query);
			pstmt.setString(1, produitSelectionne.getNom());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Id = rs.getInt("id");
			} else {
				showAlert(AlertType.ERROR, "produit introuvable", "Le produit spécifiée n'existe pas.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL",
					"Une erreur s'est produite lors de la récupération de la catégorie.");
			return;
		}
		String deleteArticleQuery = "DELETE FROM article WHERE id = ?";
		String deleteProduitQuery = "DELETE FROM produit WHERE id = ?";
		try {
			PreparedStatement pstmtDeleteArticle = Connexion.getConn().prepareStatement(deleteArticleQuery);
			pstmtDeleteArticle.setInt(1, Id);
			int rowsAffectedArticle = pstmtDeleteArticle.executeUpdate();

			PreparedStatement pstmtDeleteProduit = Connexion.getConn().prepareStatement(deleteProduitQuery);
			pstmtDeleteProduit.setInt(1, Id);
			int rowsAffectedProduit = pstmtDeleteProduit.executeUpdate();

			if (rowsAffectedArticle > 0 && rowsAffectedProduit > 0) {
				list1.remove(produitSelectionne);
				TableProds.refresh();
				showAlert(AlertType.INFORMATION, "Suppression réussie", "Le produit a été supprimé avec succès.");
			} else {
				showAlert(AlertType.ERROR, "Erreur lors de la suppression", "La suppression du produit a échoué.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la suppression du produit.");
		}
	}

	@FXML
	void UpdateProd(ActionEvent event) {
		Produit produitSelectionne = TableProds.getSelectionModel().getSelectedItem();
		if (produitSelectionne == null) {
			showAlert(AlertType.ERROR, "Aucun produit sélectionné",
					"Veuillez sélectionner un produit à mettre à jour.");
			return;
		}

		String nouveauNom = ProdName.getText();
		int nouvelleQuantite = Integer.parseInt(Prodquant.getText());
		double nouveauPrix = Double.parseDouble(ProdPrice.getText());
		String nouvelleCategorie = comboCategorie.getValue();

		if (nouveauNom.isEmpty() || nouvelleCategorie.isEmpty()) {
			showAlert(AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
			return;
		}

		String query = "SELECT id FROM categorie WHERE nom = ?";
		int nouvelleCategorieId = 0;
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query);
			pstmt.setString(1, nouvelleCategorie);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				nouvelleCategorieId = rs.getInt("id");
			} else {
				showAlert(AlertType.ERROR, "Catégorie introuvable", "La catégorie spécifiée n'existe pas.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL",
					"Une erreur s'est produite lors de la récupération de la catégorie.");
			return;
		}

		String query2 = "SELECT id FROM article WHERE nom = ?";
		int IdPRODUIT = 0;
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query2);
			pstmt.setString(1, produitSelectionne.getNom());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				IdPRODUIT = rs.getInt("id");
			} else {
				showAlert(AlertType.ERROR, "produit introuvable", "Le produit spécifiée n'existe pas.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL",
					"Une erreur s'est produite lors de la récupération de l'id produit.");
			return;
		}

		String updateQuery = "UPDATE article SET nom = ?, quantité = ?, prix = ? WHERE id = ?";
		try {
			PreparedStatement pstmtUpdate = Connexion.getConn().prepareStatement(updateQuery);
			pstmtUpdate.setString(1, nouveauNom);
			pstmtUpdate.setInt(2, nouvelleQuantite);
			pstmtUpdate.setDouble(3, nouveauPrix);

			pstmtUpdate.setInt(4, IdPRODUIT);
			int rowsAffected = pstmtUpdate.executeUpdate();
			if (rowsAffected > 0) {
				String updateCategorieQuery = "UPDATE produit SET categorie = ? WHERE id = ?";
				PreparedStatement pstmtUpdateCategorie = Connexion.getConn().prepareStatement(updateCategorieQuery);
				pstmtUpdateCategorie.setInt(1, nouvelleCategorieId);
				pstmtUpdateCategorie.setInt(2, produitSelectionne.getId());
				pstmtUpdateCategorie.executeUpdate();

				produitSelectionne.setNom(nouveauNom);
				produitSelectionne.setQuantite(nouvelleQuantite);
				produitSelectionne.setPrix(nouveauPrix);
				produitSelectionne.setCategorie(nouvelleCategorieId);
				TableProds.refresh();

				showAlert(AlertType.INFORMATION, "Mise à jour réussie", "Le produit a été mis à jour avec succès.");
			} else {
				showAlert(AlertType.ERROR, "Erreur lors de la mise à jour", "La mise à jour du produit a échoué.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la mise à jour du produit.");
		}
	}

	@FXML
	void searchProduct(String searchText) {
		ObservableList<Produit> filteredList = FXCollections.observableArrayList();

		if (searchText == null || searchText.isEmpty()) {
			filteredList.addAll(list1);
		} else {
			for (Produit produit : list1) {
				if (produit.getNom().toLowerCase().contains(searchText.toLowerCase())) {
					filteredList.add(produit);
				}
			}
		}
		TableProds.setItems(filteredList);
	}

	private String getCategorieName(int categorieId) {
		String query = "SELECT nom FROM categorie WHERE id = ?";
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query);
			pstmt.setInt(1, categorieId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("nom");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	ObservableList<Produit> list1 = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TableProds.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				ProdName.setText(newSelection.getNom());
				Prodquant.setText(String.valueOf(newSelection.getQuantite()));
				ProdPrice.setText(String.valueOf(newSelection.getPrix()));

				comboCategorie.setValue(getCategorieName(newSelection.getCategorie()));
			}
		});

		String query = "SELECT a.nom, a.quantité, a.prix, p.categorie FROM article a INNER JOIN produit p ON a.id = p.id";
		try {
			Statement stmt = Connexion.getConn().createStatement();
			ResultSet rs = stmt.executeQuery(query);

			list1.clear();

			while (rs.next()) {
				String nom = rs.getString("nom");
				int quantite = rs.getInt("quantité");
				double prix = rs.getDouble("prix");
				int categorie = rs.getInt("categorie");

				Produit produit = new Produit(nom, quantite, prix, categorie);

				list1.add(produit);
			}

			TableProds.setItems(list1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		TProdName.setCellValueFactory(new PropertyValueFactory<>("nom"));
		TProdquan.setCellValueFactory(new PropertyValueFactory<>("quantite"));
		TProdPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
		TProdCategory.setCellValueFactory(new PropertyValueFactory<>("categorie"));

		ArrayList<String> listCategories = new ArrayList<>();
		String queryCategories = "SELECT nom FROM categorie";
		try {
			Statement stmtCategories = Connexion.getConn().createStatement();
			ResultSet rsCategories = stmtCategories.executeQuery(queryCategories);
			while (rsCategories.next()) {
				listCategories.add(rsCategories.getString("nom"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ObservableList<String> cb = FXCollections.observableArrayList(listCategories);
		comboCategorie.setItems(cb);

		TableProds.setItems(list1);
		searchLabel.textProperty().addListener((observable, oldValue, newValue) -> {
			searchProduct(newValue);
		});
	}

}
