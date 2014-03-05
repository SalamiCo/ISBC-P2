package t131417.cbr;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public final class ChoppedSolution implements CaseComponent {

    /** Identifier for jColibri */
    private long id;

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

}
