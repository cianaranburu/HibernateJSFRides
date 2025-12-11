package bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import businessLogic.BLFacade;
import domain.Ride;

@Named("queryRidesBean")
@ViewScoped
public class QueryRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> fromOptions;
    private List<String> toOptions;
    private String selectedFrom;
    private String selectedTo;
    private Date selectedDate;
    private List<Ride> searchResults;
    
    private boolean optionsLoaded = false;

    private transient BLFacade facadeBL; // transient avoids serialization issues

    @PostConstruct
    public void init() {
        facadeBL = FacadeBean.getBusinessLogic(); // get lazy BLFacade

        fromOptions = facadeBL.getDepartCities();
        selectedFrom = fromOptions.isEmpty() ? null : fromOptions.get(0);

        toOptions = selectedFrom != null ? facadeBL.getDestinationCities(selectedFrom) : new ArrayList<>();
        selectedTo = toOptions.isEmpty() ? null : toOptions.get(0);

        searchResults = new ArrayList<>();
    }

    public void updateToOptions() {
        toOptions = (selectedFrom != null) ? facadeBL.getDestinationCities(selectedFrom) : new ArrayList<>();
        selectedTo = toOptions.isEmpty() ? null : toOptions.get(0);
    }

    public void searchRides() {
        searchResults.clear();
        if (selectedFrom != null && selectedTo != null) {
            searchResults = facadeBL.getRides(selectedFrom, selectedTo, selectedDate);
        }
    }

    // Getters and Setters
    public List<String> getFromOptions() {
        if (!optionsLoaded) {
            fromOptions = facadeBL.getDepartCities();
            if (!fromOptions.isEmpty()) {
                selectedFrom = fromOptions.get(0);
                toOptions = facadeBL.getDestinationCities(selectedFrom);
                selectedTo = toOptions.isEmpty() ? null : toOptions.get(0);
            }
            optionsLoaded = true;
        }
        return fromOptions;
    }
    public List<String> getToOptions() { return toOptions; }

    public String getSelectedFrom() { return selectedFrom; }
    public void setSelectedFrom(String selectedFrom) { this.selectedFrom = selectedFrom; }

    public String getSelectedTo() { return selectedTo; }
    public void setSelectedTo(String selectedTo) { this.selectedTo = selectedTo; }

    public Date getSelectedDate() { return selectedDate; }
    public void setSelectedDate(Date selectedDate) { this.selectedDate = selectedDate; }

    public List<Ride> getSearchResults() { return searchResults; }
}
