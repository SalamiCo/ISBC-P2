package t131417;

import teams.ucmTeam.Behaviour;

public abstract class MultiBehaviour extends Behaviour {

    /**
     * Informs this behaviour of how many other behaviours of the same class exist.
     * 
     * @param you Bumber of behaviour
     * @param total Total number of behaviours
     */
    public abstract void multi (int you, int total);
}
