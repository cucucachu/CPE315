import java.util.*;
import java.io.*;

public class lab4 {
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
	public static ArrayList<Label> labels;
	public static int instructionsExecuted = 0;
	public static String script = null;
	
	public static MipsCPU cpu;
	//public static Simulator sim;
	
	public static void main(String[] args) {
		File assemblyFile;
		String assemblyCode;
		
		//sim = new Simulator();
		//System.out.println("Simulator: " + sim);
		
		if (args.length > 0) {
			try {
				assemblyFile = new File(args[0]);
				
				assemblyCode = Formatter.formatAssemblyFile(assemblyFile);
				
				labels = Label.findLabels(assemblyCode);
				
				cpu = new MipsCPU(assemblyCode, labels);
				
				if (args.length > 1)
					script = args[1];
				
				start();
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
	
	private static void start()
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
						System.out.println("\npc = " + cpu.getProgramCounter());
						System.out.println(cpu.getRegisterFileString());
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
								//sim.nextInstruction(cpu.getNextInstruction());
								
								if (cpu.execute())
									instructionsExecuted++;	
							}
							System.out.println("\n" + cpu.getSimString());
							//System.out.print("        " + instructionsExecuted
							//	+ " instruction(s) executed");
							instructionsExecuted = 0;
						}
						catch (NumberFormatException ex) {
							System.out.println("s <numSteps>");
						}
					}
					else if (firstChar.compareTo("r") == 0) {
						while(cpu.execute());
						instructionsExecuted = 0;
					}
					else if (firstChar.compareTo("m") == 0) {
						try {
							int startAddress;
							int endAddress;
					
							lineScanner = new Scanner(input);
						
							lineScanner.next();
							startAddress = new Integer(lineScanner.next());
							endAddress = new Integer(lineScanner.next());
						
							System.out.println(cpu.getMemoryString(startAddress, endAddress));
						}
						catch (NoSuchElementException ex) {
							System.out.println("m <startAddress> <endAddress>");
						}
						catch (NumberFormatException ex) {
							System.out.println("m <startAddress> <endAddress>");
						}
					}
					else if (firstChar.compareTo("c") == 0) {
						cpu.reset();
						System.out.println("        Simulator reset");
					}					
					else if (firstChar.compareTo("i") == 0) {
						System.out.println("Next instruction: \"" 
							+ cpu.getNextInstruction() + "\" at pc=" 
							+ cpu.getProgramCounter());
					}
					else if (firstChar.compareTo("a") == 0) {
						System.out.println(cpu.instructionMemoryToSting());
					}
					else if (firstChar.compareTo("l") == 0) {
						System.out.println("Labels");
						for (Label label : labels)
							System.out.println(label);
					} 
					else if (firstChar.compareTo("t") == 0) {
						System.out.println(cpu.getSimString());
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
			System.out.println("Assembly line " + cpu.getProgramCounter() + "\n   " + ex);
		}
	}
}
