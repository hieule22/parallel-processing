# Compiler
CC = gcc
# Compiler options
CFLAGS = -std=c11 -Wall -Wextra --pedantic
# Linking libraries
LIBS = -lpthread -lm

# Executable name
TARGET = pi_approximation

$(TARGET) : pi_approximation.c
	$(CC) $(CFLAGS) -o $(TARGET) pi_approximation.c list.h $(LIBS)

clean:
	$(RM) *.o *~ $(TARGET)
