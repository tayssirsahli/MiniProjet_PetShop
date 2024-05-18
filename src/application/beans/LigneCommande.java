package application.beans;

public class LigneCommande extends Article {
    private int id;
    private int quantite;
    private double prixLigneTotale;
    private double prix;
    private int produit_id;
    private int commande_id;

    public LigneCommande() {
    	super();
    }

    
    

	public LigneCommande(int quantite, double prixLigneTotale, double prix, int produit_id, int commande_id) {
		super();
		this.quantite = quantite;
		this.prixLigneTotale = prixLigneTotale;
		this.prix = prix;
		this.produit_id = produit_id;
		this.commande_id = commande_id;
	}






	public LigneCommande(int id, int quantite, double prixLigneTotale, double prix, int produit_id, int commande_id) {
		super();
		this.id = id;
		this.quantite = quantite;
		this.prixLigneTotale = prixLigneTotale;
		this.prix = prix;
		this.produit_id = produit_id;
		this.commande_id = commande_id;
	}






	public LigneCommande(String selectedName, int quantite, double prixLigneTotale, double prix) {
		super(selectedName);
		this.quantite = quantite;
		this.prixLigneTotale = prixLigneTotale;
		this.prix = prix;
	}




	public LigneCommande(int id,String nom, int quantite, double prixLigneTotale, double prix) {
		super(nom);
		this.id = id;
		this.quantite = quantite;
		this.prixLigneTotale = prixLigneTotale;
		this.prix = prix;
		
	}



	public double getPrix() {
		return prix;
	}






	public void setPrix(double prix) {
		this.prix = prix;
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

    public double getPrixLigneTotale() {
        return prixLigneTotale;
    }

    public void setPrixLigneTotale(double prixLigneTotale) {
        this.prixLigneTotale = prixLigneTotale;
    }

    public int getProduit_id() {
        return produit_id;
    }

    public void setProduit_id(int produit_id) {
        this.produit_id = produit_id;
    }

    public int getCommande_id() {
        return commande_id;
    }

    public void setCommande_id(int commande_id) {
        this.commande_id = commande_id;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", prixLigneTotale=" + prixLigneTotale +
                ", produit_id=" + produit_id +
                ", commande_id=" + commande_id +
                '}';
    }
}
