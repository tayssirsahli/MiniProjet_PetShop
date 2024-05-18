package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import connexion.Connexion;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.chart.AreaChart;

public class HomeController implements Initializable {

	@FXML
	private Label petsD;
	@FXML
	private Label productsId;

	@FXML
	private Label totaleId;

	@FXML
	private AreaChart<String, Number> IncomeDataChart;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    String queryAnimals = "SELECT count(*) FROM animal a INNER JOIN Article art ON a.id = art.id";
	    String queryProducts = "SELECT count(*) FROM produit p INNER JOIN Article art ON p.id = art.id";
	    String queryTotale = "SELECT SUM(Totale) FROM commande";


	    try {
	        PreparedStatement pstAnimals = Connexion.getConn().prepareStatement(queryAnimals);
	        ResultSet rsAnimals = pstAnimals.executeQuery();

	        if (rsAnimals.next()) {
	            int count = rsAnimals.getInt(1);
	            petsD.setText(String.valueOf(count));
	        }
	    } catch (SQLException e) {
	        petsD.setText("error");
	        e.printStackTrace();
	    }

	    try {
	        PreparedStatement pstProducts = Connexion.getConn().prepareStatement(queryProducts);
	        ResultSet rsProducts = pstProducts.executeQuery();

	        if (rsProducts.next()) {
	            int count = rsProducts.getInt(1);
	            productsId.setText(String.valueOf(count));
	        }
	    } catch (SQLException e) {
	        productsId.setText("error");
	        e.printStackTrace();
	    }

	    try {
	        PreparedStatement pstTotale = Connexion.getConn().prepareStatement(queryTotale);
	        ResultSet rsTotale = pstTotale.executeQuery();

	        if (rsTotale.next()) {
	            double total = rsTotale.getDouble(1);
	            totaleId.setText(String.valueOf(total));
	        }
	    } catch (SQLException e) {
	        totaleId.setText("error");
	        e.printStackTrace();
	    }
	    
	    HomeIncomeChart();
	}


	public void HomeIncomeChart() {
		String sql = "SELECT dateCommande, SUM(Totale) FROM commande GROUP BY dateCommande ORDER BY TIMESTAMP(dateCommande) ASC LIMIT 6";

		Task<XYChart.Series<String, Number>> task = new Task<>() {
			@Override
			protected XYChart.Series<String, Number> call() throws Exception {
				XYChart.Series<String, Number> chartSeries = new XYChart.Series<>();
				try {
					if (Connexion.getConn() != null && !Connexion.getConn().isClosed()) {
						PreparedStatement prepare = Connexion.getConn().prepareStatement(sql);
						ResultSet rs = prepare.executeQuery();

						while (rs.next()) {
							String date = rs.getString(1);
							int total = rs.getInt(2);
							chartSeries.getData().add(new XYChart.Data<>(date, total));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
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
	
	

}
