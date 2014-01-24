package t131417.actions;

import java.util.ArrayList;
import java.util.List;

import t131417.RobotUtils;
import EDU.gatech.cc.is.util.Vec2;

public final class MoveToAction extends Action {
    
    private Vec2 target;
    
    private boolean avoidOurTeam;
    private boolean avoidOppTeam;

    public MoveToAction () {
        setFieldTarget(new Vec2(0, 0));
        avoidOurTeam(false);
        avoidOpponentTeam(false);
    }
    
    public void setFieldTarget (Vec2 target) {
        this.target = target;
    }
    
    public void setEgoTarget (Vec2 target) {
        setFieldTarget(RobotUtils.egoToField(getRobot(), target));
    }

    @Override
    public boolean finished () {
        return distanceToTarget() < getRobot().getPlayerRadius();
    }

    @Override
    public void step () {
        List<Vec2> avoid = new ArrayList<Vec2>(10);
        
        if (avoidOppTeam) {
            for (Vec2 pos : getRobot().getOpponents()) {
                avoid.add(pos);
            }
        }
        
        if (avoidOurTeam ) {
            for (Vec2 pos : getRobot().getTeammates()) {
                avoid.add(pos);
            }
        }
        
        RobotUtils.moveTo(getRobot(), RobotUtils.fieldToEgo(getRobot(), target), 0.95, avoid);
    }
    
    @Override
    public String getDisplay () {
        Vec2 tgego = RobotUtils.fieldToEgo(getRobot(), target);
        return String.format("GO (%.2f, %.2f)", tgego.x, tgego.y);
    }

    public double distanceToTarget () {
        return RobotUtils.fieldToEgo(getRobot(), target).r;
    }

    public void avoidOurTeam (boolean enabled) {
        avoidOurTeam = enabled;
    }

    public void avoidOpponentTeam (boolean enabled) {
        avoidOppTeam = enabled;
    }
}
