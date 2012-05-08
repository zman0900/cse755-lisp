# Java compiler
JAVAC = javac

# Java archiver
JAR = jar

# Java compiler flags
JAVAFLAGS = -g

# Source directory
SOURCE = src

# Build directory
BUILD = build

# Output jar
OUT = lisp.jar

# Main class
MAIN = com.cse755.LispInterpreter
MAINFILE = com/cse755/LispInterpreter.java

# The first target is the one that is executed when you invoke
# "make". 

all : archive
	$(MAKE) clean-build

build :
	mkdir -p $(BUILD)
	$(JAVAC) $(JAVAFLAGS) -sourcepath $(SOURCE) -cp $(BUILD) -d $(BUILD) $(SOURCE)/$(MAINFILE)

archive : build
	$(JAR) cvfe $(OUT) $(MAIN) -C $(BUILD) .
	echo "java -jar "$(OUT) > Runfile
	chmod +x Runfile

clean : clean-build clean-jar
	
clean-build :
	rm -rf $(BUILD)

clean-jar :
	rm -f $(OUT)
	rm -r Runfile

run :
	java -jar $(OUT)
