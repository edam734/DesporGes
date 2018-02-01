package fcul.css.desporges.dbutils;

import java.io.IOException;
import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fcul.css.desporges.facade.exceptions.ApplicationException;
import fcul.css.desporges.facade.startup.DesporGes;

/**
 * Utility that removes tables created by the application.
 * The script used is:
 * drop tables MATCHTABLE_REFEREE,SPORTEVENT_MATCHTABLE,SPORTEVENT_PARTICIPANT,MATCHTABLE,PARTICIPANT,REFEREE,SPORTEVENT;

 */
public class ResetTables {

    public static void resetTables() throws IOException, SQLException, ApplicationException {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory(DesporGes.PUNIT_NAME);
        new RunSQLScript().runScript(emf, Config.RESET_TABLES_SCRIPT);
        emf.close();
    }

    public static void main(String[] args) throws IOException, SQLException, ApplicationException {
        resetTables();
    }
}
