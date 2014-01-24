package t131417.actions;

import teams.ucmTeam.RobotAPI;

public abstract class Action {

    private RobotAPI robot;

    public final void enter (RobotAPI robot) {
        this.robot = robot;
    }

    public final void leave () {
        this.robot = null;
    }

    protected final RobotAPI getRobot () {
        return robot;
    }

    public abstract boolean finished ();

    public abstract void step ();
    
    public String getDisplay () {
        return getClass().getSimpleName();
    }
}
