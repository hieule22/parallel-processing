/**
 * Program to find a list of times when a group of people are all vailable to
 * meet.
 */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// Maximum number of meetings in a person's schedule.
#define MAX_ARRAY_SIZE 50
// Number of schedules to process.
#define SCHEDULE_COUNT 3

/**
 * Reads input from input file to given array of specified size.
 */
void ReadArrayFromFile(FILE *source, int *data, int size) {
  for (int i = 0; i < size; ++i) {
    fscanf(source, "%d", data + i);
  }
}

/**
 * Struct containing information about a person's meeting schedule.
 */
typedef struct Schedule {
  // The number of meetings.
  int meeting_count;
  // The times for all meetings.
  int meeting_times[MAX_ARRAY_SIZE];
} Schedule;

/**
 * Checks if an input schedule contains a specified time.
 * Returns 1 if found and 0 otherwise.
 */
int ContainsTime(Schedule *schedule, int time) {
  for (int i = 0; i < schedule->meeting_count; ++i) {
    if (schedule->meeting_times[i] == time) {
      return 1;
    }
  }
  return 0;
}

// Other schedules to match with base schedule.
Schedule other_schedules[SCHEDULE_COUNT - 1];

/**
 * Function to be executed by each thread.
 * Determines if a time from base schedule is present in all other schedules.
 */
void* FindCommonTime(void *arg) {
  int base_time = *(int *) arg;
  free(arg);
  
  int *status = (int *) malloc(sizeof(int));
  *status = 1;
  for (int i = 0; i < SCHEDULE_COUNT - 1; ++i) {
    if (!ContainsTime(&other_schedules[i], base_time)) {
      *status = 0;
      return (void *) status;
    }
  }

  if (*status) {
    fprintf(stdout, "%d is a common meeting time.\n", base_time);
  }
  return (void *) status;
}

int main(int argc, char **argv) {
  if (argc < 2) {
    fprintf(stderr, "Missing command line argument(s).\n");
    exit(EXIT_FAILURE);
  }

  
  FILE *input_file = fopen(argv[1], "r");
  if (input_file == NULL) {
    fprintf(stderr, "Error opening input file: %s\n", argv[1]);
    exit(EXIT_FAILURE);
  }

  Schedule base_schedule;

  // Read input from file
  fscanf(input_file, "%d", &base_schedule.meeting_count);
  ReadArrayFromFile(input_file, base_schedule.meeting_times,
		    base_schedule.meeting_count);
  for (int i = 0; i < SCHEDULE_COUNT - 1; ++i) {
    fscanf(input_file, "%d", &other_schedules[i].meeting_count);
    ReadArrayFromFile(input_file, other_schedules[i].meeting_times,
		      other_schedules[i].meeting_count);
  }
  fclose(input_file);
  
  int thread_count = base_schedule.meeting_count;
  pthread_t **workers = (pthread_t **) malloc(sizeof(pthread_t *) *
					      thread_count);
  // Allocate memory for pthread_t's.
  for (int i = 0; i < thread_count; ++i) {
    workers[i] = (pthread_t *) malloc(sizeof(pthread_t));
  }

  // Create the worker threads.
  int* base_time = NULL;
  for (int i = 0; i < thread_count; ++i) {
    base_time = (int *) malloc(sizeof(int));
    *base_time = base_schedule.meeting_times[i];
    if (pthread_create(workers[i], NULL, FindCommonTime, (void *) base_time)) {
      fprintf(stderr, "Error creating thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
  }

  int found = 0;
  for (int i = 0; i < thread_count; ++i) {
    void *status = (void *) malloc(sizeof(int));
    if (pthread_join(*workers[i], &status)) {
      fprintf(stderr, "Error joining thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
    found = (found || (*(int *) status));
    free(status);
  }

  if (!found) {
    fprintf(stdout, "There is no common meeting time.\n");
  }

  // Clean up and exit.
  for (int i = 0; i < thread_count; ++i) {
    free(workers[i]);
  }
  free(workers);
  exit(EXIT_SUCCESS);
}
