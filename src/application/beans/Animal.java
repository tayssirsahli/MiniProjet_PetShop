package application.beans;

public class Animal extends Article{

	
	    private String race;
	    private String dateNaissance;
	   

	    
	    public Animal() {
	    	super();
	    }

	    
	    public Animal(int id, String nom, int quantite, int prix, String race, String dateNaissance) {
	        super(id, nom, quantite, prix);
	        
	        this.race = race;
	        this.dateNaissance = dateNaissance;
	        
	    }
	    


	    public Animal(String nom, int quantite, int prix, String race, String dateNaissance) {
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

	    public String getDateNaissance() {
	        return dateNaissance;
	    }

	    public void setDateNaissance(String dateNaissance) {
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
	}



