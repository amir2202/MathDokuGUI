import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileHandler {
	private BufferedReader reader;
	private ArrayList<String> alllines;
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

	public boolean parseLine(String line) {
		boolean firstpart = false;
		boolean secondpart = false;
		String[] part = line.split(" ");
		if(part.length == 0) {
			return false;
		}
		if(part[0].matches("[0-9]+[\\-+x÷·]+$") || part[0].matches("[0-9]+")) {
			firstpart = true;
		}
		//rework later
		if(part[1].split(",").length != -1){
			secondpart = true;
		}
	
		return firstpart && secondpart;
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
	
	
	
	
	//Check line for duplicates// check if cells are adjacent within cage 
	
	

class ConfigurationError extends Exception{
	public ConfigurationError(String message) {
		super(message);
	}
}
}


