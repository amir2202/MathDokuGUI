
public class Cage {
	private String label;
	private int[] cordinates;
	private int[] converted;
	private int target;
	public Cage(String label, int... cordinates) {
		this.label = label;
		this.cordinates = cordinates;
	}
	
	public void addCords(Integer[] cords) {
		
	}
	
	public int[] getCords() {
		return this.cordinates;
	}
	
	public String getLabel() {
		return this.label;
	}
	
}
