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
	//Already stored thingies 
	//then if contains also error
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
	

	public ArrayList<String> getLines(){
		return this.alllines;
	}
	
	
	public boolean checkCage(int[] cages, int dimension) {
		UnionFind union = new UnionFind(dimension * dimension);
		Arrays.sort(cages);
		
		for(int i = 0; i< cages.length;i++) {
				for(int j = 0; j < cages.length;j++) {
					if(Grid.adjacentCells(cages[i], dimension).contains(cages[j]) && i != j) {
						union.union(cages[i], cages[j]);
					}
				}
		}
		
		for(int i = 0; i< cages.length;i++) {
			for(int j = 0; j < cages.length;j++) {
				if(union.isConnected(cages[i], cages[j]) != true) {
					return false;
				}
			}
		}
		
		return true;
	}
	

}


