package fcul.css.desporges.dbutils;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fcul.css.desporges.domain.Referee;
import fcul.css.desporges.facade.exceptions.ApplicationException;
import fcul.css.desporges.facade.startup.DesporGes;

public class CreateDatabase {

    /**
     * An utility class should not have public constructors
     */
    private CreateDatabase() {
    }

    public static void main(String[] args) throws ApplicationException {
 
    	HashMap<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
		properties.put("javax.persistence.schema-generation.create-source", "metadata");
		properties.put("javax.persistence.schema-generation.drop-source", "metadata");
		
        Persistence.generateSchema(DesporGes.PUNIT_NAME, properties);
        
		createReferees();
    }
    
	private static void createReferees() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(DesporGes.PUNIT_NAME);
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for (int i = 1; i < 10; i++) {
			Referee r = new Referee("ref "+i,i);
			em.persist(r);
		}
		em.getTransaction().commit();
		em.close();
		emf.close();
	}

}
