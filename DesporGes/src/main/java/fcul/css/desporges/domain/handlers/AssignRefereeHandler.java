package fcul.css.desporges.domain.handlers;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import fcul.css.desporges.application.IMatch;
import fcul.css.desporges.domain.Match;
import fcul.css.desporges.domain.MatchCatalog;
import fcul.css.desporges.domain.Referee;
import fcul.css.desporges.domain.RefereeCatalog;
import fcul.css.desporges.domain.SportEvent;
import fcul.css.desporges.domain.SportEventCatalog;
import fcul.css.desporges.facade.exceptions.ApplicationException;

/**
 *
 * @author Thibault Langlois
 */
public class AssignRefereeHandler {

    private final EntityManagerFactory emf;
    private SportEvent currentSportEvent;

    /**
     * The constructor associates an entity manager factory, and the necessary
     * catalogs (referee catalog, sport event catalog and match catalog).
     *
     * @param emf
     */
    public AssignRefereeHandler(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Selects the sport event given its designation
     *
     * @param sportEventDesignation
     * @throws ApplicationException
     */
    public void selectSportSportEvent(String sportEventDesignation) throws ApplicationException {
        SportEventCatalog seCat = new SportEventCatalog();
        EntityManager em = emf.createEntityManager();
        try {
            currentSportEvent = seCat.getSportEventByDesignation(em, sportEventDesignation);
        } finally {
            em.close();
        }
    }

    /**
     * Returns the list of the matches of the current sport event that have an
     * incomplete referee team.
     *
     * @return
     */
    public Iterable<IMatch> getMatchesCurrentSEWithMissingReferees() {
        EntityManager em = emf.createEntityManager();
        try {
            currentSportEvent = em.merge(currentSportEvent);
            return new ArrayList<>(currentSportEvent.getMatchesWithIncompleteRefereeTeam());
        } finally {
            em.close();
        }
    }

    /**
     * Adds a referee (given its license number) to a match (represented by its
     * number) provided the rules are
     *
     * @param matchNumber
     * @param licenseNumber
     * @return
     * @throws ApplicationException
     */
    public boolean addReferee(int matchNumber, int licenseNumber) throws ApplicationException {
        boolean result = false;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Referee referee = new RefereeCatalog().getRefereeByNumber(em, licenseNumber);
            Match match = new MatchCatalog().getMatchByNumber(em, matchNumber);
            currentSportEvent = em.merge(currentSportEvent);
            result = referee.isFree(match.getMatchDate())
                    && currentSportEvent.areCompatible(referee, match);
            if (result) {
                currentSportEvent.addReferee(match, referee);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApplicationException("Internal error while adding referee.", e);
        } finally {
            em.close();
        }
        return result;
    }

}
