package t1314grupo17;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import t1314grupo17.behaviours.NopBehaviour;
import t1314grupo17.cbr.ChoppedCBR;
import t1314grupo17.cbr.ChoppedCase;
import t1314grupo17.cbr.ChoppedSolution;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public final class ChoppedTeamManager extends TeamManager {

    private static final File CBRFILE = new File("./robots/t1314grupo17/cases.cbr");

    private long lastTick = 0L;

    private final ChoppedCBR cbr = new ChoppedCBR();

    private ChoppedCase lastCase;

    private ChoppedSolution lastSolution;

    @Override
    public Behaviour[] createBehaviours () {
        return new Behaviour[0];
    }

    @Override
    public Behaviour getDefaultBehaviour (final int arg0) {
        return new NopBehaviour();
    }

    @Override
    public int onConfigure () {
        ChoppedTeamManager.class.getResource("/default.cbr");

        try {
            CBRFILE.getParentFile().mkdirs();
            cbr.load(CBRFILE);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return RobotAPI.ROBOT_OK;
    }

    @Override
    protected void onTakeStep () {
        final long now = System.nanoTime();

        final int justScored = _players[0].getRobotAPI().getJustScored();

        if (justScored != 0 && lastCase != null) {
            cbr.add(lastCase, lastSolution, justScored > 0);
            try {
                cbr.save(CBRFILE);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        if (now - lastTick > TimeUnit.SECONDS.toNanos(10) || justScored != 0) {
            tick();
            lastTick = now;
        }
    }

    void tick () {
        final RobotAPI robot = _players[0].getRobotAPI();
        final ChoppedCase currentCase =
            new ChoppedCase(robot.getMyScore(), robot.getOpponentScore(), robot.getMatchRemainingTime() / 1000);
        final ChoppedSolution solution = cbr.findSolution(currentCase);

        final Map<Class<? extends MultiBehaviour>,Integer> ttimes =
            new HashMap<Class<? extends MultiBehaviour>,Integer>();
        final List<Class<? extends MultiBehaviour>> behs = solution.getBehaviours();
        for (int i = 0; i < 5; i++) {
            try {
                final Class<? extends MultiBehaviour> cls = behs.get(i);
                _players[i].setBehaviour(cls.newInstance());

                if (ttimes.containsKey(cls)) {
                    ttimes.put(cls, ttimes.get(cls) + 1);
                } else {
                    ttimes.put(cls, 1);
                }

            } catch (final Exception exc) {
                exc.printStackTrace();
            }
        }

        final Map<Class<? extends MultiBehaviour>,Integer> times =
            new HashMap<Class<? extends MultiBehaviour>,Integer>();
        for (int i = 0; i < 5; i++) {
            final MultiBehaviour beh = (MultiBehaviour) _players[i].getBehaviour();
            final Class<? extends MultiBehaviour> cls = beh.getClass();

            if (times.containsKey(cls)) {
                times.put(cls, ttimes.get(cls) + 1);
            } else {
                times.put(cls, 0);
            }

            beh.multi(times.get(cls), ttimes.get(cls));
        }

        lastCase = currentCase;
        lastSolution = solution;

        System.out.println(lastCase + " => " + lastSolution);
    }
}
