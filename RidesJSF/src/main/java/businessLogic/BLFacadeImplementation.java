package businessLogic;

import java.util.Date;
import java.util.List;

import dataAccess.HibernateDataAccess;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class BLFacadeImplementation implements BLFacade {

    private HibernateDataAccess dbManager;

    public BLFacadeImplementation() {
        dbManager = HibernateDataAccess.getInstance();
    }

    @Override
    public List<String> getDepartCities() {
        return dbManager.getDepartCities();
    }

    @Override
    public List<String> getDestinationCities(String from) {
        return dbManager.getArrivalCities(from);
    }

    @Override
    public void createRide(String from, String to, Date date, int nPlaces, float price, Driver driver) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
        dbManager.createRide(from, to, date, nPlaces, price, driver);

    }

    @Override
    public List<Ride> getRides(String from, String to, Date date) {
        return dbManager.getRides(from, to, date);
    }

    @Override
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        return dbManager.getThisMonthDatesWithRides(from, to, date);
    }

    @Override
    public void initializeBD() {
        dbManager.initializeDB();
    }
}
