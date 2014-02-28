package t131417.behaviours;

import t131417.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class BlockerBehaviour extends Behaviour {
	
	/**
     * State used internally by this behaviour.
     * 
     * @author Daniel Escoz
     * @author Pedro Morgado
     * @author Arturo Pareja
     */
	
    private static enum State {    	
        /** Go to the opponent's goal */
        GOTO,

        /** Block the closest opponent */
        BLOCK
    }

    private RobotAPI robot;

    private State state;

	@Override
	public void configure() {
		
	}

	@Override
	public void end() {
		
	}

	@Override
	public void onInit(RobotAPI robot) {
		this.robot = robot;
        state = State.GOTO;		
	}

	@Override
	public void onRelease(RobotAPI robot) {
		this.robot = null;		
	}

	@Override
	public int takeStep() {		
		switch (state) {
		
			case GOTO: {
				stepGoto();
				break;
			}
		
	        case BLOCK: {
	            stepBlock();
	            break;
	        }
     
		}

	    robot.setDisplayString("BLOCKER | " + state);
	    return WorldAPI.ROBOT_OK;
	}
	
	private void stepGoto () {
		Vec2 goal = robot.getOpponentsGoal();

        if (goal.r >= robot.getPlayerRadius() * 5) {
            RobotUtils.moveEgo(robot, goal);

        } else {
            state = State.BLOCK;
        }
	}
	
	private void stepBlock () {
		Vec2 goal = robot.getOpponentsGoal();
		
		Vec2 gk = RobotUtils.getOpponentsGoalKeeper(robot);
		RobotUtils.moveEgo(robot, gk);
       	
       	//Always close to the opponent's area
       	if (goal.r >= robot.getPlayerRadius() * 10) {
       		state = State.GOTO;
        }
		
    }

}
