package application.controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import application.beans.Article;
import application.beans.Commande;
import application.beans.LigneCommande;
import connexion.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class PurchaseController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<String> nomChoice;

    @FXML
    private ComboBox<String> Typechoise;

    @FXML
    private Spinner<Integer> quantityChoice;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField amountInput;

    @FXML
    private Label restLabel;

    @FXML
    private Button payBtn;

    @FXML
    private TableView<LigneCommande> tablePur;

    @FXML
    private TableColumn<LigneCommande, String> purNameCol;

    @FXML
    private TableColumn<LigneCommande, Integer> purQuantityCol;

    @FXML
    private TableColumn<LigneCommande, Double> purPriceCol;

    @FXML
    private TableColumn<LigneCommande, Double> purTotalCol;
    @FXML
    private TableColumn<LigneCommande, Integer> idpur;

    @FXML
    private Button deleteId;
    
    private Commande currentCommande;

    @FXML
    void AddPur(ActionEvent event) {
        String selectedName = nomChoice.getValue();
        String selectedType = Typechoise.getValue();
        Integer quantity = quantityChoice.getValue();

        if (selectedName == null || selectedType == null || quantity == null) {
            showAlert(AlertType.ERROR, "Input Error", "Please make sure all fields are selected.");
            return;
        }

        Article article = getArticleByName(selectedName);
        if (article == null) {
            showAlert(AlertType.ERROR, "Database Error", "Article not found in database.");
            return;
        }

        double total = article.getPrix() * quantity;

        if (currentCommande == null || !"non".equals(currentCommande.getType())) {
            currentCommande = createNewCommande();
            if (currentCommande == null) {
                showAlert(AlertType.ERROR, "Database Error", "Failed to create a new order.");
                return;
            }
        }

        LigneCommande newOrderLine = new LigneCommande(quantity, total,article.getPrix(), article.getId(), currentCommande.getId());

        if (addOrderLineToDatabase(newOrderLine)) {
            tablePur.getItems().add(new LigneCommande(newOrderLine.getId(),selectedName, quantity,total,article.getPrix()));
            updateTotal();
        } else {
            showAlert(AlertType.ERROR, "Database Error", "Failed to add the order line to the database.");
        }
    }

    @FXML
    void PayBtn(ActionEvent event) {
        if (currentCommande == null) {
            showAlert(AlertType.ERROR, "Payment Error", "No current order to pay for.");
            return;
        }

        if ("non".equals(currentCommande.getType())) {
        	String totalText = totalLabel.getText();
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = null;
			try {
				number = format.parse(totalText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            double totalAmount = number.doubleValue();

            String updateCommandeQuery = "UPDATE commande SET Totale = ?, type = 'payer' WHERE id = ?";
            try (PreparedStatement pstmt = Connexion.getConn().prepareStatement(updateCommandeQuery)) {
                pstmt.setDouble(1, totalAmount);
                pstmt.setInt(2, currentCommande.getId());
                pstmt.executeUpdate();
                updateRest();
                //clearOrderLines();
                tablePur.getItems().clear();
                currentCommande = null;
                totalLabel.setText("0.00");
                amountInput.clear();
                restLabel.setText("0.00");

                showAlert(AlertType.INFORMATION, "Payment Successful", "Order has been successfully paid.");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error updating the order: " + e.getMessage());
            }
        }
    }

    private Article getArticleByName(String productName) {
        String query = "SELECT id, prix FROM article WHERE nom = ?";
        try (PreparedStatement pstmt = Connexion.getConn().prepareStatement(query)) {
            pstmt.setString(1, productName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Article(rs.getInt("id"), productName, rs.getDouble("prix"));
                }
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error fetching article from database: " + e.getMessage());
        }
        return null;
    }

    private Commande createNewCommande() {
        String insertQuery = "INSERT INTO commande (dateCommande, Totale, type) VALUES (NOW(), 0, 'non')";
        try (PreparedStatement pstmt = Connexion.getConn().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        double totale = 0.0;
                        Date dateC = new Date(System.currentTimeMillis());
                        String typeC = "non";
                        return new Commande(id, dateC, totale, typeC);
                    }
                }
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error creating new order: " + e.getMessage());
        }
        return null;
    }

    private boolean addOrderLineToDatabase(LigneCommande orderLine) {
        String insertQuery = "INSERT INTO lignecommande (commande_id, produit_id, quantité, prixLigneTotale) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = Connexion.getConn().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, orderLine.getCommande_id());
            pstmt.setInt(2, orderLine.getProduit_id());
            pstmt.setInt(3, orderLine.getQuantite());
            pstmt.setDouble(4, orderLine.getPrixLigneTotale());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderLine.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error adding order line to database: " + e.getMessage());
        }
		return false;
    }

	/*
	 * private void clearOrderLines() { String deleteQuery =
	 * "DELETE FROM lignecommande WHERE commande_id = ?"; try (PreparedStatement
	 * pstmt = Connexion.getConn().prepareStatement(deleteQuery)) { pstmt.setInt(1,
	 * currentCommande.getId()); pstmt.executeUpdate(); } catch (SQLException e) {
	 * showAlert(AlertType.ERROR, "Database Error", "Error deleting order lines: " +
	 * e.getMessage()); } }
	 */

    private void updateTotal() {
        double total = tablePur.getItems().stream().mapToDouble(LigneCommande::getPrixLigneTotale).sum();
        totalLabel.setText(String.format("%.2f", total));
    }

    private void updateRest() {
        
    	String total = totalLabel.getText();
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = null;
		try {
			number = format.parse(total);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        double totalP = number.doubleValue();
        String amount = amountInput.getText();
        NumberFormat formatP = NumberFormat.getInstance(Locale.FRANCE);
        Number numberp = null;
		try {
			numberp = formatP.parse(amount);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        double amountP = numberp.doubleValue();
            if(amountP<totalP )
            {
                showAlert(AlertType.ERROR, "Calculation Error", "Please enter a valid amount.");

            }
            double remaining = amountP - totalP;
            restLabel.setText(String.format("%.2f", remaining));
        
    }

    @FXML
    void onTypeChoiceChanged(ActionEvent event) {
        String selectedType = Typechoise.getValue();
        if (selectedType != null) {
            populateNomChoice(selectedType);
        }
    }

    private void populateNomChoice(String type) {
        String query;
        if (type.equalsIgnoreCase("animal")) {
            query = "SELECT a.nom FROM article a INNER JOIN animal an ON a.id = an.id";
        } else if (type.equalsIgnoreCase("product")) {
            query = "SELECT a.nom FROM article a INNER JOIN produit p ON a.id = p.id";
        } else {
            return;
        }

        ObservableList<String> names = FXCollections.observableArrayList();
        try (Statement stmt = Connexion.getConn().createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                names.add(rs.getString("nom"));
                System.out.println(names);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error fetching names from database: " + e.getMessage());
        }
        nomChoice.setItems(names);
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
    @FXML
    void deletebtn(ActionEvent event) {
        LigneCommande ligneSelectionne = tablePur.getSelectionModel().getSelectedItem();
        if (ligneSelectionne == null) {
            showAlert(AlertType.ERROR, "Aucun produit sélectionné", "Veuillez sélectionner un produit à supprimer.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer la ligne commande sélectionnée ?");
        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        int ligneCommandeId = ligneSelectionne.getId();

        String deleteLigneQuery = "DELETE FROM lignecommande WHERE id = ?";
        try (PreparedStatement pstmtDeleteLigne = Connexion.getConn().prepareStatement(deleteLigneQuery)) {
            pstmtDeleteLigne.setInt(1, ligneCommandeId);
            System.out.println(ligneCommandeId);
            int rowsAffected = pstmtDeleteLigne.executeUpdate();

            if (rowsAffected > 0) {
                list1.remove(ligneSelectionne);
                tablePur.refresh();
                showAlert(AlertType.INFORMATION, "Suppression réussie", "La ligne a été supprimée avec succès.");
                updateTotal();
            } else {
                showAlert(AlertType.ERROR, "Erreur lors de la suppression", "La suppression de la ligne a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la suppression de la ligne.");
        }
    }
    

    ObservableList<LigneCommande> list1 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> types = FXCollections.observableArrayList("animal", "product");
        Typechoise.setItems(types);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantityChoice.setValueFactory(valueFactory);
        
        purNameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        purQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        purPriceCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        purTotalCol.setCellValueFactory(new PropertyValueFactory<>("prixLigneTotale"));
        idpur.setCellValueFactory(new PropertyValueFactory<>("id"));

        

        loadCurrentOrder();
        loadOrderLines();
    }

    private void loadCurrentOrder() {
        String query = "SELECT id, type, Totale FROM commande WHERE type = 'non'";
        try (Statement stmt = Connexion.getConn().createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                currentCommande = new Commande(rs.getInt("id"), rs.getString("type"), rs.getDouble("Totale"));
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading current order: " + e.getMessage());
        }
    }

    private void loadOrderLines() {
        String query = "SELECT l.id,a.nom, l.quantité, a.prix, l.prixLigneTotale FROM lignecommande l INNER JOIN article a ON l.produit_id = a.id WHERE l.commande_id = ?";
        if (currentCommande == null) return;
        try (PreparedStatement pstmt = Connexion.getConn().prepareStatement(query)) {
            pstmt.setInt(1, currentCommande.getId());
            ResultSet rs = pstmt.executeQuery();
            list1.clear();
            while (rs.next()) {
            	int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int quantite = rs.getInt("quantité");
                double prix = rs.getDouble("prix");
                double totaleL = rs.getDouble("prixLigneTotale");
                list1.add(new LigneCommande(id,nom, quantite, totaleL, prix));
            }
            tablePur.setItems(list1);
            updateTotal();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading order lines: " + e.getMessage());
        }
    }

    @FXML
    void onAmountChanged(ActionEvent event) {
        updateRest();
    }
}
