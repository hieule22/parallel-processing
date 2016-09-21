#!/bin/bash
# Script to execute test suites for Common Meeting Time program.

# First argument is executable binary to test.
TARGET_PROG=$1
# Number of tests to run.
TEST_COUNT=8

TEST_PROG=common_meeting_time_tester
FILE_NAME=input.txt

# Compile test program.
g++ -std=c++11 -o $TEST_PROG $TEST_PROG.cpp

# Execute test program.
for i in `seq 0 $TEST_COUNT`;
do
    ./$TEST_PROG w $i $FILE_NAME && ./$TARGET_PROG $FILE_NAME | ./$TEST_PROG t $i
done

# Clean up.
rm $FILE_NAME
rm $TEST_PROG
