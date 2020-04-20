import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;

public class SolvingTask2 extends Task {
	private String[] gridconfig;
	private int dimension; 
	public SolvingTask2(String[] config, int dimension) {
		this.gridconfig = config;
		this.dimension = dimension;
	}
	
	protected Object call() throws Exception {
			HashMap values = new HashMap<Integer,Integer>();
			ThreadSolve2 solver = new ThreadSolve2();
			Grid temp = new Grid(this.dimension);
			for(String cage:this.gridconfig) {
				String[] split = cage.split(" ");
				String label = split[0];
				String[] argum = split[1].split(",");
				if(argum.length == 1) {
					temp.setCage(label, Integer.valueOf(split[1]));
				}
				else {
					int[] arguments = new int[argum.length];
					for(int i = 0;i < arguments.length;i++) {
						arguments[i] = Integer.valueOf(argum[i]);
					}
					temp.setCage(label, arguments);
				}
			}
			solver.solve(temp);
			for(int position = 1; position <= temp.getDimensions() * temp.getDimensions(); position++) {
				values.put(position, temp.getCell(position).getNumber());
			}
			
			ArrayList<Integer[]> solutions = solver.getSolutions();
			
			return solutions;
	}
	
	public HashMap<Integer,Integer> solveNoThread() {
		HashMap values = new HashMap<Integer,Integer>();
		ThreadSolve solver = new ThreadSolve();
		Grid temp = new Grid(this.dimension);
		for(String cage:this.gridconfig) {
			String[] split = cage.split(" ");
			String label = split[0];
			String[] argum = split[1].split(",");
			if(argum.length == 1) {
				temp.setCage(label, Integer.valueOf(split[1]));
			}
			else {
				int[] arguments = new int[argum.length];
				for(int i = 0;i < arguments.length;i++) {
					arguments[i] = Integer.valueOf(argum[i]);
				}
				temp.setCage(label, arguments);
			}
		}
		solver.solve(temp);
		for(int position = 1; position <= temp.getDimensions() * temp.getDimensions(); position++) {
			values.put(position, temp.getCell(position).getNumber());
		}
		
		return values;
	}

}
