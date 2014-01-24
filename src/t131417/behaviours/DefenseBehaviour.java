package t131417.behaviours;

import t131417.actions.MoveToAction;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public final class DefenseBehaviour extends ActionBasedBehaviour {

	private enum State {
        GOTO, DEFEND, KICK;
    }
	
	private State state;
	
	private boolean first = true;
	
	private final MoveToAction moveAction = new MoveToAction();
	
	@Override
    public void init () {
        state = State.GOTO;
    }
	
	@Override
    public void finish () {
        setAction(null);
    }
	
	private final void changeState (State state) {
        this.state = state;
        first = true;
    }
	
	@Override
    public void step () {
        switch (state) {
            case GOTO:
                stepGoto(first);
                break;
            case DEFEND:
                stepDefend(first);
                break;
            case KICK:
                stepKick(first);
                break;
        }

        first = false;
    }
	
	private void stepGoto (boolean first) {
        if (first) {
            setAction(moveAction);
            
            Vec2 goal = new Vec2(1.0, 0.0);
            moveAction.setFieldTarget(goal);
        }

        /*if (moveAction.distanceToTarget() < getRobot().getPlayerRadius() * 4) {
            changeState(State.DEFEND);
        }*/
    }

    private void stepDefend (boolean first) {
        if (first) {
            setAction(moveAction);
        }
        
        Vec2 goal = getRobot().getOurGoal();
        Vec2 ball = getRobot().getBall();
        
        goal.setx(goal.x - getRobot().getPlayerRadius() * getRobot().getFieldSide());
        goal.sety(goal.y + ((ball.y + getRobot().getPosition().y) / RobotAPI.getUpperFieldBound() * 0.25));
        
        ball.sub(goal);
        ball.setr(getRobot().getPlayerRadius() * 2);
        ball.add(goal);
        
        moveAction.setEgoTarget(ball);
        
        if (getRobot().getBall().r < getRobot().getPlayerRadius() * 2.9) {
            changeState(State.KICK);
        }
    }

    private void stepKick (boolean first) {
        if (first) {
            setAction(moveAction);
        }
        
        Vec2 ball = getRobot().getBall();
        ball.setx(ball.x + getRobot().getPlayerRadius() * getRobot().getFieldSide());
        
        moveAction.setEgoTarget(ball);
        getRobot().kick();
        
        if (getRobot().getBall().r > getRobot().getPlayerRadius() * 3.1) {
            changeState(State.GOTO);
        }
    }

    @Override
    protected String getDisplay () {
        return String.format("DEFENSE [%s]", state);
    }
}
