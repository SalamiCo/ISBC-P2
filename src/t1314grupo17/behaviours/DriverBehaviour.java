package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
import t1314grupo17.RobotUtils;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public final class DriverBehaviour extends MultiBehaviour {

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
        DRIVE;
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
    public void onInit (final RobotAPI robot) {
        this.robot = robot;
        state = State.WAIT;
    }

    @Override
    public void onRelease (final RobotAPI robot) {
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
        }

        robot.setDisplayString("DRIVER | " + state);
        return RobotAPI.ROBOT_OK;
    }

    private void stepWait () {
        final Vec2 ball = robot.getBall();

        Vec2 pos = new Vec2(robot.getFieldSide() * robot.getPlayerRadius() * 3, 0);
        pos = robot.toEgocentricalCoordinates(pos);

        if (ball.x * robot.getFieldSide() > 0) {
            pos.sety(ball.y);
        }

        RobotUtils.moveEgo(robot, pos);

        // If ball in opponent's team or at our y, stop waiting
        if (!RobotUtils.ballOnRobotSide(robot)
            || (ball.x * robot.getFieldSide() < 0 && Math.abs(ball.y) < robot.getPlayerRadius() * 1.5))
        {
            state = State.GET;
        }
    }

    private void stepGet () {
        // Go get the ball
        final Vec2 ball = robot.getBall();
        final Vec2 goal = robot.getOpponentsGoal();

        ball.sub(goal);
        ball.setr(ball.r + robot.getPlayerRadius());
        ball.add(goal);

        RobotUtils.moveEgo(robot, ball, 1.0);

        // If ball in our field and not at our y, waits
        if (RobotUtils.ballOnRobotSide(robot)
            && (ball.x * robot.getFieldSide() > 0 || Math.abs(ball.y) > robot.getPlayerRadius() * 1.5))
        {
            state = State.WAIT;
        }

        // If I have the ball, drive!
        if (ball.r < robot.getPlayerRadius() * 1.1) {
            state = State.DRIVE;
        }
    }

    private void stepDrive () {
        RobotUtils.driveBall(robot, robot.getOpponentsGoal());

        if (robot.alignedToBallandGoal()) {
            robot.kick();
            state = State.GET;
        }

        // If ball in our team, wait
        if (RobotUtils.ballOnRobotSide(robot)) {
            state = State.WAIT;
        }

        // If I don't have the ball, get it!
        if (robot.getBall().r > robot.getPlayerRadius() * 1.5) {
            state = State.GET;
        }
    }

    @Override
    public void multi (final int you, final int total) {
        // TODO Setup drivers to work only if part of the field
    }

}
