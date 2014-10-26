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
				lastLabel = "";
				while (containsLabel(curLine)) {
					lastLabel = lastLabel + getLabel(curLine) + ":";
					curLine = removeLabel(curLine);
				}
				
				try {
					while (curLine.compareTo("") == 0) {
						curLine = scanner.nextLine();
						curLine = removeCommentsAndTrim(curLine);
						if (containsLabel(curLine)) {
							while (containsLabel(curLine)) {
								lastLabel = lastLabel + getLabel(curLine) + ":";
								curLine = removeLabel(curLine);
								curLine = curLine.trim();
							}
							
							//throw new SyntaxException("Two Labels in a row");
						}
					}
				
					curLine = lastLabel + curLine;
				}
				catch (NoSuchElementException ex) {
					//throw new SyntaxException("Label at end of doc.");
					curLine = lastLabel;
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
	
	public static boolean containsLabel(String in) {
		if (in.indexOf(":") == -1)
			return false;
		
		return true;
	}
	
	public static String removeLabel(String in) {
		int index;
		
		index = in.indexOf(":");
		
		if (index != -1 && index < in.length())
			return in.substring(index + 1);
		
		return in;
	}
	
	public static String getLabel(String in) {
		if (containsLabel(in)) 
			return in.substring(0, in.indexOf(":"));
		
		return "";
	}
	
	public static String removeLabels(String in) {
		String out;
		
		out = in;
		
		while (containsLabel(out))
			out = removeLabel(out);
			
		return out;
	}
}
