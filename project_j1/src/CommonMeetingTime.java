import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Hieu Le
 * @version 9/18/16
 */
public class CommonMeetingTime {
    public static final int N_SCHEDULES = 3;

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Filename must be passed as program argument.");
        }

        FileReader inputFile = null;
        try {
            inputFile = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Scanner scanner = new Scanner(inputFile);

        List<Integer> baseSchedule = readSchedule(scanner);
        List<List<Integer>> otherSchedules = new ArrayList<>(N_SCHEDULES - 1);
        for (int i = 0; i < N_SCHEDULES - 1; ++i) {
            otherSchedules.add(readSchedule(scanner));
        }

        final int THREAD_COUNT = baseSchedule.size();
        SearcherFrame[] frames = new SearcherFrame[THREAD_COUNT];
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; ++i) {
            frames[i] = new SearcherFrame(baseSchedule.get(i), otherSchedules);
            threads[i] = new Thread(frames[i]);
        }

        for (int i = 0;i < THREAD_COUNT; ++i) {
            threads[i].start();
        }

        boolean hasCommonTime = false;
        for (int i = 0; i < THREAD_COUNT; ++i) {
            try {
                threads[i].join();
                hasCommonTime = (hasCommonTime || frames[i].isCommon());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!hasCommonTime) {
            System.out.println("There is no common meeting time.");
        }

        try {
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> readSchedule(final Scanner scanner) {
        int size = scanner.nextInt();
        List<Integer> schedule = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            schedule.add(scanner.nextInt());
        }
        return schedule;
    }
}

class SearcherFrame implements Runnable {

    private final int baseTime;
    private final List<List<Integer>> otherSchedules;
    private boolean isCommon;

    public SearcherFrame(int baseTime, List<List<Integer>> otherSchedules) {
        this.baseTime = baseTime;
        this.otherSchedules = otherSchedules;
        this.isCommon = false;
    }

    @Override
    public void run() {
        this.isCommon = true;
        for (List<Integer> schedule : otherSchedules) {
            if (!schedule.contains(baseTime)) {
                this.isCommon = false;
                return;
            }
        }
        System.out.printf("%d is a common meeting time.\n", baseTime);
    }

    public boolean isCommon() {
        return isCommon;
    }
}