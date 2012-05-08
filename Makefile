# Java compiler
JAVAC = javac

# Java compiler flags
JAVAFLAGS = -g

# Source directory
SOURCE = src

# Output directory
OUT = bin

# Main class
MAIN = 

# Creating a .class file
COMPILE = $(JAVAC) $(JAVAFLAGS) -sourcepath $(SOURCE) -classpath $(OUT) -d $(OUT)

# The first target is the one that is executed when you invoke
# "make". 

all:
	mkdir $(OUT)
	$(COMPILE) $(SOURCE)/com/cse755/LispInterpreter.java
	
clean:
	rm -rvf $(OUT)/*

run:
	java -classpath $(OUT) com.cse755.LispInterpreter
