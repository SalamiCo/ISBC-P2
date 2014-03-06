package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
import t1314grupo17.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public final class DefenseBehaviour extends MultiBehaviour {

    /**
     * State used internally by this behaviour.
     * 
     * @author Daniel Escoz
     * @author Pedro Morgado
     * @author Arturo Pareja
     */
    private static enum State {
        /** Go to the goal */
        GOTO,

        /** Defend the goal */
        DEFEND,

        /** Find and kick the ball */
        KICK;
    }

    private RobotAPI robot;

    private State state;
    
    double min;
    double max;

    @Override
    public void configure () {

    }

    @Override
    public void end () {

    }

    @Override
    public void onInit (RobotAPI robot) {
        this.robot = robot;
        state = State.GOTO;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        switch (state) {
            case GOTO:
                stepGoto();
                break;
            case DEFEND:
                stepDefend();
                break;
            case KICK:
                stepKick();
                break;
        }

        robot.setDisplayString("DEFENSE | " + state);
        return WorldAPI.ROBOT_OK;
    }

    private void stepGoto () {
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        if (goal.r >= robot.getPlayerRadius() * 24) {
            RobotUtils.moveEgo(robot, goal);

        } else {
            robot.setSpeed(0.0);
            robot.setSteerHeading(new Vec2(-robot.getFieldSide(), 0).t);
            state = State.DEFEND;
        }
    }

    private void stepDefend () {
        Vec2 ball = robot.getBall();

        // Move to a defending position
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        Vec2 defvec = new Vec2(ball.x - goal.x, ball.y - goal.y);
        defvec.setr(robot.getPlayerRadius() * 10);
        // defvec.sety(defvec.y * 2);

        Vec2 defpos = new Vec2(goal.x + defvec.x, goal.y + defvec.y);
        RobotUtils.restrictMovementY(robot, defpos, min, max);
        RobotUtils.moveEgo(robot, defpos);

        // If the ball is close, change to KICK
        if (RobotUtils.ballOnRobotSide(robot)) {
            state = State.KICK;
        }
    }

    private void stepKick () {
        // Move to kick the ball
        RobotUtils.driveBall(robot, robot.getOpponentsGoal());
        
        if (robot.alignedToBallandGoal()) {
            robot.kick();
            state = State.DEFEND;
        }

        if (robot.blocked()) {
            state = State.DEFEND;
        }

        // If the ball is far, change to GOTO
        if (!(RobotUtils.ballOnRobotSide(robot))) {
            state = State.GOTO;
        }
    }

    @Override
    public void multi (int you, int total) {
        min = (((RobotAPI.getUpperFieldBound() - RobotAPI.getLowerFieldBound()) / total) * you) + RobotAPI.getLowerFieldBound();
        max = (((RobotAPI.getUpperFieldBound() - RobotAPI.getLowerFieldBound()) / total) * (you + 1)) + RobotAPI.getLowerFieldBound();
    }

}
