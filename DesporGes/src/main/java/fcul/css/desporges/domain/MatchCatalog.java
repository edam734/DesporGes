package fcul.css.desporges.domain;

import javax.persistence.EntityManager;

import fcul.css.desporges.facade.exceptions.ApplicationException;

/**
 * Match catalog.
 *
 * @author Thibault Langlois
 */
public class MatchCatalog {

    /**
     * Returns a Match given its unique number.
     *
     * @param em
     * @param number
     * @return a match instance.
     */
    public Match getMatchByNumber(EntityManager em, int number) throws ApplicationException {
        Match m = em.find(Match.class, number);
        if (m == null) {
            throw new ApplicationException("Match with number" + number + "not found.");
        }
        return m;
    }

}
