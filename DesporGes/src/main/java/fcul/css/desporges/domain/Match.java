package fcul.css.desporges.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import fcul.css.desporges.application.IMatch;
import fcul.css.desporges.domain.utils.DateUtil;

import javax.persistence.ManyToOne;

/**
 * Represents a match between two participants.
 *
 * @author Thibault Langlois
 */
@Entity
@Table(name = "MATCHTABLE") // match is a reserved keyword in SQL.
public class Match implements IMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int number;

    @ManyToMany 
    private List<Referee> referees;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar matchDate;
    
    @ManyToOne
    private Participant participant1;
    @ManyToOne
    private Participant participant2;
 
    private int phase;

    Match() {
        referees = new ArrayList<>();
    }

    public Match(Calendar matchDate, Participant p1, Participant p2, int phase) {
        this.matchDate = matchDate;
        this.participant1 = p1;
        this.participant2 = p2;
        referees = new ArrayList<>();
        this.phase = phase;
    }

    /**
     * Returns the match date.
     *
     * @return the match date.
     */
    @Override
    public Calendar getMatchDate() {
        return matchDate;
    }

    /**
     * Returns match first participant.
     *
     * @return match first participant.
     */
    @Override
    public Participant getPart1() {
        return participant1;
    }

    /**
     * Returns match second participant.
     *
     * @return match second participant
     */
    @Override
    public Participant getPart2() {
        return participant2;
    }

    /**
     * Add a referee to the referee team.
     *
     * @param r
     */
    void addReferee(Referee r) {
        referees.add(r);
    }

    /**
     * Returns the list of referees.
     *
     * @return the list of referees.
     */
    public List<Referee> getReferees() {
        return referees;
    }


    /**
     * Returns the number of elements in the current referee team.
     *
     * @return the number of referees.
     */
    @Override
    public int getNumberOfReferees() {
        return referees.size();
    }

    /**
     * Returns the match unique number. This number is assigned automatically.
     *
     * @return the match unique number.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Returns the match phase.
     *
     * @return
     */
    @Override
    public int getPhase() {
        return phase;
    }


    @Override
    public String toString() {
        String dates = DateUtil.myFormat(matchDate);
        StringBuilder sb = new StringBuilder();
        sb.append("Match #").append(String.format("%03d", number)).append(" ");
        sb.append("Phase "+ phase + " ");
        if (participant1 == null) {
            sb.append("unkown");
        } else {
            sb.append(participant1);
        }
        sb.append(" vs ");
        if (participant2 == null) {
            sb.append("unkown");
        } else {
            sb.append(participant2);
        }
        sb.append(" on ").append(dates).append(" ");
        if (referees.isEmpty()) {
            sb.append("(No referee assigned)");
        } else {
            sb.append(" Referees: ");
            for (Referee r : referees) {
                sb.append("#").append(String.format("%02d", r.getLicenseNumber())).append(" ");
            }
        }
        return sb.toString();
    }
	   
}
