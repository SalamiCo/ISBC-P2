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
        /** Find the ball */
        FIND,
        
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
    public void onInit (RobotAPI robot) {
        this.robot = robot;
        state = State.FIND;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        switch (state) {
            case FIND: {
                stepFind();
                break;
            }
            
            case DRIVE: {
                stepDrive();
                break;
            }
        }

        robot.setDisplayString("DRIVER | " + state);
        return WorldAPI.ROBOT_OK;
    }

    private void stepFind () {
        Vec2 ball = robot.getBall();
        RobotUtils.moveWithBallTo(robot, robot.getOpponentsGoal());
        
        if (ball.r < robot.getPlayerRadius() * 1.4) {
            state = State.DRIVE;
        }
    }
    
    private void stepDrive () {
        Vec2 ball = robot.getBall();
        
        RobotUtils.moveWithBallTo(robot, robot.getOpponentsGoal());
        
        if (ball.r > robot.getPlayerRadius() * 1.5) {
            state = State.FIND;
        }
    }

}
