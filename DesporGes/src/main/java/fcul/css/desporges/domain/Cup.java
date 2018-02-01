package fcul.css.desporges.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;

import fcul.css.desporges.domain.utils.DateUtil;

/**
 *
 * @author Thibault Langlois
 */
@Entity
public class Cup extends SportEvent {

    Cup(String designation, Periodicity p, Calendar beginingDate, List<Participant> lp, int nRefereesPerMatch) {
        super(designation, p, beginingDate, lp, nRefereesPerMatch);
        matches = makeMatchesForCup();
    }

    protected Cup() {
    }

    /**
     * Makes the set of matches for sport event of kind cup. Generates all
     * matches but only assigns participants for the matches of the first phase.
     *
     * @requires nParticipants is a power of 2 and > 0
     */
    private List<Match> makeMatchesForCup() {
        List<Match> result = new ArrayList<>();
 
        int phase = 1;
        int numberMatchesInPhase = nParticipants;
        Calendar date = beginingDate;
       
        while (numberMatchesInPhase != 1) {
            numberMatchesInPhase /= 2;
            for (int i = 0; i < numberMatchesInPhase; i++) {
                Participant p1 = null;
                Participant p2 = null;
                if (phase == 1) {
                    p1 = participants.get(2*i);
                    p2 = participants.get(2*i + 1);
                }
                Match m = new Match(date, p1, p2, phase);
                result.add(m);
            }
            date = DateUtil.add(date, periodicity);
            phase++;
        }

        return result;
    }

    @Override
    public boolean areCompatible(Referee referee, Match match) {
        return true; //no constraints in this case of event
    }

}
