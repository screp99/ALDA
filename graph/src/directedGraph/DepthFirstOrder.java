// O. Bittel;
// 22.02.2017
package directedGraph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Klasse f�r Tiefensuche.
 *
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class DepthFirstOrder<V> {

	private final List<V> preOrder = new LinkedList<>();
	private final List<V> postOrder = new LinkedList<>();
	private final DirectedGraph<V> myGraph;
	private int numberOfDFTrees = 0;

	/**
	 * F�hrt eine Tiefensuche f�r g durch.
	 *
	 * @param g gerichteter Graph.
	 */
	public DepthFirstOrder(DirectedGraph<V> g) {
		myGraph = g;
		visitDF(g);
	}

	void visitDF(DirectedGraph<V> g) {
		Set<V> visited = new TreeSet<>();
		for (V node : g.getVertexSet()) {
			if (!visited.contains(node)) {
				visitDF(node, g, visited);
				numberOfDFTrees++;
			}
		}
	}

	void visitDF(V v, DirectedGraph<V> g, Set<V> visited) {
		visited.add(v);
		preOrder.add(v);
		for (V succ : g.getSuccessorVertexSet(v)) {
			if (!visited.contains(succ)) { // w noch nicht besucht
				visitDF(succ, g, visited);
			}
		}
		postOrder.add(v);
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
	 * Pre-Order-Reihenfolge zur�ck.
	 *
	 * @return Pre-Order-Reihenfolge der Tiefensuche.
	 */
	public List<V> preOrder() {
		return Collections.unmodifiableList(preOrder);
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
	 * Post-Order-Reihenfolge zur�ck.
	 *
	 * @return Post-Order-Reihenfolge der Tiefensuche.
	 */
	public List<V> postOrder() {
		return Collections.unmodifiableList(postOrder);
	}

	/**
	 *
	 * @return Anzahl der B�ume des Tiefensuchwalds.
	 */
	public int numberOfDFTrees() {
		return numberOfDFTrees;
	}

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 5);
		g.addEdge(5, 1);
		g.addEdge(2, 6);
		g.addEdge(3, 7);
		g.addEdge(4, 3);
		g.addEdge(4, 6);
		// g.addEdge(7,3);
		g.addEdge(7, 4);

		DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
		System.out.println(dfs.numberOfDFTrees()); // 2
		System.out.println(dfs.preOrder()); // [1, 2, 5, 6, 3, 7, 4]
		System.out.println(dfs.postOrder()); // [5, 6, 2, 1, 4, 7, 3]

	}
}
