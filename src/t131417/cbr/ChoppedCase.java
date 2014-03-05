package t131417.cbr;


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
    private long matchTime;
    
    public int getGoalsUs() {
		return goalsUs;
	}

	public void setGoalsUs(int goalsUs) {
		this.goalsUs = goalsUs;
	}

	public int getGoalsThem() {
		return goalsThem;
	}

	public void setGoalsThem(int goalsThem) {
		this.goalsThem = goalsThem;
	}

	public long getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(long matchTime) {
		this.matchTime = matchTime;
	}

    public ChoppedCase (int goalsUs, int goalsThem, int matchTime) {
        this.goalsThem = goalsThem;
    }

    public double similarity (ChoppedCase other) {
    	int few = 2, many = 4;
    	int goalsUs1 = other.getGoalsUs();
    	int goalsThem1 = other.getGoalsThem();
    	
    	int fewGoalsUs = (goalsUs < few && goalsUs1 < few)? 1:0;
    	int manyGoalsUs = (goalsUs > many && goalsUs1 > many)? 1:0;
    	int fewGoalsThem = (goalsThem < few && goalsThem1 < few)? 1:0;    	
    	int manyGoalsThem = (goalsThem < many && goalsThem1 < many)? 1:0;
    	
    	double scoreDifference = Math.abs(goalsUs - goalsThem) - Math.abs(goalsUs1 - goalsThem1);
    	int fewDifference = (scoreDifference < few)? 1:0;
    	int manyDifference = (scoreDifference > many)? 1:0;
    	
    	double weightQuantity = 0.1;
    	double similarQuantity = (fewGoalsUs + manyGoalsUs + fewGoalsThem + manyGoalsThem) * weightQuantity;
    	
    	double weightDifference = 0.1;
    	double similarDifference = (fewDifference + manyDifference) * weightDifference;
    	
        return Double.NaN;
    }
}
