package fcul.css.desporges.facade.startup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fcul.css.desporges.application.RefereeServices;
import fcul.css.desporges.application.SportEventServices;
import fcul.css.desporges.domain.handlers.AssignRefereeHandler;
import fcul.css.desporges.domain.handlers.CreateSportEventHandler;
import fcul.css.desporges.domain.handlers.ShowRefereeCalendarHandler;
import fcul.css.desporges.facade.exceptions.ApplicationException;

/**
 * The start up use case.
 * 
 * @author Francisco Martins
 */
public class DesporGes {
	
    public static final String PUNIT_NAME = "fcul-css-DesporGesPU";

	private EntityManagerFactory emf;
	private CreateSportEventHandler createSportEventHandler;

	
    public void run() throws ApplicationException {
    	
    	try {
    		emf = Persistence.createEntityManagerFactory(PUNIT_NAME);		
    		createSportEventHandler = new CreateSportEventHandler(emf); 
    		
    		// exceptions thrown by JPA are not checked
        } catch (Exception e) {
            throw new ApplicationException("Error connecting database", e);
        }
    }

    public void stopRun() {
        // Closes the database connection
    	emf.close();
    }

    public SportEventServices getSportEventServices() {
    	// since the AssignRefereeHandler is stateful 
        return new SportEventServices(createSportEventHandler, new AssignRefereeHandler(emf));
    }

    public RefereeServices getRefereeServices() {
    	// since the ShowRefereeCalendarHandler is stateful
        return new RefereeServices(new ShowRefereeCalendarHandler(emf));
    }

}
