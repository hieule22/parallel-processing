CC = gcc
CSTD = -std=c99
CFLAGS = -Wall -g
LINKOPTS = -lpthread

common_meeting_time : common_meeting_time.c
	$(CC) $(CSTD) $(CFLAGS) common_meeting_time.c $(LINKOPTS)