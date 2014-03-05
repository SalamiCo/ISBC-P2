package t131417.cbr;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;

/**
 * CBR application to use in the team manager.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedCBR implements StandardCBRApplication {

    /** Case base */
    private final CBRCaseBase baseCase = new LinealCaseBase();

    /** Database connector */
    private final Connector connector = new DataBaseConnector();

    @Override
    public void configure () throws ExecutionException {
    	try{
    		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(""));
    	} catch (Exception e) {
    		throw new ExecutionException(e);
    	}
    	
    }

    @Override
    public CBRCaseBase preCycle () throws ExecutionException {
        baseCase.init(connector);
        return baseCase;
    }

    @Override
    public void cycle (CBRQuery query) throws ExecutionException {

    }

    @Override
    public void postCycle () throws ExecutionException {
    	baseCase.close();
    }

}
