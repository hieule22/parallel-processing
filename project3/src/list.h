/**
 * CS 435 - Project 4: A Concurrent Computation of PI.
 * Implemenation of a list of pairs of integer and double.
 * @author Hieu Le
 * @version December 7th, 2016
 */

#include <stddef.h>
#include <stdlib.h>

typedef struct {
  // The integer component.
  size_t first;
  // The double component.
  double second;
} entry_t;

typedef struct {
  // The content of this list.
  entry_t *entries;
  // The size of this list.
  size_t size;
} list_t;

/**
 * Initializes a list with specified capacity.
 * @param list a pointer to the list to be initialized
 * @param capacity the maximum number of values stored in the list
 */
void list_init(list_t *list, const size_t capacity) {
  list->entries = (entry_t *) malloc(capacity * sizeof(entry_t));
  list->size = 0;
}

/**
 * Destroys a list.
 * @param list a pointer to the list to be destroyed
 */
void list_destroy(list_t *list) {
  free(list->entries);
}

/**
 * Adds an entry to the end of this list.
 * @param list a pointer to the list to be appended
 * @param entry the entry to be added
 */
void list_add(list_t *list, const entry_t *entry) {
  int index = list->size;
  list->entries[index].first = entry->first;
  list->entries[index].second = entry->second;
  ++list->size;
}

/**
 * Returns the entry at position index of the list.
 * @param list a pointer to the list to be queried
 * @param index the position of the entry to retrieve
 * @return a pointer to the target entry
 */
const entry_t *list_get(const list_t *list, const size_t index) {
  return &list->entries[index];
}

/**
 * Returns the size of a list.
 * @param list a pointer to the list
 * @return the size of the list
 */
size_t list_size(const list_t *list) {
  return list->size;
}

