package t131417.behaviours;

import t131417.MultiBehaviour;
import t131417.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class ZombieBehaviour extends MultiBehaviour {
	private enum State {
        ZOMBIE, UNBLOCK;
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
        state = State.ZOMBIE;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        switch (state) {
            case ZOMBIE: {
                Vec2 where = selectWhere();
                where = robot.toEgocentricalCoordinates(where);

                RobotUtils.moveEgo(robot, where, 1.0);
            }
                break;

            case UNBLOCK: {
                Vec2 cm = robot.getClosestMate();
                Vec2 co = robot.getClosestOpponent();

                Vec2 closest = co.r < cm.r ? co : cm;
                closest.setr(-robot.getPlayerRadius() * 10);

                RobotUtils.moveEgo(robot, closest, 1.0);
            }
        }

        if (robot.blocked()) {
            state = State.UNBLOCK;
        } else if (robot.getClosestMate().r > robot.getPlayerRadius() * 1.2
            && robot.getClosestOpponent().r > robot.getPlayerRadius() * 1.2)
        {
            state = State.ZOMBIE;
       }

        robot.setDisplayString("ZOMBIE");
        return WorldAPI.ROBOT_OK;
    }

    private Vec2 selectWhere () {
        double x = RobotAPI.getLeftFieldBound() + (Math.random() * (RobotAPI.getRightFieldBound() - RobotAPI.getLeftFieldBound()));
        double y = RobotAPI.getLowerFieldBound() + (Math.random() * (RobotAPI.getUpperFieldBound() - RobotAPI.getLowerFieldBound()));
        
        return new Vec2(x, y);
    }

    @Override
    public void multi (int you, int total) {
        // Zombies don't care
    }
}
