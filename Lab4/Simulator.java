import java.util.*;

public class Simulator {

	public static final String stallStr = "stall";
	public static final int IF = 3;
	public static final int ID = 2;
	public static final int EX = 1;
	public static final int MEM = 0;
	public static final int NUM_STAGES = 4;
	public static final int BRANCH_PENALTY = 3;
	
	private int cycles;
	private LinkedList<MipsInstruction> queue;
	private InstructionMemory im;
	
	public Simulator(InstructionMemory im) {
		this.im = im;
		reset();
	}
	
	public void reset() {
		queue = new LinkedList<MipsInstruction>();
		cycles = 0;
		for (int i = 0; i < NUM_STAGES; i++) {
			queue.add(new MipsInstruction(MipsInstruction.EMPTY));
		}
	
	}
	
	public void setPC(int pc) throws SyntaxException, MemoryException,
		NoSuchElementException, RegNotFoundException, NumberFormatException {
		
		if (pc < im.size() && pc >= 0)
			queue.add(new MipsInstruction(im.get(pc), pc));
		else
			queue.add(new MipsInstruction(MipsInstruction.END, pc));
		
	}
	
	public void updateQueue() {
		
		checkForJump();
		
		/*
		if (squash) {
					squash();
				}
				 
		*/
		queue.remove();
		cycles++;
	}
	
	public boolean checkForLW(int reg) {
		MipsInstruction ex, id;
		boolean hazard;
		
		hazard = false;
		
		ex = queue.get(reg);
		id = queue.get(reg + 1);
		 
		try {
			if (ex.op.compareTo("lw") == 0) {
				if (ex.rt != null && ex.rt.compareTo("$0") != 0) {
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
			//System.out.println("Stall!!!!!!!!!!!!!!!!!!");
		}
		return hazard;
	}
	
	public void stall() {
		queue.add(ID + 1, new MipsInstruction(MipsInstruction.STALL));
	}
	
	private void checkForJump() {
		if (queue.get(IF).isJump())
			queue.get(IF + 1).squash();
	}
	
	public MipsInstruction getIF() {
		return queue.get(IF);
	}
	
	public MipsInstruction getID() {
		return queue.get(ID);
	}
	
	public MipsInstruction getEX() {
		return queue.get(EX);
	}
	
	public void squash() {
		int numSquashed = 0;
		int curReg;
		MipsInstruction inst;
		
		curReg = ID;
		
		try {
			while (numSquashed < BRANCH_PENALTY) {
				if (curReg >= queue.size()) {
					queue.add(new MipsInstruction(MipsInstruction.SQUASH));
					numSquashed++;
				}
				else {
					inst = queue.get(curReg);
					if (inst.squashable()) {
						inst.squash();
						numSquashed++;
					}
				}
				curReg++;
			}
		}
		catch (Exception ex){
			System.out.println("squash() caught " + ex);
		}
	}
	
	public MipsInstruction endInstruction() {
		return queue.get(0);
	}
	
	public String toString() {
		String returnStr;
		ListIterator it;
		int i;
		
		it = queue.listIterator();
		
		returnStr ="";
		i = 0;
		while (it.hasNext() && i++ < NUM_STAGES)
			returnStr = " " + it.next().toString() + returnStr;
		
		return returnStr;
	}

	private int getPC() {
		int i;
		int pc;
		i = IF;
		pc = queue.get(i).pc;
		
		while(pc == -1 && i > 0) {
			pc = queue.get(i--).pc;
		}
		
		if (pc == -1)
			return 0;
		
		return pc;
	}

	public int getCycles() {
		return cycles;
	}
	
	public String getQueue() {
		String queueStr;
		
		queueStr = "";
		
		for (int i = queue.size() - 1; i >= 0; i--)
			queueStr = queueStr + queue.get(i).toString() + " ";
		
		return queueStr;
	}
}
