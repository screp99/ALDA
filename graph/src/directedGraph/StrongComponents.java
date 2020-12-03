// O. Bittel;
// 05-09-2018

package directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Klasse für Bestimmung aller strengen Komponenten. Kosaraju-Sharir
 * Algorithmus.
 * 
 * @author Oliver Bittel
 * @since 02.03.2020
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert fuer jede Komponente die zughÃ¶rigen Knoten.
	// Die Komponenten sind numeriert: 0, 1, 2, ...
	// Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
	// Component 0: 5, 6, 7,
	// Component 1: 8,
	// Component 2: 1, 2, 3,
	// Component 3: 4,

	private final Map<Integer, Set<V>> comp = new TreeMap<>();
	private int componentId = 0;

	/**
	 * Ermittelt alle strengen Komponenten mit dem Kosaraju-Sharir Algorithmus.
	 * 
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		visitDF(g.invert(), getReversedPostOrder(g));
	}

	void visitDF(DirectedGraph<V> invertedGraph, List<V> invertedPostOrderList) {
		Set<V> visited = new TreeSet<>();
		for (V v : invertedPostOrderList) {
			if (!visited.contains(v)) {
				this.comp.put(componentId, new TreeSet<V>());
				visitDF(v, invertedGraph, visited);
				componentId++;
			}
		}
	}

	private void visitDF(V v, DirectedGraph<V> g, Set<V> visited) {
		visited.add(v);
		this.comp.get(componentId).add(v);

		for (var w : g.getSuccessorVertexSet(v)) {
			if (!visited.contains(w))
				visitDF(w, g, visited);
		}
	}

	private List<V> getReversedPostOrder(DirectedGraph<V> g) {
		// get depth first post order of graph
		DepthFirstOrder<V> dfo = new DepthFirstOrder<>(g);
		List<V> list = new LinkedList<V>(dfo.postOrder());
		// invert post order
		Collections.reverse(list);
		return list;
	}

	/**
	 * 
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return comp.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var entry : this.comp.entrySet()) {
			sb.append("Component ").append(entry.getKey()).append(": ");
			for (var v : entry.getValue()) {
				sb.append(v).append(", ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Liest einen gerichteten Graphen von einer Datei ein.
	 * 
	 * @param fn Dateiname.
	 * @return gerichteter Graph.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		Scanner sc = new Scanner(fn);
		sc.nextLine();
		sc.nextLine();
		while (sc.hasNextInt()) {
			int v = sc.nextInt();
			int w = sc.nextInt();
			g.addEdge(v, w);
		}
		return g;
	}

	private static void test1() {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 1);
		g.addEdge(2, 3);
		g.addEdge(3, 1);

		g.addEdge(1, 4);
		g.addEdge(5, 4);

		g.addEdge(5, 7);
		g.addEdge(6, 5);
		g.addEdge(7, 6);

		g.addEdge(7, 8);
		g.addEdge(8, 2);

		StrongComponents<Integer> sc = new StrongComponents<>(g);

		System.out.println(sc.numberOfComp()); // 4

		System.out.println(sc);
		// Component 0: 5, 6, 7,
		// Component 1: 8,
		// Component 2: 1, 2, 3,
		// Component 3: 4,
	}

	private static void test2() throws FileNotFoundException {
		DirectedGraph<Integer> g = readDirectedGraph(new File("src/resources/mediumDG.txt"));
		System.out.println(g.getNumberOfVertexes());
		System.out.println(g.getNumberOfEdges());
		System.out.println(g);

		System.out.println("");

		StrongComponents<Integer> sc = new StrongComponents<>(g);
		System.out.println(sc.numberOfComp()); // 10
		System.out.println(sc);

	}

	public static void main(String[] args) throws FileNotFoundException {
		test1();
		test2();
	}
}
