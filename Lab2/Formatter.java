import java.util.*;
import java.io.*;

public class Formatter {

	public static String formatAssemblyFile(File input) 
		throws FileNotFoundException, SyntaxException {
		Scanner scanner;
		String formatted;
		String curLine;
		String lastLabel;
		int index;
		
		scanner = new Scanner(input);
		formatted = "";
		
		while (scanner.hasNextLine()) {
			curLine = scanner.nextLine();
			
			curLine = removeCommentsAndTrim(curLine);
			
			if (containsLabel(curLine)) {
				lastLabel = getLabel(curLine);
			
				if ((lastLabel + ":").compareTo(curLine) == 0) {
					try {
						do {
							curLine = scanner.nextLine();
							curLine = removeCommentsAndTrim(curLine);
							if (containsLabel(curLine))
								throw new SyntaxException("Two Labels in a row");
						} while (curLine.compareTo("") == 0);
					
						curLine = lastLabel + ": " + curLine;
					}
					catch (NoSuchElementException ex) {
						throw new SyntaxException("Label at end of doc.");
					}
				}
			}
			
			curLine = curLine.replace("$", " $");
			curLine = curLine.replace("( $", "($");
			curLine = curLine.replace(",", ", ");
			
			if (curLine.compareTo("") != 0)
				formatted = formatted + curLine + "\n";
		}

		return formatted;
	}
	
	private static String removeCommentsAndTrim(String in) {
		int index;
		
		if (in == null)
			return "";
			
		index = in.indexOf("#");
		if (index != -1)
			in = in.substring(0, index);
		
		in = in.trim();
		
		return in;
	}
	
	private static boolean containsLabel(String in) {
		if (in.indexOf(":") == -1)
			return false;
		
		return true;
	}
	
	private static String getLabel(String in) {
		if (containsLabel(in)) 
			return in.substring(0, in.indexOf(":"));
		
		return "";
	}
}
