import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author Hieu Le
 * @version 9/18/16
 */
public class CommonMeetingTime {
    /** The total number of lists to process. */
    public static final int N_LISTS = 3;

    /**
     * Main method.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java CommonMeetingTime <filename>");
	        System.exit(-1);
        }

        FileReader inputFile = null;
        try {
            inputFile = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
	        System.err.printf("Error opening file: %s\n", args[0]);
            System.exit(-1);
        }

        Scanner scanner = new Scanner(inputFile);

	    /* Base list is at the beginning of input file. */
        List<Integer> baseList = readList(scanner);
        List<List<Integer>> otherLists = new ArrayList<>(N_LISTS - 1);
        for (int i = 0; i < N_LISTS - 1; ++i) {
            otherLists.add(readList(scanner));
        }

        final int N_THREADS = baseList.size();
        SearcherThread[] frames = new SearcherThread[N_THREADS];
        Thread[] threads = new Thread[N_THREADS];

        for (int i = 0; i < N_THREADS; ++i) {
            frames[i] = new SearcherThread(baseList.get(i), otherLists);
            threads[i] = new Thread(frames[i]);
        }

        for (int i = 0;i < N_THREADS; ++i) {
            threads[i].start();
        }

        boolean hasCommonValue = false;
        for (int i = 0; i < N_THREADS; ++i) {
            try {
                threads[i].join();
                hasCommonValue = (hasCommonValue || frames[i].isCommonValue());
            } catch (InterruptedException e) {
	    	    System.err.printf("Error joining thread %d.\n", i);
		        System.exit(-1);
            }
        }

        if (!hasCommonValue) {
            System.out.println("There is no common meeting time.");
        }

        try {
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and returns an immutable list of numbers from a given Scanner.
     * The first number n of the input represents the size of the list.
     * The following n numbers represent the values in this list.
     */
    private static List<Integer> readList(Scanner scanner) {
        final int SIZE = scanner.nextInt();
        List<Integer> result = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; ++i) {
            result.add(scanner.nextInt());
        }
        return Collections.unmodifiableList(result);
    }
}
