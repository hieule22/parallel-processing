# Compiler
CC = gcc
# Compiler standard and flags
CSTD = -std=c99
CFLAGS = -Wall -g
# Libraries to link
LINKOPTS = -lpthread

# Build target executable
TARGET = common_meeting_time
$(TARGET) : $(TARGET).c
	$(CC) $(CSTD) $(CFLAGS) -o $(TARGET) $(TARGET).c $(LINKOPTS)
