package fcul.css.desporges.application;

import fcul.css.desporges.domain.handlers.ShowRefereeCalendarHandler;
import fcul.css.desporges.facade.exceptions.ApplicationException;

import java.util.Calendar;


/**
 * The referee services class.
 * 
 * @author Thibault Langlois
 */
public class RefereeServices {
    
    private final ShowRefereeCalendarHandler srch;

    /**
     * The constructor receives the handler.
     * @param rh 
     */
    public RefereeServices(ShowRefereeCalendarHandler rh) {
        this.srch = rh;
    }
    
    /**
     * Obtains a referee's calendar given his license number.
     * This referee becomes the current referee
     * @param licenseNumber
     * @return 
     */
    public Iterable<MatchDTO> getRefereeCalendar(int licenseNumber) throws ApplicationException {
        return srch.getRefereeMatches(licenseNumber);
    }
    
    /**
     * Obtains the referees of the match that the current referee
     * has in the given date
     * @param date
     * @return 
     * @throws ApplicationException 
     */
    public Iterable<IReferee> getMatchRefereeList(Calendar date) throws ApplicationException {
       return srch.getMatchRefereeList(date);
   }
}
