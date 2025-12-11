package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;

@Entity
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id 
    private String email; // using email as primary key
    private String name; 

    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Ride> rides = new Vector<>();

    public Driver() { }

    public Driver(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Ride> getRides() { return rides; }

    @Override
    public String toString(){
        return email + ";" + name + rides;
    }

    public Ride addRide(String from, String to, Date date, int nPlaces, float price) {
        Ride ride = new Ride(from, to, date, nPlaces, price, this);
        rides.add(ride);
        return ride;
    }

    public boolean doesRideExists(String from, String to, Date date) {
        for (Ride r : rides)
            if (from.equals(r.getFrom()) && to.equals(r.getTo()) && date.equals(r.getDate()))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Driver)) return false;
        Driver other = (Driver) obj;
        return email.equals(other.email);
    }

    public Ride removeRide(String from, String to, Date date) {
        for (Ride r : rides) {
            if (from.equals(r.getFrom()) && to.equals(r.getTo()) && date.equals(r.getDate())) {
                rides.remove(r);
                return r;
            }
        }
        return null;
    }
}
