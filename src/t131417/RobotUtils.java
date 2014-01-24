package t131417;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.abstractrobot.SocSmallSim;
import EDU.gatech.cc.is.util.Vec2;

public final class RobotUtils {

    public static Vec2 fieldToEgo (RobotAPI robot, Vec2 vec) {
        Vec2 rv = robot.getPosition();
        Vec2 res = new Vec2(vec.x - rv.x, vec.y - rv.y);
        return res;
    }

    public static Vec2 egoToField (RobotAPI robot, Vec2 vec) {
        Vec2 rv = robot.getPosition();
        Vec2 res = new Vec2(rv.x + vec.x, rv.y + vec.y);
        return res;
    }

    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     * @param inertia Inertia of the movement
     * @param avoid Objects (egocentrical) to avoid
     */
    public static void moveTo (RobotAPI robot, Vec2 position, double inertia, List<Vec2> avoid) {
        if (avoid == null) {
            avoid = Collections.emptyList();
        }

        Vec2 target = getMoveTarget(robot, position, avoid);

        robot.setSteerHeading(target.t);

        double dif = Math.abs(angleDifference(target.t, robot.getSteerHeading()));
        double spd = Math.max(getSpeed(robot) * inertia, Math.min(1 - dif / 2, target.r * 8));

        robot.setSpeed(spd);
    }

    private static Vec2 getMoveTarget (RobotAPI robot, Vec2 position, List<Vec2> avoids) {
        Vec2 target = new Vec2(position);
        
        // Check if some block
        Vec2 closeMate = robot.getClosestMate();
        Vec2 closeOpp = robot.getClosestOpponent();

        Vec2 blocker = closeMate.r < closeOpp.r ? closeMate : closeOpp;
        if (blocker.r < robot.getPlayerRadius() * 1.1) {
            Vec2 obj = new Vec2(-blocker.x, -blocker.y);
            target.sett(obj.t);
            target.setr(16);
        
        } else {
            for (Vec2 avoid : avoids) {
                
            }
        }
        
        return target;
    }

    /**
     * Sets up the robot to go to the specified position, trying to keep the ball with him. If he doesn't have the ball,
     * he will try to reach it.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     */
    public static void moveWithBallTo (RobotAPI robot, Vec2 position) {
        Vec2 ball = robot.getBall();

        Vec2 target;
        if (ball.r < robot.getPlayerRadius() * 1.1 || angleDifference(position.t, ball.t) >= 0.1) {
            Vec2 vec = new Vec2(position);
            vec.sub(ball);
            vec.setr(robot.getPlayerRadius());

            target = new Vec2(ball);
            target.sub(vec);

        } else {
            target = position;
        }

        robot.setSteerHeading(target.t);

        double dif = Math.abs(angleDifference(position.t, robot.getSteerHeading()));
        double spd = Math.max(0.7, Math.min(1 - dif / 2, position.r * 8));

        robot.setSpeed(spd);

    }

    /**
     * Compute the difference between two angles, returning it in the range <tt>(-PI, PI]</tt>.
     * 
     * @param a First angle
     * @param b Second angle
     * @return The difference of angles <tt>a</tt> and <tt>b</tt>
     */
    public static double angleDifference (double a, double b) {
        double dif = a - b;
        while (dif > Math.PI) {
            dif -= Math.PI;
        }
        while (dif <= -Math.PI) {
            dif += Math.PI;
        }
        return dif;
    }

    // ===============
    // === HACKISH ===

    private static final Field FIELD_ROBOT;
    private static final Field FIELD_SPEED;
    static {
        Field fRobot = null;
        Field fSpeed = null;

        try {
            fRobot = RobotAPI.class.getDeclaredField("abstract_robot");
            fRobot.setAccessible(true);

            fSpeed = SocSmallSim.class.getDeclaredField("speed");
            fSpeed.setAccessible(true);

        } catch (Exception exc) {
            fRobot = null;
            fSpeed = null;
        }

        FIELD_ROBOT = fRobot;
        FIELD_SPEED = fSpeed;
    }

    /**
     * Returns, through reflection, the speed of a robot.
     * 
     * @param robot Robot to get the speed from
     * @return Speed of the robot
     */
    public static double getSpeed (RobotAPI robot) {
        try {
            Double spd = (Double) FIELD_SPEED.get(FIELD_ROBOT.get(robot));
            return spd.doubleValue();

        } catch (Exception exc) {
            return 0;
        }
    }

    private RobotUtils () {
        throw new AssertionError();
    }
}
