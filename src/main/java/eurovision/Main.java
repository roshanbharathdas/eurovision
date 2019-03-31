package eurovision;

import java.io.IOException;

/**
 * Main --- entry class
 *
 * @author Roshan Bharath Das
 */
public class Main {

    static final private int ARGUMENTS_SIZE = 3;

    /**
     * Three elements from command line argument used to perform two separate functions:
     * 1) Load - Loads a file and puts it under a year
     * 2) Results - Prints out the top 10 vote results for a country and year in ascending order
     *
     * @param args A string array containing the command line arguments.
     * @return No return value.
     */
    public static void main(String[] args) {

        if (args.length == ARGUMENTS_SIZE) {

            Eurovision eurovision = new Eurovision();

            String command = args[0];

            if (command.equals("load")) {
                String file = args[1];
                String year = args[2];

                try {
                    eurovision.load(file, year);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (command.equals("results")) {
                String votedFor = args[1];
                String year = args[2];

                try {
                    eurovision.results(votedFor, year);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.print("Unsupported command\n");
            }

        } else {
            System.out.print("Error: 3 arguments expected.\n" +
                    "Usage:\n" +
                    "load <file> <year>\n" +
                    "results <country> <year>");
        }

    }

}
