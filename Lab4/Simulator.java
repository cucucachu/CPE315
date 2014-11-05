import java.util.LinkedList;

public class Simulator {

	public static final String stallStr = "stall";
	public static final int numStages = 5;
	
	private MipsInstruction[] instructions;
	private LinkedList<MipsInstruction> queue;
	
	public Simulator() {
		instructions = new MipsInstruction[numStages];
		queue = new LinkedList<MipsInstruction>();
		
		for (int i = 0; i < instructions.length; i++) {
			instructions[i] = new MipsInstruction();
			instructions[i].op = "empty";
		}
	}
	
	public void nextInstruction(String next) {
		MipsInstruction instruction;
	
		for (int i = instructions.length - 1; i > 0; i--) {
			instructions[i] = instructions[i - 1].makeCopy();
		}
		
		try {
			instruction = MipsInstruction.mipsInstructionFactory(next);
			queue.add(instruction);
			instructions[0] = queue.remove();
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		
		checkForStalls();
	}
	
	private void checkForStalls() {
		return;
	}
	
	public String toString() {
		String returnStr;
		
		returnStr = "";
		
		for (int i = 0; i < instructions.length; i++) 
			returnStr = returnStr + instructions[i].toString() + " ";
		
		return returnStr;
	}
}
