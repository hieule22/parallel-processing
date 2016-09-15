/**
 * The Common Meeting Time problem is the problem of finding a list of times
 * when a group of people are all available to meet. This program illustrates
 * a simple concurrent approach to find the solution to the Common Meeting Time
 * problem for 3 people.
 * @author Hieu Le
 * @version September 13th, 2016
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// =============================================================================
// Wrapper for integer array and related helper functions.

/**
 * An array of integers whose size is unknown at compile-time.
 */
struct int_array {
  size_t size;
  int *data;
};

/**
 * Checks if an int_array contains a given value.
 * @param arr pointer to an int_array
 * @param value value to search for
 * @return 1 if value is found; 0 otherwise
 */
int contains (const struct int_array *arr, int value);

/**
 * Reads data from input file to specified array. First number n must represent
 * the array size. The following n numbers represent the array elements.
 * @param input_file pointer to input file to read from
 * @param arr pointer to an int_array that needs to be filled
 */
void read_array_from_file (FILE *input_file, struct int_array *arr);

// =============================================================================

typedef struct int_array schedule_t;

// Number of schedules to process.
const size_t N_SCHEDULES = 3;

// Other schedules to match with base schedule. Visible to all threads.
schedule_t *other_schedules;

/**
 * Determines if a time from base schedule is present in all other schedules.
 * Logs to stdout if base time is common. Used as argument to pthread_create().
 * @param arg a pointer to base time
 * @return 1 if base time is common among all schedules; 0 otherwise
 */
void *is_common_time (void *arg) {
  int base_time = *(int *) arg;
  free (arg);

  // Flag checking if base time is common among all schedules.
  // Initially set to true.
  int *is_common = (int *) malloc (sizeof(int));
  *is_common = 1;
  for (size_t i = 0; i < N_SCHEDULES - 1; ++i) {
    if (!contains (&other_schedules[i], base_time)) {
      *is_common = 0;
      pthread_exit ((void *) is_common);
    }
  }
  // Log to stdout and returns is_common to parent thread.
  fprintf (stdout, "%d is a common meeting time.\n", base_time);
  pthread_exit ((void *) is_common);
}

/**
 * Main method.
 * Input file name must be passed as a command line argument.
 */
int main (int argc, char *argv[]) {
  if (argc != 2) {
    fprintf (stderr, "Usage: %s <input_filename>\n", argv[0]);
    exit (EXIT_FAILURE);
  }

  // Allocates memory for all schedules.
  schedule_t base_schedule;  // Local to main thread.
  other_schedules =
    (schedule_t *) malloc (sizeof(schedule_t) * (N_SCHEDULES - 1));

  // Open and read data from input file.
  FILE *input_file = fopen(argv[1], "r");
  if (input_file == NULL) {
    fprintf (stderr, "Error opening input file: %s\n", argv[1]);
    exit (EXIT_FAILURE);
  }
  // Base schedule is always at the beginning of input file.
  read_array_from_file (input_file, &base_schedule);
  for (size_t i = 0; i < N_SCHEDULES - 1; ++i) {
    read_array_from_file (input_file, &other_schedules[i]);
  }
  fclose (input_file);

  // Initialize and allocate memory for all searcher threads.
  const size_t thread_count = base_schedule.size;
  pthread_t *searcher_threads =
      (pthread_t *) malloc (sizeof(pthread_t) * thread_count);
  
  // Create each searcher thread.
  for (size_t i = 0; i < thread_count; ++i) {
    int *base_time = (int *) malloc (sizeof(int));
    *base_time = base_schedule.data[i];
    if (pthread_create (&searcher_threads[i],
                        NULL,
                        is_common_time,
                        (void *) base_time)) {
      fprintf (stderr, "Error creating thread %zu.\n", i);
      exit (EXIT_FAILURE);
    }
  }

  // Flag checking the existence of a common time. Initialized to false.
  int has_common_time = 0;
  for (size_t i = 0; i < thread_count; ++i) {
    int *is_common = NULL;
    if (pthread_join (searcher_threads[i], (void **) &is_common)) {
      fprintf (stderr, "Error joining thread %zu.\n", i);
      exit (EXIT_FAILURE);
    }
    has_common_time = (has_common_time || *is_common);
    free (is_common);
  }

  if (!has_common_time) {
    fprintf (stdout, "There is no common meeting time.\n");
  }

  // Clean up and exit.
  free (searcher_threads);
  
  free (base_schedule.data);
  for (size_t i = 0; i < N_SCHEDULES - 1; ++i) {
    free (other_schedules[i].data);
  }
  free (other_schedules);

  exit (EXIT_SUCCESS);
}

int contains (const struct int_array *arr, const int value) {
  for (size_t i = 0; i < arr->size; ++i) {
    if (arr->data[i] == value) {
      return 1;
    }
  }
  return 0;
}

void read_array_from_file (FILE *input_file, struct int_array *arr) {
  fscanf (input_file, "%zu", &(arr->size));
  arr->data = (int *) malloc (sizeof(int) * arr->size);
  for (size_t i = 0; i < arr->size; ++i) {
    fscanf (input_file, "%d", &(arr->data[i]));
  }
}
