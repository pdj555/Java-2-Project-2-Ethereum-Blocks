import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class Blocks implements Comparable<Blocks> {
	private int number;
	private String miner;
	private long timestamp;
	private int transactions;
	private static ArrayList<Blocks> blocks = new ArrayList<Blocks>();
	
	
	/**
	 * This constructs a Blocks object without initiating the number 
	 * and miner variables.
	 */
	public Blocks() {}
	
	
	/**
	 * This constructs a Blocks object. It initiates the number variable but
	 * does not initiate the miner variable
	 * @param number The number that identifies which block we are referring to
	 */
	public Blocks(int number) {
		this.number = number;
	}
	
	
	/**
	 * This constructs a Blocks object and initiates the variables
	 * number and miner. 
	 * @param number The number that identifies which block we are referring to
	 * @param miner The address of the block
	 */
	public Blocks(int number, String miner) {
		this.number = number;
		this.miner = miner;
	}
	
	
	/**
	 * This constructs a Block object and initiates the variables number, miner, timestamp, and transactions.
	 * @param number The number that identifies which block we are referring to
	 * @param miner The address of the block 
	 * @param timestamp The time that the block was added to the chain
	 * @param transactions The number that identifies the specific transaction we are referring to
	 */
	public Blocks(int number, String miner, long timestamp, int transactions) {
		this.number = number;
		this.miner = miner;
		this.timestamp = timestamp;
		this.transactions = transactions;
	}
	
	
	/**
	 * Returns the number associated with a block object.
	 * @return The number used to identify a block
	 */
	public int getNumber() {
		return number;
	}
	
	
	/**
	 * Returns the miner associated with a block.
	 * @return The miner address
	 */
	public String getMiner() {
		return miner;
	}
	
	
	/**
	 * Returns the transaction number associated with a block.
	 * @return The transaction number
	 */
	public int getTransactions() {
		return transactions;
	}
	
	
	/**
	 * Returns the data the block was added to the blockchain. The date is converted from 
	 * unix time to the desired format.
	 * @return The date block was added to chain.
	 */
	public String getDate() {
		Date unix = new Date(timestamp * 1000);
		SimpleDateFormat format = new SimpleDateFormat("EE, dd MMMM yyyy HH:mm:ss z");
		TimeZone cst = TimeZone.getTimeZone("CST");
		format.setTimeZone(cst);
		String date = format.format(unix);
		return date;
	}
	
	
	/**
	 * Creates and returns a copy of the ArrayList blocks
	 * @return A copy of blocks ArrayList
	 */
	public static ArrayList<Blocks> getBlocks() {
		ArrayList<Blocks> copy = new ArrayList<Blocks>();
		
		for (int i = 0; i < blocks.size(); ++i) {
			copy.add(blocks.get(i));
		}
		
		return copy;
	}
	
	
	/**
	 * Counts the number of unique miner addresses and prints the frequency that
	 * that each miner address occurs in the data file.
	 */
	public static void calUniqMiners() {
		ArrayList<String> uMiners = new ArrayList<String>();
		ArrayList<Integer> count = new ArrayList<Integer>();
		String miner;
		
		for (int i = 0; i < blocks.size(); ++i) {
			miner = blocks.get(i).getMiner();
			
			// If miner address is in the uMiners list than it has already been read in
			
			// Correlate the index of the miner address in uMiners to the frequency of 
			// that address in count 
			
			if (uMiners.contains(miner)) {
				count.set(uMiners.indexOf(miner), count.get(uMiners.indexOf(miner)) + 1);
			}
			else {
				uMiners.add(miner);
				count.add(1);
			}
		}
		
		System.out.println("Number of unique Miners: " + uMiners.size());
		System.out.println();
		System.out.println("Each unique Miner and its frequency:");
		
		for (int i = 0; i < uMiners.size(); ++i) {
			System.out.println("Miner Address: " + uMiners.get(i));
			System.out.println("Miner Frequency: " + count.get(i));
			System.out.println();
			
		}
		
	}
	
	
	/**
	 * Returns the difference between two block numbers.
	 * @param A Block A
	 * @param B Block B
	 * @return The int value of the difference between Block A's number and Block B's number
	 */
	public static int blockDiff(Blocks A, Blocks B) {
		return A.number - B.number;
	}
	
	
	/**
	 * Returns the block associated with the number used as a parameter.
	 * @param num Number of a block
	 * @return The block with the parameter number
	 */
	public static Blocks getBlockByNumber(int num) {
		if (blocks != null) {		
			for (int i = 0; i < blocks.size(); i++) {
				if (blocks.get(i).getNumber() == num) {
					return blocks.get(i);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Outputs the information stored on a block.
	 */
	public String toString() {
		if (getMiner() == null && getNumber() == 0) {
			return "Empty Block";
		}
		else if (getMiner() == null && getNumber() != 0) {
			return "Block Number: " + getNumber();
		}
		else {
			return "Block Number: " + getNumber() + " Miner Address: " + getMiner();
		}
	}
	
	
	/**
	 * Reads and stores the 1st, 10th, 17th, and 18th columns of a file. 
	 * @param filename File that you want to be read
	 * @throws NumberFormatException 
	 * @throws IOException
	 */
	public static void readFile(String filename) throws NumberFormatException, IOException {
		blocks.clear();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] parsedLine = line.split(",");
			Blocks nB = new Blocks(Integer.parseInt(parsedLine[0]), parsedLine[9], 
					Long.parseLong(parsedLine[16]), Integer.parseInt(parsedLine[17]));
			blocks.add(nB);
		}
		br.close();
	}
	
	
	/**
	 * This sorts the Blocks by their Block number. 
	 */
	public static void sortBlocksByNumber() {
		Collections.sort(blocks);
	}
	
	
	/**
	 * This prints out the difference between the timestamps of the two blocks.
	 * @param first Represents one of the Blocks
	 * @param second Represents the other Block
	 */
	public static void timeDiff(Blocks first, Blocks second) {
		long largeTime = 0, smallTime = 0;
		int diff = 0;
		int hr = 0, min = 0, sec = 0, remainder = 0;
		String hrStr = "", minStr = "", secStr = "";
		
		
		
		if (first == null || second == null) {
			System.out.println("A given Block is null.");
		}
		else {
			if (first.timestamp > second.timestamp) {
				largeTime = first.timestamp;
				smallTime = second.timestamp;
			}
			else {
				largeTime = second.timestamp;
				smallTime = first.timestamp;
			}
			
			diff = (int) (largeTime - smallTime);
			
			hr = diff / 3600;
			remainder = diff % 3600;
			min = remainder / 60;
			sec = remainder % 60;
			
			if (hr == 1) {
				hrStr = " hour, ";
			}
			else {
				hrStr = " hours, ";
			}
			
			if (min == 1) {
				minStr = " minute, and ";
			}
			else {
				minStr = " minutes, and ";
			}
			
			if (sec == 1) {
				secStr = " second.";
			}
			else {
				secStr = " seconds.";
			}
			
			System.out.println("The difference in time between Block " + first.getNumber() + " and Block " + second.getNumber() +
						" is " + hr + hrStr + min + minStr + sec + secStr);	
		}
	}
	
	
	/**
	 * This returns the difference in transaction number between the two blocks given.
	 * @param first One of the blocks being compared. This will be printed first in the print statement.
	 * @param second The other block being compared. This will be printed second in the print statement.
	 */
	public static int transactionDiff(Blocks first, Blocks second) {
		int sum = 0;
		
		if (first == null || second == null) {
			return -1;
		}
		else if (first.getNumber() == second.getNumber()) {
			return -1;
		}
		else if (first.getNumber() > second.getNumber()) {
			return -1;
		}
		
		sortBlocksByNumber();
		
		for (int i = blocks.indexOf(first) + 1; i < blocks.indexOf(second); ++i) {
			sum += blocks.get(i).getTransactions();
		}
		
		return sum;
	}
	
	/**
	 * This compares two blocks. Returns a positive number if this.block is larger than the parameter block.
	 * Returns a negative number if this.block is smaller than the parameter block. Returns a 0 if the two blocks
	 * are equal. 
	 */
	@Override
	public int compareTo(Blocks otherBlock) {
		return Integer.compare(number, otherBlock.getNumber());
	}
}

