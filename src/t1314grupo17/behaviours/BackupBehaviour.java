package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
import t1314grupo17.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class BackupBehaviour extends MultiBehaviour {

    private enum State {
        BACKUP, UNBLOCK;
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
        state = State.BACKUP;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        switch (state) {
            case BACKUP: {
                Vec2 where = selectWhere();
                where = robot.toEgocentricalCoordinates(where);

                RobotUtils.moveEgo(robot, where, 0.8);
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
            state = State.BACKUP;
        }

        robot.setDisplayString("BACKUP");
        return WorldAPI.ROBOT_OK;
    }

    private Vec2 selectWhere () {
        double x = 0;
        double y = 0;

        if (RobotUtils.ballOnRobotSide(robot)) {
            x = RobotAPI.getLeftFieldBound() / 2;
        } else {
            x = RobotAPI.getRightFieldBound() / 2;
        }

        if (robot.getBall().y > 0) {
            y = RobotAPI.getLowerFieldBound() / 2;
        } else {
            y = RobotAPI.getUpperFieldBound() / 2;
        }

        return new Vec2(x, y);
    }

    @Override
    public void multi (int you, int total) {
        // We don't care -- multiple backups don't matter
    }
}
