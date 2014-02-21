package t131417.behaviours;

import t131417.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public final class DriverBehaviour extends Behaviour {

    /**
     * State used internally by this behaviour.
     * 
     * @author Daniel Escoz
     * @author Pedro Morgado
     * @author Arturo Pareja
     */
    private static enum State {
        /** Wait for the ball to be on our side */
        WAIT,

        /** Go get the ball! */
        GET,

        /** Drive the ball to the goal */
        DRIVE,

        /** Kick the ball! */
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
        state = State.WAIT;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        switch (state) {
            case WAIT: {
                stepWait();
                break;
            }

            case GET: {
                stepGet();
                break;
            }

            case DRIVE: {
                stepDrive();
                break;
            }

            case KICK: {
                stepKick();
                break;
            }
        }

        robot.setDisplayString("DRIVER | " + state);
        return WorldAPI.ROBOT_OK;
    }

    private void stepWait () {
        // If ball in opponent's team, stop waiting
        if (!RobotUtils.ballOnRobotSide(robot)) {
            state = State.GET;
        }
    }

    private void stepGet () {
        // Go get the ball
        RobotUtils.moveEgo(robot, robot.getBall());

        // If ball in our team, wait
        if (RobotUtils.ballOnRobotSide(robot)) {
            state = State.WAIT;
        }

        // If I have the ball, drive!
        if (robot.getBall().r < robot.getPlayerRadius() * 1.1) {
            state = State.DRIVE;
        }
    }

    private void stepDrive () {
        // If ball in our team, wait
        if (RobotUtils.ballOnRobotSide(robot)) {
            state = State.WAIT;
        }

        // If I don't the ball, get it!
        if (robot.getBall().r > robot.getPlayerRadius() * 1.15) {
            state = State.GET;
        }
    }

    private void stepKick () {
        Vec2 ball = robot.getBall();
        Vec2 absBall = robot.toFieldCoordinates(robot.getBall());

        // If ball in our team, wait
        if (RobotUtils.ballOnRobotSide(robot)) {
            state = State.WAIT;
        }
    }
}
