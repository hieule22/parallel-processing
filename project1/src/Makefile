# Compiler
CC = gcc
# Compiler options
CFLAGS = -std=c11 -Wall
# Libraries to link
LIBS = -lpthread

# Build target executable
TARGET = common_meeting_time

$(TARGET) : $(TARGET).c
	$(CC) $(CFLAGS) -o $(TARGET) $(TARGET).c $(LIBS)

clean :
	$(RM) *.o *~ $(TARGET)
