package fcul.css.desporges.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;

import fcul.css.desporges.domain.utils.DateUtil;

/**
 * A class that represents a special type of SportEvent: Championship
 *
 * 
 * @author Thibault Langlois
 * @author Antonia Lopes
 *
 */
@Entity
public class Championship extends SportEvent {

    /**
     * Creates a ChampionshipSportEvent There are two phases; each phase has
     * lp.size()-1 rounds with lp.size()/2 matches. In each phase, each
     * participant plays with every other participant.
     *
     * @param designation
     * @param p
     * @param beginingDate
     * @param lp
     * @param nRefereesPerMatch
     */
    Championship(String designation, Periodicity p, Calendar beginingDate, List<Participant> lp, int nRefereesPerMatch) {
        super(designation, p, beginingDate, lp, nRefereesPerMatch);
        matches = makeMatchesForChampionship();
    }

    protected Championship() {
    }

    /**
     * Makes the set of matches for sport event of kind championship. Uses an
     * algorithm to generate the matches that rotates clockwise all participants
     * but the first.
     *
     * @requires nParticipants%2==0
     */
    private List<Match> makeMatchesForChampionship() {

        //the list with the positions of participants 
        //this is the list that is rotated in the algorithm
        List<Integer> listPos = new ArrayList<>();
        for (int i = 0; i <= nParticipants; i++) {
            listPos.add(i);
        }

        List<Match> listMatches = new ArrayList<>();
        Calendar roundDateFirstPhase = beginingDate;
        Calendar roundDateSecondPhase = DateUtil.add(beginingDate, periodicity, nParticipants - 1);

        int numMatchesByPhase = nParticipants / 2;
        for (int i = 0; i < nParticipants - 1; i++) {
            for (int j = 0; j < numMatchesByPhase; j++) {
                Participant p1 = participants.get(listPos.get(nParticipants - j - 1));
                Participant p2 = participants.get(listPos.get(j));
                listMatches.add(new Match(roundDateFirstPhase, p1, p2, 1));
                listMatches.add(new Match(roundDateSecondPhase, p2, p1, 2));
            }
            listPos.add(1, listPos.remove(nParticipants - 1));
            roundDateFirstPhase = DateUtil.add(roundDateFirstPhase, periodicity);
            roundDateSecondPhase = DateUtil.add(roundDateSecondPhase, periodicity);
        }
        return listMatches;
    }

    @Override
    public boolean areCompatible(Referee referee, Match match) {
        return match.getPart1() == null || match.getPart2() == null
                || findSimilarMatchInOtherPhase(match).getReferees().contains(referee);
    }

    private Match findSimilarMatchInOtherPhase(Match match) {
        for (Match m : matches) {
            if (Objects.equals(m.getPart1(), match.getPart2())
                    && Objects.equals(m.getPart2(), match.getPart1())) {
                return m;
            }
        }
        //should never happen by construction of matches 	
        throw new IllegalStateException(); 
    }

}
