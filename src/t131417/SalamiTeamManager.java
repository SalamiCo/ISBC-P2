package t131417;

import t131417.behaviours.DefenseBehaviour;
import t131417.behaviours.DriverBehaviour;
import t131417.behaviours.GoalKeeperBehaviour;
import t131417.behaviours.NopBehaviour;
import t131417.behaviours.UnblockBehaviour;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public final class SalamiTeamManager extends TeamManager {

    @Override
    public Behaviour[] createBehaviours () {
        return new Behaviour[0];
    }

    @Override
    public Behaviour getDefaultBehaviour (int id) {
        return new NopBehaviour();
    }

    @Override
    public int onConfigure () {
        return WorldAPI.ROBOT_OK;
    }

    @Override
    protected void onTakeStep () {
        designateDriver();

        designateGoalkeeper();
        
        designateDefense();
    }

    private void designateDriver () {
        UCMPlayer closest = null;
        double distance = 0;

        for (UCMPlayer player : _players) {
            Behaviour behaviour = player.getBehaviour();

            // If there's driver, end
            if (behaviour instanceof DriverBehaviour) {
                return;
            }

            double dtob = player.getRobotAPI().getBall().r;
            if (!(behaviour instanceof UnblockBehaviour) && !(behaviour instanceof GoalKeeperBehaviour) && (closest == null || dtob < distance)) {
                closest = player;
            }
        }

        if (closest != null) {
            closest.setBehaviour(new DriverBehaviour());
        }
    }

    private void designateGoalkeeper () {
        UCMPlayer closest = null;
        double distance = 0;

        for (UCMPlayer player : _players) {
            Behaviour behaviour = player.getBehaviour();

            // If there's a goalkeeper, end
            if (behaviour instanceof GoalKeeperBehaviour) {
                return;
            }

            double dtog = player.getRobotAPI().getOurGoal().r;
            if (!(behaviour instanceof UnblockBehaviour) && (closest == null || dtog < distance)) {
                closest = player;
            }
        }

        if (closest != null) {
            closest.setBehaviour(new GoalKeeperBehaviour());
        }
    }
    
    private void designateDefense () {
        UCMPlayer closest = null;
        double distance = 0;

        for (UCMPlayer player : _players) {
            Behaviour behaviour = player.getBehaviour();

            // If there's a defense, end
            if (behaviour instanceof DefenseBehaviour) {
                return;
            }

            double dtob = player.getRobotAPI().getOurGoal().r;
            if (!(behaviour instanceof UnblockBehaviour) && !(behaviour instanceof GoalKeeperBehaviour) && (closest == null || dtob < distance)) {
                closest = player;
            }
        }

        if (closest != null) {
            closest.setBehaviour(new DefenseBehaviour());
        }
    }
    
    

}
