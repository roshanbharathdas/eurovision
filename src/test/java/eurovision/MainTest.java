package eurovision;

import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class MainTest {

    @Test
    public void testMainNormal() {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        String testOutput = "Belgium 2019 voting results:\n" +
                "8 points go to Ireland\n" +
                "10 points go to Serbia\n" +
                "12 points go to Latvia\n";


        Main.main(new String[]{"load", "./src/test/resources/testFile", "2019"});
        Main.main(new String[]{"results", "Belgium", "2019"});

        assertEquals(testOutput, os.toString());

    }

    @Test
    public void testMainIncorrectInput() {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        String testOutput = "Error: 3 arguments expected.\n" +
                "Usage:\n" +
                "load <file> <year>\n" +
                "results <country> <year>";


        Main.main(new String[]{"arg1"});
        assertEquals(testOutput, os.toString());

    }

    @Test
    public void testMainUnsupportedCommand() {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        String testOutput = "Unsupported command\n";


        Main.main(new String[]{"command", "./src/test/resources/testFile", "2019"});
        assertEquals(testOutput, os.toString());

    }



}
