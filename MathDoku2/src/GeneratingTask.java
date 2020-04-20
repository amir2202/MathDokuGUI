import javafx.concurrent.Task;

public class GeneratingTask extends Task{
	private int dimension;
	private int difficulty;
	private boolean unique;
	public GeneratingTask(int dimension, int difficulty, boolean unique) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.unique = unique;
	}
	protected Object call() throws Exception {
		// TODO Auto-generated method stub
		Generator gen = new Generator();
		Grid generated = gen.generate(this.dimension,this.difficulty,this.unique);
		return generated;
	}

}
