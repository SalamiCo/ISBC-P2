package t131417;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final File CBRFILE = new File("./t131417.cbr");

    private long lastTick = 0L;

    private final ChoppedCBR cbr = new ChoppedCBR();

    private ChoppedCase lastCase;

    private ChoppedSolution lastSolution;

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
        URL defaultCbrUrl = ChoppedTeamManager.class.getResource("/default.cbr");

        try {
            cbr.load(CBRFILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WorldAPI.ROBOT_OK;
    }

    @Override
    protected void onTakeStep () {
        long now = System.nanoTime();

        int justScored = _players[0].getRobotAPI().getJustScored();

        if (justScored != 0 && lastCase != null) {
            cbr.add(lastCase, lastSolution, justScored > 0);
            try {
                cbr.save(CBRFILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (now - lastTick > TimeUnit.SECONDS.toNanos(10) || justScored != 0) {
            tick();
            lastTick = now;
        }
    }

    void tick () {
        RobotAPI robot = _players[0].getRobotAPI();
        ChoppedCase currentCase =
            new ChoppedCase(robot.getMyScore(), robot.getOpponentScore(), robot.getMatchRemainingTime() / 1000);
        ChoppedSolution solution = cbr.findSolution(currentCase);

        Map<Class<? extends MultiBehaviour>,Integer> ttimes = new HashMap<Class<? extends MultiBehaviour>,Integer>();
        List<Class<? extends MultiBehaviour>> behs = solution.getBehaviours();
        for (int i = 0; i < 5; i++) {
            try {
                Class<? extends MultiBehaviour> cls = behs.get(i);
                _players[i].setBehaviour(cls.newInstance());

                if (ttimes.containsKey(cls)) {
                    ttimes.put(cls, ttimes.get(cls) + 1);
                } else {
                    ttimes.put(cls, 1);
                }

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        Map<Class<? extends MultiBehaviour>,Integer> times = new HashMap<Class<? extends MultiBehaviour>,Integer>();
        for (int i = 0; i < 5; i++) {
            MultiBehaviour beh = (MultiBehaviour) _players[i].getBehaviour();
            Class<? extends MultiBehaviour> cls = (Class<? extends MultiBehaviour>) beh.getClass();

            if (times.containsKey(cls)) {
                times.put(cls, ttimes.get(cls) + 1);
            } else {
                times.put(cls, 0);
            }

            beh.multi(times.get(cls), ttimes.get(cls));
        }

        lastCase = currentCase;
        lastSolution = solution;
    }
}
