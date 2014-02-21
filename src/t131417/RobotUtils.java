package t131417;

import java.lang.reflect.Field;

import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.abstractrobot.SocSmall;
import EDU.gatech.cc.is.abstractrobot.SocSmallSim;
import EDU.gatech.cc.is.util.Vec2;

public final class RobotUtils {

    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     */
    public static void moveEgo (RobotAPI robot, Vec2 position) {
        robot.setSteerHeading(position.t);

        double dif = Math.abs(angleDifference(position.t, robot.getSteerHeading()));
        double spd = Math.max(getSpeed(robot) * 0.95, Math.min(1 - dif / 2, position.r * 8));

        robot.setSpeed(spd);
    }

    /**
     * Sets up the robot to kick the ball away from it.
     * 
     * @param robot The robot to move
     */
    public static void moveToKickBallAway (RobotAPI robot) {
        Vec2 ball = robot.getBall();

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

    /**
     * 
     * @param robot
     * @return
     */
    private static double getSpeed (RobotAPI robot) {
        try {
            Field f1 = RobotAPI.class.getDeclaredField("abstract_robot");
            f1.setAccessible(true);
            SocSmall ss = (SocSmall) f1.get(robot);
            
            Field f2 = SocSmallSim.class.getDeclaredField("speed");
            f2.setAccessible(true);
            Double spd = (Double) f2.get(ss);
            
            return Math.max(spd.doubleValue(), 0.1);
            
        } catch (Exception exc) {
            return 0.1;
        }
    }

    private RobotUtils () {
        throw new AssertionError();
    }
}
