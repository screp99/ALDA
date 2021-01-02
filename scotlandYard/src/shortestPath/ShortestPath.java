// O. Bittel;
// 28.02.2019

package shortestPath;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import directedGraph.DirectedGraph;
import sim.SYSimulation;

/**
 * Kuerzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * 
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {

	private static final double HEURISTIC_FACTOR = 1.0 / 30.0;

	SYSimulation sim = null;
	Map<V, Double> dist; // Distanz fuer jeden Knoten
	Map<V, V> pred; // Vorgaenger fuer jeden Knoten
	Heuristic<V> heuristic;
	DirectedGraph<V> graph;
	Boolean isDijkstra = false;
	V start;
	V goal;

	/**
	 * Konstruiert ein Objekt, das im Graph g kuerzeste Wege nach dem A*-Verfahren
	 * berechnen kann. Die Heuristik h schaetzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewaehlt, dann ist das Verfahren identisch mit dem
	 * Dijkstra-Verfahren.
	 * 
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kuerzeste Wege nach dem
	 *          Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		this.dist = new TreeMap<V, Double>();
		this.pred = new TreeMap<V, V>();
		this.graph = g;
		this.heuristic = h;
		if (heuristic == null) {
			this.isDijkstra = true;
		}
	}

	/**
	 * Diese Methode sollte nur verwendet werden, wenn kuerzeste Wege in
	 * Scotland-Yard-Plan gesucht werden. Es ist dann ein Objekt fuer die
	 * Scotland-Yard-Simulation zu uebergeben.
	 * <p>
	 * Ein typische Aufruf fuer ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * if (sim != null)
	 * 	sim.visitStation((Integer) v, Color.blue);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kuerzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als naechstes aus der Kandidatenliste besucht wird, animiert.
	 * 
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		this.start = s;
		this.goal = g;

		// PriorityQueue<V> kl = new PriorityQueue<V>(); // leere Kandidatenliste
		LinkedList<V> kl = new LinkedList<>();
		for (V node : graph.getVertexSet()) {
			this.dist.put(node, Double.MAX_VALUE);
			this.pred.put(node, null);
		}
		this.dist.put(s, 0.0); // Startknoten
		kl.add(s);
		while (!kl.isEmpty()) {
			V currentNode = null;
			double minDistance = Double.MAX_VALUE;
			// lösche Knoten v aus kl mit minimaler Distanz;
			for (V node : kl) {

				double distance;
				if (isDijkstra)
					distance = dist.get(node);
				else
					distance = dist.get(node) + heuristic.estimatedCost(node, g) * HEURISTIC_FACTOR;

				if (distance < minDistance) {
					currentNode = node;
					minDistance = distance;
				}
			}
			kl.remove(currentNode);

			System.out.println(
					String.format("Besuche Knoten %d mit d = %f", (Integer) currentNode, dist.get(currentNode)));
			// Station durch einen farbigen Kreis auf der Karte kennzeichnen
			if (sim != null)
				sim.visitStation((Integer) currentNode, Color.blue);

			// Zielknoten erreicht
			if (!isDijkstra && currentNode.equals(g)) {
				return;
			}

			for (V successor : graph.getSuccessorVertexSet(currentNode)) {
				if (dist.get(successor).equals(Double.MAX_VALUE)) // w noch nicht besucht und nicht in Kandidatenliste
					kl.add(successor);
				if (dist.get(currentNode) + graph.getWeight(currentNode, successor) < dist.get(successor)) {
					pred.put(successor, currentNode);
					dist.put(successor, dist.get(currentNode) + graph.getWeight(currentNode, successor));
				}
			}
		}
	}

	/**
	 * Liefert einen kuerzesten Weg von Startknoten s nach Zielknoten g. Setzt eine
	 * erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * 
	 * @throws IllegalArgumentException falls kein kuerzester Weg berechnet wurde.
	 * @return kuerzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		if (goal == null) {
			throw new IllegalArgumentException("Es wurde noch kein kürzester Weg berechnet");
		}
		LinkedList<V> path = new LinkedList<>();
		path.add(goal);
		V currentNode = pred.get(goal);
		while (currentNode != start) {
			path.add(currentNode);
			currentNode = pred.get(currentNode);
		}
		path.add(start);
		Collections.reverse(path);
		return path;
	}

	/**
	 * Liefert die Laenge eines kuerzesten Weges von Startknoten s nach Zielknoten g
	 * zurueck. Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * 
	 * @throws IllegalArgumentException falls kein kuerzester Weg berechnet wurde.
	 * @return Laenge eines kuerzesten Weges.
	 */
	public double getDistance() {
		if (goal == null) {
			throw new IllegalArgumentException("Es wurde noch kein kürzester Weg berechnet");
		}
		return this.dist.get(goal);
	}

}
