package eurovision;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Eurovision --- a program to load an input file to year
 * and show the top ten vote results for a country in ascending order.
 *
 * @author Roshan Bharath Das
 */
public class Eurovision {

    private final static int[] pointsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 12};
    private final static int LIMIT = 10;



    /**
     * Loads the input file ({@code file}) to year ({@code year}) by
     * creating a hard link.
     *
     * @param file The path of the input file
     * @param year The path for creating the hard link to the input file
     * @return No return value.
     * @throws IOException it is raised when no file is found
     */
    void load(String file, String year) throws IOException {

        Path filePath = Paths.get(file);
        Path linkPath = Paths.get(".", "src", "main", "resources", year);
        Path resourcesPath = Paths.get(".", "src", "main", "resources");

        if (!Files.exists(resourcesPath)) {
            Files.createDirectory(resourcesPath);
        }

        Files.deleteIfExists(linkPath);
        Files.createLink(linkPath, filePath); //create hard link

    }

    /**
     * Counts the vote for a country ({@code votedFor}) from the file path ({@code file})
     *
     * @param votedFor The country that was voted
     * @param file     The path of the input file
     * @return the votes for a country as a {@code HashMap}
     * @throws IOException it is raised when no file is found
     */
    HashMap<String, Integer> countVotes(String votedFor, Path file) throws IOException {

        HashMap<String, Integer> voteMap = new HashMap<>();

        //identify a pattern ("votedFor":"country") from the line for filtering
        StringBuilder votedForPattern = new StringBuilder();
        votedForPattern.append("\"votedFor\":\"");
        votedForPattern.append(votedFor);
        votedForPattern.append("\"");

        Files.lines(file)
                .filter(line -> line.contains(votedForPattern))
                .map(line -> line.split("\"")[3]) //take the fourth string after splitting
                .forEach(word -> voteMap.compute(word, (country, count) -> count == null ? 1 : count + 1));


        return voteMap;
    }

    /**
     * Sorts the top 10 votes in ascending order from a hashmap ({@code voteMap})
     *
     * @param voteMap The hashmap of list of votes
     * @return the sorted votes for a country as a {@code List}
     */
    List<String> getTopSorted(HashMap<String, Integer> voteMap) {

        List<String> sortedList = voteMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(LIMIT) //take top 10
                .sorted(Map.Entry.comparingByValue()) //sort in ascending order
                .map(fromMap -> fromMap.getKey())
                .collect(Collectors.toList());

        return sortedList;

    }

    /**
     * Prints the results in a particular format
     *
     * @param sortedVoteList The list of top 10 sorted votes
     * @param votedFor       The country for which the votes were counted
     * @param year           The year of the eurovision event
     * @return No return value
     */
    void showResults(List<String> sortedVoteList, String votedFor, String year) {

        //adjust the position when the map contains less than 10 elements
        int position = LIMIT - sortedVoteList.size();

        StringBuilder output = new StringBuilder();

        output.append(votedFor);
        output.append(" ");
        output.append(year);
        output.append(" voting results:\n");


        for (String vote : sortedVoteList) {

            output.append(pointsArray[position]);
            if (pointsArray[position] == 1) {
                output.append(" point goes to ");
            } else {
                output.append(" points go to ");
            }
            output.append(vote);
            output.append("\n");

            position++;
        }
        System.out.print(output);

    }

    /**
     * Prints out the top 10 vote results for a country ({@code votedFor})
     * and year ({@code year}) in ascending order. It performs three tasks:
     * 1) Update votes in to a hashmap
     * 2) Sort the votes and keep the top 10 votes in ascending order
     * 3) Shows the votes in a format
     *
     * @param votedFor The country that other countries voterFor
     * @param year     The file that was put under year
     * @return No return value.
     * @throws IOException it is raised when no file is found under year
     */
    void results(String votedFor, String year) throws IOException {

        Path file = Paths.get(".", "src", "main", "resources", year);

        //update vote
        HashMap<String, Integer> voteMap = countVotes(votedFor, file);

        //sort and limit top 10 in ascending
        List<String> sortedVoteList = getTopSorted(voteMap);

        //create and show output
        showResults(sortedVoteList, votedFor, year);

    }


}
