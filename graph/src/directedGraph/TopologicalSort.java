// O. Bittel;
// 22.02.2017

package directedGraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * 
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
	private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * 
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		Optional<List<V>> result = topSort(g);
		if (result.isPresent()) {
			ts = result.get();
		} else {
			System.out.println("Sorting failed. The given graph is cyclic...");
		}
	}

	private Optional<List<V>> topSort(DirectedGraph<V> g) {
		List<V> result = new LinkedList<>();
		Map<V, Integer> inDegree = new HashMap<>(); // Anz. noch nicht besuchter Vorgänger
		Queue<V> q = new LinkedList<>();
		for (V vertex : g.getVertexSet()) {
			inDegree.put(vertex, g.getInDegree(vertex));
			if (inDegree.get(vertex) == 0) {
				q.add(vertex);
			}
		}
		while (!q.isEmpty()) {
			V v = (V) q.poll();
			result.add(v);
			for (V succ : g.getSuccessorVertexSet(v)) {
				inDegree.put(succ, inDegree.get(succ) - 1);
				if (inDegree.get(succ) == 0) {
					q.add(succ);
				}
			}
		}
		if (result.size() != g.getNumberOfVertexes())
			return Optional.empty(); // Graph zyklisch
		else
			return Optional.of(result);
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück, die
	 * topologisch sortiert ist.
	 * 
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
		return Collections.unmodifiableList(ts);
	}

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		System.out.println(g);

		TopologicalSort<Integer> ts = new TopologicalSort<>(g);

		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
		}

		testWinterClothings();
		testWinterClothingsWithStrangeCondition();
	}

	private static void testWinterClothings() {
		DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
		g.addEdge("Socken", "Schuhe");
		g.addEdge("Schuhe", "Handschuhe");
		g.addEdge("Unterhose", "Hose");
		g.addEdge("Hose", "Schuhe");
		g.addEdge("Hose", "Gürtel");
		g.addEdge("Gürtel", "Mantel");
		g.addEdge("Mantel", "Schal");
		g.addEdge("Schal", "Handschuhe");
		g.addEdge("Unterhemd", "Hemd");
		g.addEdge("Hemd", "Pulli");
		g.addEdge("Pulli", "Mantel");
		g.addEdge("Muetze", "Handschuhe");

		TopologicalSort<String> ts = new TopologicalSort<>(g);
		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList());
		}

	}

	private static void testWinterClothingsWithStrangeCondition() {
		DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
		g.addEdge("Socken", "Schuhe");
		g.addEdge("Schuhe", "Handschuhe");
		g.addEdge("Unterhose", "Hose");
		g.addEdge("Hose", "Schuhe");
		g.addEdge("Hose", "Gürtel");
		g.addEdge("Gürtel", "Mantel");
		g.addEdge("Mantel", "Schal");
		g.addEdge("Schal", "Handschuhe");
		g.addEdge("Unterhemd", "Hemd");
		g.addEdge("Hemd", "Pulli");
		g.addEdge("Pulli", "Mantel");
		g.addEdge("Muetze", "Handschuhe");

		g.addEdge("Schal", "Hose");
		// g.addEdge("Hose", "Schal");

		TopologicalSort<String> ts = new TopologicalSort<>(g);
		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList());
		}
	}
}
