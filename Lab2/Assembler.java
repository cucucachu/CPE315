import java.io.*;
import java.util.*;

public class Assembler {
	
	public static void assemble(File input) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		String cur;
		String machineCode;
		
		findLabels(input);
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
	
	public static Hashtable<String, Integer> findLabels(File input) 
		throws FileNotFoundException {
		Scanner scanner;
		Scanner lineScanner;
		int curLineNumber;
		String curLine;
		String label;
		String inst;
		Hashtable<String, Integer> table;
		
		table = new Hashtable<String, Integer>();
		scanner = new Scanner(input);
		curLineNumber = 0;
		
		while (scanner.hasNextLine()) {
			curLine = scanner.nextLine();
			if (curLine.contains(":")) {
				label = curLine.substring(0, curLine.indexOf(":")).trim();
			}
			
			curLine = cleanUpLine(curLine);
			lineScanner = new Scanner(curLine);
			inst = "";
			if (lineScanner.hasNext())
				inst = lineScanner.next();
			try {
				Instructions.getOpCode(inst);
				curLineNumber++;
			}
			catch(BadInstructionException ex)
			{
				
			}
			
			if (lable.compareTo("") != 0) {
				table.add(label, curLineNumber);
				System.out.println("Label found at line " + curLineNumber 
					+ " : " + label);
			}
			
		}
		
		return table;
	}
	
	public static String translateLine(String line) 
		throws BadInstructionException, RegNotFoundException, SyntaxException {
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
			
			line = cleanUpLine(line);
			
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
	
	private static String cleanUpLine(String line) throws SyntaxException {
		Scanner scanner;
		String temp;
		int comment;
		int label;
		int endOfInst;
	
		if (line.trim().length() == 0)
			return "";
			
		comment = line.indexOf("#");
		if (comment != -1) 
			line = line.substring(0, comment);
		
		label = line.indexOf(":");
		if (label != -1) 
			line = line.substring(label + 1);
		
		line = line.trim();
		
	
		if (line.length() == 0)
			return "";
			
		if (line.charAt(0) != 'j') {
			endOfInst = line.indexOf("$");
			if (endOfInst == -1)
				throw new SyntaxException("No \"$\" found in instruction: " + line);
			
			line = line.substring(0, endOfInst) + " " + line.substring(endOfInst);
		}
		
		line = line.replace(',', ' ');
		
		//System.out.println("Clean line: " + line);
		
		return line;
	}
}


