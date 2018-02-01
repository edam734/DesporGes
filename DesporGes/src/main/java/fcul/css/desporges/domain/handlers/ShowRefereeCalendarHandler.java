package fcul.css.desporges.domain.handlers;

import fcul.css.desporges.application.IReferee;
import fcul.css.desporges.application.MatchDTO;
import fcul.css.desporges.domain.Match;
import fcul.css.desporges.domain.Referee;
import fcul.css.desporges.domain.RefereeCatalog;
import fcul.css.desporges.domain.SportEvent;
import fcul.css.desporges.domain.SportEventCatalog;
import fcul.css.desporges.facade.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * The Show Referee Calendar Handler.
 *
 * @author Thibault Langlois
 */
public class ShowRefereeCalendarHandler {

    /**
     * Entity manager factory for accessing the persistence service
     */
    private final EntityManagerFactory emf;
  
    private Referee currentReferee;

    /**
     * Creates a RefereeHandler that associate an EntityManagerFactory and a
     * RefereeCatalog.
     *
     * @param emf
     */
    public ShowRefereeCalendarHandler(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Returns the list of matches assigned to the referee designated by the
     * license number.
     *
     * @param licenseNumber
     * @return
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException
     */
    public Iterable<MatchDTO> getRefereeMatches(int licenseNumber) throws ApplicationException {
    	SportEventCatalog sec = new SportEventCatalog();
    	RefereeCatalog rc = new RefereeCatalog();
    	
    	List<MatchDTO> ml = new ArrayList<>();
        
    	EntityManager em = emf.createEntityManager();
        try {
            currentReferee = rc.getRefereeByNumber(em, licenseNumber);            
            for (Match m : currentReferee.getMatches()) {
                SportEvent se = sec.getSportEventByMatch(em, m);
                ml.add(new MatchDTO(m, se.getDesignation()));
            }
            Collections.sort(ml, MatchDTO.comparator());
        } finally {
            em.close();
        }
        return ml;
    }

    /**
     * Returns the list of referees assigned to the match that the currentReferee
     * has in given date
     *
     * @param date
     * @return
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException
     */
    public Iterable<IReferee> getMatchRefereeList(Calendar date) throws ApplicationException {
        EntityManager em = emf.createEntityManager();
        List<IReferee> rl = new ArrayList<>();
        try {
            Match m = findMatch(date, em);
            if (m == null)
            	throw new ApplicationException("Invalid date: no match of the referee in this date.");
            rl.addAll(m.getReferees());
            rl.remove(currentReferee);
		} finally {
            em.close();
        }
        return rl;
    }

    private Match findMatch(Calendar date, EntityManager em) throws ApplicationException {
        currentReferee = em.merge(currentReferee);
        for (Match m : currentReferee.getMatches()) {
            if (m.getMatchDate().equals(date)) {
                return m;
            }
        }
        return null;
    }
}
