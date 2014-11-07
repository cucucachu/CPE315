import java.util.*;

public class Simulator {

	public static final String stallStr = "stall";
	public static final int IF = 3;
	public static final int ID = 2;
	public static final int EX = 1;
	public static final int MEM = 0;
	public static final int NUM_STAGES = 4;
	
	private int pc;
	private int cycles;
	private int instructions;
	private boolean executionEnded;
	private boolean simulationEnded;
	private LinkedList<MipsInstruction> queue;
	
	public Simulator() {
		reset();
	}
	
	public void reset() {
		queue = new LinkedList<MipsInstruction>();
		pc = 0;
		instructions = 0;
		cycles = 0;
		executionEnded = false;
		simulationEnded = false;
		for (int i = 0; i < NUM_STAGES; i++) {
			queue.add(new MipsInstruction(true, "empty"));
		}
	
	}
	
	public void nextInstructionIs(String next) throws SyntaxException, MemoryException,
		NoSuchElementException, RegNotFoundException, NumberFormatException {
		MipsInstruction instruction;
		String removed;
		
		instruction = new MipsInstruction(next);
		
		removed = queue.remove().op;
		
		if (queue.get(0).op != null && queue.get(0).op.compareTo("empty") == 0 && executionEnded)
			simulationEnded = true;
		
		if (!simulationEnded) {		
			if (removed != null
				&& removed.compareTo("empty") != 0
				&& removed.compareTo("stall") != 0
				&& removed.compareTo("squash") != 0)
				instructions++;
		
			queue.add(instruction);
			checkForLW();
			//checkForJump();
			//checkForBranch();
		
			pc++;
			cycles++;
		}
	}
	
	private void checkForLW() {
		MipsInstruction ex, id;
		boolean hazard;
		
		hazard = false;
		
		ex = queue.get(EX);
		id = queue.get(ID);
		 
		try {
			if (ex.op.compareTo("lw") == 0) {
				if (ex.rt != null) {
					if (id.getType() == MipsInstruction.RTYPE || id.isBranch()) {
						if (id.rs != null && id.rs.compareTo(ex.rt) == 0) {
							hazard = true;
						}
						if (id.rt != null && id.rt.compareTo(ex.rt) == 0) {
							hazard = true;
						}
					}
					else if (id.getType() == MipsInstruction.ITYPE) {
						if (id.rs != null && id.rs.compareTo(ex.rt) == 0) {
							hazard = true;
						}
					}
				}
			}
		}
		catch (Exception exception) {
			System.out.println("Exception caught in checkForLW(): " + exception);
		}
		
		if (hazard) {
			pc--;
			queue.add(EX + 1, new MipsInstruction(true, "stall"));
		}
	}
	
	private void checkForJump() {
		
	}
	
	private void checkForBranch() {
		
	}
	
	public void programEnd() {
		executionEnded = true;
	}
	
	public boolean simulationEnded() {
		return simulationEnded;
	}
	
	public String toString() {
		String returnStr;
		ListIterator it;
		int i;
		
		it = queue.listIterator();
		
		returnStr ="";
		i = 0;
		while (it.hasNext() && i++ < NUM_STAGES)
			returnStr = "\t" + it.next().toString() + returnStr;
		
		returnStr = "pc if/id id/exe\texe/mem\tmem/wb\n" + pc + returnStr;
		
		return returnStr;
	}

	public int getCycles() {
		return cycles;
	}
	
	public int getInstructions() {
		return instructions;
	}
}
