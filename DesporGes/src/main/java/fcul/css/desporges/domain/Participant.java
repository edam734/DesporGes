package fcul.css.desporges.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fcul.css.desporges.application.IParticipant;

/**
 * Represents a participant. Participants have a read-only unique ID that is
 * generated by the constructor.
 *
 * @author Thibault Langlois
 */
@Entity
public class Participant implements IParticipant{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sequencialId;

	private String name;
	
	Participant() {
		name = "Participant";
	}

	@Override
	public String getName() {
		return name+" "+sequencialId;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.sequencialId;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Participant other = (Participant) obj;
		if (this.sequencialId != other.sequencialId) {
			return false;
		}
		return true;
	}

}
