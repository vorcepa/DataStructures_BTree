import javax.naming.InvalidNameException;
import java.io.*;
import java.util.ArrayList;

public class GeneBankCreateBTree {
    public static BTree readGeneBankFile(File DNAfile, int sequenceSize, BTree tree) throws IOException {
        boolean readingDNAChars = false;

        InputStream fileStream = new FileInputStream(DNAfile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

        String currentLine = reader.readLine();
        String nextLine;


        while(currentLine != null) {
            if (currentLine.contains("ORIGIN")){
                currentLine = reader.readLine();
                readingDNAChars = true;
            }
            if (currentLine.contains("//")){
                readingDNAChars = false;
            }
            nextLine = reader.readLine();

            if (readingDNAChars){
                ArrayList<TreeObject> nextSequences = generateKeys(sequenceSize, currentLine, nextLine);
                for (int i = 0; i < nextSequences.size(); i++){
                    tree.insert(tree, nextSequences.get(i));
                }

            }

            currentLine = nextLine;
        }
        reader.close();

        return tree;
    }

    public static ArrayList<TreeObject> generateKeys(int sequenceSize, String currentLine, String nextLine){
        currentLine = currentLine.replaceAll("\\s+|\\d", "");
        nextLine = nextLine.replaceAll("\\s+|\\d", "");
        String sequence;
        ArrayList<TreeObject> retVal = new ArrayList<TreeObject>();

        for (int i = 0; i < currentLine.length(); i++){
            if ((i + sequenceSize) > currentLine.length()){
                if (nextLine.contains("//")){
                    break;
                }
                int getFromNextLine = sequenceSize - currentLine.substring(i).length();
                sequence = currentLine.substring(i) + nextLine.substring(0, getFromNextLine);
            }
            else{
                sequence = currentLine.substring(i, (i + sequenceSize));
            }

            if (!sequence.contains("n")) {
                retVal.add(new TreeObject(sequence));
            }
        }

        return retVal;
    }

    private static void printUsage(){
        System.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.exit(-1);
    }

    private static void verifyArguments(String[] args){
        if (args.length < 4 || args.length > 6){
            printUsage();
        }

        if (!args[0].equals("0") && !args[0].equals("1")){
            System.out.println("First argument (no cache/cache) must be 0 or 1");
            printUsage();
        }

        try{
            int degree = Integer.parseInt(args[1]);
            if (degree < 0){
                System.out.println("Degree of BTree (second argument) must be a non-negative integer.");
                printUsage();
            }
        }
        catch (NumberFormatException e){
            System.out.println("Degree of BTree (second argument) must be a non-negative integer.");
            printUsage();
        }

        try{
            File file = new File(args[2]);
            if (!file.exists()){
                throw new FileNotFoundException(file.getAbsolutePath());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found for gbk file.");
            printUsage();
        }

        try{
            int sequenceLength = Integer.parseInt(args[3]);
            if (sequenceLength < 1 || sequenceLength > 31){
                System.out.println("Sequence length (4th argument) must be between 1 and 31 (inclusive)");
                printUsage();
            }
        }
        catch (NumberFormatException e){
            System.out.println("Invalid integer for sequence length (4th argument).  Sequence length must be an integer between 1 and 31 (inclusive)");
            printUsage();
        }


    }

    public static void main(String[] args) throws IOException {
        verifyArguments(args);
        File DNAfile = new File(args[2]);

        String binaryFileName;

        int degree;
        int tempDegree = Integer.parseInt(args[1]);
        if (tempDegree == 0){
            degree = 12;
        }
        else{
            degree = tempDegree;
        }

        int sequenceLength = Integer.parseInt(args[3]);
        int cacheSize = 0;
        binaryFileName = args[2] + ".btree.data." + sequenceLength + "." + degree;

        if ((args.length == 5 || args.length == 6) && args[0].equals("1") && Integer.parseInt(args[4]) < 20){
            System.out.println("Please specify a cache of at least 20");
        }
        else{
            cacheSize = Integer.parseInt(args[4]);
        }

        int debugLevel = 0;
        try{
            if (args.length == 5 && args[0].equals("0")){
                debugLevel = Integer.parseInt(args[4]);
                if (debugLevel != 0 && debugLevel != 1){
                    throw new NumberFormatException();
                }
            }

            if (args.length == 6){
                debugLevel = Integer.parseInt(args[5]);
                if (debugLevel != 0 && debugLevel != 1){
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException e){
            System.out.println("Debug level (5th argument) must be an intger (0 or 1, only)");
            printUsage();
        }

        File file = new File(binaryFileName);
        if (file.exists()){
            file.delete();
        }
        RandomAccessFile fileRAF = new RandomAccessFile(binaryFileName, "rw");
        BTree tree = new BTree(degree, sequenceLength, fileRAF, true, cacheSize);
        tree = readGeneBankFile(DNAfile, sequenceLength, tree);

        if (debugLevel == 1) {
            String dumpFileName = args[2] + ".btree.dump." + sequenceLength;
            File dumpFile = new File(dumpFileName);
            if (dumpFile.exists()) {
                dumpFile.delete();
            }
            dumpFile.createNewFile();
            FileWriter fw = new FileWriter(dumpFile);
            tree.writeDump(fw, tree.getRoot());
            fw.close();
        }
    }
}
