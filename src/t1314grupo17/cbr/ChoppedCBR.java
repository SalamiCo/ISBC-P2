package t1314grupo17.cbr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import t1314grupo17.MultiBehaviour;

/**
 * CBR application to use in the team manager.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @author Arturo Pareja García
 */
public final class ChoppedCBR {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final List<Entry> entries = new ArrayList<Entry>();

    public ChoppedSolution findSolution (final ChoppedCase ccase) {
        Collections.sort(entries, new ChoppedSimilarityComparator(ccase));

        ChoppedSolution selSol = null;
        double selVal = Double.MIN_VALUE;

        int n = 10;
        for (final Iterator<Entry> it = entries.iterator(); n > 0 && it.hasNext(); n--) {
            final Entry entry = it.next();
            final double val = valorate(entry);

            System.out.println("sim: " + entry.originalCase.similarity(ccase) + " -- val: " + val);

            if (selVal < val) {
                selSol = entry.solution;
                selVal = val;
            }
        }

        if (selSol == null) {
            System.out.println(ccase + " $ RANDOM");
            return ChoppedSolution.createRandom();
        }

        System.out.println(ccase + " % " + selVal);
        return selSol;
    }

    public void clear () {
        entries.clear();
    }

    public void load (final File file) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF8));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                final String[] pieces = line.split("\\s+");

                final int gu = Integer.parseInt(pieces[0]);
                final int gt = Integer.parseInt(pieces[1]);
                final int t = Integer.parseInt(pieces[2]);
                final ChoppedCase ocase = new ChoppedCase(gu, gt, t);

                final List<Class<? extends MultiBehaviour>> classes = new ArrayList<Class<? extends MultiBehaviour>>();
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[3]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[4]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[5]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[6]));
                classes.add((Class<? extends MultiBehaviour>) Class.forName(pieces[7]));
                final ChoppedSolution sol = new ChoppedSolution(classes);

                // Create the entry
                final Entry entry = new Entry();
                entry.originalCase = ocase;
                entry.solution = sol;
                entry.positive = Integer.parseInt(pieces[8]);
                entry.negative = Integer.parseInt(pieces[9]);

                // Add the entry
                entries.add(entry);
            }
        } catch (final ClassNotFoundException exc) {
            exc.printStackTrace();

        } finally {
            in.close();
        }
    }

    public void add (final ChoppedCase ccase, final ChoppedSolution solution, final boolean positive) {
        Entry e = null;

        // Loop over the entries
        for (final Entry entry : entries) {
            if (entry.originalCase.similarity(ccase) > 0.9 && entry.solution.equals(solution)) {
                e = entry;
                break;
            }
        }

        // If we didn't find it, add a new one
        if (e == null) {
            e = new Entry();
            e.originalCase = ccase;
            e.solution = solution;
            e.positive = 0;
            e.negative = 0;

            entries.add(e);
        }

        if (positive) {
            e.positive++;
        } else {
            e.negative++;
        }
    }

    public void save (final File file) throws IOException {
        final PrintWriter pw = new PrintWriter(file);
        try {
            for (final Entry entry : entries) {
                pw.print(entry.originalCase.getGoalsUs());
                pw.print("\t");
                pw.print(entry.originalCase.getGoalsThem());
                pw.print("\t");
                pw.print(entry.originalCase.getMatchTime());
                pw.print("\t");
                for (final Class<? extends MultiBehaviour> cls : entry.solution.getBehaviours()) {
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

    private double valorate (final Entry entry) {
        if (entry.positive + entry.negative == 0) {
            return 0.0;
        }

        final double pos = entry.positive;
        final double n = entry.positive + entry.negative;

        // Magic algorithm for +/- votes valoration found here:
        // http://www.evanmiller.org/how-not-to-sort-by-average-rating.html
        // Used in Reddit for comments so it must be good!
        final double z = 1.644853646608357; // pre-calculated for 0.90 confidence
        final double phat = 1.0 * pos / n;
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

        /* package */public ChoppedSimilarityComparator (final ChoppedCase ccase) {
            this.ccase = ccase;
        }

        @Override
        public int compare (final Entry cc1, final Entry cc2) {
            final double d1 = ccase.similarity(cc1.originalCase);
            final double d2 = ccase.similarity(cc2.originalCase);

            return d1 > d2 ? -1 : //
                d1 < d2 ? +1 : 0;
        }

    }
}
