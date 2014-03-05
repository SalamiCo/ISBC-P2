package t131417.cbr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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

    private static final Charset UTF8 = Charset.forName("UTF-8");

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

    public void clear () {
        entries.clear();
    }

    public void load (File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF8));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String[] pieces = line.split("\\s+");

                int gu = Integer.parseInt(pieces[0]);
                int gt = Integer.parseInt(pieces[1]);
                int t = Integer.parseInt(pieces[2]);
                ChoppedCase ocase = new ChoppedCase(gu, gt, t);

                List<Class<? extends MultiBehaviour>> classes = new ArrayList<Class<? extends MultiBehaviour>>();
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[3]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[4]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[5]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[6]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[7]));
                ChoppedSolution sol = new ChoppedSolution(classes);

                // Create the entry
                Entry entry = new Entry();
                entry.originalCase = ocase;
                entry.solution = sol;
                entry.positive = Integer.parseInt(pieces[8]);
                entry.negative = Integer.parseInt(pieces[9]);
                
                // Add the entry
                entries.add(entry);
            }
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace();
            
        } finally {
            in.close();
        }
    }

    public void save (File file) throws IOException {

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
