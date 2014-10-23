import java.util.*;

public class RegisterFile extends Memory {
	public static final Map<String, Integer> REGISTER_MAP;
    static
    {
        REGISTER_MAP = new HashMap<String, Integer>();
        REGISTER_MAP.put("$zero", 0);
        REGISTER_MAP.put("$0", 0);
        //REGISTER_MAP.put("", 1);
        REGISTER_MAP.put("$v0", 2);
        REGISTER_MAP.put("$v1", 3);
        REGISTER_MAP.put("$a0", 4);
        REGISTER_MAP.put("$a1", 5);
        REGISTER_MAP.put("$a2", 6);
        REGISTER_MAP.put("$a3", 7);
        REGISTER_MAP.put("$t0", 8);
        REGISTER_MAP.put("$t1", 9);
        REGISTER_MAP.put("$t2", 10);
        REGISTER_MAP.put("$t3", 11);
        REGISTER_MAP.put("$t4", 12);
        REGISTER_MAP.put("$t5", 13);
        REGISTER_MAP.put("$t6", 14);
        REGISTER_MAP.put("$t7", 15);
        REGISTER_MAP.put("$s0", 16);
        REGISTER_MAP.put("$s1", 17);
        REGISTER_MAP.put("$s2", 18);
        REGISTER_MAP.put("$s3", 19);
        REGISTER_MAP.put("$s4", 20);
        REGISTER_MAP.put("$s5", 21);
        REGISTER_MAP.put("$s6", 22);
        REGISTER_MAP.put("$s7", 23);
        REGISTER_MAP.put("$t8", 24);
        REGISTER_MAP.put("$t9", 25);
        //REGISTER_MAP.put("$", 26);
        //REGISTER_MAP.put("$", 27);
        REGISTER_MAP.put("$gp", 28);
        REGISTER_MAP.put("$sp", 29);
        REGISTER_MAP.put("$fp", 30);
        REGISTER_MAP.put("$ra", 31);
    }
    
    private static final String[] REGISTER_NAMES = {
    	"$0", "$v0", "$v1","$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6",
    	"$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra" 
    	};
	
	public static final int NUM_MIPS_REGISTERS = 32;
	public RegisterFile() {
		super(NUM_MIPS_REGISTERS);
	}
	
	public int get(String regName) throws RegNotFoundException {
		Integer index;
		
		regName = regName.toLowerCase();
		regName = regName.replace(",", " ");
		regName = regName.trim();
		
		index = REGISTER_MAP.get(regName);
		
		if (index == null)
			throw new RegNotFoundException("No register with name \"" + regName + "\"");
		
		return memory[index];
	}
	
	public void set(String regName, int value) throws RegNotFoundException {
		Integer index;
		
		regName = regName.toLowerCase();
		regName = regName.replace(",", " ");
		regName = regName.trim();
		
		index = REGISTER_MAP.get(regName);
		
		if (index == null)
			throw new RegNotFoundException("No register with name \"" + regName + "\"");
		
		memory[index] = value;
	}
	
	public String toString() {
		String toReturn;
		String regName;
		int indent;
		
		toReturn = "";
		indent = 0;
		
		for (int i = 0; i < REGISTER_NAMES.length; i++) {
			regName = REGISTER_NAMES[i];
			toReturn = toReturn + regName + " = " + memory[REGISTER_MAP.get(regName)];
			
			if (i == 0) {
				toReturn = toReturn + " ";
			}
			if (indent < 3) {
				if (i != REGISTER_NAMES.length - 1)
					toReturn = toReturn + "         ";
				indent++;
			}
			else if (i < REGISTER_NAMES.length - 1) {
				toReturn = toReturn + "\n";
				indent = 0;
			}
		}
		
		return toReturn;
	}
}
