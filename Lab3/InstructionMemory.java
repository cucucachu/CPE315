import java.util.Scanner;
import java.util.ArrayList;

public class InstructionMemory {
	protected ArrayList<String> memory;
	
	public InstructionMemory(String assemblyCode) {
		Scanner scanner;
		
		memory = new ArrayList<String>();
		
		if (assemblyCode != null && assemblyCode.compareTo("") != 0) {
			scanner = new Scanner(assemblyCode);
			
			while (scanner.hasNextLine()) {
				memory.add(scanner.nextLine());
			}
		}
	}
	
	public String get(int address) throws MemoryException {
		if (address >= memory.size() || address < 0)
			throw new MemoryException("Invalid Instruction Address " + address);
			
		return memory.get(address);
	}
	
	public int size() {
		return memory.size();
	}
}
