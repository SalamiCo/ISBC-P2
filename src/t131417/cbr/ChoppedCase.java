package t131417.cbr;

import java.util.Comparator;

/**
 * Description of the cases for the {@link t131417.ChoppedTeam}.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedCase {

    /** Goals by our team */
    private int goalsUs;

    /** Goals by their team */
    private int goalsThem;

    /** Time left in the match, in seconds */
    private int matchTime;

    public ChoppedCase (int goalsUs, int goalsThem, int matchTime) {
        this.goalsThem = goalsThem;
    }

}
