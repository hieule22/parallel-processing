/**
 * CS 435 - Project 4: A Concurrent Computation of PI.
 * Multi-threaded program to perform the Monte Carlo simulation method for
 * approximating the value of PI.
 * @author Hieu Le
 * @version December 7th, 2016
 */

#include <math.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "list.h"

#define RADIUS 0.5

/**
 * A struct storing the information associated with a simulation.
 */
typedef struct {
  // The number of points inside the unit circle.
  int n_interior;
  // The total number of points examined so far.
  int n_total;
  // The maximum number of experiments to be performed.
  int maximum;
  // The number of experiments between printing of approximation.
  int interval;
} simulation_t;

// Main simulation and its guarding mutex.
simulation_t simulation;
pthread_mutex_t simulation_lock;
// Approximation list and its guarding mutex.
list_t list;
pthread_mutex_t list_lock;
// Mutex guarding the random number generator.
pthread_mutex_t rand_lock;
// The condition variable that signals if a new element has been added
// to the approximation list.
pthread_cond_t list_has_new_elements;

/*
 * Actions to be performed by the simulation threads.
 */
void *simulate(void *arg) {
  while (true) {
    // Generation of a random data point must be guarded with a mutex since
    // the standard library implementation of rand() is not thread-safe.
    pthread_mutex_lock(&rand_lock);
    int x = rand();
    int y = rand();
    pthread_mutex_unlock(&rand_lock);

    // Normalize the coordinates of the random point and compute its distance
    // from the center of the unit circle. It is arguably more efficient to
    // compare the square of the distance with the square of the radius, since
    // the sqrt() function can be expensive.
    double dx = ((double) x) / RAND_MAX - RADIUS;
    double dy = ((double) y) / RAND_MAX - RADIUS;
    double squared_dist = dx * dx + dy * dy;

    // Updates to the simulation's counter and accumulator must be atomic.
    pthread_mutex_lock(&simulation_lock);
    // Drop the lock and terminate if the simulation has completed.
    if (simulation.n_total == simulation.maximum) {
      pthread_mutex_unlock(&simulation_lock);
      pthread_exit(NULL);
    }
    ++simulation.n_total;
    simulation.n_interior += (squared_dist <= RADIUS * RADIUS) ? 1 : 0;
    // Add to the list the information necessary to compute the current
    // approximation if the number of experiments performed so far is a
    // multiple of interval or equals the maximum number of experiments.
    if (simulation.n_total % simulation.interval == 0
        || simulation.n_total == simulation.maximum) {
      pair_t element = {simulation.n_interior, simulation.n_total};
      // Atomically add a new element to the approximation list and signal
      // printing thread of this change.
      pthread_mutex_lock(&list_lock);
      list_add(&list, &element);
      pthread_cond_broadcast(&list_has_new_elements);
      pthread_mutex_unlock(&list_lock);
    }
    pthread_mutex_unlock(&simulation_lock);
  }

  // Defensive programming. This code segment should never be executed.
  fprintf(stderr, "Programming error.\n");
  exit(EXIT_FAILURE);
}

/**
 * Actions to be performed by the printing thread.
 */
void *print(void *arg) {
  // index stores the position from the list of the next approximation to print.
  size_t index = 0;
  while (index < list.capacity) {
    pthread_mutex_lock(&list_lock);
    // The predicate is index < size(list), i.e there are unprinted
    // approximations from the list..
    while (index >= list_size(&list)) {
      pthread_cond_wait(&list_has_new_elements, &list_lock);
    }
    const pair_t *element = list_get(&list, index);
    pthread_mutex_unlock(&list_lock);
    // Compute and print the current approximation.
    double estimate = ((double) (element->first * 4)) / element->second;
    fprintf(stdout, "%zd: %.9f\n", element->second, estimate);
    ++index;
  }

  pthread_exit(NULL);
}

int main(int argc, char *argv[]) {
  if (argc != 4) {
    fprintf(stderr, "Usage: %s <number of simulation threads> "
            "<total number of experiments> "
            "<number of experiments between printing>.\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  simulation.maximum = atoi(argv[2]);
  simulation.interval = atoi(argv[3]);
  simulation.n_interior = simulation.n_total = 0;
  if (pthread_mutex_init(&simulation_lock, NULL)) {
    fprintf(stderr, "Error initializing simulation_lock.\n");
    exit(EXIT_FAILURE);
  }

  // The list's capacity, i.e the total number of approximations to print, can
  // be shown to be equal to ceiling(maximum / interval).
  int n_estimates = ceil(((double) simulation.maximum) / simulation.interval);
  list_init(&list, n_estimates);
  if (pthread_mutex_init(&list_lock, NULL)) {
    fprintf(stderr, "Error initializing list_lock.\n");
    exit(EXIT_FAILURE);
  }

  srand(time(NULL));
  if (pthread_mutex_init(&rand_lock, NULL)) {
    fprintf(stderr, "Error initializing rand_lock.\n");
    exit(EXIT_FAILURE);
  }

  if (pthread_cond_init(&list_has_new_elements, NULL)) {
    fprintf(stderr, "Error initializing condition variable.\n");
    exit(EXIT_FAILURE);
  }

  const int n_simulators = atoi(argv[1]);
  pthread_t simulators[n_simulators];
  for (int i = 0; i < n_simulators; ++i) {
    if (pthread_create(&simulators[i], NULL, simulate, NULL)) {
      fprintf(stderr, "Error creating simulation thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
  }

  pthread_t printer;
  if (pthread_create(&printer, NULL, print, NULL)) {
    fprintf(stderr, "Error creating printing thread.\n");
    exit(EXIT_FAILURE);
  }

  // Clean up.
  for (int i = 0; i < n_simulators; ++i) {
    if (pthread_join(simulators[i], NULL)) {
      fprintf(stderr, "Error joining simulation thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
  }

  if (pthread_join(printer, NULL)) {
    fprintf(stderr, "Error joining printing thread.\n");
    exit(EXIT_FAILURE);
  }

  exit(EXIT_SUCCESS);
}
