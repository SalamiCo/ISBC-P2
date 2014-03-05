package t131417.cbr;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.selection.SelectCases;

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
    	NNConfig simConfig = new NNConfig();
    	
    	//Global similitude function
    	simConfig.setDescriptionSimFunction(new Average());
    	
    	//Local similitude functions
    	simConfig.addMapping(new Attribute("score", ChoppedDescription.class), new Equal());
    	
    	//Change weights
    	//simConfig.setWeight(new Attribute("score", ChoppedDescription.class), 0.5);
    	
    	//Retrieve solutions
    	Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(baseCase.getCases(), query, simConfig);
    	
    	//Select k best cases
    	eval = SelectCases.selectTopKRR(eval, 5);
    }

    @Override
    public void postCycle () throws ExecutionException {
    	baseCase.close();
    }

}
