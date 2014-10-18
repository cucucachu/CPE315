default: Lab2.class Assembler.class Registers.class RegNotFoundException.class \
	Instructions.class BadInstructionException.class SyntaxException.class Formatter.class \
	Label.class

Lab2.class: Lab2.java
	javac Lab2.java
	
Assembler.class: Assembler.java
	javac Assembler.java
	
Registers.class: Registers.java
	javac Registers.java
	
Formatter.class: Formatter.java
	javac Formatter.java
	
Instructions.class: Instructions.java
	javac Instructions.java
	
Label.class: Label.java
	javac Label.java

RegNotFoundException.class: RegNotFoundException.java
	javac RegNotFoundException.java

BadInstructionException.class: BadInstructionException.java
	javac BadInstructionException.java

SyntaxException.class: SyntaxException.java
	javac SyntaxException.java

clean:
	rm *.class
