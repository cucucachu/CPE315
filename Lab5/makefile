default: lab5.class Memory.class RegisterFile.class Label.class RegNotFoundException.class \
	MemoryException.class SyntaxException.class Formatter.class InstructionMemory.class \
	MipsCPU.class BranchPredictor.class
	
lab5: lab5.class Memory.class RegisterFile.class Label.class RegNotFoundException.class \
	MemoryException.class SyntaxException.class Formatter.class InstructionMemory.class \
	MipsCPU.class BranchPredictor.class
	
lab5.class: lab5.java
	javac lab5.java
	
BranchPredictor.class: BranchPredictor.java
	javac BranchPredictor.java
	
MipsCPU.class: MipsCPU.java
	javac MipsCPU.java
	
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
