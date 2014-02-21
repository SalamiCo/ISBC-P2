package t131417.behaviours;

import EDU.gatech.cc.is.util.Vec2;
import t131417.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public final class GoalKeeperBehaviour extends Behaviour {

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
            case GOTO: {
                stepGoto();
                break;
            }

            case DEFEND: {
                stepDefend();
                break;
            }
            case KICK: {
                stepKick();
                break;
            }
        }

        robot.setDisplayString("GKEEP | " + state);
        return WorldAPI.ROBOT_OK;
    }

    private void stepGoto () {
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        if (goal.r >= robot.getPlayerRadius() * 5) {
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
        defvec.setr(robot.getPlayerRadius() * 2);
        defvec.sety(defvec.y * 2);

        Vec2 defpos = new Vec2(goal.x + defvec.x, goal.y + defvec.y);
        RobotUtils.moveEgo(robot, defpos);

        // If the ball is close, change to KICK
        Vec2 bg = new Vec2(ball.x - goal.x, ball.y - goal.y);
        if (bg.r < robot.getPlayerRadius() * 6) {
            state = State.KICK;
        }
    }

    private void stepKick () {
        Vec2 ball = robot.getBall();
        
        // Move to kick the ball
        RobotUtils.moveToKickBallAway(robot);

        // If the ball is far, change to GOTO
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());
        
        Vec2 bg = new Vec2(ball.x - goal.x, ball.y - goal.y);
        if (bg.r > robot.getPlayerRadius() * 7) {
            state = State.GOTO;
        }
    }

}
