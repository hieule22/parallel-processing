/**
 * CS 435 - Project 4: A Concurrent Computation of PI.
 * Implementation of a FIFO queue of double values.
 * @author Hieu Le
 * @version December 7th, 2016
 */

#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
  // Array of double values storing the queue's content.
  double *data;
  // Index of the queue's head.
  int head;
  // Index of the queue's tail.
  int tail;
} queue_t;

/**
 * Initializes a queue with specified capacity.
 * @param queue a pointer to the queue to be initialized
 * @param capacity the maximum number of values stored in the queue.
 */
void queue_init(queue_t *queue, const int capacity) {
  queue->data = (double *) malloc(capacity * sizeof(double));
  queue->head = 0;
  queue->tail = -1;
}

/**
 * Destroys a specified queue.
 * @param queue a pointer to the queue to be destroyed
 */
void queue_destroy(queue_t *queue) {
  free(queue->data);
}

/**
 * Checks if a specified queue is empty.
 * @param queue a pointer to the queue to be examined
 * @return true if the queue is empty; false otherwise
 */
bool queue_empty(queue_t *queue) {
  return queue->head > queue->tail;
}

/**
 * Adds a value to the end of a queue.
 * @param queue a pointer to the queue to be updated
 * @param value the value to be added
 */
void queue_add(queue_t *queue, const double value) {
  queue->data[++queue->tail] = value;
}

/**
 * Removes and returns the element at the head of the queue.
 * @param queue a pointer to the queue to be polled
 * @return the element at the head of the queue
 */
double queue_remove(queue_t *queue) {
  return queue->data[queue->head++];
}
