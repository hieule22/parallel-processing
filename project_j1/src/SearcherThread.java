import java.util.List;

/**
 * A class to instantiate a thread that verifies if a value T is common to
 * given list of lists.
 * @author Hieu Le
 * @version September 23rd, 2016
 */

public class SearcherThread implements Runnable {

    /* The value to search for in other lists. */
    private final int value;
    /* The lists from which value is searched. */
    private final List<List<Integer>> targetLists;
    /* Flag to signal if value is common to all lists. */
    private boolean isCommonValue;

    /**
     * Constructs a SearcherThread from given value and list of lists..
     */
    public SearcherThread(int value, List<List<Integer>> targetLists) {
	this.value = value;
	this.targetLists = targetLists;
	this.isCommonValue = false;
    }

    /**
     * Executes this thread.
     */
    @Override
    public void run() {
	isCommonValue = true;
	for (List<Integer> schedule : targetLists) {
	    if (!schedule.contains(value)) {
		isCommonValue = false;
		return;
	    }
	}
	System.out.printf("%d is a common meeting time.\n", value);
    }

    /**
     * Returns whether value is common to all lists.
     */
    public boolean isCommonValue() {
	return isCommonValue;
    }
}
