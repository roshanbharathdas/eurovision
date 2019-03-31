package eurovision;

import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class EurovisionTest {

    Eurovision classUnderTest = new Eurovision();

    final String YEAR = "2019";
    final String WRONG_FORMAT_YEAR = ".";
    final String UNKNOWN_YEAR = "19$872";


    final String FILE = "./src/test/resources/testFile";
    final String EMPTY_FILE = "./src/test/resources/emptyFile";
    final String NON_EXISTENT_FILE = "./src/test/resources/nonExistantFile";

    final String LOADED_FILE_PATH = "./src/main/resources/" + YEAR;
    Path file = Paths.get(".", "src", "main", "resources", YEAR);

    final String VOTED_FOR_BELGIUM = "Belgium";


    @Test
    public void testLoadFile() throws IOException {

        classUnderTest.load(FILE, YEAR);
        assertTrue(new File(LOADED_FILE_PATH).exists());

    }

    @Test(expected = IOException.class)
    public void testLoadNonExistantFile() throws IOException {

        classUnderTest.load(NON_EXISTENT_FILE, YEAR);

    }

    @Test(expected = IOException.class)
    public void testLoadWrongFormatYear() throws IOException {

        classUnderTest.load(EMPTY_FILE, WRONG_FORMAT_YEAR);

    }

    @Test
    public void testCountVotesNormal() throws IOException {

        classUnderTest.load(FILE, YEAR);

        HashMap<String, Integer> testMap = new HashMap<>();
        testMap.put("Latvia", 4);
        testMap.put("Serbia", 3);
        testMap.put("Ireland", 1);

        HashMap<String, Integer> voteMap = classUnderTest.countVotes(VOTED_FOR_BELGIUM, file);

        assertTrue(voteMap.equals(testMap));

    }

    @Test
    public void testCountVotesWrongVotedFor() throws IOException {

        classUnderTest.load(FILE, YEAR);
        HashMap<String, Integer> voteMap;


        //empty
        voteMap = classUnderTest.countVotes("", file);
        assertTrue(voteMap.isEmpty());

        //half
        voteMap = classUnderTest.countVotes("Belg", file);
        assertTrue(voteMap.isEmpty());

        //case sensitivity
        voteMap = classUnderTest.countVotes("BELGIUM", file);
        assertTrue(voteMap.isEmpty());

        //unknown
        voteMap = classUnderTest.countVotes("ABCD", file);
        assertTrue(voteMap.isEmpty());

    }

    @Test(expected = IOException.class)
    public void testCountVotesWrongYear() throws IOException {

        classUnderTest.load(FILE, YEAR);

        Path wrong_file = Paths.get(".", "src", "main", "resources", UNKNOWN_YEAR);

        classUnderTest.countVotes(VOTED_FOR_BELGIUM, wrong_file);

    }

    @Test
    public void testGetTopSortedNormal() throws IOException {

        classUnderTest.load(FILE, YEAR);

        HashMap<String, Integer> voteMap = classUnderTest.countVotes(VOTED_FOR_BELGIUM, file);

        List<String> testSortedList = new ArrayList<>();
        testSortedList.add("Ireland");
        testSortedList.add("Serbia");
        testSortedList.add("Latvia");

        List<String> sortedVoteList = classUnderTest.getTopSorted(voteMap);

        assertThat(sortedVoteList, is(testSortedList));
    }

    @Test
    public void testShowResultsNormal() throws IOException {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        classUnderTest.load(FILE, YEAR);

        HashMap<String, Integer> voteMap = classUnderTest.countVotes(VOTED_FOR_BELGIUM, file);

        List<String> sortedVoteList = classUnderTest.getTopSorted(voteMap);

        classUnderTest.showResults(sortedVoteList, VOTED_FOR_BELGIUM, YEAR);

        String testOutput = "Belgium 2019 voting results:\n" +
                "8 points go to Ireland\n" +
                "10 points go to Serbia\n" +
                "12 points go to Latvia\n";

        assertEquals(testOutput, os.toString());

    }

    @Test
    public void testResultsNormal() throws IOException {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        classUnderTest.load(FILE, YEAR);

        classUnderTest.results(VOTED_FOR_BELGIUM, YEAR);

        String testOutput = "Belgium 2019 voting results:\n" +
                "8 points go to Ireland\n" +
                "10 points go to Serbia\n" +
                "12 points go to Latvia\n";

        assertEquals(testOutput, os.toString());

    }

    @Test(expected = IOException.class)
    public void testResultsUnknownYear() throws IOException {

        classUnderTest.load(FILE, YEAR);

        classUnderTest.results(VOTED_FOR_BELGIUM, UNKNOWN_YEAR);

    }

}
