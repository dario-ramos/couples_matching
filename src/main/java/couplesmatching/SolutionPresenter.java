package couplesmatching;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SolutionPresenter {

    private PrintWriter outputWriter;

    public SolutionPresenter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    /*
     * Output format:
     * ------------------------------
     * Couple count: XXXX
     * Elapsed time: XXXX ms
     * -----------SOLUTION-----------
     * #1: Man1 - Woman1
     * #2: Man2 - Woman2
     * ...
     * #N: ManN - WomanN
     * TODO: Display time in us/ms/s/min/hours/day, if there is a class for that in the JDK
     */
    public void write(ArrayList couples, long timeElapsedInMillis) {
        String line = "------------------------------";
        outputWriter.println(line);
        System.out.println(line);
        line = "Couple count: " + couples.size();
        outputWriter.println(line);
        System.out.println(line);
        line = "Elapsed time: " + timeElapsedInMillis + " ms";
        outputWriter.println(line);
        System.out.println(line);
        line = "-----------SOLUTION-----------";
        outputWriter.println(line);
        System.out.println(line);
        for (int i = 0; i < couples.size(); i++) {
            Couple p = (Couple) couples.get(i);
            line = "#" + (i + 1) + ": ";
            line += p.getMan().getName();
            line += " - ";
            line += p.getWoman().getName();
            outputWriter.println(line);
            System.out.println(line);
        }
        outputWriter.flush();
    }
}
