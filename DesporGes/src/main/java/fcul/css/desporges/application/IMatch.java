package fcul.css.desporges.application;

import java.util.Calendar;

/**
 * This interface allows the transfer of Match information between domain and
 * presentation layers (via service layer) without exposing the domain
 * internals.
 *
 * @author Thibault Langlois
 */
public interface IMatch {

    /**
     * @return the match date.
     */
    public Calendar getMatchDate();

    /**
     * @return the match first participant.
     */
    public IParticipant getPart1();

    /**
     *
     * @return the match second participant.
     */
    public IParticipant getPart2();
 
    /**
     * @return the number of elements in referee team.
     */
    public int getNumberOfReferees();

    /**
     * @return the unique, automatically assigned match number.
     */
    public int getNumber();

    /**
     * @return the match phase.
     */
    public int getPhase();

}
