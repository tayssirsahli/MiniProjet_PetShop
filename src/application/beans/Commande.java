package application.beans;

import java.sql.Date;
import java.util.Objects;

public class Commande {
    private int id;
    private Date date;
    private double totale;
    private String type;

    public Commande() {
    }

  

	public Commande(int id, Date date, double totale, String type) {
        this.id = id;
        this.date = date;
        this.totale = totale;
        this.type = type;
    }

    public Commande(Date date, double totale, String type) {
		super();
		this.date = date;
		this.totale = totale;
		this.type = type;
	}

	public Commande(int id2) {
		this.id=id2;
	}

	public Commande(int id, String type, double totale) {
		this.id = id;
        this.totale = totale;
        this.type = type;
	}



	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", Date=" + date +
                ", totale=" + totale +
                ", type='" + type + '\'' +
                '}';
    }
}
