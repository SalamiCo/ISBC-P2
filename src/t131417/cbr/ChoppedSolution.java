package t131417.cbr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import t131417.MultiBehaviour;

public final class ChoppedSolution implements CaseComponent {

    /** Identifier for jColibri */
    private long id;

    /** List of behaviours */
    private List<Class<? extends MultiBehaviour>> behaviours = Collections.emptyList();

    @Override
    public Attribute getIdAttribute () {
        return new Attribute("id", getClass());
    }

    public long getId () {
        return id;
    }

    public void setId (long id) {
        this.id = id;
    }

    public List<Class<? extends MultiBehaviour>> getBehaviours () {
        return new ArrayList<Class<? extends MultiBehaviour>>(behaviours);
    }

    public void setBehaviours (List<Class<? extends MultiBehaviour>> behaviours) {
        this.behaviours = new ArrayList<Class<? extends MultiBehaviour>>(behaviours);
    }

    public static ChoppedSolution createRandom () {
        return null;
    }
}
