package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
import t1314grupo17.RobotUtils;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

/**
 * A behaviour for a goalkeeper that will try to block the ball from entering the goal.
 * 
 * @author Daniel Escoz
 * @author Pedro Morgado
 * @author Arturo Pareja
 */
public final class GoalKeeperBehaviour extends MultiBehaviour {

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
        KICK,

        /** If we are being blocked, unblock */
        UNBLOCK;
    }

    private RobotAPI robot;

    private State state;

    private double min;

    private double max;

    @Override
    public void configure () {

    }

    @Override
    public void end () {

    }

    @Override
    public void onInit (final RobotAPI robot) {
        this.robot = robot;
        state = State.GOTO;
    }

    @Override
    public void onRelease (final RobotAPI robot) {
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

            case UNBLOCK: {
                stepUnblock();
                break;
            }
        }

        if (robot.blocked()) {
            // state = State.UNBLOCK;
        }

        robot.setDisplayString("GKEEP | " + state);
        return RobotAPI.ROBOT_OK;
    }

    private void stepGoto () {
        final Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        if (goal.r >= robot.getPlayerRadius() * 5) {
            // restrict y-axis movement
            RobotUtils.restrictMovementY(robot, goal, min, max);
            RobotUtils.moveEgo(robot, goal);

        } else {
            robot.setSpeed(0.0);
            robot.setSteerHeading(new Vec2(-robot.getFieldSide(), 0).t);
            state = State.DEFEND;
        }
    }

    private void stepDefend () {
        final Vec2 ball = robot.getBall();

        // Move to a defending position
        final Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        final Vec2 defvec = new Vec2(ball.x - goal.x, ball.y - goal.y);
        defvec.setr(robot.getPlayerRadius() * 2);
        defvec.sety(defvec.y * 2.5);

        final Vec2 defpos = new Vec2(goal.x + defvec.x, goal.y + defvec.y);
        RobotUtils.restrictMovementY(robot, defpos, min, max);
        RobotUtils.moveEgo(robot, defpos, -1);

        // If the ball is close, change to KICK
        final Vec2 bg = new Vec2(ball.x - goal.x, ball.y - goal.y);
        if (bg.r < robot.getPlayerRadius() * 6) {
            state = State.KICK;
        }
    }

    private void stepKick () {
        final Vec2 ball = robot.getBall();

        // Move to kick the ball
        RobotUtils.moveToKickBallAway(robot);

        // If the ball is far, change to GOTO
        final Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        final Vec2 bg = new Vec2(ball.x - goal.x, ball.y - goal.y);
        if (bg.r > robot.getPlayerRadius() * 7) {
            state = State.GOTO;
        }
    }

    private void stepUnblock () {
        final Vec2 cm = robot.getClosestMate();
        final Vec2 co = robot.getClosestOpponent();

        final Vec2 blocker = (cm.r < co.r) ? cm : co;

        final Vec2 target = new Vec2(blocker);
        target.setr(-100);
        RobotUtils.restrictMovementY(robot, target, min, max);
        RobotUtils.moveEgo(robot, target);

        if (!robot.blocked() && blocker.r > robot.getPlayerRadius() * 2.2) {
            state = State.GOTO;
        }
    }

    @Override
    public void multi (final int you, final int total) {
        final double aTop = 0.5;
        final double aBottom = -0.5;
        // Setup to use only part of the goal or something like that
        // total 0 < x <= 2
        final double areaHeight = aTop - aBottom;
        final double barHeight = areaHeight / total;

        min = aBottom + barHeight * you;
        max = aBottom + barHeight * (you + 1);
    }

}
