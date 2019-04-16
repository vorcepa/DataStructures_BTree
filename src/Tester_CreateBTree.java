import java.io.File;
import java.io.FileNotFoundException;

public class Tester_CreateBTree {
    private int totalRun = 0;
    private int passes = 0;
    private int failures = 0;
    private boolean printFailuresOnly = false;

    public static void main(String[] args){
        Tester_CreateBTree tester = new Tester_CreateBTree();
        tester.runTests();
    }

    private void printTest(String testDesc, boolean result){
        totalRun++;
        if (result) {
            passes++;
        } else {
            failures++;
        }
        if (!result || !printFailuresOnly) {
            System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
        }
    }

    private File ShortTestWindowSize5() throws FileNotFoundException{
        File file = new File("shortTest1.gbk");
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        return file;
    }


    private windowScenario ShortTestWindowSize5 = () -> ShortTestWindowSize5();

    private void runTests(){
        String FILE_SHORTTEST1 = "shortTest1.gbk";
        int WINDOW_SIZE = 5;

        String shortTest1ExpectedOutput = "gatccatccatccacccacccacccacccacccatccatccatctatctc";
        shortTest1ExpectedOutput += "tctcgctcggtcggtcggtcggtctgtctctctccctccctcccacccaa";
        shortTest1ExpectedOutput += "ccaaacaaagaaagtaagtgagtgcgtgcttgctagctagctaggtagga";
        shortTest1ExpectedOutput += "aggatggattgattgattgcttgcatgcaggcaggcaggcaggccggcct";
        shortTest1ExpectedOutput += "gcctgcctgactgagtgagcgagccagccagccacccacccaccgaccgc";
        shortTest1ExpectedOutput += "ccgcgcgcgcgcgcccgcccgcccacccagccagccagctagctggctgc";
        shortTest1ExpectedOutput += "ctgcctgcctgccttccttgcttgtttgtgtgtgcgtgcttgcttgcttt";
        shortTest1ExpectedOutput += "cttttttttatttaattaattaatcaatccatccctcccacccagccagc";
        shortTest1ExpectedOutput += "cagcaagcacgcactcacttactttctttctttcattcagtcagacagag";
        shortTest1ExpectedOutput += "agagggaggcaggccggccagccaaccaagcaaggaaggcaggcaggcag";
        shortTest1ExpectedOutput += "gcaggcaggcaggcgggcgagcgatcgatcgatcaatcagtcagccagct";
        shortTest1ExpectedOutput += "agctggctgactgagtgagggaggtaggtc";

        // test window size 5
        testWindowSize5(ShortTestWindowSize5, "ShortTestWindowSize5", FILE_SHORTTEST1, WINDOW_SIZE, shortTest1ExpectedOutput);
    }

    private void testWindowSize5(windowScenario ws, String scenarioName, String fileName, int windowSize, String expectedOutput){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
        try {
            printTest(scenarioName, testWindow(ws.build(), windowSize, expectedOutput));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
    }

    private boolean testWindow(File file, int windowSize, String expectedOutput){
        return false;
    }
}

interface windowScenario{
    File build() throws FileNotFoundException;
}
