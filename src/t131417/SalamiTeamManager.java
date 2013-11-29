package t131417;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;

public final class SalamiTeamManager extends TeamManager {

	private Behaviour[] behaviours = { new NopBehaviour() };

	@Override
	public Behaviour[] createBehaviours() {
		return behaviours;
	}

	@Override
	public Behaviour getDefaultBehaviour(int id) {
		return behaviours[0];
	}

	@Override
	public int onConfigure() {
		return 0;
	}

	@Override
	protected void onTakeStep() {

	}

}
