package t131417.behaviours;

import java.util.List;
import java.util.Queue;

import t131417.RobotUtils;
import t131417.comm.PlayersMessage;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.Message;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.UCMPlayer;
import EDU.gatech.cc.is.util.Vec2;

public final class DefenseBehaviour extends Behaviour {

    private static final double FIELD_TOP = 0.7625;
    private static final double FIELD_BOTTOM = -0.7625;

    private static final double FIELD_HEIGHT = Math.abs(FIELD_TOP - FIELD_BOTTOM);

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

    private double yMin = FIELD_TOP;
    private double yMax = FIELD_BOTTOM;

    private String msg = "";

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

        Queue<Message> queue = getPendingMessages();
        while (!queue.isEmpty()) {
            processMessage(queue.remove());
        }

        robot.setDisplayString("DEFENSE | " + state + msg);
        return WorldAPI.ROBOT_OK;
    }

    private void stepGoto () {
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());
        goal = robot.toFieldCoordinates(goal);
        goal.sety((yMin + yMax) / 2);
        goal = robot.toEgocentricalCoordinates(goal);

        if (goal.r >= robot.getPlayerRadius() * 10) {
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
        defpos = robot.toFieldCoordinates(defpos);
        defpos.sety(Math.min(yMax, Math.max(yMin, defpos.y)));
        defpos = robot.toEgocentricalCoordinates(defpos);
        RobotUtils.moveEgo(robot, defpos);

        // If the ball is close, change to KICK
        if (RobotUtils.ballOnRobotSide(robot) //
            && ball.y < yMax + robot.getPlayerRadius() * 3 //
            && ball.y > yMin - robot.getPlayerRadius() * 3)
        {
            state = State.KICK;
        }
    }

    private void stepKick () {
        // Move to kick the ball
        RobotUtils.moveToKickBallAway(robot);

        // If the ball is far, change to GOTO
        Vec2 goal = robot.getOurGoal();
        goal.setx(goal.x - robot.getFieldSide() * robot.getPlayerRadius());

        if (!(RobotUtils.ballOnRobotSide(robot))) {
            state = State.GOTO;
        }
    }

    private void processMessage (Message message) {
        if (message instanceof PlayersMessage) {
            PlayersMessage pm = (PlayersMessage) message;

            int total = 0;
            int us = 0;

            for (UCMPlayer player : pm.getPlayers()) {
                if (player.getRobotAPI().getID() == robot.getID()) {
                    us = total;
                }

                if (player.getBehaviour() instanceof DefenseBehaviour) {
                    total++;
                }
            }

            yMin = FIELD_BOTTOM + (FIELD_HEIGHT / total) * us + robot.getPlayerRadius() * 1.2;
            yMax = yMin + (FIELD_HEIGHT / total) - robot.getPlayerRadius() * 1.2;

            msg = " | " + (us + 1) + "/" + total;
        }
    }
}
