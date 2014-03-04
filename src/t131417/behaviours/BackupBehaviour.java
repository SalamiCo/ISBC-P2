package t131417.behaviours;

import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class BackupBehaviour extends Behaviour {

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
    	//TODO
    	
        robot.setDisplayString("BACKUP");
        return WorldAPI.ROBOT_OK;
    }
}
