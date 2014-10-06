import java.io.*;
import java.util.*;

public class Assembler {
	
	public static void assemble(File input) throws FileNotFoundException {
		Scanner scanner = new Scanner(input);
		String cur;
		while (scanner.hasNext()) {
			cur = scanner.next();
			if (cur.contains('$'))
				System.out.println(cur);
		}	
	}
}
