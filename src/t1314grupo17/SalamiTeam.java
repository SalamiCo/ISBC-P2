package t1314grupo17;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public final class SalamiTeam extends UCMPlayer {

    @Override
    protected TeamManager createTeamManager () {
        return new SalamiTeamManager();
    }

}
