package t131417;

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
    public static boolean ballOnRobotSide (RobotAPI robot) {
        return (robot.getFieldSide() * robot.toFieldCoordinates(robot.getBall()).x) > 0;
    }



    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     */
    public static void moveEgo (RobotAPI robot, Vec2 position) {
        moveEgo(robot, position, 0.0);
    }
    
    /**
     * Sets up the robot to go to the specified position.
     * 
     * @param robot The robot to move
     * @param position Position to move to (egocentrical)
     * @param inertia Minimum speed
     */
    public static void moveEgo (RobotAPI robot, Vec2 position, double inertia) {
        robot.setSteerHeading(position.t);

        double dif = Math.abs(angleDifference(position.t, robot.getSteerHeading()));
        double spd = Math.max(Math.max(inertia, getSpeed(robot)) * 0.95, 2 - dif);
        
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
    
    public static Vec2 getOpponentsGoalKeeper(RobotAPI robot){
    	double min_distance = Double.MAX_VALUE;
    	Vec2 goal = robot.getOpponentsGoal();    	
    	Vec2[] opponents = robot.getOpponents();
    	Vec2 opponentGoalKeeper = opponents[0]; //initial value
    	
    	for(Vec2 opponent : opponents){
    		double distance = opponent.distance(goal);
    		
    		//get the opponent closest to goal
    		if ( distance < min_distance ){
    			opponentGoalKeeper = opponent;
    			min_distance = distance;
    		}
    	}
    	return opponentGoalKeeper;
    }
}
