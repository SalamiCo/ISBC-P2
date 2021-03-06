package t1314grupo17;

import java.lang.reflect.Field;

import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.abstractrobot.SocSmall;
import EDU.gatech.cc.is.abstractrobot.SocSmallSim;
import EDU.gatech.cc.is.util.Vec2;

public final class RobotUtils {

    /**
     * @param robot Robot to check
     * @return Whether the ball is on the side of that robot
     */
    public static boolean ballOnRobotSide (final RobotAPI robot) {
        return (robot.getFieldSide() * robot.toFieldCoordinates(robot.getBall()).x) > 0;
    }

    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     */
    public static void moveEgo (final RobotAPI robot, final Vec2 position) {
        moveEgo(robot, position, 0.0);
    }

    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     * @param inertia Minimum speed
     */
    public static void moveEgo (final RobotAPI robot, final Vec2 position, final double inertia) {
        robot.setSteerHeading(position.t);

        final double dif = Math.abs(angleDifference(position.t, robot.getSteerHeading()));
        double spd = Math.max(Math.max(inertia, getSpeed(robot)) * 0.95, 2 - dif);

        if (inertia < 0) {
            spd = Math.min(spd, position.r / robot.getPlayerRadius());
        }

        robot.setSpeed(spd);
    }

    /**
     * Sets up the robot to kick the ball away from it.
     * 
     * @param robot The robot to move
     */
    public static void moveToKickBallAway (final RobotAPI robot) {
        final Vec2 ball = robot.getBall();

        if (ball.r >= robot.getPlayerRadius() * 2) {
            ball.setx(ball.x + robot.getPlayerRadius() * robot.getFieldSide() * 1.5);
        }

        RobotUtils.moveEgo(robot, ball);
        robot.kick();
    }

    /**
     * Compute the difference between two angles, returning it in the range <tt>(-PI, PI]</tt>.
     * 
     * @param a First angle
     * @param b Second angle
     * @return The difference of angles <tt>a</tt> and <tt>b</tt>
     */
    public static double angleDifference (final double a, final double b) {
        double dif = a - b;
        while (dif > Math.PI) {
            dif -= Math.PI;
        }
        while (dif <= -Math.PI) {
            dif += Math.PI;
        }
        return dif;
    }

    /**
     * 
     * @param robot
     * @return
     */
    private static double getSpeed (final RobotAPI robot) {
        try {
            final Field f1 = RobotAPI.class.getDeclaredField("abstract_robot");
            f1.setAccessible(true);
            final SocSmall ss = (SocSmall) f1.get(robot);

            final Field f2 = SocSmallSim.class.getDeclaredField("speed");
            f2.setAccessible(true);
            final Double spd = (Double) f2.get(ss);

            return Math.max(spd.doubleValue(), 0.1);

        } catch (final Exception exc) {
            return 0.1;
        }
    }

    private RobotUtils () {
        throw new AssertionError();
    }

    public static Vec2 getOpponentsGoalKeeper (final RobotAPI robot) {
        double min_distance = Double.MAX_VALUE;
        final Vec2 goal = robot.getOpponentsGoal();
        final Vec2[] opponents = robot.getOpponents();
        Vec2 opponentGoalKeeper = opponents[0]; // initial value

        for (final Vec2 opponent : opponents) {
            final double distance = opponent.distance(goal);

            // get the opponent closest to goal
            if (distance < min_distance) {
                opponentGoalKeeper = opponent;
                min_distance = distance;
            }
        }
        return opponentGoalKeeper;
    }

    public static void driveBall (final RobotAPI robot, final Vec2 goal) {
        if (robot.getBall().r > robot.getPlayerRadius() * 1.05) {
            final Vec2 ball = robot.getBall();

            final Vec2 dif = new Vec2(ball);
            dif.sub(goal);
            dif.setr(robot.getPlayerRadius());

            ball.add(dif);
            RobotUtils.moveEgo(robot, ball, 1.0);
        } else {
            RobotUtils.moveEgo(robot, goal, 1.0);
        }
    }

    public static void restrictMovementY (final RobotAPI robot, Vec2 goal, final double min, final double max) {
        // to field coordinates
        final Vec2 pos = robot.toFieldCoordinates(goal);

        if (pos.y < min) {
            pos.sety(min);
        } else if (pos.y > max) {
            pos.sety(max);
        }

        // to egocentrical coordinates
        goal = robot.toEgocentricalCoordinates(pos);
    }
}
