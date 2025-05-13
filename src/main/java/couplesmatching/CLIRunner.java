package couplesmatching;

import java.io.*;
import java.util.ArrayList;

public class CLIRunner {

    private enum ExitCodes {
        INPUT_FILE_NOT_SPECIFIED,
        ERROR_READING_INPUT_FILE,
        ERROR_WRITING_BT_OUTPUT_FILE,
        ERROR_WRITING_GS_OUTPUT_FILE
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("error: must specify input file path.");
            System.exit(ExitCodes.INPUT_FILE_NOT_SPECIFIED.ordinal());
        }

        Agency agency;
        try {
            FileReader inputFileReader = new FileReader(args[0]);
            BufferedReader inputReader = new BufferedReader(inputFileReader);
            AgencyBuilder builder = new AgencyBuilder(inputReader);
            agency = builder.build();
        } catch (Exception ex) {
            System.err.println("Fatal error reading input file: " + ex.getMessage());
            System.exit(ExitCodes.ERROR_READING_INPUT_FILE.ordinal());
            return;
        }

        solve(agency);
    }

    private static void solve(Agency agency) {
        long startTime, endTime;
        // Perform garbage collection before starting, for a more precise measurement
        System.gc();
        startTime = System.currentTimeMillis();
        ArrayList couples = agency.assignUsingBacktracking();
        endTime = System.currentTimeMillis();
        presentSolution(
                couples, "sc_BT.txt", "BT",
                ExitCodes.ERROR_WRITING_BT_OUTPUT_FILE.ordinal(), endTime - startTime
        );

        // Break up all couples before running Gale Shapley.
        for (int i = 0; i < couples.size(); i++) {
            Couple p = (Couple) couples.get(i);
            p.breakUp();
        }

        System.gc();
        startTime = System.currentTimeMillis();
        couples = agency.assignUsingGaleShapley();
        endTime = System.currentTimeMillis();
        presentSolution(
                couples, "sc_GS.txt", "GS",
                ExitCodes.ERROR_WRITING_GS_OUTPUT_FILE.ordinal(), endTime - startTime
        );
    }

    private static void presentSolution(
            ArrayList couples, String outputFilePath, String algorithm, int fatalErrorCode, long elapsedTimeInMillis
    ) {
        FileWriter outputFileWriter;
        try {
            outputFileWriter = new FileWriter(outputFilePath);
        } catch (IOException e) {
            System.err.println("Fatal error writing " + algorithm + " solution: " + e.getMessage());
            System.exit(fatalErrorCode);
            return;
        }
        PrintWriter btOutputWriter = new PrintWriter(new BufferedWriter(outputFileWriter));
        SolutionPresenter btSolutionPresenter = new SolutionPresenter(btOutputWriter);
        btSolutionPresenter.write(couples, elapsedTimeInMillis);
    }
}
