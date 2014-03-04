package t131417;

import t131417.behaviours.NopBehaviour;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;

public final class ChoppedTeamManager extends TeamManager {

    @Override
    public Behaviour[] createBehaviours () {
        return null;
    }

    @Override
    public Behaviour getDefaultBehaviour (int arg0) {
        return new NopBehaviour();
    }

    @Override
    public int onConfigure () {
        return WorldAPI.ROBOT_OK;
    }

    @Override
    protected void onTakeStep () {
        
    }

}
