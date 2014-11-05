import java.util.*;

public class MipsInstruction {
	public static String op;
	public static String rd;
	public static String rs;
	public static String rt;
	public static String imm;
	public static String shamt;
	public static String c;

	public MipsInstruction() {
		op = rd = rs = rt = imm = shamt = c = null;
	}
	
	public static MipsInstruction mipsInstructionFactory(String instruction)  
		throws SyntaxException, MemoryException, NoSuchElementException, RegNotFoundException,
			NumberFormatException {

		MipsInstruction mipsInstruction;
		Scanner scanner;
		String operation;		
		
		mipsInstruction = new MipsInstruction();
		mipsInstruction.op = "empty";
		
		instruction = instruction.trim();
		
		instruction = Formatter.removeLabels(instruction);
	
		instruction = instruction.replace("(", " ");
		instruction = instruction.replace(")", " ");
	
		if (instruction.compareTo("") == 0)
			return mipsInstruction;
	
		scanner = new Scanner(instruction);

		operation = scanner.next();

		operation = operation.trim();
		operation = operation.toLowerCase();
		mipsInstruction.op = operation;

		if (operation.compareTo("and") == 0
			|| operation.compareTo("or") == 0
			|| operation.compareTo("add") == 0
			|| operation.compareTo("slt") == 0
			|| operation.compareTo("sub") == 0) {	
			
			mipsInstruction.rd = scanner.next();
			mipsInstruction.rs = scanner.next();
			mipsInstruction.rt = scanner.next();
		}
		else if (operation.compareTo("addi") == 0) {
	
			mipsInstruction.rt = scanner.next();
			mipsInstruction.rs = scanner.next();
			mipsInstruction.imm = scanner.next();
		}
		else if (operation.compareTo("sll") == 0) {
		
			mipsInstruction.rd = scanner.next();
			mipsInstruction.rt = scanner.next();
			mipsInstruction.shamt = scanner.next();
		}
		else if (operation.compareTo("beq") == 0
			|| operation.compareTo("bne") == 0) {
	
			mipsInstruction.rt = scanner.next();
			mipsInstruction.rs = scanner.next();
			mipsInstruction.c = scanner.next();
		}
		else if (operation.compareTo("lw") == 0
			|| operation.compareTo("sw") == 0) {
	
			mipsInstruction.rt = scanner.next();
			mipsInstruction.c = scanner.next();
			mipsInstruction.rs = scanner.next();
		}
		else if (operation.compareTo("j") == 0
		|| operation.compareTo("jal") == 0) {
			mipsInstruction.c = scanner.next();
		}
		else if (operation.compareTo("jr") == 0) {
			mipsInstruction.rs = scanner.next();
		}
		else
			throw new SyntaxException("Unknown isntruction: " + operation);
			
		return mipsInstruction;
	}
	
	public MipsInstruction makeCopy() {
		MipsInstruction toReturn;
		toReturn = new MipsInstruction();
		
		toReturn.op = this.op;
		toReturn.rd = this.rd;
		toReturn.rs = this.rs;
		toReturn.rt = this.rt;
		toReturn.imm = this.imm;
		toReturn.shamt = this.shamt;
		toReturn.c = this.c;
		
		return toReturn;
	}
	
	public String toString() {
		return this.op;
	}
}
