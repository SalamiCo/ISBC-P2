package t131417;

import teams.ucmTeam.Behaviour;

public abstract class MultiBehaviour extends Behaviour {

    /**
     * Constructs a behaviour that for nowit's the only one
     */
    protected MultiBehaviour () {
        this(0, 1);
    }

    /**
     * Constructs a behaviour with known parameters.
     * 
     * @param you Number of this behaviour
     * @param total Total number of behaviours
     */
    protected MultiBehaviour (int you, int total) {
        multi(you, total);
    }

    /**
     * Informs this behaviour of how many other behaviours of the same class exist.
     * 
     * @param you Number of this behaviour
     * @param total Total number of behaviours
     */
    public abstract void multi (int you, int total);
}
