package fcul.css.desporges.domain.handlers;

import fcul.css.desporges.domain.Participant;
import fcul.css.desporges.domain.ParticipantCatalog;
import fcul.css.desporges.domain.Periodicity;
import fcul.css.desporges.domain.SportEvent;
import fcul.css.desporges.domain.SportEventCatalog;
import fcul.css.desporges.domain.utils.DateUtil;
import fcul.css.desporges.facade.exceptions.ApplicationException;

import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * The sport event handler.
 *
 * @author Thibault Langlois
 */
public class CreateSportEventHandler {

    private static final int CUP = 0;
    private static final int CHAMPIONSHIP = 1;

    private final EntityManagerFactory emf;

    /**
     * The constructor associates an entity manager factory, and the necessary
     * catalogs (referee catalog, sport event catalog and match catalog).
     *
     * @param emf
     */
    public CreateSportEventHandler(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Creates a championship after checking that the provided information is
     * valid.
     *
     * @param designation
     * @param nParticipants
     * @param nRefereesPerMatch
     * @param beginingDate
     * @param periodicity
     * @throws ApplicationException
     *
     */
    public void createChampionshipSportEvent(String designation,
            int nParticipants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity periodicity) throws ApplicationException {

        validateChampionshipNParticipants(nParticipants);
        createSportEvent(designation, nParticipants, nRefereesPerMatch, beginingDate,
                periodicity, CHAMPIONSHIP);
    }

    /**
     * Creates a cup after checking that the provided information is valid.
     *
     * @param designation
     * @param nParticipants
     * @param nRefereesPerMatch
     * @param beginingDate
     * @param periodicity
     * @throws ApplicationException
     *
     */
    public void createCupSportEvent(String designation,
            int nParticipants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity periodicity) throws ApplicationException {

        validateCupNParticipants(nParticipants);
        createSportEvent(designation, nParticipants, nRefereesPerMatch, beginingDate,
                periodicity, CUP);
    }

    /**
     * Takes care of the common part of the creation of a sport event
     *
     * @param designation
     * @param nParticipants
     * @param nRefereesPerMatch
     * @param beginingDate
     * @param p
     * @throws ApplicationException
     *
     */
    private void createSportEvent(String designation,
            int nParticipants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity periodicity, int evenType) throws ApplicationException {

        validateBeginingDate(beginingDate);

        SportEventCatalog seCat = new SportEventCatalog();
        ParticipantCatalog partCat = new ParticipantCatalog();

        EntityManager em = emf.createEntityManager();

        try {
            List<SportEvent> sel = seCat.findSportEventByDesignation(em, designation);
            if (sel.isEmpty()) {
                em.getTransaction().begin();
                List<Participant> participants = partCat.makeParticipants(nParticipants);
                SportEvent se;
                if (evenType == CUP) 
                	se = seCat.makeCup(designation, participants, nRefereesPerMatch, beginingDate, periodicity); 
                else
                    se = seCat.makeChampionship(designation, participants, nRefereesPerMatch, beginingDate, periodicity);
      
                em.persist(se);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApplicationException("Internal error while creating sport event.", e);
        } finally {
            em.close();
        }
    }

    /**
     * Validates the number of participants.
     *
     * @param nParticipants
     * @param type
     * @throws ApplicationException
     */
    private void validateCupNParticipants(int nParticipants) throws ApplicationException {
        if (nParticipants <= 0) {
            throw new ApplicationException("The number of participants must be greater greater than 0.");
        }
        if (isNotPowerOf2(nParticipants)) {
            throw new ApplicationException("The number of participants must be a power of 2.");
        }
    }

    private void validateChampionshipNParticipants(int nParticipants) throws ApplicationException {
        if (nParticipants <= 0) {
            throw new ApplicationException("The number of participants must be greater than 0.");
        }
        if (nParticipants % 2 != 0) {
            throw new ApplicationException("The number of participants must be even.");
        }
    }

    /**
     * Is n a power of 2 ?
     *
     * @param n
     * @return
     */
    private boolean isNotPowerOf2(int n) {
        return ((n & (n - 1)) != 0);
    }

    /**
     * Checks if date is valid: saturday or sunday.
     *
     * @param beginingDate
     * @throws ApplicationException
     */
    private void validateBeginingDate(Calendar beginingDate) throws ApplicationException {
        if (!DateUtil.isSaturdayOrSunday(beginingDate)) {
            throw new ApplicationException("The sport event should start a saturday or a sunday.");
        }
    }

    /**
     * Finds a sport event based on a designation.
     *
     *
     * INCLUDED ONLY FOR VISUALIZATION PURPOSE OF THE STATE IN SIMPLE CLIENT !
     * TO BE REMOVED!
     *
     * @param designation
     * @return
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException
     */
    public SportEvent getSportEventByDesignation(String designation) throws ApplicationException {
        EntityManager em = emf.createEntityManager();
        SportEvent se;
        try {
            SportEventCatalog seCat = new SportEventCatalog();
            se = seCat.getSportEventByDesignation(em, designation);
        } finally {
            em.close();
        }
        return se;
    }

}
