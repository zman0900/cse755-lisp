# Java programs
JAVAC = javac
JAR = jar
JAVADOC = javadoc
JAVA = java

# Java compiler flags
JAVAFLAGS = -g

# Source directory
SOURCE = src

# Build directory
BUILD = build

# Output jar
OUT = lisp.jar

# Documentation output
DOC = doc

# Main class
MAINCLASS = LispInterpreter
MAINPACKAGE = com.cse755
MAINFILE = com/cse755/LispInterpreter.java

# The first target is the one that is executed when you invoke
# "make". 

all : archive
	$(MAKE) clean-build

build :
	mkdir -p $(BUILD)
	$(JAVAC) $(JAVAFLAGS) -sourcepath $(SOURCE) -cp $(BUILD) -d $(BUILD) $(SOURCE)/$(MAINFILE)

archive : build
	$(JAR) cvfe $(OUT) $(MAINPACKAGE).$(MAINCLASS) -C $(BUILD) .
	echo "java -jar "$(OUT) > Runfile
	chmod +x Runfile

doc : clean-doc
	$(JAVADOC) -sourcepath $(SOURCE) -d $(DOC) $(MAINPACKAGE)

clean : clean-build clean-jar
	
clean-build :
	rm -rf $(BUILD)

clean-jar :
	rm -f $(OUT)
	rm -f Runfile

clean-doc :
	rm -rf $(DOC)

run :
	$(JAVA) -jar $(OUT)
