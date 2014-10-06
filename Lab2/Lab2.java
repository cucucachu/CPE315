import java.io.*;

public class Lab2 {
   
   public static void main(String args[]) {
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
      	Assembler.assemble(input);
   	}
   	catch (Exception ex) {
   		System.out.println("Caught " + ex + " while assembling.");
   	}
   }

}
