//Array based union find

public class UnionFind {

	private int[] id;
	
	public UnionFind(int N) {
		id = new int[N+1];
		for(int i = 0; i<=N;i++) {
			id[i] = i;
		}
	}
	
	private int root(int i) {
		while(i != id[i]) {
			i = id[i];
		}
		return i;
	}
	
	public boolean isConnected(int position, int position2){
		return root(position) == root(position2);
	}
	public void union(int p, int q) {
		int i = root(p);
		int j = root(q);
		id[i] = j;
	}
	

}
