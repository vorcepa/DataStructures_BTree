import java.io.*;

public class GeneBankCreateBTree {
    public String readFile(File file, int sequenceSize) throws IOException {
        boolean readingDNAChars = false;

        StringBuilder retVal = new StringBuilder();
        InputStream fileStream = new FileInputStream(file);
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
                retVal.append(generateKeys(sequenceSize, currentLine, nextLine));
            }

            currentLine = nextLine;
        }
        reader.close();

        return retVal.toString();
    }

    public String generateKeys(int sequenceSize, String currentLine, String nextLine){
        currentLine = currentLine.replaceAll("\\s+|\\d", "");
        nextLine = nextLine.replaceAll("\\s+|\\d", "");
        String sequence;
        StringBuilder retVal = new StringBuilder();

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
                retVal.append(sequence);
            }
        }

        return retVal.toString();
    }

    public static void main(String[] args){
        TreeObject t = new TreeObject("catg");
        System.out.println(Long.toBinaryString(t.getSequence()));
    }
}
