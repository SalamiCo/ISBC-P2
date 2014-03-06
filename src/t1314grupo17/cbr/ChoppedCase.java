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

    public ChoppedCase (int goalsUs, int goalsThem, long time) {
        this.goalsUs = goalsUs;
        this.goalsThem = goalsThem;
        this.matchTime = time;
    }

    public double similarity (ChoppedCase other) {
        int few = 2, many = 4;
        int goalsUs1 = other.getGoalsUs();
        int goalsThem1 = other.getGoalsThem();
        long matchTime1 = other.getMatchTime();

        int fewGoalsUs = (goalsUs < few && goalsUs1 < few) ? 1 : 0;
        int manyGoalsUs = (goalsUs > many && goalsUs1 > many) ? 1 : 0;
        int fewGoalsThem = (goalsThem < few && goalsThem1 < few) ? 1 : 0;
        int manyGoalsThem = (goalsThem < many && goalsThem1 < many) ? 1 : 0;

        double scoreDifference = Math.abs(goalsUs - goalsThem) - Math.abs(goalsUs1 - goalsThem1);
        int fewDifference = (scoreDifference < few) ? 1 : 0;
        int manyDifference = (scoreDifference > many) ? 1 : 0;

        double weightQuantity = 0.3;
        double similarQuantity = ((fewGoalsUs + manyGoalsUs + fewGoalsThem + manyGoalsThem) / 4) * weightQuantity;

        double weightDifference = 0.5;
        double similarDifference = ((fewDifference + manyDifference) / 2) * weightDifference;

        double weightTime = 0.2;
        int fewTime = 10;
        int timeDifference = (Math.abs(matchTime - matchTime1) < fewTime) ? 1 : 0;
        double similarTime = timeDifference * weightTime;

        return similarQuantity + similarDifference + similarTime;
    }

    @Override
    public String toString () {
        return goalsUs + "|" + goalsThem + " (" + matchTime + ")";
    }
}
