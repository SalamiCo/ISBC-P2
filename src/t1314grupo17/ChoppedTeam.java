package t1314grupo17;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public final class ChoppedTeam extends UCMPlayer {

    @Override
    protected TeamManager createTeamManager () {
        return new ChoppedTeamManager();
    }

}
