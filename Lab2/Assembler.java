import java.io.*;
import java.util.*;

public class Assembler {
	
	public static void assemble(File input) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		String cur;
		while (scanner.hasNextLine()) {
			cur = scanner.nextLine();
			if (cur.charAt(0) == '$') {
				System.out.println(cur);
			}
		}	
	}
	
	public static String translateLine(String line) 
		throws BadInstructionException, RegNotFoundException, SyntaxException {
			int comment;
			int type;
			String inst;
			String machineCode;
			Scanner scanner;
			
			
			machineCode = "";
			comment = line.indexOf("#");
			
			if (comment != -1) 
				line = line.substring(0, comment);
			
			scanner = new Scanner(line);
			
			if (!scanner.hasNext())
				return machineCode;
			
			try {
				inst = scanner.next();
			}
			catch (Exception ex) {
				throw new SyntaxException("No instruction");
			}
			
			type = Instructions.getType(inst);
			
			if (type == Instructions.RTYPE) {
			
			}
			else if (type == Instructions.ITYPE) {
			
			}
			else if (type == Instructions.JTYPE) {
			
			}
			else
				throw new SyntaxInstruction("Bad Instruction " + inst);
			
			return machineCode;
	}
}


