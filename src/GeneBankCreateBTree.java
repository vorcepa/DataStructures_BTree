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

    public static void main(String[] args) throws IOException {
        File file = new File("testFile.txt");
        if (file.exists()){
            file.delete();
        }

        File DNAfile = new File("test3.gbk");

        RandomAccessFile fileRAF = new RandomAccessFile("testFile.txt", "rw");
        int sequenceLength = 7;
        BTree tree = new BTree(3, sequenceLength, fileRAF, true);
        tree = readGeneBankFile(DNAfile, sequenceLength, tree);


        File dumpFile = new File("dumpTest2.txt");
        if (dumpFile.exists()){
            dumpFile.delete();
        }
        dumpFile.createNewFile();
        FileWriter fw = new FileWriter(dumpFile);
        tree.writeDump(fw, tree.getRoot());
        fw.close();
    }
}
