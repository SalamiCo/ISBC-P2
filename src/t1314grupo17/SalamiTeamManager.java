package t1314grupo17;

import t1314grupo17.behaviours.BackupBehaviour;
import t1314grupo17.behaviours.BlockerBehaviour;
import t1314grupo17.behaviours.DefenseBehaviour;
import t1314grupo17.behaviours.DriverBehaviour;
import t1314grupo17.behaviours.GoalKeeperBehaviour;
import t1314grupo17.behaviours.NopBehaviour;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public final class SalamiTeamManager extends TeamManager {

    private final Behaviour[] behaviours = { //
        new NopBehaviour(), new GoalKeeperBehaviour(), new DefenseBehaviour(), new BackupBehaviour(),
            new DriverBehaviour(), new BlockerBehaviour() };

    private final long[] lastBlock = new long[5];
    private final long[] lastUnblock = new long[5];

    @Override
    public Behaviour[] createBehaviours () {
        return behaviours;
    }

    @Override
    public Behaviour getDefaultBehaviour (final int id) {
        switch (id) {
            case 0:
                return behaviours[1];

            case 1:
                return behaviours[2];

            case 2:
                return behaviours[3];

            case 3:
                return behaviours[4];

            case 4:
                return behaviours[5];

            default:
                return behaviours[0];
        }
    }

    @Override
    public int onConfigure () {
        return RobotAPI.ROBOT_OK;
    }

    @Override
    protected void onTakeStep () {
        final long now = System.currentTimeMillis();

        int blockedId = -1;
        int backupId = -1;
        for (int i = 0; i < 5; i++) {
            if (_players[i].getRobotAPI().blocked()) {
                lastBlock[i] = now;
            } else {
                lastUnblock[i] = now;
            }

            if (_players[i].getBehaviour() instanceof BackupBehaviour) {
                backupId = i;

            } else if (blockedId < 0 && !(_players[i].getBehaviour() instanceof BlockerBehaviour)
                && lastBlock[i] - lastUnblock[i] > 1000)
            {
                blockedId = i;
            }
        }

        if (blockedId > 0 && backupId > 0) {
            try {
                _players[backupId].setBehaviour(_players[blockedId].getBehaviour().getClass().newInstance());
                _players[blockedId].setBehaviour(new BackupBehaviour());
            } catch (final Exception e) {
                // Gotta catch 'em all!
            }
        }
    }

}
