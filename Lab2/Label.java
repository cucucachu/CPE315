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
}
