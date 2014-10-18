import java.io.*;

public class Lab2 {
   
   public static void main(String args[]) {
   	String formattedInput;
   
   	if (args.length < 1) {
   		System.out.println("Bad arguement");
   		return;
   	}
      File input = new File(args[0]);
      if (!input.exists()) {
      	System.out.println("Could not locate assembly file \""
      		+ input.getName() + "\".");
      	return;
      }
      
      try {
      	formattedInput = Formatter.formatAssemblyFile(input);
      	//System.out.println("Formatted assembly file: \n" + formattedInput);
      	Assembler.assemble(formattedInput);
   	}
   	catch (Exception ex) {
   		System.out.println("Caught " + ex + " while assembling.");
   	}
   }

}
