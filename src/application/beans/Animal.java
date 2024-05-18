package application.beans;

import java.time.LocalDate;

public class Animal extends Article{

	
	    private String race;
	    private LocalDate dateNaissance;
	   

	    
	    public Animal() {
	    	super();
	    }

	    
	  
	    


	    public Animal(String nom, int quantite, Double prix, String race, LocalDate dateNaissance) {
	        super(nom, quantite, prix);
	        
	        this.race = race;
	        this.dateNaissance = dateNaissance;
	      
	    }



	    public String getRace() {
	        return race;
	    }

	    public void setRace(String race) {
	        this.race = race;
	    }

	    public LocalDate getDateNaissance() {
	        return dateNaissance;
	    }

	    public void setDateNaissance(LocalDate dateNaissance) {
	        this.dateNaissance = dateNaissance;
	    }

	   

	    // Méthode toString pour l'affichage
	    @Override
	    public String toString() {
	        return "Animal{" +
	                ", race='" + race + '\'' +
	                ", dateNaissance='" + dateNaissance + '\'' +
	             
	                '}';
	    }
	    
public Animal(int id, String nom, int quantite, double prix, String race, LocalDate dateNaissance) {
    super(id, nom, quantite, prix);
    this.race = race;
    this.dateNaissance = dateNaissance;
}






public Animal(String nom, int quantite, double prix, String race2, LocalDate dateNaissance2) {
	 super( nom, quantite, prix);
	    this.race = race2;
	    this.dateNaissance = dateNaissance2;
}


}



