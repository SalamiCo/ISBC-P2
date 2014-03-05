package t131417.cbr;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import t131417.MultiBehaviour;

/**
 * CBR application to use in the team manager.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedCBR {

    private List<Entry> entries = new ArrayList<Entry>();

    public ChoppedSolution findSolution (ChoppedCase ccase) {
        Collections.sort(entries, new ChoppedSimilarityComparator(ccase));

        ChoppedSolution selSol = null;
        double selVal = Double.MIN_VALUE;

        int n = 10;
        for (Iterator<Entry> it = entries.iterator(); n > 0 && it.hasNext(); n--) {
            Entry entry = it.next();

            double val = valorate(entry);
        }
        
        return selSol;
    }

    public void load (File file) throws IOException {
        
    }
    
    public void save (File file) throws IOException {
    	PrintWriter pw = new PrintWriter(file);
    	try {
        	for(Entry entry : entries){
        		pw.print(entry.originalCase.getGoalsUs());
        		pw.print("\t");
        		pw.print(entry.originalCase.getGoalsThem());
        		pw.print("\t");
        		pw.print(entry.originalCase.getMatchTime());
        		pw.print("\t");
        		for(Class<? extends MultiBehaviour> cls : entry.solution.getBehaviours()){
        			pw.print(cls.getName());
        			pw.print("\t");
        		}
        		pw.print(entry.positive);
        		pw.print("\t");
        		pw.print(entry.negative);
        		pw.println();
        	}
        } finally {
        	pw.close();
        }
    }

    private double valorate (Entry entry) {
        if (entry.positive + entry.negative == 0) {
            return 0.0;
        }

        double pos = entry.positive;
        double n = entry.positive + entry.negative;

        // Magic algorithm for +/- votes valoration found here:
        // http://www.evanmiller.org/how-not-to-sort-by-average-rating.html
        // Used in Reddit for comments so it must be good!
        double z = 1.9599639715843482; // pre-calculated for 0.95 confidence
        double phat = 1.0 * pos / n;
        return (phat + z * z / (2 * n) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * n)) / n)) / (1 + z * z / n);
    }

    /**
     * Struct-like class for internal entries.
     * 
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     * @author Arturo Pareja García
     */
    private static class Entry {
        /* package */Entry () {
        }

        /* package */ChoppedCase originalCase;
        /* package */ChoppedSolution solution;
        /* package */long positive;
        /* package */long negative;
    }

    private static class ChoppedSimilarityComparator implements Comparator<Entry> {

        private final ChoppedCase ccase;

        /* package */public ChoppedSimilarityComparator (ChoppedCase ccase) {
            this.ccase = ccase;
        }

        @Override
        public int compare (Entry cc1, Entry cc2) {
            double d1 = ccase.similarity(cc1.originalCase);
            double d2 = ccase.similarity(cc2.originalCase);

            return d1 > d2 ? -1 : //
                d1 < d2 ? +1 : 0;
        }

    }
}
