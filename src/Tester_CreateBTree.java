import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Tester_CreateBTree {
    private int totalRun = 0;
    private int passes = 0;
    private int failures = 0;
    private boolean printFailuresOnly = false;

    private int secTotal = 0;
    private int secPasses = 0;
    private int secFails = 0;

    private int BTreeDegree = 0;

    private enum Result{
        Pass, Fail, FileNotFound, MatchingValue,
        Exception, NoSuchElement, UnexpectedException,
        NoException, IndexOutOfBounds
    }

    private static final TreeObject KEY_AAAA = new TreeObject("aaaa");

    public static void main(String[] args){
        Tester_CreateBTree tester = new Tester_CreateBTree();
        tester.runWindowTests();
        tester.runBTreeNodeTests();
        tester.runBTreeTests();
    }

    private void printFinalSummary() {
        String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
                totalRun, passes, passes * 100.0 / totalRun, failures);
        String line = "";
        for (int i = 0; i < verdict.length(); i++) {
            line += "-";
        }
        System.out.println(line);
        System.out.println(verdict);
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

    private void printSectionSummary() {
        secTotal = totalRun - secTotal;
        secPasses = passes - secPasses;
        secFails = failures - secFails;
        System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
        secTotal = totalRun; //reset for next section
        secPasses = passes;
        secFails = failures;
        System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
                totalRun, passes, passes * 100.0 / totalRun, failures);
    }


    /*
    **********
    **********

    WINDOW SCENARIO BUILDERS

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

    private File multiSequenceWindow5() throws FileNotFoundException{
        File file = new File("multiSequence.gbk");
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        return file;
    }

    private windowScenario multiSequenceWindow5 = () -> multiSequenceWindow5();



    /*
    **********
    **********

    BTREE_NODE SCENARIO BUILDERS

    **********
    **********
    */
    private BTreeNode emptyBTreeNode2(){
        BTreeNode btn = new BTreeNode(null);

        return btn;
    }
    private BTreeNodeScenario emptyBTreeNode2 = () -> emptyBTreeNode2();

    private BTreeNode emptyNode2_InsertAAAA_AAAA(){
        BTreeNode btn = emptyBTreeNode2();
        btn.insertKey(KEY_AAAA);

        return btn;
    }
    private BTreeNodeScenario emptyNode2_InsertAAAA_AAAA = () -> emptyNode2_InsertAAAA_AAAA();

        /*
    **********
    **********

    BTREE_TEST SCENARIO BUILDERS

    **********
    **********
    */

    private static BTree emptyBTree(int degree){
        BTree tree = new BTree(degree, null);

        return tree;
    }
    private BTreeScenario emptyBTree = () -> emptyBTree(BTreeDegree);

    /*
    **********
    **********

    RUNWINDOWTESTS()

    **********
    **********
    */
    private void runWindowTests(){
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

        String brokenSequence_5_ExpectedOutput = "tccacccacccacccacccacccatccatccatctatctc";
        brokenSequence_5_ExpectedOutput += "tctcgctcggtcggtcccaa";
        brokenSequence_5_ExpectedOutput += "ccaaacaaagaaagtaagtgagtgcgtgcttgctcgctctctctgtctgc";
        brokenSequence_5_ExpectedOutput += "ctgcctgcctgccttccttgcttgtttgtg";
        brokenSequence_5_ExpectedOutput += "ttttatttaattaattaatcaatccatccctcccacccagccagc";
        brokenSequence_5_ExpectedOutput += "cagca";

        String multiSequence_5_ExpectedOutput = "gatccatccatccacccacccacccacccacccatccatccatctatctc";
        multiSequence_5_ExpectedOutput += "tctcgctcggtcggtcggtcggtctgtctctctccctccctcccacccaa";
        multiSequence_5_ExpectedOutput += "ccaaacaaagaaagtaagtgagtgcgtgcttgctcgctctctctgtctgc";
        multiSequence_5_ExpectedOutput += "ctgcctgcctgccttccttgcttgtttgtgtgtgcgtgcttgcttgcttt";
        multiSequence_5_ExpectedOutput += "cttttttttatttaattaattaatcaatccatccctcccacccagccagc";
        multiSequence_5_ExpectedOutput += "cagcaagcacgcactcacttactttctttc";
        multiSequence_5_ExpectedOutput += "attgcttgcctgcctgcctccctccctccctcccacccaaccaaacaaag";
        multiSequence_5_ExpectedOutput += "aaaggaaggcaggccggccagccatccatacatatatatctatctatctc";
        multiSequence_5_ExpectedOutput += "tctccctccatccaaccaatcaatcaatcaatcaatcaaccaacaaacat";
        multiSequence_5_ExpectedOutput += "acatgcatggatggctggctggctggctggctgggtgggcgggcgggcgc";
        multiSequence_5_ExpectedOutput += "gcgcgcgcgagcgatcgatggatggatggctggctggcttgcttgcttgt";
        multiSequence_5_ExpectedOutput += "ttgtgtgtgcgtgcttgcttgctttctttt";

        // test window size 5
        testWindowSize5(ShortTestWindowSize5, "ShortTestWindowSize5", WINDOW_SIZE, shortTest1ExpectedOutput);
        testWindowSize5(brokenSequenceWindow5, "brokenSequenceWindow5", WINDOW_SIZE, brokenSequence_5_ExpectedOutput);
        testWindowSize5(multiSequenceWindow5, "multiSequenceWindow5", WINDOW_SIZE, multiSequence_5_ExpectedOutput);
    }


    /*
    **********
    **********

    RUN_BTREENODE_TESTS()

    **********
    **********
    */
    private void runBTreeNodeTests(){
        TreeObject[] NODE_A = {KEY_AAAA};
        long SEQUENCE_A = 0;


        // Node degree 2
        testEmptyNode(emptyBTreeNode2, "emptyBTreeNode");
        testOneKeyNode(emptyNode2_InsertAAAA_AAAA, "emptyNode_InsertAAAA_AAAA", NODE_A, SEQUENCE_A);
    }

    /*
    **********
    **********

    RUN_BTREE_TESTS()

    **********
    **********
     */

    private void runBTreeTests(){
        BTreeDegree = 3;
        BTreeNode oneKey = new BTreeNode(null);
        oneKey.insertKey(new TreeObject("atcg"));
        BTreeNode[] oneNode = {oneKey};
        testEmptyTree(emptyBTree, "emptyBTree", oneNode);

        printFinalSummary();
    }

    //////////////////////////////////
    //XXX Tests for BTree
    //////////////////////////////////
    private void testEmptyTree(BTreeScenario scenario, String scenarioName, BTreeNode[] contents){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);

        try{
            printTest(scenarioName + "insertKey", insertKey(scenario.build(), contents, Result.MatchingValue));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
        finally {
            printSectionSummary();
        }
    }

    //////////////////////////////////
    //XXX Tests for BTreeNode
    //////////////////////////////////

    /**
     * Run all tests on scenarios that begin with an empty BTree.  After an individual
     * test completes, the BTree may or may not contain elements.
     *
     * @param scenario      lambda reference to the BTree scenario builder reference
     * @param scenarioName  name of the scenario being tested
     */
    private void testEmptyNode(BTreeNodeScenario scenario, String scenarioName){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
        try {
            printTest(scenarioName + "_removeAt0", testRemoveAtIndex(scenario.build(),  null, 0, Result.NoSuchElement));
            printTest(scenarioName + "_testGetFirstItem", testGetKeyAtIndex(scenario.build(), 0, null, Result.NoSuchElement));
            printTest(scenarioName + "_InsertKey", testInsertKey(scenario.build(), KEY_AAAA, Result.NoException));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
        finally {
            printSectionSummary();
        }
    }

    private void testOneKeyNode(BTreeNodeScenario scenario, String scenarioName, TreeObject[] contents, long sequenceString){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
        try {
            printTest(scenarioName + "_removeFirstItem", testRemoveAtIndex(scenario.build(), contents[0], 0, Result.MatchingValue));
            printTest(scenarioName + "_removeAt1", testRemoveAtIndex(scenario.build(), null, 1, Result.IndexOutOfBounds));
            printTest(scenarioName + "_testGetFirstItem", testGetKeyAtIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
            printTest(scenarioName + "_InsertKey", testInsertKey(scenario.build(), KEY_AAAA, Result.NoException));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
        finally {
            printSectionSummary();
        }
    }

    ////////////////////////////
    // XXX BTREE TEST METHODS
    ////////////////////////////
    private boolean insertKey(BTree tree, BTreeNode[] expectedNodeStates, Result expectedResult){
        Result result;
//        try{
//            tree.
//        }
        return false;
    }

    //////////////////////////////////
    //XXX Tests for Window to read files
    //////////////////////////////////
    private void testWindowSize5(windowScenario ws, String scenarioName, int windowSize, String expectedOutput){
        System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
        try {
            printTest(scenarioName, testWindow(ws.build(), windowSize, expectedOutput, Result.MatchingValue));
        }
        catch (Exception e){
            System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
            e.printStackTrace();
        }
        finally {
            printSectionSummary();
        }
    }

    private boolean testWindow(File file, int windowSize, String expectedOutput, Result expectedResult){
        GeneBankCreateBTree bt = new GeneBankCreateBTree();
        Result result;

        try {
            String retVal = bt.readGeneBankFile(file, windowSize);
            if (retVal.equals(expectedOutput)){
                result = Result.MatchingValue;
            }
            else{
                result = Result.Fail;
            }
        }
        catch (IOException e){
            if (e instanceof FileNotFoundException){
                result = Result.FileNotFound;
            }
            else {
                result = Result.Exception;
            }
        }

        return result == expectedResult;
    }

    ////////////////////////////
    // XXX BTREE_NODE TEST METHODS
    ////////////////////////////

    private boolean testRemoveAtIndex(BTreeNode node, TreeObject expectedObject, int index, Result expectedResult){
        Result result;
        try{
            TreeObject retVal = node.removeKey(index);
            if (retVal.equals(expectedObject)){
                result = Result.MatchingValue;
            }
            else{
                result = Result.Fail;
            }
        }
        catch (NoSuchElementException e){
            result = Result.NoSuchElement;
        }
        catch (IndexOutOfBoundsException e){
            result = Result.IndexOutOfBounds;
        }
        catch (Exception e){
            System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
            e.printStackTrace();
            result = Result.UnexpectedException;
        }

        return result == expectedResult;
    }

    private boolean testGetKeyAtIndex(BTreeNode node, int index, TreeObject expectedObject, Result expectedResult){
        Result result;
        try{
            TreeObject retVal = node.getKey(index);
            if (retVal.equals(expectedObject)){
                result = Result.MatchingValue;
            }
            else{
                result = Result.Fail;
            }
        }
        catch (NoSuchElementException e){
            result = Result.NoSuchElement;
        }
        catch (Exception e){
            System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
            e.printStackTrace();
            result = Result.UnexpectedException;
        }

        return result == expectedResult;
    }

    private boolean testInsertKey(BTreeNode node, TreeObject object, Result expectedResult){
        Result result;
        try{
            node.insertKey(object);
            result = Result.NoException;
        }
        catch (IndexOutOfBoundsException e){
            result = Result.IndexOutOfBounds;
        }
        catch (Exception e){
            System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
            e.printStackTrace();
            result = Result.UnexpectedException;
        }

        return result == expectedResult;
    }
}

interface windowScenario{
    File build() throws FileNotFoundException;
}

interface BTreeNodeScenario{
    BTreeNode build();
}

interface BTreeScenario{
    BTree build();
}
