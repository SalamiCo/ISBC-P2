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

    public ChoppedSolution (final List<Class<? extends MultiBehaviour>> behaviours) {
        if (behaviours.size() != 5) {
            throw new IllegalArgumentException("behaviours.size() != 5");
        }

        this.behaviours = Collections.unmodifiableList(new ArrayList<Class<? extends MultiBehaviour>>(behaviours));
    }

    public List<Class<? extends MultiBehaviour>> getBehaviours () {
        return behaviours;
    }

    public static ChoppedSolution createRandom () {
        final Random rand = new Random();
        final List<Class<? extends MultiBehaviour>> bs = new ArrayList<Class<? extends MultiBehaviour>>();

        @SuppressWarnings("unchecked")
        final Class<? extends MultiBehaviour>[] rands = new Class[] {//
            ZombieBehaviour.class, GoalKeeperBehaviour.class, BackupBehaviour.class, BlockerBehaviour.class,
                DefenseBehaviour.class, DriverBehaviour.class };
        final int[] lefts = { 3, 2, 1, 2, 2, 2 };
        int num = rands.length;

        for (int i = 0; i < 5; i++) {
            final int n = rand.nextInt(num);

            bs.add(rands[n]);
            final int left = --lefts[n];

            if (left == 0) {
                num--;
                rands[n] = rands[num];
                lefts[n] = lefts[num];
            }

        }

        return new ChoppedSolution(bs);
    }

    @Override
    public boolean equals (final Object obj) {
        if (!(obj instanceof ChoppedSolution)) {
            return false;
        }

        final ChoppedSolution cs = (ChoppedSolution) obj;
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
