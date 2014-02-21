package t131417;

import t131417.behaviours.GoalKeeperBehaviour;
import t131417.behaviours.NopBehaviour;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public final class SalamiTeamManager extends TeamManager {

	private Behaviour[] behaviours = { new NopBehaviour(),
			new GoalKeeperBehaviour() };

	@Override
	public Behaviour[] createBehaviours() {
		return behaviours;
	}

	@Override
	public Behaviour getDefaultBehaviour(int id) {
		switch (id) {
		case 0:
			return behaviours[1];

		default:
			return behaviours[0];
		}
	}

	@Override
	public int onConfigure() {
		return WorldAPI.ROBOT_OK;
	}

	@Override
	protected void onTakeStep() {
	}

}
