import java.util.*;

public class MipsCPU {

	public static final int MEM_SIZE = 8192;
	
	private Memory memory = new Memory(MEM_SIZE);
	private RegisterFile registerFile = new RegisterFile();
	private InstructionMemory instructionMemory;
	private ArrayList<Label> labels;
	
	
	private int programCounter = 0;
	
	public MipsCPU(String assemblyCode, ArrayList<Label> labels) {
		memory = new Memory(MEM_SIZE);
		registerFile = new RegisterFile();
		instructionMemory = new InstructionMemory(assemblyCode);
		this.labels = labels;
		reset();
	}
	
	public void reset() {
		memory.clear();
		registerFile.clear();
		programCounter = 0;	
	}
	
	public String instructionMemoryToSting() {
		return instructionMemory.toString();
	}
	
	public String getNextInstruction() {
		try {
			return instructionMemory.get(programCounter);
		}
		catch (MemoryException ex) {
			return "No more instructions.";
		}
	}
	
	public String getMemoryString(int from, int to) {
		return memory.toString(from, to);
	}
	
	public String getRegisterFileString() {
		return registerFile.toString();
	}
	
	public int getProgramCounter() {
		return programCounter;
	}
	
	public boolean execute() 
		throws SyntaxException, MemoryException, NoSuchElementException, RegNotFoundException,
			NumberFormatException {
		String instruction;
		Scanner scanner;
		String operation;
		int labelIndex;
		
		
		if (programCounter >= instructionMemory.size()) 
			return false;
			
		instruction = instructionMemory.get(programCounter);
		instruction = instruction.trim();
		
		instruction = Formatter.removeLabels(instruction);
	
		instruction = instruction.replace("(", " ");
		instruction = instruction.replace(")", " ");
	
		if (instruction.compareTo("") == 0)
			return false;
	
		scanner = new Scanner(instruction);

		operation = scanner.next();

		operation = operation.trim();
		operation = operation.toLowerCase();

		if (operation.compareTo("and") == 0) {
			String rd, rs, rt;
	
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
	
			registerFile.set(rd, registerFile.get(rs) & registerFile.get(rt));
		}
		else if (operation.compareTo("or") == 0) {
			String rd, rs, rt;
	
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
	
			registerFile.set(rd, registerFile.get(rs) | registerFile.get(rt));
		}
		else if (operation.compareTo("add") == 0) {
			String rd, rs, rt;
	
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
	
			registerFile.set(rd, registerFile.get(rs) + registerFile.get(rt));
		}
		else if (operation.compareTo("addi") == 0) {
			String rt, rs, imm;
	
			rt = scanner.next();
			rs = scanner.next();
			imm = scanner.next();
	
			registerFile.set(rt, registerFile.get(rs) + new Integer(imm));
		}
		else if (operation.compareTo("sll") == 0) {
			String rd, rt, shamt;
			Integer shiftAmount;
	
			rd = scanner.next();
			rt = scanner.next();
			shamt = scanner.next();
	
			shiftAmount = new Integer(shamt);
	
			if (shiftAmount < 0 || shiftAmount > 31)
				throw new SyntaxException("Ilegal shift amount: " + shiftAmount);
	
			registerFile.set(rd, registerFile.get(rt) << shiftAmount);
		}
		else if (operation.compareTo("sub") == 0) {
			String rd, rs, rt;
	
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
	
			registerFile.set(rd, registerFile.get(rs) - registerFile.get(rt));
		}
		else if (operation.compareTo("slt") == 0) {
			String rd, rs, rt;
	
			rd = scanner.next();
			rs = scanner.next();
			rt = scanner.next();
	
			if (registerFile.get(rs) < registerFile.get(rt))
				registerFile.set(rd, 1);
			else
				registerFile.set(rd, 0);
		}
		else if (operation.compareTo("beq") == 0) {
			String rt, rs, C;
			int index, address;
	
			rt = scanner.next();
			rs = scanner.next();
			C = scanner.next();
	
			index = labels.indexOf(new Label(C.trim(), 0));
	
			if (index == -1)
				address = new Integer(C);
			else
				address = labels.get(index).getRelativeLineNumber(programCounter);
	
			address += programCounter;
	
			if (address >= 0 && address < instructionMemory.size()) {
				if (registerFile.get(rs) == registerFile.get(rt))
					programCounter = address;	
			}
			else
				throw new SyntaxException("Branch to address outside of program: " + address);
		}
		else if (operation.compareTo("bne") == 0) {
			String rt, rs, C;
			int index, address;
	
			rt = scanner.next();
			rs = scanner.next();
			C = scanner.next();
	
			index = labels.indexOf(new Label(C.trim(), 0));
	
			if (index == -1)
				address = new Integer(C);
			else
				address = labels.get(index).getRelativeLineNumber(programCounter);
	
			address += programCounter;
	
			if (address >= 0 && address < instructionMemory.size()) {
				if (registerFile.get(rs) != registerFile.get(rt))
					programCounter = address;	
			}
			else
				throw new SyntaxException("Branch to address outside of program: " + address);			
		}
		else if (operation.compareTo("lw") == 0) {
			String rt, C, rs;
			int address;
	
			rt = scanner.next();
			C = scanner.next();
			rs = scanner.next();
	
			address = registerFile.get(rs) + new Integer(C);
	
			registerFile.set(rt, memory.get(address));
		}
		else if (operation.compareTo("sw") == 0) {
			String rt, C, rs;
			int address;
	
			rt = scanner.next();
			C = scanner.next();
			rs = scanner.next();
	
			address = registerFile.get(rs) + new Integer(C);
	
			memory.set(address, registerFile.get(rt));
		}
		else if (operation.compareTo("j") == 0) {
			String C;
			int index, address;
			C = scanner.next();
	
			index = labels.indexOf(new Label(C.trim(), 0));
	
			if (index == -1)
				address = new Integer(C);
			else
				address = labels.get(index).getAbsoluteLineNumber();
	
			if (address >= 0 && address < instructionMemory.size()) {
				programCounter = address - 1;	
			}
			else
				throw new SyntaxException("Jump to address outside of program: " + address);
	
		}
		else if (operation.compareTo("jr") == 0) {
			String rs;
			int address;
			rs = scanner.next();
	
			address = registerFile.get(rs);
	
			if (address >= 0 && address < instructionMemory.size()) {
				programCounter = address - 1;	
			}
			else
				throw new SyntaxException("Jump to address outside of program: " + address);
		}
		else if (operation.compareTo("jal") == 0) {
			String C;
			int index, address;
			C = scanner.next();
	
			index = labels.indexOf(new Label(C.trim(), 0));
	
			if (index == -1)
				address = new Integer(C);
			else
				address = labels.get(index).getAbsoluteLineNumber();
	
			if (address >= 0 && address < instructionMemory.size()) {
				registerFile.set("$ra", programCounter+1);
				programCounter = address - 1;	
			}
			else
				throw new SyntaxException("Jump to address outside of program: " + address);
		}
		else
			throw new SyntaxException("Unknown isntruction: " + operation);
	
		programCounter++;
	
		return true;
	}
}
