default: lab3.class Memory.class RegisterFile.class Label.class RegNotFoundException.class \
	MemoryException.class SyntaxException.class Formatter.class InstructionMemory.class
	
lab3: lab3.class Memory.class RegisterFile.class Label.class RegNotFoundException.class \
	MemoryException.class SyntaxException.class Formatter.class InstructionMemory.class
	
lab3.class: lab3.java
	javac lab3.java
	
Memory.class: Memory.java
	javac Memory.java
	
InstructionMemory.class: InstructionMemory.java
	javac InstructionMemory.java
	
RegisterFile.class: RegisterFile.java
	javac RegisterFile.java
	
Formatter.class: Formatter.java
	javac Formatter.java
	
Label.class: Label.java
	javac Label.java

MemoryException.class: MemoryException.java
	javac MemoryException.java

RegNotFoundException.class: RegNotFoundException.java
	javac RegNotFoundException.java

SyntaxException.class: SyntaxException.java
	javac SyntaxException.java

clean:
	rm *.class
