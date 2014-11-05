import java.util.*;

public class Label {

	private String myLabel;
	private int lineNumber;
	
	public Label(String inLabel, int lineNumber) {
		myLabel = inLabel;
		this.lineNumber = lineNumber;
	}
	
	public int getAbsoluteLineNumber() {
		return lineNumber;
	}
	
	public int getRelativeLineNumber(int from) {
		return -1 * (from + 1 - lineNumber);
	}
	
	public String getLabel() {
		return myLabel;
	}
	
	public boolean equals(Object o) {
		Label other;
		
		if (o instanceof Label) {
			other = (Label) o;
			if (other.getLabel().compareTo(this.myLabel) == 0)
				return true;
		}
		
		return false;
	}
	
	public String toString() {
		return myLabel + " at line number " + lineNumber;
	}
	public static ArrayList<Label> findLabels(String input) {
		Scanner scanner;
		String curLine;
		int lineNumber;
		String label;
		ArrayList<Label> labels;
		
		int index;
		
		labels = new ArrayList<Label>();
		scanner = new Scanner(input);
		lineNumber = 0;
		
		while (scanner.hasNextLine()) {
			curLine = scanner.nextLine();
			
			while (Formatter.containsLabel(curLine)) {
				label = Formatter.getLabel(curLine);
				labels.add(new Label(label, lineNumber));
				curLine = Formatter.removeLabel(curLine);
			}
			
			lineNumber++;
		}
		
		return labels;
		
	}
}
