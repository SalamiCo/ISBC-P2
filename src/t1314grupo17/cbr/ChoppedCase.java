package t1314grupo17.cbr;

/**
 * Description of the cases for the {@link t1314grupo17.ChoppedTeam}.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedCase {

    /** Goals by our team */
    private final int goalsUs;

    /** Goals by their team */
    private final int goalsThem;

    /** Time left in the match, in seconds */
    private final long matchTime;

    public int getGoalsUs () {
        return goalsUs;
    }

    public int getGoalsThem () {
        return goalsThem;
    }

    public long getMatchTime () {
        return matchTime;
    }

    public ChoppedCase (final int goalsUs, final int goalsThem, final long time) {
        this.goalsUs = goalsUs;
        this.goalsThem = goalsThem;
        matchTime = time;
    }

    public double similarity (final ChoppedCase other) {
        final int few = 2, many = 4;
        final int goalsUs1 = other.getGoalsUs();
        final int goalsThem1 = other.getGoalsThem();
        final long matchTime1 = other.getMatchTime();

        final int fewGoalsUs = (goalsUs < few && goalsUs1 < few) ? 1 : 0;
        final int manyGoalsUs = (goalsUs > many && goalsUs1 > many) ? 1 : 0;
        final int fewGoalsThem = (goalsThem < few && goalsThem1 < few) ? 1 : 0;
        final int manyGoalsThem = (goalsThem < many && goalsThem1 < many) ? 1 : 0;

        final double scoreDifference = Math.abs(goalsUs - goalsThem) - Math.abs(goalsUs1 - goalsThem1);
        final int fewDifference = (scoreDifference < few) ? 1 : 0;
        final int manyDifference = (scoreDifference > many) ? 1 : 0;

        final double weightQuantity = 0.3;
        final double similarQuantity = ((fewGoalsUs + manyGoalsUs + fewGoalsThem + manyGoalsThem) / 4) * weightQuantity;

        final double weightDifference = 0.5;
        final double similarDifference = ((fewDifference + manyDifference) / 2) * weightDifference;

        final double weightTime = 0.2;
        final int fewTime = 10;
        final int timeDifference = (Math.abs(matchTime - matchTime1) < fewTime) ? 1 : 0;
        final double similarTime = timeDifference * weightTime;

        return similarQuantity + similarDifference + similarTime;
    }

    @Override
    public String toString () {
        return goalsUs + "|" + goalsThem + " (" + matchTime + ")";
    }
}
