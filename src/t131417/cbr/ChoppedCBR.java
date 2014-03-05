package t131417.cbr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

        ChoppedSolution selSol = ChoppedSolution.createRandom();
        double selVal = Double.MIN_VALUE;

        int n = 10;
        for (Iterator<Entry> it = entries.iterator(); n > 0 && it.hasNext(); n--) {
            Entry entry = it.next();

            double val = valorate(entry);
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

}
