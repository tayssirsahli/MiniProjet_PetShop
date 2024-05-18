package application.controllers;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import application.beans.Commande;
import connexion.Connexion;

public class dashboardController implements Initializable {

	@FXML
	private BorderPane bp;
	@FXML
	private Button logoutId;
	@FXML
	private Button HomeId;
	@FXML
	private Button AddPets;
	@FXML
	private Button PurchaseId;
	@FXML
	private Button ProductsId;
	@FXML
	private Button closeBtn;
	@FXML
	private Label usernameId;
	@FXML
	private Label petsD;
	@FXML
	private Label productsId;
	@FXML
	private Label totaleId;
	@FXML
	private Button exportID;
	@FXML
	private AreaChart<String, Number> IncomeDataChart;

	@FXML
	void close() {
		System.exit(0);
	}

	@FXML
	void AddPets(MouseEvent event) {
		loadPage("bodyAdd");
	}

	@FXML
	void DashboardWindow(MouseEvent event) {
		loadPage("bodyDashboard");
	}

	@FXML
	void ProductsWindow(MouseEvent event) {
		loadPage("Products");
	}

	@FXML
	void PurchaseWindow(MouseEvent event) {
		loadPage("Purchase");
	}

	@FXML
	void hideContent(MouseEvent event) {
		bp.setCenter(null);
	}

	@FXML
	void logout(MouseEvent event) {
		showAlert(Alert.AlertType.INFORMATION, "Déconnexion réussie", "Vous avez été déconnecté avec succès.");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/loginDesign.fxml"));
			Stage stage = (Stage) logoutId.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Erreur de chargement",
					"Une erreur s'est produite lors du chargement de l'écran de connexion.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.show();
	}

	private void loadPage(String page) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/" + page + ".fxml"));
			bp.setCenter(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void ExportBtn(ActionEvent event) {
	    try (Connection conn = Connexion.getConn();
	         FileWriter outputfile = new FileWriter(new File("fichiers/commandes.csv"), true);
	         BufferedWriter writer = new BufferedWriter(outputfile)) {

	        if (new File("fichiers/commandes.csv").length() == 0) {
	            writer.write("ID Commande;Total;Date;Type;id Ligne Commande;Quantité;Prix Ligne Totale;ID Produit");
	            writer.newLine();
	        }

	        String sqlCommande = "SELECT id, Totale, dateCommande, type FROM commande";
	        try (PreparedStatement pstmtCommande = conn.prepareStatement(sqlCommande);
	             ResultSet rsCommande = pstmtCommande.executeQuery()) {

	            while (rsCommande.next()) {
	                int commandeId = rsCommande.getInt("id");
	                double totale = rsCommande.getDouble("Totale");
	                String dateCommande = rsCommande.getDate("dateCommande").toString();
	                String type = rsCommande.getString("type");

	                String sqlLigneCommande = "SELECT id, quantité, prixLigneTotale, produit_id FROM lignecommande WHERE commande_id = ?";
	                try (PreparedStatement pstmtLigneCommande = conn.prepareStatement(sqlLigneCommande)) {
	                    pstmtLigneCommande.setInt(1, commandeId);
	                    try (ResultSet rsLigneCommande = pstmtLigneCommande.executeQuery()) {
	                        while (rsLigneCommande.next()) {
	                            int ligneCommId = rsLigneCommande.getInt("id");
	                            int quantite = rsLigneCommande.getInt("quantité");
	                            double prixLigneTotale = rsLigneCommande.getDouble("prixLigneTotale");
	                            int produitId = rsLigneCommande.getInt("produit_id");

	                            String row = String.join(";", String.valueOf(commandeId), String.valueOf(totale),
	                                    dateCommande, type, String.valueOf(ligneCommId), String.valueOf(quantite),
	                                    String.valueOf(prixLigneTotale), String.valueOf(produitId));
	                            writer.write(row);
	                            writer.newLine();
	                        }
	                    }
	                }
	            }
	        }

	        showAlert(Alert.AlertType.INFORMATION, "Export réussi",
	                "Les données ont été ajoutées avec succès au fichier commandes.csv");

	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Erreur d'exportation",
	                "Une erreur s'est produite lors de l'exportation des données.");
	    }
	}



	public void HomeIncomeChart() {
	    String sql = "SELECT dateCommande, SUM(Totale) FROM commande GROUP BY dateCommande ORDER BY TIMESTAMP(dateCommande) ASC LIMIT 6";

	    Task<XYChart.Series<String, Number>> task = new Task<>() {
	        @Override
	        protected XYChart.Series<String, Number> call() throws Exception {
	            XYChart.Series<String, Number> chartSeries = new XYChart.Series<>();
	            Connection conn = null;
	            try {
	                conn = Connexion.getConn();
	                if (conn != null && !conn.isClosed()) {
	                    PreparedStatement prepare = conn.prepareStatement(sql);
	                    ResultSet rs = prepare.executeQuery();

	                    while (rs.next()) {
	                        String date = rs.getString(1);
	                        int total = rs.getInt(2);
	                        chartSeries.getData().add(new XYChart.Data<>(date, total));
	                    }
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            } finally {
	                if (conn != null) {
	                    conn.close();
	                }
	            }

	            return chartSeries;
	        }
	    };

	    task.setOnSucceeded(event -> IncomeDataChart.getData().add(task.getValue()));
	    task.setOnFailed(event -> {
	        task.getException().printStackTrace();
	        petsD.setText("Error");
	    });

	    new Thread(task).start();
	}




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try (BufferedReader bf = new BufferedReader(new FileReader("fichiers/admin.txt"))) {
			String line;
			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(" ");
				usernameId.setText("  " + parts[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Connection conn = Connexion.getConn()) {
			updateDashboardData(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			petsD.setText("Error");
			productsId.setText("Error");
			totaleId.setText("Error");
		}

		HomeIncomeChart();
	}

	private void updateDashboardData(Connection conn) throws SQLException {
		updatePetsCount(conn);
		updateProductsCount(conn);
		updateTotalIncome(conn);
	}

	private void updatePetsCount(Connection conn) throws SQLException {
		String queryAnimals = "SELECT count(*) FROM animal a INNER JOIN Article art ON a.id = art.id";
		try (PreparedStatement pstAnimals = conn.prepareStatement(queryAnimals);
				ResultSet rsAnimals = pstAnimals.executeQuery()) {
			if (rsAnimals.next()) {
				int count = rsAnimals.getInt(1);
				petsD.setText(String.valueOf(count));
			}
		}
	}

	private void updateProductsCount(Connection conn) throws SQLException {
		String queryProducts = "SELECT count(*) FROM produit p INNER JOIN Article art ON p.id = art.id";
		try (PreparedStatement pstProducts = conn.prepareStatement(queryProducts);
				ResultSet rsProducts = pstProducts.executeQuery()) {
			if (rsProducts.next()) {
				int count = rsProducts.getInt(1);
				productsId.setText(String.valueOf(count));
			}
		}
	}

	private void updateTotalIncome(Connection conn) throws SQLException {
		String queryTotale = "SELECT SUM(Totale) FROM commande";
		try (PreparedStatement pstTotale = conn.prepareStatement(queryTotale);
				ResultSet rsTotale = pstTotale.executeQuery()) {
			if (rsTotale.next()) {
				double total = rsTotale.getDouble(1);
				totaleId.setText(String.valueOf(total));
			}
		}
	}
}
