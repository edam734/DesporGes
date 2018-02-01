package fcul.css.desporges.application;

import fcul.css.desporges.domain.Periodicity;
import fcul.css.desporges.domain.SportEvent;
import fcul.css.desporges.domain.handlers.AssignRefereeHandler;
import fcul.css.desporges.domain.handlers.CreateSportEventHandler;
import fcul.css.desporges.facade.exceptions.ApplicationException;

import java.util.Calendar;

/**
 * The sport event services class.
 * 
 * @author Thibault Langlois
 */
public class SportEventServices {
    
    private final CreateSportEventHandler cseh;
    private final AssignRefereeHandler arh;

    /**
     * The constructor receives the handler.
     * @param cseh 
     * @param arh 
     */
    public SportEventServices(CreateSportEventHandler cseh, AssignRefereeHandler arh) {
        this.cseh = cseh;
        this.arh = arh;
    }
    
    /**
     * Calls the handler in order to create a sport event.
     * 
     * @param designation
     * @param nParticipants
     * @param nRefereesPerMatch
     * @param beginingDate
     * @param p
     * @throws ApplicationException 
     */
    public void createCupSportEvent(String designation, 
            int nParticipants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity p) throws ApplicationException {
        cseh.createCupSportEvent(designation, nParticipants, nRefereesPerMatch, beginingDate, p);
    }
    
    public void createChampionshipSportEvent(String designation,
            int nParticipants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity p) throws ApplicationException {
        cseh.createChampionshipSportEvent(designation, nParticipants, nRefereesPerMatch, beginingDate, p);
    }
    
    /**
     * Calls the handler to obtain a sport event given a designation.
     * 
     * INCLUDED ONLY FOR VISUALIZATION PURPOSE OF THE STATE IN SIMPLE CLIENT ! 
	 * TO BE REMOVED! 
     * @param sportEventDesignation
     * @return 
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException 
     */
    public SportEvent getSportEventByDesignation(String sportEventDesignation) throws ApplicationException {
         return cseh.getSportEventByDesignation(sportEventDesignation);
    }

    
    /**
     * Calls the handler to obtain a sport event given a designation.
     * 
     * @param sportEventDesignation 
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException 
     */
    public void selectSportEvent(String sportEventDesignation) throws ApplicationException {
         arh.selectSportSportEvent(sportEventDesignation);
    }
    
    /**
     * Calls the handler to obtain the list of sport event's matches that have 
     * an incomplete referee team.
     * 
     * @return 
     */
    public Iterable<IMatch> getMatchesCurrentSEWithMissingReferees() {
        return arh.getMatchesCurrentSEWithMissingReferees();
    }
    
    /**
     * Calls the handler to add a referee to a match.
     * 
     * @param matchNumber
     * @param refereeNumber
     * @return 
     * @throws ApplicationException 
     */
    public boolean addReferee(int matchNumber, int refereeNumber) throws ApplicationException {
        return arh.addReferee(matchNumber, refereeNumber);
    }
}
