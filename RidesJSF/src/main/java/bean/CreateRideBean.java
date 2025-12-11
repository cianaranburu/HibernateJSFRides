package bean;

import java.io.Serializable;
import java.util.Date;

import businessLogic.BLFacade;
import domain.Driver;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("rideBean")
@SessionScoped
public class CreateRideBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String from;
    private String to;
    private Date date;
    private int places;
    private float price;
    private String msg = "";

    // Use lazy singleton BLFacade
    private transient BLFacade facadeBL = FacadeBean.getBusinessLogic();

    // Getters and Setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getPlaces() { return places; }
    public void setPlaces(int places) { this.places = places; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    // Action method for submitting the form
    public void createRide() {
        try {
        	Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
        	facadeBL.createRide(from, to, date, places, price, driver1);
        	System.out.println("RIDE CREATED IN BEAN");
        	setMsg("Ride created succesfully");
        } catch (RideMustBeLaterThanTodayException e1) {
        	System.out.println("EXCEPTION 1 IS SEEN");
            setMsg("Ride must be later than today");
        } catch (RideAlreadyExistException e2) {
        	System.out.println("EXCEPTION 2 IS SEEN");
            setMsg("Ride already exists");
        }
    }
}
