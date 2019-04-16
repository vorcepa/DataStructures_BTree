import java.io.*;

public class GeneBankCreateBTree {
    public String readFile(File file) throws IOException {
        StringBuilder retVal = new StringBuilder();
        InputStream fileStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            retVal.append(line);
        }
        reader.close();
        System.out.println(retVal.toString());

        return retVal.toString();
    }
}
