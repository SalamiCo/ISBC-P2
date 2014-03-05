package t131417;

import java.util.List;
import java.util.concurrent.TimeUnit;

import t131417.behaviours.NopBehaviour;
import t131417.cbr.ChoppedCBR;
import t131417.cbr.ChoppedCase;
import t131417.cbr.ChoppedSolution;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public final class ChoppedTeamManager extends TeamManager {

    private long lastTick = 0L;

    private final ChoppedCBR cbr = new ChoppedCBR();

    @Override
    public Behaviour[] createBehaviours () {
        return new Behaviour[0];
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
        RobotAPI robot = _players[0].getRobotAPI();
        ChoppedCase currentCase =
            new ChoppedCase(robot.getMyScore(), robot.getOpponentScore(), robot.getMatchRemainingTime());
        ChoppedSolution solution = cbr.findSolution(currentCase);

        List<Class<? extends MultiBehaviour>> behs = solution.getBehaviours();
        for (int i = 0; i < 5; i++) {
            try {
                _players[i].setBehaviour(behs.get(i).newInstance());
                
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
