import java.util.*;

public class MipsInstruction {
	public static final int RTYPE = 0;
	public static final int ITYPE = 1;
	public static final int JTYPE = 2;
	public static final int NOTYPE = 3;
	
	public String op;
	public String rd;
	public String rs;
	public String rt;
	public String imm;
	public String shamt;
	public String c;
	public int numStall;
	public int numSquash;

	public MipsInstruction() {
		op = rd = rs = rt = imm = shamt = c = null;
		numStall = numSquash = 0;
	}

	public MipsInstruction(boolean custom, String op) {
		this.op = op.trim().toLowerCase();
		rd = rs = rt = imm = shamt = c = null;
	}

	public MipsInstruction(String op, int numStall, int numSquash) {
		this.op = op.trim().toLowerCase();
		rd = rs = rt = imm = shamt = c = null;
		this.numStall = numStall;
		this.numSquash = numSquash;
	}
	
	
	public MipsInstruction(String instruction) throws SyntaxException, 
		MemoryException, NoSuchElementException, RegNotFoundException,
			NumberFormatException {
		Scanner scanner;
		String operation;		
		
		op = rd = rs = rt = imm = shamt = c = null;
		numStall = numSquash = 0;
		
		op = "empty";
		
		instruction = instruction.trim();
		
		instruction = Formatter.removeLabels(instruction);
	
		instruction = instruction.replace("(", " ");
		instruction = instruction.replace(")", " ");
		instruction = instruction.replace(",", " ");
	
		if (instruction.compareTo("") == 0 || instruction.compareTo("empty") == 0)
			return;
	
		scanner = new Scanner(instruction);

		operation = scanner.next();

		operation = operation.trim();
		operation = operation.toLowerCase();
		op = operation;

		if (operation.compareTo("and") == 0
			|| operation.compareTo("or") == 0
			|| operation.compareTo("add") == 0
			|| operation.compareTo("slt") == 0
			|| operation.compareTo("sub") == 0) {	
			
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
		}
		else if (operation.compareTo("addi") == 0) {
	
			rt = scanner.next();
			rs = scanner.next();
			imm = scanner.next();
		}
		else if (operation.compareTo("sll") == 0) {
		
			rd = scanner.next();
			rt = scanner.next();
			shamt = scanner.next();
		}
		else if (operation.compareTo("beq") == 0
			|| operation.compareTo("bne") == 0) {
	
			rt = scanner.next();
			rs = scanner.next();
			c = scanner.next();
		}
		else if (operation.compareTo("lw") == 0
			|| operation.compareTo("sw") == 0) {
	
			rt = scanner.next();
			c = scanner.next();
			rs = scanner.next();
		}
		else if (operation.compareTo("j") == 0
		|| operation.compareTo("jal") == 0) {
			c = scanner.next();
		}
		else if (operation.compareTo("jr") == 0) {
			rs = scanner.next();
		}
		else
			throw new SyntaxException("Unknown isntruction: " + operation);
			
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
	
	public int getType() throws SyntaxException {
		if (op == null)
			throw new SyntaxException("No type for null instruction.");
		
		if (op.compareTo("and") == 0)
			return RTYPE;
		else if (op.compareTo("or") == 0)
			return RTYPE;
		else if (op.compareTo("add") == 0)
			return RTYPE;
		else if (op.compareTo("addi") == 0)
			return ITYPE;
		else if (op.compareTo("sll") == 0)
			return ITYPE;
		else if (op.compareTo("sub") == 0)
			return RTYPE;
		else if (op.compareTo("slt") == 0)
			return RTYPE;
		else if (op.compareTo("beq") == 0)
			return ITYPE;
		else if (op.compareTo("bne") == 0)
			return ITYPE;
		else if (op.compareTo("sw") == 0)
			return ITYPE;
		else if (op.compareTo("lw") == 0)
			return ITYPE;
		else if (op.compareTo("j") == 0)
			return JTYPE;
		else if (op.compareTo("jr") == 0)
			return RTYPE;
		else if (op.compareTo("jal") == 0)
			return JTYPE;
		else if (op.compareTo("empty") == 0)
			return NOTYPE;
		else if (op.compareTo("stall") == 0)
			return NOTYPE;
		else if (op.compareTo("squash") == 0)
			return NOTYPE;
		else
			throw new SyntaxException("Could not find type for " + op + ".");
	}
	
	public boolean isShift() {
		if (op.compareTo("sll") == 0)
			return true;
		
		return false;
	}
	
	public boolean isBranch() {
		
		if (op.compareTo("beq") == 0)
			return true;
		else if (op.compareTo("bne") == 0)
			return true;
		return false;
	}
	
	public  boolean isMemAccess() {
		
		if (op.compareTo("sw") == 0)
			return true;
		else if (op.compareTo("lw") == 0)
			return true;
		return false;
	}
	
	public String toString() {
		return op;
	}
}
