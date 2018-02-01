package fcul.css.desporges.domain;

import static javax.persistence.EnumType.ORDINAL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fcul.css.desporges.facade.exceptions.ApplicationException;

/**
 * Defines a Sport Event.
 *
 * @author Thibault Langlois
 * @author Antonia Lopes
 */
@Entity
@NamedQueries({
	@NamedQuery(name = SportEvent.GET_SPORT_EVENT_BY_DESIGNATION,
			query = "SELECT se FROM SportEvent se WHERE se.designation LIKE :designation"),
	@NamedQuery(name = SportEvent.GET_SPORT_EVENT_BY_MATCH, 
	query = "SELECT se FROM SportEvent se WHERE :m MEMBER OF se.matches")
})
public abstract class SportEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String designation;

	int nParticipants;

	@OneToMany(cascade = CascadeType.ALL) 
	List<Participant> participants;

	private int nRefereesPerMatch;

	@Temporal(TemporalType.DATE)
	Calendar beginingDate;

	@Enumerated(ORDINAL)
	Periodicity periodicity;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SPORTEVENT_ID")
	List<Match> matches;

	public static final String GET_SPORT_EVENT_BY_DESIGNATION = "SportEvent.findByDesignation";
	public static final String GET_SPORT_EVENT_BY_MATCH = "SportEvent.findByMatch";

	SportEvent() {
	}

	/**
	 * Makes a SporEvent instance.
	 *
	 * @param designation
	 * @param p
	 * @param beginingDate
	 * @param nParticipants
	 * @param nRefereesPerMatch
	 *
	 * @requires DateUtil.isSaturdayOrSunday(beginingDate)
	 *
	 */
	SportEvent(String designation, Periodicity p, Calendar beginingDate, 
			List<Participant> lp, int nRefereesPerMatch) {
		this.designation = designation;
		this.beginingDate = beginingDate;
		this.nParticipants = lp.size();
		participants = lp;
		this.nRefereesPerMatch = nRefereesPerMatch;
		this.periodicity = p;
	}

	/**
	 * Returns the id.
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Returns the designation of the sport event.
	 *
	 * @return the designation.
	 */
	public String getDesignation() {
		return designation;
	}
	
	/**
	 * Adds a referee to a given match.
	 *
	 * @param m
	 * @param r
	 * @throws ApplicationException
	 *
	 */
	public void addReferee(Match m, Referee r) throws ApplicationException {
		r.addMatch(m);
		m.addReferee(r);
	}


	/**
	 * Returns the list of sport event matches that have an incomplete referee
	 * team.
	 *
	 * @return
	 */
	public List<Match> getMatchesWithIncompleteRefereeTeam() {
		// Just for illustrative purposes. A professional application
		// either maintains an additional relation (list) with the matches 
		// with an incomplete number of referees or executes a JPQL to 
		// retrieve the information, delegating the search to the DBMS 
		// instead of this naive sequential search. 
		List<Match> lm = new ArrayList<>();
		for (Match m : matches) {
			if (m.getNumberOfReferees() != nRefereesPerMatch) {
				lm.add(m);
			}
		}
		return lm;
	}

	/**
	 * @param referee
	 * @param match
	 * @return whether the given referee is admissible for a given match 
	 */
	public abstract boolean areCompatible(Referee referee, Match match); 
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sport Event ").append(designation).append("\n");
		sb.append("Participants: ");
		for (Participant p : participants) {
			sb.append(p).append(" ");
		}
		sb.append("\n");
		sb.append("Matches:\n");
		ArrayList<Match> copyMatches = new ArrayList<>(matches);
		//bug in eclispelink does not allow me to use lambda expressions here
		Comparator<Match> comparator  = new Comparator<Match>() {
			public int compare(Match m1, Match m2){ 
				return m1.getMatchDate().compareTo(m2.getMatchDate());
			}
		};
		Collections.sort(copyMatches, comparator);
		for (Match m : copyMatches) {
			sb.append(m).append("\n");
		}
		return sb.toString();
	}
}
