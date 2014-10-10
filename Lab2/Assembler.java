import java.io.*;
import java.util.*;

public class Assembler {
	
	public static void assemble(File input) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		String cur;
		String machineCode;
		while (scanner.hasNextLine()) {
			cur = scanner.nextLine();
			try {
				machineCode = translateLine(cur);
				if (machineCode.equals("") == false)
					System.out.println(machineCode);
			}
			catch (Exception ex) {
				System.out.println(ex);
			}
			
		}	
	}
	
	public static String translateLine(String line) 
		throws BadInstructionException, RegNotFoundException, SyntaxException {
			int comment;
			int label;
			int type;
			String inst;
			String regD;
			String regS;
			String regT;
			String immediate;
			String machineCode;
			Scanner scanner;
			Integer immVal;
			String immStr;
			
			
			machineCode = "";
			comment = line.indexOf("#");
			label = line.indexOf(":");
			
			if (comment != -1) 
				line = line.substring(0, comment);
			
			if (label != -1) 
				line = line.substring(label + 1);
			
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
			
			machineCode = Instructions.getOpCode(inst);
			
			try {
				if (type == Instructions.RTYPE) {
			
					regD = scanner.next();
					regS = scanner.next();
					regT = scanner.next();
					
					if (Instructions.usesShamt(inst))
						machineCode = machineCode + " 00000";
					else
						machineCode = machineCode + " " 
							+ Registers.getRegisterCode(regS);
					
					machineCode = machineCode + " " 
						+ Registers.getRegisterCode(regT);
					
					machineCode = machineCode + " "
						+ Registers.getRegisterCode(regD);
						
					if (Instructions.usesShamt(inst))
						machineCode = machineCode + " "
							+ Integer.toBinaryString(new Integer(regS));
					else
						machineCode = machineCode + " 00000";
					
					machineCode = machineCode + " "
						+ Instructions.getFunct(inst);
				}
				else if (type == Instructions.ITYPE) {
					regT = scanner.next();
					regS = scanner.next();
					immediate = scanner.next();
					
					machineCode = machineCode + " " 
						+ Registers.getRegisterCode(regS);
					
					machineCode = machineCode + " " 
						+ Registers.getRegisterCode(regT);
						
					immVal = new Integer(immediate);
					immStr = Integer.toBinaryString(immVal);
					
					for (int i = immStr.length(); i < 16; i++)
						immStr = "0" + immStr;
						
					for (int i = immStr.length(); i > 16; i--)
						immStr = immStr.substring(1, immStr.length());
					
					machineCode = machineCode + " " + immStr;
				}
				else if (type == Instructions.JTYPE) {
			
				}
				else
					throw new SyntaxException("Bad Instruction " + inst);
			}
			catch (NoSuchElementException | IllegalStateException ex) {
				machineCode = "Could not read registers";
			}
			catch (NumberFormatException ex) {
				machineCode = "Bad Immediat Value";
			}
			
			return machineCode;
	}
}


