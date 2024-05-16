package application.beans;

public class Article {

	private int id;
    private String nom;
    private int quantite;
    private int prix;
    
    
	public Article() {
		super();
	}
	public Article(String nom, int quantite, int prix) {
		this.nom = nom;
		this.quantite = quantite;
		this.prix = prix;
	}
	public Article(int id, String nom, int quantite, int prix) {
		this.id = id;
		this.nom = nom;
		this.quantite = quantite;
		this.prix = prix;
	}
	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	public int getQuantite() {
		return quantite;
	}
	public int getPrix() {
		return prix;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
    
    
}
