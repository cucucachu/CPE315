import java.util.*;

public class MipsCPU {

	public static final int MEM_SIZE = 8192;
	
	private Memory memory = new Memory(MEM_SIZE);
	private RegisterFile registerFile = new RegisterFile();
	private InstructionMemory instructionMemory;
	private Simulator sim;
	private ArrayList<Label> labels;
	private boolean taken;
	
	public static int instructionsExecuted = 0;
	
	private int programCounter = 0;
	
	public MipsCPU(String assemblyCode, ArrayList<Label> labels) {
		memory = new Memory(MEM_SIZE);
		registerFile = new RegisterFile();
		instructionMemory = new InstructionMemory(assemblyCode);
		this.labels = labels;
		sim = new Simulator(instructionMemory);
		reset();
	}
	
	public void reset() {
		memory.clear();
		registerFile.clear();
		sim.reset();
		programCounter = 0;
		instructionsExecuted = 0;
		taken = false;
	}
	
	public boolean execute() 
		throws SyntaxException, MemoryException, NoSuchElementException, RegNotFoundException,
			NumberFormatException {
		
		sim.setPC(programCounter++);
		
		if (checkForEnd())
			return false;
		
		if (taken) {
			sim.squash();
			taken = false;
		}
		
		EX();
		ID();
		IF();
		
		sim.updateQueue();
		
		return true;
	}
	
	private void IF() throws SyntaxException, MemoryException, NoSuchElementException,
		RegNotFoundException, NumberFormatException {
		MipsInstruction mips;
		
		mips = sim.getIF();
		
		if (mips.isJump()) {
			if (mips.op.compareTo("j") == 0) {
				int index, address;
	
				index = labels.indexOf(new Label(mips.c.trim(), 0));
	
				if (index == -1)
					address = new Integer(mips.c);
				else
					address = labels.get(index).getAbsoluteLineNumber();
	
				if (address >= 0 && address < instructionMemory.size()) {
					programCounter = address;	
					System.out.println("Jumping to " + programCounter);
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
	
			}
			else if (mips.op.compareTo("jr") == 0) {
				int address;
	
				address = registerFile.get(mips.rs);
	
				if (address >= 0 && address < instructionMemory.size()) {
					programCounter = address;
					System.out.println("JR to " + address);
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
			}
			else if (mips.op.compareTo("jal") == 0) {
				int index, address;
	
				index = labels.indexOf(new Label(mips.c.trim(), 0));
	
				if (index == -1)
					address = new Integer(mips.c);
				else
					address = labels.get(index).getAbsoluteLineNumber();
	
				if (address >= 0 && address < instructionMemory.size()) {
					registerFile.set("$ra", sim.getIF().pc + 1);
					programCounter = address;	
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
			} 
		}
	}
	
	private void ID() throws SyntaxException, MemoryException, NoSuchElementException,
		RegNotFoundException, NumberFormatException {
		MipsInstruction mips;
		
		mips = sim.getID();	
		
		if (mips.op.compareTo("beq") == 0) {
			int index, address;

			index = labels.indexOf(new Label(mips.c.trim(), 0));

			if (index == -1)
				address = new Integer(mips.c);
			else
				address = labels.get(index).getRelativeLineNumber(programCounter);

			address += programCounter + 1;

			if (address >= 0 && address < instructionMemory.size()) {
				if (registerFile.get(mips.rs) == registerFile.get(mips.rt)) {
					programCounter = address;
					System.out.println("Branching to " + address);
					taken = true;
				}
			}
			else
				throw new SyntaxException("Branch to address outside of program: " + address);
		}
		else if (mips.op.compareTo("bne") == 0) {
			int index, address;

			index = labels.indexOf(new Label(mips.c.trim(), 0));

			if (index == -1)
				address = new Integer(mips.c);
			else
				address = labels.get(index).getRelativeLineNumber(programCounter);

			address += programCounter + 1;

			if (address >= 0 && address < instructionMemory.size()) {
				if (registerFile.get(mips.rs) != registerFile.get(mips.rt)) {
					programCounter = address;
					taken = true;
				}
			}
			else
				throw new SyntaxException("Branch to address outside of program: " + address);	
		}
	}
	
	private void EX() throws SyntaxException, MemoryException, NoSuchElementException,
		RegNotFoundException, NumberFormatException {
		MipsInstruction mips;
		mips = sim.getEX();
			
		if (!mips.doNothing()) {

			if (mips.op.compareTo("and") == 0) {	
				registerFile.set(mips.rd, registerFile.get(mips.rs) & registerFile.get(mips.rt));
			}
			else if (mips.op.compareTo("or") == 0) {
				registerFile.set(mips.rd, registerFile.get(mips.rs) | registerFile.get(mips.rt));
			}
			else if (mips.op.compareTo("add") == 0) {
				registerFile.set(mips.rd, registerFile.get(mips.rs) + registerFile.get(mips.rt));
			}
			else if (mips.op.compareTo("addi") == 0) {
				registerFile.set(mips.rt, registerFile.get(mips.rs) + new Integer(mips.imm));
			}
			else if (mips.op.compareTo("sll") == 0) {
				Integer shiftAmount;
				shiftAmount = new Integer(mips.shamt);
	
				if (shiftAmount < 0 || shiftAmount > 31)
					throw new SyntaxException("Ilegal shift amount: " + shiftAmount);
	
				registerFile.set(mips.rd, registerFile.get(mips.rt) << shiftAmount);
			}
			else if (mips.op.compareTo("sub") == 0) {
				registerFile.set(mips.rd, registerFile.get(mips.rs) - registerFile.get(mips.rt));
			}
			else if (mips.op.compareTo("slt") == 0) {
				if (registerFile.get(mips.rs) < registerFile.get(mips.rt))
					registerFile.set(mips.rd, 1);
				else
					registerFile.set(mips.rd, 0);
			}
			else if (mips.op.compareTo("beq") == 0) {
			}
			else if (mips.op.compareTo("bne") == 0) {
			}
/*
			else if (mips.op.compareTo("beq") == 0) {
			
				int index, address;
	
				index = labels.indexOf(new Label(mips.c.trim(), 0));
	
				if (index == -1)
					address = new Integer(mips.c);
				else
					address = labels.get(index).getRelativeLineNumber(programCounter);
	
				address += programCounter + 1;
	
				if (address >= 0 && address < instructionMemory.size()) {
					if (registerFile.get(mips.rs) == registerFile.get(mips.rt)) {
						programCounter = address;
						System.out.println("Branching to " + address);
						squash = true;
					}
				}
				else
					throw new SyntaxException("Branch to address outside of program: " + address);
			}
			else if (mips.op.compareTo("bne") == 0) {
				int index, address;
	
				index = labels.indexOf(new Label(mips.c.trim(), 0));
	
				if (index == -1)
					address = new Integer(mips.c);
				else
					address = labels.get(index).getRelativeLineNumber(programCounter);
	
				address += programCounter + 1;
	
				if (address >= 0 && address < instructionMemory.size()) {
					if (registerFile.get(mips.rs) != registerFile.get(mips.rt)) {
						programCounter = address;
						squash = true;
					}
				}
				else
					throw new SyntaxException("Branch to address outside of program: " + address);	
			} 
*/
			else if (mips.op.compareTo("lw") == 0) {
				int address;
				address = registerFile.get(mips.rs) + new Integer(mips.c);
	
				registerFile.set(mips.rt, memory.get(address));
			}
			else if (mips.op.compareTo("sw") == 0) {
				int address;
	
				address = registerFile.get(mips.rs) + new Integer(mips.c);
	
				memory.set(address, registerFile.get(mips.rt));
			}			
			else if (mips.op.compareTo("j") == 0) {
			}
			else if (mips.op.compareTo("jr") == 0) {
			}
			else if (mips.op.compareTo("jal") == 0) {
			}
			else
				throw new SyntaxException("Unknown isntruction: " + mips.op);
			
			instructionsExecuted++;
		}
	}
	
	private boolean checkForEnd() {
		MipsInstruction mips;
		mips = sim.endInstruction();
		
		return mips.end;
	}
	
	public String instructionMemoryToSting() {
		return instructionMemory.toString();
	}
	
	
	public String getQueue() {
			return sim.getQueue();
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
	
	public String getSimString() {
		return sim.toString();
	}
	
	public int getCycles() {
		return sim.getCycles();
	}
}
