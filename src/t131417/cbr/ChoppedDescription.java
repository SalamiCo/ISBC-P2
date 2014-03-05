package t131417.cbr;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Description of the cases for the {@link t131417.ChoppedTeam}.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedDescription implements CaseComponent {

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
