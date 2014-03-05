package t131417;

import java.util.concurrent.TimeUnit;

import t131417.behaviours.NopBehaviour;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;

public final class ChoppedTeamManager extends TeamManager {

    private long lastTick = 0L;

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
        long now = System.nanoTime();
        if (now - lastTick > TimeUnit.SECONDS.toNanos(10)) {
            tick();
            lastTick = now;
        }
    }
    
    void tick () {
        // TODO CBR Here!
    }

}
