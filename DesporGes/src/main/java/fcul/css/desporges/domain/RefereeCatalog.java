package fcul.css.desporges.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * The catalog of Referees.
 * 
 * @author Thibault Langlois
 */
public class RefereeCatalog {
   
    /**
     * Finds a referee given its license number.
     * @param em
     * @param licenceNumber
     * @return 
     */
    public Referee getRefereeByNumber(EntityManager em, int licenceNumber) {
         TypedQuery<Referee> q = em.createNamedQuery(Referee.GET_REFEREE_BY_LICENSE_NUMBER,
                Referee.class);
        q.setParameter("number", licenceNumber);
        return q.getSingleResult();
    }
 
}
