package fcul.css.desporges.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thibault Langlois
 */
public class ParticipantCatalog {

    public List<Participant> makeParticipants(int nParticipants) {
        ArrayList<Participant> set = new ArrayList<>();
        for (int i = 0; i < nParticipants; i++) {
            Participant p = new Participant();
            set.add(p);
        }
        return set;
    }
}
