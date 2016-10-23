import main.LimitedGapString;
import main.SearcherThread;
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
public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.printf("Usage: java %s <filename> <prefix> <min>...<max> <suffix\n",
                    Main.class.getName());
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

        LimitedGapString gapString = LimitedGapString.create(args[1], args[3], args[2]);
        final int THREAD_COUNT = gapString.getMaxGapLength() - gapString.getMinGapLength() + 1;
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; ++i) {
            int gapLength = gapString.getMinGapLength() + i;
            threads[i] = new Thread(new SearcherThread(textLines, gapString.getPrefix(),
                    gapString.getSuffix(), gapLength));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                System.err.printf("Error joining thread %d.\n", i);
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        }

        for (int i = 0; i < textLines.size(); ++i) {
            TextLine currentLine = textLines.get(i);
            System.out.printf("%d: %d matches %s\n", i, currentLine.getMatchCount(),
                    currentLine.getContent());
        }
    }
}
