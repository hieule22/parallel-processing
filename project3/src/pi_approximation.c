/**
 * CS 435 - Project 4: A Concurrent Computation of PI.
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
 * Struct storing the information about a simulation.
 */
typedef struct {
  // The number of points inside the unit circle.
  int num_interior;
  // The total number of points examined so far.
  int num_total;
  // The interval between printing of approximation.
  int interval;
  // The maximum number of experiments to be performed.
  int maximum;
  // A flag indicating if the simulation has completed.
  bool complete;
} simulation_t;

/**
 * Initializes a simulation with a given interval and maximum number of
 * experiments.
 * @param sim a pointer to the simulation to be initialized
 * @param interval the number of experiments between printing of approximations
 * @param maximum the maximum number of experiments to be performed
 */
void simulation_init(simulation_t *sim, const int interval, const int maximum) {
  sim->num_interior = sim->num_total = 0;
  sim->interval = interval;
  sim->maximum = maximum;
  sim->complete = false;
}

// Main simulation and its guarding mutex.
simulation_t sim;
pthread_mutex_t s_lock;
// Result list and its guarding mutex.
list_t list;
pthread_mutex_t l_lock;
// Mutex guarding the random number generator.
pthread_mutex_t r_lock;
// Conditional variable.
pthread_cond_t list_has_new_elements;

void *experiment(void *arg) {
  while (!sim.complete) {
    // Generate a random data point and checks if it is inside the unit circle.
    pthread_mutex_lock(&r_lock);
    int x = rand();
    int y = rand();
    pthread_mutex_unlock(&r_lock);

    double dx = ((double) x) / RAND_MAX - RADIUS;
    double dy = ((double) y) / RAND_MAX - RADIUS;
    double dist = dx * dx + dy * dy;

    pthread_mutex_lock(&s_lock);
    ++sim.num_total;
    sim.num_interior += (dist <= RADIUS * RADIUS) ? 1 : 0;
    if (sim.num_total % sim.interval == 0 || sim.num_total == sim.maximum) {
      // Compute the new approximation and add it to the queue.
      double approx = sim.num_interior * 4.0 / sim.num_total;
      pthread_mutex_lock(&l_lock);
      entry_t entry;
      entry.first = sim.num_total;
      entry.second = approx;
      list_add(&list, &entry);
      pthread_cond_broadcast(&list_has_new_elements);
      pthread_mutex_unlock(&l_lock);

      if (sim.num_total == sim.maximum) {
        sim.complete = true;
      }
    }
    pthread_mutex_unlock(&s_lock);
  }

  pthread_exit(NULL);
}

void *print(void *arg) {
  int iterations = *(int *) arg;
  free(arg);
  size_t index = 0;
  while (index < iterations) {
    pthread_mutex_lock(&l_lock);
    while (index == list_size(&list)) {
      pthread_cond_wait(&list_has_new_elements, &l_lock);
    }

    const entry_t *entry = list_get(&list, index);
    ++index;
    fprintf(stderr, "%d: %.9f\n", entry->first, entry->second);
    pthread_mutex_unlock(&l_lock);
  }

  pthread_exit(NULL);
}

int main(int argc, char *argv[]) {
  if (argc != 4) {
    fprintf(stderr, "Usage: %s <number of simulation threads> "
            "<number of experiments> <interval between printing>.\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  int n_simulators = atoi(argv[1]);
  int maximum = atoi(argv[2]);
  int interval = atoi(argv[3]);
  int n_results = ceil(maximum * 1.0 / interval);
  simulation_init(&sim, interval, maximum);
  if (pthread_mutex_init(&s_lock, NULL)) {
    fprintf(stderr, "Error initializing s_lock.\n");
    exit(EXIT_FAILURE);
  }

  list_init(&list, n_results);
  if (pthread_mutex_init(&l_lock, NULL)) {
    fprintf(stderr, "Error initializing l_lock.\n");
    exit(EXIT_FAILURE);
  }

  srand(time(NULL));
  if (pthread_mutex_init(&r_lock, NULL)) {
    fprintf(stderr, "Error initializing r_lock.\n");
    exit(EXIT_FAILURE);
  }

  if (pthread_cond_init(&list_has_new_elements, NULL)) {
    fprintf(stderr, "Error initializing condition variable.\n");
    exit(EXIT_FAILURE);
  }

  // Create the simulation threads.
  pthread_t simulators[n_simulators];
  for (int i = 0; i < n_simulators; ++i) {
    if (pthread_create(&simulators[i], NULL, experiment, NULL)) {
      fprintf(stderr, "Error creating simulation thread %d.\n", i);
      exit(EXIT_FAILURE);
    }
  }

  // Create the printing thread.
  pthread_t printer;
  int *iterations = malloc(sizeof(int));
  *iterations = n_results;
  if (pthread_create(&printer, NULL, print, (void *) iterations)) {
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
    fprintf(stderr, "Error joining printer thread.\n");
    exit(EXIT_FAILURE);
  }

  exit(EXIT_SUCCESS);
}
