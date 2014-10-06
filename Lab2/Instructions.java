public class Instructions {
	public static String getOpCode(String inst) throws BadInstructionException {
		inst = inst.toLowerCase();
		inst = inst.trim();
		
		if (inst.compareTo("and") == 0)
			return "000000";
		else if (inst.compareTo("or") == 0)
			return "000000";
		else if (inst.compareTo("add") == 0)
			return "000000";
		else if (inst.compareTo("addi") == 0)
			return "001000";
		else if (inst.compareTo("sll") == 0)
			return "000000";
		else if (inst.compareTo("sub") == 0)
			return "000000";
		else if (inst.compareTo("slt") == 0)
			return "000000";
		else if (inst.compareTo("beq") == 0)
			return "000100";
		else if (inst.compareTo("bne") == 0)
			return "000101";
		else if (inst.compareTo("sw") == 0)
			return "101011";
		else if (inst.compareTo("lw") == 0)
			return "100011";
		else if (inst.compareTo("j") == 0)
			return "000010";
		else if (inst.compareTo("jr") == 0)
			return "000000";
		else if (inst.compareTo("jal") == 0)
			return "000011";
		else
			throw new BadInstructionException("Could not find opCode for " + inst + ".");
	}
	
	public static String getFunct(String inst) throws BadInstructionException {
		inst = inst.toLowerCase();
		inst = inst.trim();
		
		if (inst.compareTo("and") == 0)
			return "100100";
		else if (inst.compareTo("or") == 0)
			return "100101";
		else if (inst.compareTo("add") == 0)
			return "100000";
		else if (inst.compareTo("addi") == 0)
			return "";
		else if (inst.compareTo("sll") == 0)
			return "000000";
		else if (inst.compareTo("sub") == 0)
			return "100010";
		else if (inst.compareTo("slt") == 0)
			return "101010";
		else if (inst.compareTo("beq") == 0)
			return "";
		else if (inst.compareTo("bne") == 0)
			return "";
		else if (inst.compareTo("sw") == 0)
			return "";
		else if (inst.compareTo("lw") == 0)
			return "";
		else if (inst.compareTo("j") == 0)
			return "";
		else if (inst.compareTo("jr") == 0)
			return "001000";
		else if (inst.compareTo("jal") == 0)
			return "";
		else
			throw new BadInstructionException("Could not find opCode for " + inst + ".");
	}
}
