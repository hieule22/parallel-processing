#!/bin/bash
TARGET_PROG=common_meeting_time
TEST_PROG=common_meeting_time_tester
FILE_NAME=input.txt
TEST_COUNT=7

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