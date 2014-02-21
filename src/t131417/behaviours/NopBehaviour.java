package t131417.behaviours;

import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public final class NopBehaviour extends Behaviour {

	private RobotAPI robot;

	@Override
	public void configure() {

	}

	@Override
	public void end() {

	}

	@Override
	public void onInit(RobotAPI robot) {
		this.robot = robot;
		robot.setDisplayString("nop");
	}

	@Override
	public void onRelease(RobotAPI robot) {
		this.robot = null;
	}

	@Override
	public int takeStep() {
		robot.setSpeed(0.0);

		return WorldAPI.ROBOT_OK;
	}

}
