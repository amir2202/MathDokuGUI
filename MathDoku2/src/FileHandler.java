import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class FileHandler {
	private BufferedReader reader;
	private ArrayList<String> alllines;
	private int FileDim;
	public FileHandler() {}
	public FileHandler(File file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
		alllines = new ArrayList<String>();
	}

	public boolean readFile() throws ConfigurationError {
		try {
			int linecount=1;
			while(reader.ready() != false) {
				String line = reader.readLine();
				boolean temp = this.parseLine(line);
				if(!temp) {
					throw new ConfigurationError("Your configuration is wrong check line " + linecount);
				}
				else {
					alllines.add(line);
					linecount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean parseLine(String line) throws ConfigurationError {
		boolean formatfirst = false;
		boolean formatsecond = false;
		boolean input = false;
		String[] part = line.split(" ");
		if(part.length == 0) {
			return false;
		}
		if(part[0].matches("[0-9]+[\\-+x÷]+$") || part[0].matches("[0-9]")) {
			formatfirst = true;
		}
		//rework later
		if(part[1].matches("^\\d+(?:[ \t]*,[ \\t]*\\d+)+$")){
			formatsecond = true;
		}
		
		if(formatfirst && formatsecond) {
			String[] arguments = part[1].split(",");
			HashSet unique = new HashSet();
			for(String number:arguments) {
				unique.add(Integer.valueOf(number));
			}
			if(arguments.length != unique.size()) {
				throw new ConfigurationError("Duplicate coordinate entry in line ");
			}
			else {
				input = true;
			}
			
		}
		return formatfirst && formatsecond &&input;
	}

	
	public int getDimension() {
		ArrayList<Integer> allarguments = new ArrayList<Integer>();
		for(String line: this.getLines()) {
			String[] split = line.split(" ");
			String[] numbers = split[1].split(",");
			for(String number: numbers) {
				allarguments.add(Integer.valueOf(number));
			}
		}
		Collections.sort(allarguments);
		this.FileDim = (int) Math.sqrt(allarguments.get(allarguments.size()-1));
		return (int) Math.sqrt(allarguments.get(allarguments.size()-1));
	}
	
	//call another method to store the values
	//get the dimensions
	
	//for every line return label, and int[] args
	//check for mistakes
	public ArrayList<String> getLines(){
		return this.alllines;
	}
	
//	public boolean validCageInputs(String line) {
		
//	}
	
	
	
	
	//Check line for if cells are adjacent within cage 
	//Its a union find problem
	//Maybe order the cordinates ? 
	//Yeah order the cordinates, check if second adjacent to first if it is, union it, then if third isnt, go next -> remember cordinates
	//
	public boolean checkCages(String[] cages, int dimension) {
		int arguments[] = new int[cages.length]; 
		for(int i = 0; i <arguments.length;i++) {
			arguments[i] = Integer.valueOf(cages[i]);
		}
		UnionFind union = new UnionFind(cages.length);
		Arrays.sort(arguments);
		for(int i = 0; i< arguments.length;i++) {
				for(int j = 0; j < arguments.length;j++) {
					if(Grid.adjacentCells(arguments[i], dimension).contains(arguments[j])) {
						union.union(i, j);
					}
				}
		}
		
		int count = 0;
		for(int i = 0; i < arguments.length;i++) {
			for(int j = 0; j < arguments.length;j++) {
				if(union.isConnected(i, j) == true) {
					count++;
				}
			}
		}
		if(count != cages.length) {
			return false;
		}
		else if(count == cages.length){
			return true;
		}
		
		//If all have one connection to eachother its valid cage
		
		return false;
	}

}


