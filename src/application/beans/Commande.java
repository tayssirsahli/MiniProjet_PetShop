package application.beans;

public class Commande {
    private int id;
    private int quantite;
    private double totale;
    private String nomClient;

    public Commande() {
    }

    public Commande(int id, int quantite, double totale, String nomClient) {
        this.id = id;
        this.quantite = quantite;
        this.totale = totale;
        this.nomClient = nomClient;
    }

    public Commande(int quantite, double totale, String nomClient) {
		super();
		this.quantite = quantite;
		this.totale = totale;
		this.nomClient = nomClient;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", totale=" + totale +
                ", nomClient='" + nomClient + '\'' +
                '}';
    }
}
