/**
 * CS 435 - Project 4: A Concurrent Computation of PI.
 * Implemenation of a list of pairs of unsigned integers.
 * @author Hieu Le
 * @version December 7th, 2016
 */

#ifndef PROJECT4_SRC_LIST_H_
#define PROJECT4_SRC_LIST_H_

#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

// A struct representing a pair of unsigned integers.
typedef struct {
  uint32_t first;
  uint32_t second;
} pair_t;

// A list of unsigned integer pairs with predefined capacity.
typedef struct {
  // The content of this list.
  pair_t *data;
  // The capacity of this list.
  size_t capacity;
  // The current size of this list.
  size_t size;
} list_t;

/**
 * Initializes a list with a specified capacity.
 * @param list a pointer to the list to be initialized
 * @param capacity the maximum number of values that the list can hold
 */
void list_init(list_t *list, const size_t capacity) {
  list->data = (pair_t *) malloc(capacity * sizeof(pair_t));
  list->capacity = capacity;
  list->size = 0;
}

/**
 * Destroys a list.
 * @param list a pointer to the list to be destroyed
 */
void list_destroy(list_t *list) {
  free(list->data);
}

/**
 * Adds an element to the end of a list.
 * @param list a pointer to the list to be appended
 * @param element the pair to be added to the list
 */
void list_add(list_t *list, const pair_t *element) {
  int index = list->size;
  list->data[index].first = element->first;
  list->data[index].second = element->second;
  ++list->size;
}

/**
 * Returns the element at position index of a list.
 * @param list a pointer to the list to be queried
 * @param index the position of the element to retrieve
 * @return an immutable pointer to the target element
 */
const pair_t *list_get(const list_t *list, const size_t index) {
  return &list->data[index];
}

/**
 * Returns the current size of a list.
 * @param list a pointer to the list whose size is to be queried
 * @return the current number of elements in the list
 */
size_t list_size(const list_t *list) {
  return list->size;
}

/**
 * Returns the capacity of a list.
 * @param list a pointer to the list whose capacity is to be queried
 * @return the maximum number of elements that the list can hold
 */
size_t list_capacity(const list_t *list) {
  return list->capacity;
}

#endif  // PROJECT4_SRC_LIST_H_
