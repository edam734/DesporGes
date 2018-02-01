package fcul.css.desporges.application;

import fcul.css.desporges.domain.Match;
import fcul.css.desporges.domain.utils.DateUtil;

import java.util.Calendar;
import java.util.Comparator;

/**
 * This class is used to transfer information to the presentation layer. It is
 * necessary due to the Show Referee Calendar use case that specify that the
 * information shown must include the sport event designation. To that end, the
 * DTO include only the necessary Match information (eventually converted to
 * String) plus the sport event designation.
 *
 * See the ShowRefereeCalenderHandler class to understand how Match instances
 * are translated into MatchDTO instances. The Handler is responsible to fetch
 * the sport event designation. The information is obtained via the
 * SportEventCatalog and a new query called from the method getSportEventByMatch
 *
 * The MatchDTO class has a comparator that can be used to sort them by date.
 *
 * @author Thibault Langlois
 * @author Antonia Lopes
 */
public class MatchDTO {

    private final Calendar date;
    private final String designation;
    private final int phase;
    private final String p1Name;
    private final String p2Name;

    public MatchDTO(Match m, String designation) {
        this.date = m.getMatchDate();
        this.phase = m.getPhase();
        this.p1Name = m.getPart1() == null ? null : m.getPart1().getName();
        this.p2Name = m.getPart2() == null ? null : m.getPart2().getName();
        this.designation = designation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(designation).append(" phase ").append(phase).append(" on ");
        sb.append(DateUtil.myFormat(date));
        sb.append(": ").append(p1Name).append(" vs ").append(p2Name);
        return sb.toString();
    }

    public static Comparator<MatchDTO> comparator() {
    	//eclipselink bug does not allow me to use a lambda expression here
    	return new Comparator<MatchDTO>() {
            public int compare(MatchDTO m1, MatchDTO m2) {
                return m1.date.compareTo(m2.date);
            }
        };
    }
    
    public Calendar getDate() {
    	return date;
    }
}
