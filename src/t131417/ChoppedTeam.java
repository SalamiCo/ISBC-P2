package t131417;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public final class ChoppedTeam extends UCMPlayer {

    @Override
    protected TeamManager createTeamManager () {
        return new ChoppedTeamManager();
    }

}
