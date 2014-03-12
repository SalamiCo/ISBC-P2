package t1314grupo17.behaviours;

import t1314grupo17.MultiBehaviour;
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
    public void onInit (final RobotAPI robot) {
        this.robot = robot;
        robot.setDisplayString("nop");
    }

    @Override
    public void onRelease (final RobotAPI robot) {
        this.robot = null;
    }

    @Override
    public int takeStep () {
        robot.setSpeed(0.0);

        return RobotAPI.ROBOT_OK;
    }

    @Override
    public void multi (final int you, final int total) {
        // NOP's don't care, NOP's don't feel they just sit there judging you
    }

}
