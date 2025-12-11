package bean;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;

public class FacadeBean {

    private static FacadeBean singleton;
    private BLFacade facadeInterface;

    // Private constructor
    private FacadeBean() {
        try {
            facadeInterface = new BLFacadeImplementation();
        } catch (Exception e) {
            System.out.println("FacadeBean: negozioaren logika sortzean errorea: " + e.getMessage());
        }
    }

    // Lazy singleton getter
    public static synchronized BLFacade getBusinessLogic() {
        if (singleton == null) {
            singleton = new FacadeBean();
        }
        return singleton.facadeInterface;
    }
}
