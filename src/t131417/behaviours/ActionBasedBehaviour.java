package t131417.behaviours;

import t131417.actions.Action;
import teams.rolebased.WorldAPI;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public abstract class ActionBasedBehaviour extends Behaviour {

    private RobotAPI robot;

    private Action action;

    @Override
    public void configure () {
    }

    @Override
    public void end () {
    }

    @Override
    public final void onInit (RobotAPI robot) {
        this.robot = robot;
        init();
    }

    @Override
    public final void onRelease (RobotAPI robot) {
        finish();
        this.robot = null;
    }

    protected RobotAPI getRobot () {
        return robot;
    }

    protected void setAction (Action action) {
        if (this.action != null) {
            this.action.leave();
        }
        if (action != null) {
            action.enter(getRobot());
        }
        this.action = action;
    }

    @Override
    public final int takeStep () {
        if (action != null) {
            action.step();
        }
        
        robot.setDisplayString(getDisplay() + " | " + (action==null?"<null>":action.getDisplay()));
        step();
        return WorldAPI.ROBOT_OK;
    }

    protected abstract void step ();

    protected abstract void init ();

    protected abstract void finish ();
    
    protected String getDisplay () {
        return getClass().getSimpleName();
    }

}
