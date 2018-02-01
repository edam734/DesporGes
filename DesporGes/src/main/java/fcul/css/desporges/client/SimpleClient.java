package fcul.css.desporges.client;

import java.util.Calendar;
import java.util.List;

import fcul.css.desporges.application.IMatch;
import fcul.css.desporges.application.IReferee;
import fcul.css.desporges.application.MatchDTO;
import fcul.css.desporges.application.RefereeServices;
import fcul.css.desporges.application.SportEventServices;
import fcul.css.desporges.domain.Periodicity;
import fcul.css.desporges.domain.SportEvent;
import fcul.css.desporges.domain.utils.DateUtil;
import fcul.css.desporges.facade.exceptions.ApplicationException;
import fcul.css.desporges.facade.startup.DesporGes;

/*
 * *
 *
 * @author tl
 */
public class SimpleClient {
   

    private SportEventServices ses;
    private RefereeServices rs;
    private static final String CHAMPIONSHIP_DSGN = "Badminton Championship";
    private static final String CUP_DSGN = "Pétanque World Cup";

    public SimpleClient(SportEventServices ses, RefereeServices rs) {
        this.ses = ses;
        this.rs = rs;
        
    }
    
    
    /**
     * A simple interaction with the application services
     *
     * @param args Command line parameters
     */
    public static void main(String[] args) {
	System.out.println("Teste 1");
        // Connects to the database
        DesporGes app = new DesporGes();
        try {
            app.run();

            SimpleClient sc = new SimpleClient(app.getSportEventServices(), app.getRefereeServices());
            sc.runUseCases();
	
            app.stopRun();
        } catch (ApplicationException e) {
        	System.out.println("Error: " + e.getMessage());
        	// for debugging purposes only. Typically, in the application
        	// this information can be associated with a "details" button when
        	// the error message is displayed.
        	if (e.getCause() != null)
        		System.out.println("Cause: ");
        	e.printStackTrace();
        }
    }


    private void useCaseCreateSportEvent() throws ApplicationException {
        useCaseCreateSportEventCupOK();
        System.out.println("--------------------------------------------------------");
        useCaseCreateSportEventChampionhsipOK();

    }

    private void useCaseCreateSportEventChampionhsipOK() {
        int nRefereePerMatch = 3;
        int nParticipants = 4;
        System.out.println("Create a sport event «" + CHAMPIONSHIP_DSGN
                + "» starting on 18/06/2017.");
        System.out.println("The event has " + nParticipants + " participants and "
                + nRefereePerMatch + " referees per match.");
        try {
            ses.createChampionshipSportEvent(CHAMPIONSHIP_DSGN,
                    nParticipants, nRefereePerMatch,
                    DateUtil.makeDate(18, Calendar.JUNE, 2017), Periodicity.WEEKLY);
            SportEvent se = ses.getSportEventByDesignation(CHAMPIONSHIP_DSGN);
            System.out.println(se);
        } catch (NullPointerException e) {
            System.out.println("TOO BAD ! NullPointerException");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void useCaseCreateSportEventCupOK() throws ApplicationException {
        int nParticipants = 16;
        int nRefereesPerMatch = 3;
        System.out.println("Create a sport event «" + CUP_DSGN
                + "» starting on 18/06/2017.");
        System.out.println("The event has " + nParticipants + " participants and "
                + nRefereesPerMatch + " referees per match.");
        ses.createCupSportEvent(CUP_DSGN, 16, 3,
                DateUtil.makeDate(18, Calendar.JUNE, 2017), Periodicity.WEEKLY);
        SportEvent se = ses.getSportEventByDesignation(CUP_DSGN);
        System.out.println("SportEvent: " + se);
    }

    private void useCaseAddReferee() throws ApplicationException {
        ses.selectSportEvent(CUP_DSGN);

        Iterable<IMatch> lm = ses.getMatchesCurrentSEWithMissingReferees();
        System.out.println("Matches with incomplete referee team:");
       for (IMatch m : lm) {
            System.out.println("Match #" + m.getNumber() + " " + (m.getPart1()==null?"unknown":m.getPart1()) + " x " + (m.getPart2()==null?"unknown":m.getPart2()) );
        }
        System.out.println("Let's add referee #8 to match #12 :");
        int refereeNumber = 8;
        ses.addReferee(12, refereeNumber);
        System.out.println("Also add referee " + refereeNumber + " to matches 8.");
        if (!ses.addReferee(8, refereeNumber)) {
            System.out.println("This referee was not assigned since he has already a match in the same day.");
        }
        refereeNumber = 3;
        System.out.println("Also add referee " + refereeNumber + " to matches 12.");
        ses.addReferee(12, refereeNumber);
        System.out.println("The sport event is now:\n" + ses.getSportEventByDesignation(CUP_DSGN));

    }

    private void useCaseDisplayRefereeCalendar() throws ApplicationException {
        System.out.println("Referee 8 calendar:");
        // the date of the first match of the referee
        Calendar date = null;
        for (MatchDTO m : rs.getRefereeCalendar(8)) {
        	if (date == null)
        		date = m.getDate();
            System.out.println(m);
        }
        System.out.println("Director chose the date: " + DateUtil.myFormat(date));
        System.out.println("The other referees that will be on the match in this date are: ");
        List<IReferee> rl = (List<IReferee>) rs.getMatchRefereeList(date);
        for (IReferee r : rl) {
            System.out.println(r);
        }
    }

    public void runUseCases() throws ApplicationException {
        System.out.println("========================================================");
        System.out.println("Run the «Create Sport Event» use case for two kinds of ");
        System.out.println("sport events:");
        useCaseCreateSportEvent();
        System.out.println("========================================================");
        System.out.println("Run the «Add Referee» use case:");
        useCaseAddReferee();
        System.out.println("========================================================");
        System.out.println("Run the «Display Referee Calendar» use case:");
        useCaseDisplayRefereeCalendar();
    }
}
