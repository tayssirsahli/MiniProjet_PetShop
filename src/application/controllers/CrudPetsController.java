package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.beans.Animal;
import application.beans.Article;
import connexion.Connexion;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CrudPetsController implements Initializable {

	@FXML
	private TextField PetName;

	@FXML
	private TextField PetRaice;

	@FXML
	private DatePicker PetAge;

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
	private TableView<Animal> TablePets;

	@FXML
	private TableColumn<Article, Integer> TPetQauntity;
	@FXML
	private TableColumn<Article, String> TPetName;

	@FXML
	private TableColumn<Animal, String> TPetRace;

	@FXML
	private TableColumn<Animal, DatePicker> TPetAge;

	@FXML
	private TableColumn<Article, String> TPetPrice;

	@FXML
	private TextField PetQuantity;
	@FXML
	private Button clearBtn;

	@FXML
	void AddPet(ActionEvent event) {
		// Récupérer les valeurs des champs textuels
		String nom = PetName.getText();
		String race = PetRaice.getText();
		LocalDate dateNaissance = PetAge.getValue();
		double prix = Double.parseDouble(PetPrice.getText());
		int quantite = Integer.parseInt(PetQuantity.getText());
		Animal newAnimal = new Animal(nom, quantite, prix, race, dateNaissance);

		try {
			// Insertion des données dans la table Article
			String queryArticle = "INSERT INTO Article (nom, quantité, prix) VALUES (?, ?, ?)";
			PreparedStatement stArticle = Connexion.getConn().prepareStatement(queryArticle,
					Statement.RETURN_GENERATED_KEYS);
			stArticle.setString(1, nom); // Insérer le nom dans la colonne 'nom'
			stArticle.setInt(2, quantite); // Insérer la quantité dans la colonne 'quantité'
			stArticle.setDouble(3, prix); // Insérer le prix dans la colonne 'prix'

			int rowsAffectedArticle = stArticle.executeUpdate();
			if (rowsAffectedArticle == 0) {
				showAlert(AlertType.ERROR, "Insert Error!", "Failed to insert data into Article table");
				return;
			}
			ResultSet generatedKeys = stArticle.getGeneratedKeys();
			int articleId;
			if (generatedKeys.next()) {
				articleId = generatedKeys.getInt(1);
			} else {
				throw new SQLException("Failed to get generated article ID.");
			}

			// Insertion des données dans la table Animal
			String queryAnimal = "INSERT INTO Animal (id,race, dateNaissance) VALUES (?,?, ?)";
			PreparedStatement stAnimal = Connexion.getConn().prepareStatement(queryAnimal);
			stAnimal.setInt(1, articleId);
			stAnimal.setString(2, race);
			stAnimal.setDate(3, java.sql.Date.valueOf(dateNaissance));

			int rowsAffectedAnimal = stAnimal.executeUpdate();
			if (rowsAffectedAnimal == 0) {
				showAlert(AlertType.ERROR, "Insert Error!", "Failed to insert data into Animal table");
				return;
			}

			// Ajout de l'animal à la liste observable
			list1.add(newAnimal);

			// Affichage d'une notification de succès
			showAlert(AlertType.INFORMATION, "Success", "Animal added successfully");

			// Effacement des champs textuels après l'ajout
			clearFields();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "SQL Error", "An error occurred while executing SQL queries");
		}
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.show();
	}

	private void clearFields() {
		PetPrice.clear();
		PetRaice.clear();
		PetName.clear();
		PetAge.setValue(null); // Assurez-vous que PetAge est bien un DatePicker
		PetQuantity.clear();
	}

	@FXML
	void clear(ActionEvent event) {
		clearFields();

	}

	@FXML
	void UpdatePet(ActionEvent event) {
		Animal animalSelectionne = TablePets.getSelectionModel().getSelectedItem();
		if (animalSelectionne == null) {
			showAlert(AlertType.ERROR, "Aucun animal sélectionné", "Veuillez sélectionner un animal à mettre à jour.");
			return;
		}

		String nouveauNom = PetName.getText();
		String nouvelleRace = PetRaice.getText();
		LocalDate dateNaissance = PetAge.getValue();
		double nouveauPrix;
		int nouvelleQuantite;

		try {
			nouveauPrix = Double.parseDouble(PetPrice.getText());
			nouvelleQuantite = Integer.parseInt(PetQuantity.getText());
		} catch (NumberFormatException e) {
			showAlert(AlertType.ERROR, "Format incorrect",
					"Veuillez entrer des valeurs valides pour le prix et la quantité.");
			return;
		}

		if (nouveauNom.isEmpty() || nouvelleRace.isEmpty() || dateNaissance == null) {
			showAlert(AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
			return;
		}

		String query = "SELECT id FROM article WHERE nom = ?";
		int idAnimal = 0;
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query);
			pstmt.setString(1, animalSelectionne.getNom());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				idAnimal = rs.getInt("id");
			} else {
				showAlert(AlertType.ERROR, "Animal introuvable", "L'animal spécifié n'existe pas.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la récupération de l'animal.");
			return;
		}

		String updateArticleQuery = "UPDATE article SET nom = ?, quantité = ?, prix = ? WHERE id = ?";
		try {
			PreparedStatement pstmtUpdateArticle = Connexion.getConn().prepareStatement(updateArticleQuery);
			pstmtUpdateArticle.setString(1, nouveauNom);
			pstmtUpdateArticle.setInt(2, nouvelleQuantite);
			pstmtUpdateArticle.setDouble(3, nouveauPrix);
			pstmtUpdateArticle.setInt(4, idAnimal);
			int rowsAffectedArticle = pstmtUpdateArticle.executeUpdate();

			if (rowsAffectedArticle > 0) {
				String updateAnimalQuery = "UPDATE animal SET race = ?, dateNaissance = ? WHERE id = ?";
				PreparedStatement pstmtUpdateAnimal = Connexion.getConn().prepareStatement(updateAnimalQuery);
				pstmtUpdateAnimal.setString(1, nouvelleRace);
				pstmtUpdateAnimal.setDate(2, java.sql.Date.valueOf(dateNaissance));
				pstmtUpdateAnimal.setInt(3, idAnimal);
				pstmtUpdateAnimal.executeUpdate();

				animalSelectionne.setNom(nouveauNom);
				animalSelectionne.setQuantite(nouvelleQuantite);
				animalSelectionne.setPrix(nouveauPrix);
				animalSelectionne.setRace(nouvelleRace);
				animalSelectionne.setDateNaissance(dateNaissance);
				TablePets.refresh();

				showAlert(AlertType.INFORMATION, "Mise à jour réussie", "L'animal a été mis à jour avec succès.");
			} else {
				showAlert(AlertType.ERROR, "Erreur lors de la mise à jour", "La mise à jour de l'animal a échoué.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la mise à jour de l'animal.");
		}
	}

	@FXML
	void DeletePet(ActionEvent event) {
		// Récupérer l'animal sélectionné dans la table
		Animal animalSelectionne = TablePets.getSelectionModel().getSelectedItem();
		if (animalSelectionne == null) {
			showAlert(AlertType.ERROR, "Aucun animal sélectionné", "Veuillez sélectionner un animal à supprimer.");
			return;
		}

		// Afficher une boîte de dialogue de confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation de suppression");
		alert.setHeaderText(null);
		alert.setContentText("Êtes-vous sûr de vouloir supprimer l'animal sélectionné ?");
		if (alert.showAndWait().get() != ButtonType.OK) {
			return;
		}

		// Requête pour récupérer l'ID de l'animal à partir de son nom
		String query = "SELECT id FROM article WHERE nom = ?";
		int idAnimal = 0;
		try {
			PreparedStatement pstmt = Connexion.getConn().prepareStatement(query);
			pstmt.setString(1, animalSelectionne.getNom());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				idAnimal = rs.getInt("id");
				System.out.println(idAnimal);
			} else {
				showAlert(AlertType.ERROR, "Animal introuvable", "L'animal spécifié n'existe pas.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la récupération de l'animal.");
			return;
		}

		// Requête pour supprimer l'animal de la table 'animal'
		String deleteAnimalQuery = "DELETE FROM animal WHERE id = ?";
		String deleteArticlelQuery = "DELETE FROM article WHERE id = ?";

		try {
			PreparedStatement pstmtDeleteAnimal = Connexion.getConn().prepareStatement(deleteAnimalQuery);
			pstmtDeleteAnimal.setInt(1, idAnimal);
			int rowsAffectedAnimal = pstmtDeleteAnimal.executeUpdate();
			PreparedStatement pstmtDeleteArtcl = Connexion.getConn().prepareStatement(deleteArticlelQuery);
			pstmtDeleteArtcl.setInt(1, idAnimal);
			int rowsAffectedArticle = pstmtDeleteArtcl.executeUpdate();

			// Si la suppression réussit, retirer l'animal de la liste observable et
			// actualiser la table
			if (rowsAffectedAnimal > 0 && rowsAffectedArticle > 0) {
				list1.remove(animalSelectionne);
				TablePets.refresh();
				showAlert(AlertType.INFORMATION, "Suppression réussie", "L'animal a été supprimé avec succès.");
			} else {
				showAlert(AlertType.ERROR, "Erreur lors de la suppression", "La suppression de l'animal a échoué.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la suppression de l'animal.");
		}

	}

	ObservableList<Animal> list1 = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TablePets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				PetName.setText(newSelection.getNom());
				PetQuantity.setText(String.valueOf(newSelection.getQuantite()));
				PetPrice.setText(String.valueOf(newSelection.getPrix()));
				PetRaice.setText(String.valueOf(newSelection.getRace()));
				PetAge.setValue(newSelection.getDateNaissance()); // Utilisez setValue pour le DatePicker

			}
		});
		String query = "SELECT art.nom, a.race, a.dateNaissance, art.prix, art.quantité FROM animal a INNER JOIN Article art ON a.id = art.id";

		try {
			PreparedStatement pst = Connexion.getConn().prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			list1.clear();

			while (rs.next()) {
				String nom = rs.getString("nom");
				String race = rs.getString("race");
				LocalDate dateNaissance = rs.getDate("dateNaissance").toLocalDate();
				double prix = rs.getDouble("prix");
				int quantite = rs.getInt("quantité");

				Animal animal = new Animal(nom, quantite, prix, race, dateNaissance);
				list1.add(animal);
			}
			TablePets.setItems(list1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Liaison des colonnes de la table aux propriétés de l'objet Animal
		TPetName.setCellValueFactory(new PropertyValueFactory<>("nom"));
		TPetRace.setCellValueFactory(new PropertyValueFactory<>("race"));
		TPetAge.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
		TPetPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
		TPetQauntity.setCellValueFactory(new PropertyValueFactory<>("quantite"));

		// Affichage de la liste des animaux dans le tableau
		TablePets.setItems(list1);
		// Ajout d'un écouteur de texte pour la recherche

		searchLabel.textProperty().addListener((observable, oldValue, newValue) -> {
			searchAnimal(newValue);
		});
	}

	private void searchAnimal(String searchText) {
		ObservableList<Animal> filteredList = FXCollections.observableArrayList();

		if (searchText == null || searchText.isEmpty()) {
			// Si la recherche est vide, afficher tous les animaux
			filteredList.addAll(list1);
		} else {
			// Filtrer la liste en fonction du nom de l'animal
			for (Animal animal : list1) {
				if (animal.getNom().toLowerCase().contains(searchText.toLowerCase())) {
					filteredList.add(animal);
				}
			}
		}

		// Mettre à jour la table avec la liste filtrée
		TablePets.setItems(filteredList);
	}

}
