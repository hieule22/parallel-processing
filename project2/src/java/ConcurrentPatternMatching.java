import main.TextLine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class ConcurrentPatternMatching {
    public static void main(String[] args) {
        if (args.length <= 1) {
            System.err.printf("Usage: java %s <filename> <prefix> <min>...<max> <suffix\n",
                    ConcurrentPatternMatching.class.getName());
            System.exit(-1);
        }

        FileReader inputFile = null;
        try {
            inputFile = new FileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.printf("Error opening file: %s\n", args[0]);
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        Scanner fileScanner = new Scanner(inputFile);
        List<TextLine> textLines = new ArrayList<TextLine>();
        while (fileScanner.hasNextLine()) {
            textLines.add(new TextLine(fileScanner.nextLine()));
        }
        
    }
}
