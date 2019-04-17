import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester_CreateBTree {
    private int totalRun = 0;
    private int passes = 0;
    private int failures = 0;
    private boolean printFailuresOnly = false;

    private enum Result{
        Pass, Fail, FileNotFound, MatchingValue, Exception
    }

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


    /*
    **********
    **********

    SCENARIO BUILDERS

    **********
    **********
    */
    private File ShortTestWindowSize5() throws FileNotFoundException{
        File file = new File("shortTest1.gbk");
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        return file;
    }

    private windowScenario ShortTestWindowSize5 = () -> ShortTestWindowSize5();

    private File brokenSequenceWindow5() throws FileNotFoundException{
        File file = new File("brokenSequence.gbk");
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        return file;
    }

    private windowScenario brokenSequenceWindow5 = () -> brokenSequenceWindow5();


    /*
    **********
    **********

    RUNTESTS()

    **********
    **********
    */
    private void runTests(){
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

        String brokenSequenceExpectedOutput = "tccacccacccacccacccacccatccatccatctatctc";
        brokenSequenceExpectedOutput += "tctcgctcggtcggtcccaa";
        brokenSequenceExpectedOutput += "ccaaacaaagaaagtaagtgagtgcgtgcttgctcgctctctctgtctgc";
        brokenSequenceExpectedOutput += "ctgcctgcctgccttccttgcttgtttgtg";
        brokenSequenceExpectedOutput += "ttttatttaattaattaatcaatccatccctcccacccagccagc";
        brokenSequenceExpectedOutput += "cagca";

        // test window size 5
        testWindowSize5(ShortTestWindowSize5, "ShortTestWindowSize5", WINDOW_SIZE, shortTest1ExpectedOutput);
        testWindowSize5(brokenSequenceWindow5, "brokenSequenceWindow5", WINDOW_SIZE, brokenSequenceExpectedOutput);
    }

    private void testWindowSize5(windowScenario ws, String scenarioName, int windowSize, String expectedOutput){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
        try {
            printTest(scenarioName, testWindow(ws.build(), windowSize, expectedOutput, Result.MatchingValue));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
    }

    private boolean testWindow(File file, int windowSize, String expectedOutput, Result expectedResult){
        GeneBankCreateBTree bt = new GeneBankCreateBTree();
        Result result;

        try {
            String retVal = bt.readFile(file, windowSize);
            if (retVal.equals(expectedOutput)){
                result = Result.MatchingValue;
            }
            else{
                result = Result.Fail;
            }
        }
        catch (IOException e){
            result = Result.Exception;
        }

        return result == expectedResult;
    }
}

interface windowScenario{
    File build() throws FileNotFoundException;
}
