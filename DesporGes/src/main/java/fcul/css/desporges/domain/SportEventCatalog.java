package fcul.css.desporges.domain;

import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import fcul.css.desporges.facade.exceptions.ApplicationException;

/**
 * Sport Event catalog.
 *
 * @author Thibault Langlois
 */
public class SportEventCatalog {

    /**
     * Creates a sport event.
     *
     * @param designation
     * @param participants
     * @param nRefereesPerMatch
     * @param beginingDate
     * @param p
     * @return the new sport event.
     */
    public Championship makeChampionship(String designation,
            List<Participant> participants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity p) {
        return new Championship(designation, p, beginingDate, participants, nRefereesPerMatch);
    }

    public Cup makeCup(String designation,
            List<Participant> participants, int nRefereesPerMatch, Calendar beginingDate,
            Periodicity p) {
        return new Cup(designation, p, beginingDate, participants, nRefereesPerMatch);
    }

    /**
     * Finds sports events based on a designation.
     *
     * @param em
     * @param designation
     * @return a list of SportEvents.
     */
    public List<SportEvent> findSportEventByDesignation(EntityManager em, String designation) {
        TypedQuery<SportEvent> q = em.createNamedQuery(SportEvent.GET_SPORT_EVENT_BY_DESIGNATION,
                SportEvent.class);
        q.setParameter("designation", designation);
        return q.getResultList();
    }

    /**
     * Return a sport event, based on a desgination.
     *
     * @param em
     * @param designation
     * @return a SportEvent instance.
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException
     */
    public SportEvent getSportEventByDesignation(EntityManager em, String designation) throws ApplicationException {
        TypedQuery<SportEvent> q = em.createNamedQuery(SportEvent.GET_SPORT_EVENT_BY_DESIGNATION,
                SportEvent.class);
        q.setParameter("designation", designation);
        try {
            return q.getSingleResult();
        } catch (NonUniqueResultException e) {
            throw new ApplicationException("Internal error: duplicate Sport Event.", e);
        }
    }

    /**
     * Return a sport event to which a match belongs.
     *
     * @param em
     * @param designation
     * @return m Match instance.
     * @throws fcul.css.desporges.facade.exceptions.ApplicationException
     */
    public SportEvent getSportEventByMatch(EntityManager em, Match m) throws ApplicationException {
        TypedQuery<SportEvent> q = em.createNamedQuery(SportEvent.GET_SPORT_EVENT_BY_MATCH,
                SportEvent.class);
        q.setParameter("m", m);
        try {
            return q.getSingleResult();
        } catch (NonUniqueResultException e) {
            throw new ApplicationException("Internal error: duplicate Sport Event.");
        }
    }

}
