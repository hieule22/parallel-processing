#!/bin/bash
FILE_NAME=input.txt
TEST_PROG=common_meeting_time_tester
TARGET_PROG=common_meeting_time

g++ -std=c++11 -o $TEST_PROG $TEST_PROG.cpp

for i in `seq 0 7`;
do
    ./$TEST_PROG w $i $FILE_NAME && ./$TARGET_PROG $FILE_NAME | ./$TEST_PROG t $i
done

# Clean up
rm $FILE_NAME
rm $TEST_PROG
