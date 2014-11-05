public class Memory {
	protected Integer[] memory;
	
	public void clear() {
		for (int i = 0; i < memory.length; i++) {
			memory[i] = 0;
		}
	}
	
	public Memory(int size) {
		memory = new Integer[size];
		clear();
	}
	
	public int get(int address) throws MemoryException {
		if (address >= memory.length || address < 0)
			throw new MemoryException("Invalid Address.");
			
		return memory[address];
	}
	
	public void set(int address, int value) throws MemoryException {
		if (address >= memory.length || address < 0)
			throw new MemoryException("Invalid Address.");
		
		memory[address] = value;
	}
	
	public String toString(int start, int end){
		String mem;
		
		mem = "";
		
		if (start < 0 || start >= memory.length
			||  end < 0 || end >= memory.length || start > end)
		{
			mem = "Invalid Addresses";
		}
		else {
			for (int i = start; i <= end; i++) {
				mem = mem + "\n[" + i + "] = " + memory[i];
			} 
		}
		
		return mem;
	}
}
