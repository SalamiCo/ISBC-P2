package t131417.behaviours;

import t131417.RobotUtils;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public final class UnblockBehaviour extends Behaviour {

    private RobotAPI robot;

    @Override
    public void configure () {

    }

    @Override
    public void end () {

    }

    @Override
    public void onInit (RobotAPI robot) {
        this.robot = robot;
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        Vec2 closeMate = robot.getClosestMate();
        Vec2 closeOpp = robot.getClosestOpponent();
        
        Vec2 blocker = closeMate.r < closeOpp.r ? closeMate : closeOpp;
        
        Vec2 obj = new Vec2(-blocker.x, -blocker.y);
        obj.setr(10);

        RobotUtils.moveTo(robot, obj);

        robot.setDisplayString("UNBLOCK");
        return WorldAPI.ROBOT_OK;
    }

}
