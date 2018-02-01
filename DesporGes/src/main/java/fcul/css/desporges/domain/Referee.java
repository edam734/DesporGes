package fcul.css.desporges.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import fcul.css.desporges.application.IReferee;

/**
 * Represents a referee.
 *
 * @author Thibault Langlois
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Referee.GET_REFEREE_BY_LICENSE_NUMBER,
            query = "SELECT r FROM Referee r WHERE r.licenseNumber LIKE :number")
})
public class Referee implements IReferee {

    static final String GET_REFEREE_BY_LICENSE_NUMBER = "Referee.findByLicenceNumber";
    
    @ManyToMany(mappedBy = "referees", cascade = CascadeType.ALL)
    private List<Match> matches;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int licenseNumber;
    /**
     * This field is not required by the use cases but it is defined in order 
     * to have some information to save to the database beside the primary key.
     */
    private String name;
    
    /**
     * Constructor. Assigns a read-only unique license number to each instance.
     * @param name
     * @param licenseNumber
     */
    public Referee(String name, int licenseNumber) {
        matches = new ArrayList<>();
        this.name = name;
        this.licenseNumber = licenseNumber;
    }

    public Referee() {
    }

    public List<Match> getMatches() {
        return matches;
    }
    
    @Override
    public int getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Determines if the referee is free i.e. 
     * does not have another match on that day
     * 
     * @param date
     * @return true if referee is free in the given date
     */
    public boolean isFree(Calendar date) {
        // Other match on same day.
        for (Match ma : matches) {
            if (ma.getMatchDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a match to referee's list of matches.
     * @param m 
     */
    void addMatch(Match m) {
        matches.add(m);
    }
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + licenseNumber;
		result = prime * result + ((matches == null) ? 0 : matches.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Referee other = (Referee) obj;
		if (id != other.id)
			return false;
		if (licenseNumber != other.licenseNumber)
			return false;
		if (matches == null) {
			if (other.matches != null)
				return false;
		} else if (!matches.equals(other.matches))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Referee #").append(String.format("%02d", licenseNumber)).append(" ");
        if (matches.isEmpty()) {
            sb.append("(no matches)");
        } else {
            for (Match m : matches) {
                sb.append("match #").append(m.getNumber()).append(" ");
            }
        }
        return sb.toString();
    }
 
}
