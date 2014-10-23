import java.util.*;
import java.io.*;

public class lab3 {
	public static final String HELP_MESSAGE = 
		"\nh = show help\n" +
		"d = dump register state\n" +
		"s = single step through the program (i.e. execute 1 instruction and stop)\n" +
		"s num = step through num instructions of the program\n" +
		"r = run until the program ends\n" +
		"m num1 num2 = display data memory from location num1 to num2\n" +
		"c = clear all registers, memory, and the program counter to 0\n" +
		"q = exit the program";
		
	public static final String PROMPT = "mips> ";
	public static final int MEM_SIZE = 8192;
	
	public static Memory memory = new Memory(MEM_SIZE);
	public static RegisterFile registerFile = new RegisterFile();
	public static InstructionMemory instructionMemory;
	public static ArrayList<Label> labels;
	public static int programCounter = 0;
	public static int instructionsExecuted = 0;
	
	public static void main(String[] args) {
		File assemblyFile;
		String script;
		String assemblyCode;
		
		if (args.length > 0) {
			try {
				assemblyFile = new File(args[0]);
				
				assemblyCode = Formatter.formatAssemblyFile(assemblyFile);
				
				labels = Label.findLabels(assemblyCode);
				
				instructionMemory = new InstructionMemory(assemblyCode);
				
				if (args.length == 1)
					script = null;
				else {
					script = args[1];
				}
				
				start(script);
			}
			catch (FileNotFoundException ex) {
				System.out.println("One of the files could not be found.\n" + ex);
			}
			catch (SyntaxException ex) {
				System.out.println(ex);
			}
		}
		else
			System.out.println("No assembly file given.");
			
	}
	
	private static void start(String script)
		throws FileNotFoundException {
		String input;
		String firstChar;
		Scanner inputScanner;
		Scanner lineScanner;
		
		
		if (script == null) {
			inputScanner = new Scanner(System.in);
		}
		else
			inputScanner = new Scanner(new File(script));
		
		System.out.print(PROMPT);
				
		try {
			while (inputScanner.hasNextLine()) {
				input = inputScanner.nextLine();
				input = input.trim();
				
				if (script != null)
					System.out.println(input);
			
				if (input.compareTo("") != 0) {
					firstChar = input.substring(0, 1);
					firstChar = firstChar.toLowerCase();
			
					if (firstChar.compareTo("h") == 0) {
						System.out.println(HELP_MESSAGE);
					}
					else if (firstChar.compareTo("d") == 0) {
						System.out.println("\npc = " + programCounter);
						System.out.println(registerFile);
					}
					else if (firstChar.compareTo("s") == 0) {
						int step;
						
						try {
							lineScanner = new Scanner(input);
						
							lineScanner.next();
						
							if (lineScanner.hasNext()) {
								step = new Integer(lineScanner.next());
							}
							else {
								step = 1;
							}
							
							for (int i = 0; i < step; i++) {
									execute();
							}
							System.out.print("        " + instructionsExecuted
								+ " instruction(s) executed");
							instructionsExecuted = 0;
						}
						catch (NumberFormatException ex) {
							System.out.println("s <numSteps>");
						}
					}
					else if (firstChar.compareTo("r") == 0) {
						while (programCounter < instructionMemory.size()) {
							execute();
						}
					}
					else if (firstChar.compareTo("m") == 0) {
						try {
							int startAddress;
							int endAddress;
					
							lineScanner = new Scanner(input);
						
							lineScanner.next();
							startAddress = new Integer(lineScanner.next());
							endAddress = new Integer(lineScanner.next());
						
							System.out.println(memory.toString(startAddress, endAddress));
						}
						catch (NoSuchElementException ex) {
							System.out.println("m <startAddress> <endAddress>");
						}
						catch (NumberFormatException ex) {
							System.out.println("m <startAddress> <endAddress>");
						}
					}
					else if (firstChar.compareTo("c") == 0) {
						memory.clear();
						registerFile.clear();
						programCounter = 0;
						System.out.println("        Simulator reset");
					}
					else if (firstChar.compareTo("q") == 0) {
						return;
					}
					else {
						System.out.println("That is not a valid option. Enter \"h\" for help.");
					}
					if (firstChar.compareTo("r") != 0)
						System.out.print("\n");
				}
				
				System.out.print(PROMPT);
			}
		}
		catch (NoSuchElementException | RegNotFoundException 
			| NumberFormatException | MemoryException | SyntaxException ex) {
			System.out.println("Assembly line " + programCounter + "\n   " + ex);
		}
	}
	
	public static void execute() 
		throws SyntaxException, MemoryException, NoSuchElementException, RegNotFoundException,
			NumberFormatException {
		String instruction;
		Scanner scanner;
		String operation;
		int labelIndex;
		
		
		if (programCounter >= instructionMemory.size()) {
			//System.out.println("End of program");
		}
		else {
			instruction = instructionMemory.get(programCounter);
			instruction = instruction.trim();
			labelIndex = instruction.indexOf(":");
			
			if (labelIndex != -1) {
				instruction = instruction.substring(labelIndex + 1);
			}
			
			instruction = instruction.replace("(", " ");
			instruction = instruction.replace(")", " ");
			
			scanner = new Scanner(instruction);
			
			operation = scanner.next();
			
			operation = operation.trim();
			operation = operation.toLowerCase();
			
			if (operation.compareTo("and") == 0) {
				String rd, rs, rt;
				
				rd = scanner.next();
				rs = scanner.next();
				rt = scanner.next();
				
				registerFile.set(rd, registerFile.get(rs) & registerFile.get(rt));
			}
			else if (operation.compareTo("or") == 0) {
				String rd, rs, rt;
				
				rd = scanner.next();
				rs = scanner.next();
				rt = scanner.next();
				
				registerFile.set(rd, registerFile.get(rs) | registerFile.get(rt));
			}
			else if (operation.compareTo("add") == 0) {
				String rd, rs, rt;
				
				rd = scanner.next();
				rs = scanner.next();
				rt = scanner.next();
				
				registerFile.set(rd, registerFile.get(rs) + registerFile.get(rt));
			}
			else if (operation.compareTo("addi") == 0) {
				String rt, rs, imm;
				
				rt = scanner.next();
				rs = scanner.next();
				imm = scanner.next();
				
				registerFile.set(rt, registerFile.get(rs) + new Integer(imm));
			}
			else if (operation.compareTo("sll") == 0) {
				String rd, rt, shamt;
				Integer shiftAmount;
				
				rd = scanner.next();
				rt = scanner.next();
				shamt = scanner.next();
				
				shiftAmount = new Integer(shamt);
				
				if (shiftAmount < 0 || shiftAmount > 31)
					throw new SyntaxException("Ilegal shift amount: " + shiftAmount);
				
				registerFile.set(rd, registerFile.get(rt) << shiftAmount);
			}
			else if (operation.compareTo("sub") == 0) {
				String rd, rs, rt;
				
				rd = scanner.next();
				rs = scanner.next();
				rt = scanner.next();
				
				registerFile.set(rd, registerFile.get(rs) - registerFile.get(rt));
			}
			else if (operation.compareTo("slt") == 0) {
				String rd, rs, rt;
				
				rd = scanner.next();
				rs = scanner.next();
				rt = scanner.next();
				
				if (registerFile.get(rs) < registerFile.get(rt))
					registerFile.set(rd, 1);
				else
					registerFile.set(rd, 0);
			}
			else if (operation.compareTo("beq") == 0) {
				String rt, rs, C;
				int index, address;
				
				rt = scanner.next();
				rs = scanner.next();
				C = scanner.next();
				
				index = labels.indexOf(new Label(C.trim(), 0));
				
				if (index == -1)
					address = new Integer(C);
				else
					address = labels.get(index).getRelativeLineNumber(programCounter);
				
				address += programCounter;
				
				if (address >= 0 && address < instructionMemory.size()) {
					if (registerFile.get(rs) == registerFile.get(rt))
						programCounter = address;	
				}
				else
					throw new SyntaxException("Branch to address outside of program: " + address);
			}
			else if (operation.compareTo("bne") == 0) {
				String rt, rs, C;
				int index, address;
				
				rt = scanner.next();
				rs = scanner.next();
				C = scanner.next();
				
				index = labels.indexOf(new Label(C.trim(), 0));
				
				if (index == -1)
					address = new Integer(C);
				else
					address = labels.get(index).getRelativeLineNumber(programCounter);
				
				address += programCounter;
				
				if (address >= 0 && address < instructionMemory.size()) {
					if (registerFile.get(rs) != registerFile.get(rt))
						programCounter = address;	
				}
				else
					throw new SyntaxException("Branch to address outside of program: " + address);			
			}
			else if (operation.compareTo("lw") == 0) {
				String rt, C, rs;
				int address;
				
				rt = scanner.next();
				C = scanner.next();
				rs = scanner.next();
				
				address = registerFile.get(rs) + new Integer(C);
				
				registerFile.set(rt, memory.get(address));
			}
			else if (operation.compareTo("sw") == 0) {
				String rt, C, rs;
				int address;
				
				rt = scanner.next();
				C = scanner.next();
				rs = scanner.next();
				
				address = registerFile.get(rs) + new Integer(C);
				
				memory.set(address, registerFile.get(rt));
			}
			else if (operation.compareTo("j") == 0) {
				String C;
				int index, address;
				C = scanner.next();
				
				index = labels.indexOf(new Label(C.trim(), 0));
				
				if (index == -1)
					address = new Integer(C);
				else
					address = labels.get(index).getAbsoluteLineNumber();
				
				if (address >= 0 && address < instructionMemory.size()) {
					programCounter = address - 1;	
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
				
			}
			else if (operation.compareTo("jr") == 0) {
				String rs;
				int address;
				rs = scanner.next();
				
				address = registerFile.get(rs);
				
				if (address >= 0 && address < instructionMemory.size()) {
					programCounter = address - 1;	
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
			}
			else if (operation.compareTo("jal") == 0) {
				String C;
				int index, address;
				C = scanner.next();
				
				index = labels.indexOf(new Label(C.trim(), 0));
				
				if (index == -1)
					address = new Integer(C);
				else
					address = labels.get(index).getAbsoluteLineNumber();
				
				if (address >= 0 && address < instructionMemory.size()) {
					registerFile.set("$ra", programCounter+1);
					programCounter = address - 1;	
				}
				else
					throw new SyntaxException("Jump to address outside of program: " + address);
			}
			else
				throw new SyntaxException("Unknown isntruction: " + operation);
				
			programCounter++;
			instructionsExecuted++;
		}
	}
}