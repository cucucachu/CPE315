import java.util.*;
import java.io.*;

public class lab6 {
	
	private static int[][] caches = new int[6][512];
	private static int[] cache7 = new int[1028];
	private static int[] hits = new int[7];
	private static int count = 0;
	private static int[][] lastUsed = new int[3][512];
	
	
	public static void main(String[] args) {
		Scanner scanner;
		int address;
		
		if (args.length < 1) {
			System.out.println("No memory stream given.");
			return;
		}
		
		try {
			scanner = new Scanner(new File(args[0]));
		
			while (scanner.hasNext()) {
				scanner.next();
				address = Integer.parseInt(scanner.next(), 16);
				
				if (address < 0)
					throw new Exception("Invalid Address " + address);
				cache(address);
				
				if (count % 100000 == 0)
					System.out.printf("%.1f%%\n", (double)count * 100.0 / 5000000);
				
				count++;
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println("Could not find input file " + args[0]);
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		
		System.out.println("Cache #1\nCache size: 2048B	Associativity: 1	Block size: 1");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[0], 100.0 * (double)hits[0] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #2\nCache size: 2048B	Associativity: 1	Block size: 2");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[1], 100.0 * (double)hits[1] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #3\nCache size: 2048B	Associativity: 1	Block size: 4");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[2], 100.0 * (double)hits[2] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #4\nCache size: 2048B	Associativity: 2	Block size: 1");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[3], 100.0 * (double)hits[3] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #5\nCache size: 2048B	Associativity: 4	Block size: 1");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[4], 100.0 * (double)hits[4] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #6\nCache size: 2048B	Associativity: 4	Block size: 4");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[5], 100.0 * (double)hits[5] / (double)count);
		System.out.println("---------------------------");
		
		System.out.println("Cache #7\nCache size: 4096B	Associativity: 1	Block size: 1");
		System.out.printf("Hits: %d Hit Rate %.2f%%\n", 
			hits[6], 100.0 * (double)hits[6] / (double)count);
			
	}
	
	private static void cache(int ogAddress) throws Exception {
		int index;
		int address;
		
		address = ogAddress;
		index = (address >> 2) % 512;
		
		if (address == 0)
			System.out.println("Address Zero");
		
		if (index > caches[0].length)
			throw new Exception("Invalid index " + index);
		
		// Cache 1
		if (caches[0][index] == address)
			hits[0]++;
		else {
			caches[0][index] = address;
		}
		
		//Cache 2
		if (index % 2 == 1) {
			index--;
			address -= 4;
		}
		
		if (caches[1][index] == address)
			hits[1]++;
		else {
			caches[1][index] = address;
			caches[1][index+1] = address + 4;
		}
		
		//Cache 3
		index = (ogAddress >> 2) % 512;
		address = ogAddress - (4 * (index % 4));
		index = index - (index % 4);
		
		if (caches[2][index] == address)
			hits[2]++;
		else {
			caches[2][index] = address;
			caches[2][index+1] = address + 4;
			caches[2][index+2] = address + 8;
			caches[2][index+3] = address + 12;
		}
		
		//Cache 4 2KB, 2-Way Associative, 1-Word Blocks
		address = ogAddress;
		index = (address >> 2) % 256;
		
		if (caches[3][index] == address) {
			hits[3]++;
			lastUsed[0][index] = count;	
		}
		else if (caches[3][index+256] == address) {
			hits[3]++;
			lastUsed[0][index+256] = count;
		}
		else {
			if (lastUsed[0][index] < lastUsed[0][index+256]){
				caches[3][index] = address;
				lastUsed[0][index] = count;
			}
			else {
				caches[3][index+256] = address;
				lastUsed[0][index+256] = count;
			}
		}
		
		//Cache 5 2KB, 4-Way Associative, 1-Word Blocks
		address = ogAddress;
		index = (address >> 2) % 128;
		
		if (caches[4][index] == address) {
			hits[4]++;
			lastUsed[1][index] = count;	
		}
		else if (caches[4][index+128] == address) {
			hits[4]++;
			lastUsed[1][index+128] = count;
		}
		else if (caches[4][index+256] == address) {
			hits[4]++;
			lastUsed[1][index+256] = count;
		}
		else if (caches[4][index+256+128] == address) {
			hits[4]++;
			lastUsed[1][index+256+128] = count;
		}
		else {
			if (lastUsed[1][index] == 0 
			 || (lastUsed[1][index] < lastUsed[1][index+128]
			 && lastUsed[1][index] < lastUsed[1][index+256]
			 && lastUsed[1][index] < lastUsed[1][index+256+128])) {
				caches[4][index] = address;
				lastUsed[1][index] = count;
			}
			else if (lastUsed[1][index+128] == 0 
			 || (lastUsed[1][index+128] < lastUsed[1][index]
			 && lastUsed[1][index+128] < lastUsed[1][index+256]
			 && lastUsed[1][index+128] < lastUsed[1][index+256+128])) {
				caches[4][index+128] = address;
				lastUsed[1][index+128] = count;
			}
			else if (lastUsed[1][index+256] == 0 
			 || (lastUsed[1][index+256] < lastUsed[1][index+128]
			 && lastUsed[1][index+256] < lastUsed[1][index]
			 && lastUsed[1][index+256] < lastUsed[1][index+256+128])) {
				caches[4][index+256] = address;
				lastUsed[1][index+256] = count;
			}
			else {
				caches[4][index+256+128] = address;
				lastUsed[1][index+256+128] = count;
			}
		}
		
		//Cache 6 2KB, 4-Way Associative, 4-Word Blocks
		address = ogAddress;
		index = (address >> 2) % 128;
		address = address - (4 * (index % 4));
		index = index - (index % 4);
		
		if (caches[5][index] == address) {
			hits[5]++;
			lastUsed[2][index] = count;	
		}
		else if (caches[5][index+128] == address) {
			hits[5]++;
			lastUsed[2][index+128] = count;
		}
		else if (caches[5][index+256] == address) {
			hits[5]++;
			lastUsed[2][index+256] = count;
		}
		else if (caches[5][index+256+128] == address) {
			hits[5]++;
			lastUsed[2][index+256+128] = count;
		}
		else {
			if (lastUsed[2][index] == 0 
			 || (lastUsed[2][index] < lastUsed[2][index+128]
			 && lastUsed[2][index] < lastUsed[2][index+256]
			 && lastUsed[2][index] < lastUsed[2][index+256+128])) {
				caches[5][index] = address;
				caches[5][index+1] = address + 4;
				caches[5][index+2] = address + 8;
				caches[5][index+3] = address + 12;
				lastUsed[2][index] = count;
			}
			else if (lastUsed[2][index+128] == 0 
			 || (lastUsed[2][index+128] < lastUsed[2][index]
			 && lastUsed[2][index+128] < lastUsed[2][index+256]
			 && lastUsed[2][index+128] < lastUsed[2][index+256+128])) {
				caches[5][index+128] = address;
				caches[5][index+129] = address + 4;
				caches[5][index+130] = address + 8;
				caches[5][index+131] = address + 12;
				lastUsed[2][index+128] = count;
			}
			else if (lastUsed[2][index+256] == 0 
			 || (lastUsed[2][index+256] < lastUsed[2][index+128]
			 && lastUsed[2][index+256] < lastUsed[2][index]
			 && lastUsed[2][index+256] < lastUsed[2][index+256+128])) {
				caches[5][index+256] = address;
				caches[5][index+257] = address + 4;
				caches[5][index+258] = address + 8;
				caches[5][index+259] = address + 12;
				lastUsed[2][index+256] = count;
			}
			else {
				caches[5][index+256+128] = address;
				caches[5][index+385] = address + 4;
				caches[5][index+386] = address + 8;
				caches[5][index+387] = address + 12;
				lastUsed[2][index+256+128] = count;
			}
		}
		
		//Cache 7
		address = ogAddress;
		index = (address >> 2) % 1024;
		
		if (cache7[index] == address)
			hits[6]++;
		else {
			cache7[index] = address;
		}
		
		
	}
}
