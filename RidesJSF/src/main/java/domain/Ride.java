package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class Ride implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rideNumber;

    @Column(name = "origin")
    private String from;

    @Column(name = "destination")
    private String to;


    private int nPlaces;
    private Date date;
    private float price;

    @ManyToOne
    @JoinColumn(name = "driver_email") // foreign key column referencing Driver.email
    private Driver driver;

    public Ride() { }

    public Ride(String from, String to, Date date, int nPlaces, float price, Driver driver) {
        this.from = from;
        this.to = to;
        this.nPlaces = nPlaces;
        this.date = date;
        this.price = price;
        this.driver = driver;
    }

    public Integer getRideNumber() { return rideNumber; }
    public void setRideNumber(Integer rideNumber) { this.rideNumber = rideNumber; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public int getnPlaces() { return nPlaces; }
    public void setBetMinimum(int nPlaces) { this.nPlaces = nPlaces; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    @Override
    public String toString(){
        return rideNumber + ";" + from + ";" + to + ";" + date;
    }
}
