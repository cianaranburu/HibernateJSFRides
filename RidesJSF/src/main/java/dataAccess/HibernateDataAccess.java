package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {

    private static HibernateDataAccess instance;
    private boolean dbInitialized = false;

    private HibernateDataAccess() {}

    public static synchronized HibernateDataAccess getInstance() {
        if (instance == null) {
            instance = new HibernateDataAccess();
        }
        return instance;
    }

    private synchronized void ensureDBInitialized() {
        if (!dbInitialized) {
            initializeDB();
            dbInitialized = true;
        }
    }

    /* -----------------------   CITY LISTS   ----------------------- */

    public List<String> getDepartCities() {
        ensureDBInitialized();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT r.from FROM Ride r ORDER BY r.from",
                    String.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> getArrivalCities(String from) {
        ensureDBInitialized();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
                    String.class);
            query.setParameter(1, from);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /* -----------------------   CREATE RIDE   ----------------------- */

    public void createRide(String from, String to, Date date, int places, float price, Driver driver)  
            throws RideAlreadyExistException, RideMustBeLaterThanTodayException {

        ensureDBInitialized();
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        try {
            if(new Date().compareTo(date) > 0) {
                throw new RideMustBeLaterThanTodayException();
            }

            Driver managedDriver = em.find(Driver.class, driver.getEmail());

            if (managedDriver.doesRideExists(from, to, date)) {
                throw new RideAlreadyExistException();
            }

            Ride ride = new Ride(from, to, date, places, price, managedDriver);
            em.persist(ride);

            em.flush();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e; // rethrow runtime exceptions so they don't silently stop propagation
        } finally {
            em.close();
        }
    }


    /* -----------------------   GET RIDES   ----------------------- */

    public List<Ride> getRides(String from, String to, Date date) {
        ensureDBInitialized();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Ride> query = em.createQuery(
                    "SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
                    Ride.class);
            query.setParameter(1, from);
            query.setParameter(2, to);
            query.setParameter(3, date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /* -----------------------   GET VALID DATES   ----------------------- */

    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        ensureDBInitialized();
        EntityManager em = JPAUtil.getEntityManager();

        try {
            Date start = UtilDate.firstDayMonth(date);
            Date end = UtilDate.lastDayMonth(date);

            TypedQuery<Date> query = em.createQuery(
                    "SELECT DISTINCT r.date FROM Ride r " +
                            "WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 AND ?4",
                    Date.class);
            query.setParameter(1, from);
            query.setParameter(2, to);
            query.setParameter(3, start);
            query.setParameter(4, end);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /* -----------------------   INITIALIZE DATABASE   ----------------------- */

    public void initializeDB() {
    	EntityManager em = JPAUtil.getEntityManager();

    	
    	try {
    	    em.getTransaction().begin();

    	    Calendar today = Calendar.getInstance();
    	    int month = today.get(Calendar.MONTH) + 1;
    	    int year = today.get(Calendar.YEAR);

    	    // DRIVER 1
    	    Driver driver1 = em.find(Driver.class, "driver1@gmail.com");
    	    if (driver1 == null) {
    	        driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
    	        em.persist(driver1);
    	    }
    	    // add rides if they don't exist
    	    if (!driver1.doesRideExists("Donostia", "Bilbo", UtilDate.newDate(year, month, 15))) {
    	        driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
    	    }
    	    if (!driver1.doesRideExists("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6))) {
    	        driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
    	    }

    	    // DRIVER 2
    	    Driver driver2 = em.find(Driver.class, "driver2@gmail.com");
    	    if (driver2 == null) {
    	        driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
    	        em.persist(driver2);
    	    }
    	    if (!driver2.doesRideExists("Donostia", "Bilbo", UtilDate.newDate(year, month, 15))) {
    	        driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
    	    }

    	    // DRIVER 3
    	    Driver driver3 = em.find(Driver.class, "driver3@gmail.com");
    	    if (driver3 == null) {
    	        driver3 = new Driver("driver3@gmail.com", "Test driver");
    	        em.persist(driver3);
    	    }
    	    if (!driver3.doesRideExists("Bilbo", "Donostia", UtilDate.newDate(year, month, 14))) {
    	        driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);
    	    }

    	    em.getTransaction().commit();
    	    dbInitialized = true;
    	    System.out.println("Database initialized safely.");
    	} catch (Exception e) {
    	    if (em.getTransaction().isActive()) em.getTransaction().rollback();
    	    e.printStackTrace();
    	} finally {
    	    em.close();
    	}


    	}

}
