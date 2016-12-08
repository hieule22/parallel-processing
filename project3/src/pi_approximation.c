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
  // A flag indicating if the simulation has completed.
  bool is_done;
} simulation_t;

/**
 * Initializes a simulation with a specified maximum number of experiments
 * and interval between printing of a new approximation.
 * @param sim a pointer to the simulation to be initialized
 * @param maximum the maximum number of experiments to be performed
 * @param interval the number of experiments between printing of approximations
 */
void simulation_init(simulation_t *sim, const int maximum, const int interval) {
  sim->n_interior = sim->n_total = 0;
  sim->maximum = maximum;
  sim->interval = interval;
  sim->is_done = false;
}

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
  // Checking 'is_done' here is an atomic action since the only update to the
  // flag is done when the simulation lock is held.
  while (!simulation.is_done) {
    // Generate a random data point.
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
    ++simulation.n_total;
    simulation.n_interior += (squared_dist <= RADIUS * RADIUS) ? 1 : 0;
    if (simulation.n_total % simulation.interval == 0
        || simulation.n_total == simulation.maximum) {
      // Compute the new approximation and add it to the list.
      double estimate = simulation.n_interior * 4.0 / simulation.n_total;
      pair_t entry = {simulation.n_total, estimate};

      // Changes to the approximation list must be atomic.
      pthread_mutex_lock(&list_lock);
      list_add(&list, &entry);
      // Wake up the printing thread upon a new addition to the list.
      pthread_cond_broadcast(&list_has_new_elements);
      pthread_mutex_unlock(&list_lock);

      // Terminate the simulation after maximum number of experiments is met.
      if (simulation.n_total == simulation.maximum) {
        simulation.is_done = true;
      }
    }
    pthread_mutex_unlock(&simulation_lock);
  }

  pthread_exit(NULL);
}

/**
 * Actions to be performed by the printing thread.
 */
void *print(void *arg) {
  // The index in the list of the next approximation to print.
  size_t index = 0;
  while (index < list.capacity) {
    pthread_mutex_lock(&list_lock);
    while (index >= list_size(&list)) {
      // Wait for new approximations to be added to the list.
      pthread_cond_wait(&list_has_new_elements, &list_lock);
    }
    const pair_t *estimate = list_get(&list, index);
    pthread_mutex_unlock(&list_lock);
    // Print the approximations.
    fprintf(stdout, "%zd: %.9f\n", estimate->first, estimate->second);
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

  int n_simulators = atoi(argv[1]);
  int maximum = atoi(argv[2]);
  int interval = atoi(argv[3]);
  // The total number of approximations can be shown to be equal to
  // ceiling(maximum / interval).
  int n_approximations = ceil(((double) maximum) * 1.0 / interval);

  simulation_init(&simulation, maximum, interval);
  if (pthread_mutex_init(&simulation_lock, NULL)) {
    fprintf(stderr, "Error initializing simulation_lock.\n");
    exit(EXIT_FAILURE);
  }

  // The capacity of the list equals the total number of approximations.
  list_init(&list, n_approximations);
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

  // Create the simulation threads.
  pthread_t simulators[n_simulators];
  for (int i = 0; i < n_simulators; ++i) {
    if (pthread_create(&simulators[i], NULL, simulate, NULL)) {
      fprintf(stderr, "Error creating simulation thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
  }

  // Create the printing thread.
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
