package application.beans;

public class Produit extends Article {
    private int categorie;

    public Produit() {
        super();
    }

    public Produit(String nom, int quantite, int prix, Categorie categorie) {
        super(nom, quantite, prix);
        this.categorie = categorie.getId();
    }

    
    public int getCategorie() {
		return categorie;
	}

	public void setCategorie(int categorie) {
		this.categorie = categorie;
	}

	@Override
    public String toString() {
        return "Produit{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", quantite=" + getQuantite() +
                ", prix=" + getPrix() +
                ", categorie=" + categorie +
                '}';
    }
}
