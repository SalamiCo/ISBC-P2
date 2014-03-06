package t1314grupo17.cbr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import t1314grupo17.MultiBehaviour;
import t1314grupo17.behaviours.BackupBehaviour;
import t1314grupo17.behaviours.BlockerBehaviour;
import t1314grupo17.behaviours.DefenseBehaviour;
import t1314grupo17.behaviours.DriverBehaviour;
import t1314grupo17.behaviours.GoalKeeperBehaviour;
import t1314grupo17.behaviours.ZombieBehaviour;

public final class ChoppedSolution {

    /** List of behaviours */
    private final List<Class<? extends MultiBehaviour>> behaviours;

    public ChoppedSolution (List<Class<? extends MultiBehaviour>> behaviours) {
        if (behaviours.size() != 5) {
            throw new IllegalArgumentException("behaviours.size() != 5");
        }

        this.behaviours = Collections.unmodifiableList(new ArrayList<Class<? extends MultiBehaviour>>(behaviours));
    }

    public List<Class<? extends MultiBehaviour>> getBehaviours () {
        return behaviours;
    }

    public static ChoppedSolution createRandom () {
        Random rand = new Random();
        List<Class<? extends MultiBehaviour>> bs = new ArrayList<Class<? extends MultiBehaviour>>();

        @SuppressWarnings("unchecked")
        Class<? extends MultiBehaviour>[] rands = (Class<? extends MultiBehaviour>[]) new Class[] {//
            ZombieBehaviour.class, GoalKeeperBehaviour.class, BackupBehaviour.class, BlockerBehaviour.class,
                DefenseBehaviour.class, DriverBehaviour.class };
        int[] lefts = { 3, 2, 1, 2, 2, 2 };
        int num = rands.length;

        for (int i = 0; i < 5; i++) {
            int n = rand.nextInt(num);

            bs.add(rands[n]);
            int left = --lefts[n];

            if (left == 0) {
                num--;
                rands[n] = rands[num];
                lefts[n] = lefts[num];
            }

        }

        return new ChoppedSolution(bs);
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof ChoppedSolution)) {
            return false;
        }

        ChoppedSolution cs = (ChoppedSolution) obj;
        return cs.behaviours.equals(behaviours);
    }

    @Override
    public int hashCode () {
        return Arrays.hashCode(behaviours.toArray());
    }

    @Override
    public String toString () {
        return behaviours.toString();
    }
}
