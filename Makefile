# Compiler
CC = gcc
# Compiler standard and flags
CFLAGS = -std=c99 -Wall -g
# Libraries to link
LIBS = -lpthread

# Build target executable
TARGET = common_meeting_time

$(TARGET) : $(TARGET).c
	$(CC) $(CFLAGS) -o $(TARGET) $(TARGET).c $(LIBS)

clean :
	$(RM) *.o *~ $(TARGET)
