import java.io.*;
import java.util.*;

public class Assembler {
	
	public static void assemble(String input) {
		Scanner scanner = new Scanner(input);
		String cur;
		int lineNumber;
		String machineCode;
		ArrayList<Label> labels;
		
		labels = findLabels(input);
		
		lineNumber = 0;
		while (scanner.hasNextLine()) {
			cur = scanner.nextLine();
			try {
				machineCode = translateLine(cur, lineNumber, labels);
					System.out.println(machineCode);
			}
			catch (Exception ex) {
				System.out.println(ex);
			}
			lineNumber++;
		}
	}
	
	public static ArrayList<Label> findLabels(String input) {
		Scanner scanner;
		String curLine;
		int lineNumber;
		ArrayList<Label> labels;
		
		int index;
		
		labels = new ArrayList<Label>();
		scanner = new Scanner(input);
		lineNumber = 0;
		
		while (scanner.hasNextLine()) {
			curLine = scanner.nextLine();
			index = curLine.indexOf(":");
			if (index != -1)
				labels.add(new Label(curLine.substring(0, index), lineNumber));
			
			lineNumber++;
		}
		
		return labels;
		
	}
	

	public static String translateLine(String line, int lineNumber,
		ArrayList<Label> labels) 
		throws BadInstructionException, RegNotFoundException, SyntaxException {
			int type;
			int index;
			int labelLineNumber;
			String inst;
			String regD;
			String regS;
			String regT;
			String immediate;
			String machineCode;
			Scanner scanner;
			Integer immVal;
			String immStr;
			Label label;
			
			
			machineCode = "";
			
			index = line.indexOf(":");
			if (index != -1)
				line = line.substring(index + 1);
			
			scanner = new Scanner(line);
			
			if (!scanner.hasNext())
				return machineCode;
			
			inst = scanner.next();
			
			type = Instructions.getType(inst);
			
			machineCode = Instructions.getOpCode(inst);
			
			try {
				if (type == Instructions.RTYPE) {
					if (Instructions.isJR(inst)) {
						regS = scanner.next();
						
						machineCode = machineCode + " " + Registers.getRegisterCode(regS);
						machineCode = machineCode + " 000000000000000";
						machineCode = machineCode + " " + Instructions.getFunct(inst);
					}
					else {
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
				}
				else if (type == Instructions.ITYPE) {
					regT = scanner.next();
					regS = scanner.next();
						
					if (Instructions.isBranch(inst)) {
						immediate = scanner.next();
						machineCode = machineCode + " " 
							+ Registers.getRegisterCode(regT);
					
						machineCode = machineCode + " " 
							+ Registers.getRegisterCode(regS);
							
						index = labels.indexOf(new Label(immediate, 0));
						label = labels.get(index);
						labelLineNumber = label.getRelativeLineNumber(lineNumber);
						
						immVal = new Integer(labelLineNumber);
						immStr = Integer.toBinaryString(immVal);
					
						for (int i = immStr.length(); i < 16; i++)
							immStr = "0" + immStr;
						
						for (int i = immStr.length(); i > 16; i--)
							immStr = immStr.substring(1, immStr.length());
					
						machineCode = machineCode + " " + immStr;
					}
					else if (Instructions.isMemAccess(inst)) {
						index = regS.indexOf("(");
						if (index == -1)
							throw new SyntaxException("No \"(\" found in a lw or sw.");
						
						immediate = regS.substring(0, index);
						regS = regS.substring(index + 1, regS.length() - 1);
						
						machineCode = machineCode + " " + Registers.getRegisterCode(regS);
						machineCode = machineCode + " " + Registers.getRegisterCode(regT);
						
						immVal = new Integer(immediate);
						immStr = Integer.toBinaryString(immVal);
					
						for (int i = immStr.length(); i < 16; i++)
							immStr = "0" + immStr;
						
						for (int i = immStr.length(); i > 16; i--)
							immStr = immStr.substring(1, immStr.length());
					
						machineCode = machineCode + " " + immStr;
					}
					else {
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
				}
				else if (type == Instructions.JTYPE) {
					index = labels.indexOf(new Label(scanner.next(), 0));
					label = labels.get(index);
					labelLineNumber = label.getAbsoluteLineNumber();
					immVal = new Integer(labelLineNumber);
					immStr = Integer.toBinaryString(immVal);
					
					for (int i = immStr.length(); i < 26; i++)
						immStr = "0" + immStr;
					
					machineCode = machineCode + " " + immStr;
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


