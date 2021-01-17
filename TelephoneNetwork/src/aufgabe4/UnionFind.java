package aufgabe4;

import java.util.Arrays;

public class UnionFind {

	private int size;
	private int[] parents;

	public UnionFind(int n) {
		this.parents = new int[n];
		for (int i = 0; i < this.parents.length; i++)
			this.parents[i] = -1; // no parent
		this.size = n;
	}

	public int find​(int e) {
		while (parents[e] >= 0) // e ist keine Wurzel
			e = parents[e];
		return e;
	}

	public void union​(int s1, int s2) {
		if (parents[s1] >= 0 || parents[s2] >= 0)
			return;
		if (s1 == s2)
			return;
		if (-parents[s1] < -parents[s2]) // Höhe von s1 < Höhe von s2
			parents[s1] = s2;
		else {
			if (-parents[s1] == -parents[s2])
				parents[s1]--; // Höhe von s1 erhöht sich um 1
			parents[s2] = s1;
		}
		size--;
	}

	public int size() {
		return size;
	}

	public static void main(String args[]) {
		UnionFind uf = new UnionFind(20);
		uf.union​(2, 1);
		uf.union​(2, 2);
		uf.union​(2, 3);
		System.out.println(uf.find​(1) + " contains 1");
		System.out.println(uf.find​(2) + " contains 2");
		System.out.println(uf.find​(3) + " contains 3");
		System.out.println("Number of partitions: " + uf.size()); // size = 17
		System.out.println(Arrays.toString(uf.parents));
		System.out.println();

		uf.union​(18, 8);
		uf.union​(18, 9);
		uf.union​(18, 10);
		uf.union​(18, 11);
		System.out.println(uf.find​(8) + " contains 8");
		System.out.println(uf.find​(9) + " contains 9");
		System.out.println(uf.find​(10) + " contains 10");
		System.out.println(uf.find​(11) + " contains 11");
		System.out.println("Number of partitions: " + uf.size()); // size = 13
		System.out.println(Arrays.toString(uf.parents));
	}
}
