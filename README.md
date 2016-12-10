# Parallel & Distributed Processing

## About

This repository contains a collection of concurrent programs written using the
POSIX Thread and Java Thread Libraries. The synchronization techniques used here
include mutual exclusion, conditional synchronization and semaphores.

## Content

* [Common Meeting Time]
(https://github.com/hieule22/parallel-processing/tree/master/project1_common_meeting_time):
The *common meeting time* problem involves finding a list of times when a group
of people are all available to meet. Here we developed a multi-threaded solution
to the common meeting time problem for three people.

* [Gap String Matching]
(https://github.com/hieule22/parallel-processing/tree/master/project2_gap_string_matching):
A *gap string* is a set of strings that all have a specified non-overlapping
prefix and suffix. Any character may appear between the prefix and the suffix.
A *limited gap string* (LGS) is a gap string where only a specified number of
characters may appear between the prefix and suffix. Here we developed a
multi-threaded matcher for any LGS specified by the notation
`prefix [min, max] suffix` where `min` and `max` indicate the minimum and
maximum number of intervening characters.

* [_&pi;_ Approximation]
(https://github.com/hieule22/parallel-processing/tree/master/project3_pi_approximation):
Here we developed a multi-threaded program to perform the *Monte Carlo* simulation
method for approximating the value of _&pi;_.
