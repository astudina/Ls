import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class LsTest {
    private String[] args;
    private File file;
    private String pathTestFile;

    @Before
    public void setUp() {

        //-l -h -r -o output dir

        long[] lengthFiles = new long[]{0, 10, 1025, 1025 * 1024};
        long[] lastModified = new long[]{0, 1000 * 60 * 60, 3 * 1000 * 60 * 60, 1000 * 60 * 60 * 24};
        String[] nameFiles = new String[]{"name1", "name2", "name3", "name4"};

        File[] arrayFiles = new File[lengthFiles.length];
        for (int i = 0; i < arrayFiles.length; i++) {
            arrayFiles[i] = mock(File.class);
            when(arrayFiles[i].length()).thenReturn(lengthFiles[i]);
            when(arrayFiles[i].getName()).thenReturn(nameFiles[i]);
            when(arrayFiles[i].lastModified()).thenReturn(lastModified[i]);
        }
        File file = mock(File.class);
        when(file.listFiles()).thenReturn(arrayFiles);
        when(file.isDirectory()).thenReturn(true);
        when(file.list()).thenReturn(nameFiles);

        this.file = file;

        pathTestFile = "C:\\Users\\HP\\IdeaProjects\\testConsole\\test.txt";

    }

    @Test
    public void testLH() {
        Ls ls = new Ls(true, true, false);

        String expected = "name1  last mod.: Thu Jan 01 03:00:00 MSK 1970 length: 0,0bytes\n" +
                "name2  last mod.: Thu Jan 01 04:00:00 MSK 1970 length: 10,0bytes\n" +
                "name3  last mod.: Thu Jan 01 06:00:00 MSK 1970 length: 1,0Kb\n" +
                "name4  last mod.: Fri Jan 02 03:00:00 MSK 1970 length: 1,0Mb\n";

        String actual = ls.makeContent(file);

        assertEquals(expected, actual);
    }

    @Test
    public void testLHR() {
        Ls ls = new Ls(true, true, true);

        String expected = "name4  last mod.: Fri Jan 02 03:00:00 MSK 1970 length: 1,0Mb\n" +
                "name3  last mod.: Thu Jan 01 06:00:00 MSK 1970 length: 1,0Kb\n" +
                "name2  last mod.: Thu Jan 01 04:00:00 MSK 1970 length: 10,0bytes\n" +
                "name1  last mod.: Thu Jan 01 03:00:00 MSK 1970 length: 0,0bytes\n";

        String actual = ls.makeContent(file);

        assertEquals(expected, actual);
    }

    @Test
    public void testShortFormat() {
        Ls ls = new Ls(false, false, false);
        String expected = "name1\n" + "name2\n" + "name3\n" + "name4\n";
        String actual = ls.makeContent(file);
        assertEquals(expected, actual);

        ls = new Ls(false, false, true);
        expected = "name4\n" + "name3\n" + "name2\n" + "name1\n";
        actual = ls.makeContent(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testSystemOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Ls ls = new Ls(false, false, true);

        ls.output(file);

        String expectedOutput = "name4\n" + "name3\n" + "name2\n" + "name1\n";

        String actualOutput = outContent.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFileOutput() {
        Ls ls = new Ls(false, false, true, pathTestFile);

        ls.output(file);

        String expectedOutput = "name4\n" + "name3\n" + "name2\n" + "name1\n";

        File actualFile = new File(pathTestFile);
        String actualOutput = "";
        if (actualFile.exists()) {
            try {
                FileReader fr = new FileReader(actualFile);
                BufferedReader bufferedReader = new BufferedReader(fr);
                String tmp;
                while ((tmp = bufferedReader.readLine()) != null) {
                    actualOutput += tmp + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}