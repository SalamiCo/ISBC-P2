package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.RobotAPI;

public final class NopBehaviour extends MultiBehaviour {

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
        robot.setDisplayString("nop");
    }

    @Override
    public void onRelease (RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        robot.setSpeed(0.0);

        return WorldAPI.ROBOT_OK;
    }

    @Override
    public void multi (int you, int total) {
        // NOP's don't care, NOP's don't feel they just sit there judging you
    }

}
