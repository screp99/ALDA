// O. Bittel;
// 19.03.2018

package directedGraph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap für die
 * Nachfolgerknoten und einer einer doppelten TreeMap für die Vorgängerknoten.
 * <p>
 * Beachte: V muss vom Typ Comparable sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung mit schnellem Zugriff auf die
 * Knoten.
 * 
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListDirectedGraph<V extends Comparable<?>> implements DirectedGraph<V> {

	// doppelte Map für die Nachfolgerknoten:
	private final Map<V, Map<V, Double>> successor = new TreeMap<>();

	// doppelte Map für die Vorgängerknoten:
	private final Map<V, Map<V, Double>> predecessor = new TreeMap<>();

	private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) {
		if (!containsVertex(v)) {
			predecessor.put(v, new TreeMap<V, Double>());
			successor.put(v, new TreeMap<V, Double>());
			return true;
		}
		return false;
	}

	@Override
	public boolean addEdge(V v, V w) {
		return addEdge(v, w, 1.0);
	}

	@Override
	public boolean addEdge(V v, V w, double weight) {
		addVertex(v);
		addVertex(w);
		if (containsEdge(v, w)) {
			successor.get(v).put(w, weight);
			predecessor.get(w).put(v, weight);
			return false;
		} else {
			successor.get(v).put(w, weight);
			predecessor.get(w).put(v, weight);
			this.numberEdge++;
			return true;
		}
	}

	@Override
	public boolean containsVertex(V v) {
		if (successor.containsKey(v)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsEdge(V v, V w) {
		if (containsVertex(v) && successor.get(v).containsKey(w)) {
			return true;
		}
		return false;
	}

	@Override
	public double getWeight(V v, V w) {
		try {
			return successor.get(v).get(w);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Edge does not exist!", e);
		}
	}

	@Override
	public int getInDegree(V v) {
		try {
			return predecessor.get(v).size();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Vertex does not exist!", e);
		}
	}

	@Override
	public int getOutDegree(V v) {
		try {
			return successor.get(v).size();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Vertex does not exist!", e);
		}
	}

	@Override
	public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(successor.keySet()); // nicht modifizierbare Sicht
	}

	@Override
	public Set<V> getPredecessorVertexSet(V v) {
		return Collections.unmodifiableSet(predecessor.get(v).keySet());
	}

	@Override
	public Set<V> getSuccessorVertexSet(V v) {
		return Collections.unmodifiableSet(successor.get(v).keySet());
	}

	@Override
	public int getNumberOfVertexes() {
		return successor.size();
	}

	@Override
	public int getNumberOfEdges() {
		return this.numberEdge;
	}

	@Override
	public DirectedGraph<V> invert() {
		AdjacencyListDirectedGraph<V> newGraph = new AdjacencyListDirectedGraph<>();
		for (var node : predecessor.entrySet()) {
			for (var v : node.getValue().entrySet()) {
				newGraph.addEdge(node.getKey(), v.getKey(), v.getValue());
			}
		}
		return newGraph;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var node : successor.entrySet()) {
			for (var pred : node.getValue().entrySet()) {
				sb.append(node.getKey()).append(" --> ").append(pred.getKey()).append(" weight = ")
						.append(pred.getValue()).append("\n");
			}
		}
		return sb.toString();
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
		g.addEdge(7, 4);

		System.out.println(g.getNumberOfVertexes()); // 7
		System.out.println(g.getNumberOfEdges()); // 8
		System.out.println(g.getVertexSet()); // 1, 2, ..., 7
		System.out.println(g);
		// 1 --> 2 weight = 1.0
		// 2 --> 5 weight = 1.0
		// 2 --> 6 weight = 1.0
		// 3 --> 7 weight = 1.0
		// ...

		System.out.println("");
		System.out.println(g.getOutDegree(2)); // 2
		System.out.println(g.getSuccessorVertexSet(2)); // 5, 6
		System.out.println(g.getInDegree(6)); // 2
		System.out.println(g.getPredecessorVertexSet(6)); // 2, 4

		System.out.println("");
		System.out.println(g.containsEdge(1, 2)); // true
		System.out.println(g.containsEdge(2, 1)); // false
		System.out.println(g.getWeight(1, 2)); // 1.0
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1, 2)); // 5.0

		System.out.println("");
		System.out.println(g.invert());
		// 1 --> 5 weight = 1.0
		// 2 --> 1 weight = 5.0
		// 3 --> 4 weight = 1.0
		// 4 --> 7 weight = 1.0
		// ...

		Set<Integer> s = g.getSuccessorVertexSet(2);
		System.out.println(s);
		System.out.println("RuntimeException - Cannot remove from unmodifiable view!");
		// s.remove(5); // Laufzeitfehler! Warum? -> unmodifiable view!
	}
}
