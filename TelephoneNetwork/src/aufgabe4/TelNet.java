package aufgabe4;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

public class TelNet {

	private int lbg;
	private List<TelVerbindung> minimalSpanningTree;
	private HashMap<TelKnoten, Integer> knoten;
	private int count = 0;

	public TelNet(int lbg) {
		this.lbg = lbg;
		this.minimalSpanningTree = new LinkedList<>();
		this.knoten = new HashMap<>();
	}

	public boolean addTelKnoten(int x, int y) {
		TelKnoten knoten = new TelKnoten(x, y);
		if (this.knoten.containsKey(knoten))
			return false;
		this.knoten.put(knoten, count++);
		return true;
	}

	public boolean computeOptTelNet() throws IllegalStateException {
		UnionFind forest = new UnionFind(count);
		PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(count, Comparator.comparing(x -> x.c));
		List<TelVerbindung> minSpanTree = new LinkedList<>();
		// fülle Proritätsliste
		for (var v : knoten.entrySet()) {
			for (var w : knoten.entrySet()) {
				if (v.equals(w))
					continue;

				int cost = Math.abs(v.getKey().x - w.getKey().x) + Math.abs(v.getKey().y - w.getKey().y);
				if (cost <= this.lbg)
					edges.add(new TelVerbindung(v.getKey(), w.getKey(), cost));
			}
		}
		// Solange der Wald noch mehr als ein Baum enthält und es noch
		// Kanten gibt
		while (forest.size() != 1 && !edges.isEmpty()) {
			// Wähle Kante mit kleinstem Gewicht die 2 Bäume aus dem Wald verbindet
			TelVerbindung currentVerbindung = edges.poll();
			// finde den baum der u resp. v enthält
			int t1 = forest.find​(knoten.get(currentVerbindung.u));
			int t2 = forest.find​(knoten.get(currentVerbindung.v));
			if (t1 != t2) {
				// Vereinige die beiden Bäume zu einem Baum
				forest.union​(t1, t2);
				minSpanTree.add(currentVerbindung);
			}
		}
		if (edges.isEmpty() && forest.size() != 1) {
			return false;
		} else {
			this.minimalSpanningTree = minSpanTree;
			return true;
		}

	}

	public List<TelVerbindung> getOptTelNet() {
		return this.minimalSpanningTree;
	}

	public int getOptTelNetKosten() throws IllegalStateException {
		int totalCost = 0;
		for (var e : this.minimalSpanningTree) {
			totalCost += e.c;
		}
		return totalCost;
	}

	public void drawOptTelNet(int xMax, int yMax) throws IllegalStateException {
		StdDraw.setCanvasSize(500, 500);
		StdDraw.setXscale(0, xMax + 1);
		StdDraw.setYscale(0, yMax + 1);

		for (var e : this.minimalSpanningTree) {
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.line(e.u.x, e.u.y, e.v.x, e.u.y);
			StdDraw.line(e.v.x, e.v.y, e.v.x, e.u.y);
			StdDraw.setPenColor(Color.BLUE);
			StdDraw.filledSquare(e.u.x, e.u.y, 0.5);
			StdDraw.filledSquare(e.v.x, e.v.y, 0.5);
		}
		StdDraw.show(0);
	}

	public void generateRandomTelNet(int n, int xMax, int yMax) {
		int i = 0;
		while (i < n) {
			if (addTelKnoten(ThreadLocalRandom.current().nextInt(0, xMax + 1),
					ThreadLocalRandom.current().nextInt(0, yMax + 1)))
				i++;
		}
	}

	public int size() {
		return this.knoten.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TelNet: {\n");
		sb.append("\tLeitungsbegrenzungswert:" + lbg + "\n");
		sb.append("\tVerbindungen:\n");
		for (TelVerbindung n : this.minimalSpanningTree) {
			sb.append("\t\t").append(n.toString()).append("\n");
		}
		sb.append("}");
		return sb.toString();
	}

	public static void main(String args[]) {
		test1();
//		test2();
	}

	private static void test1() {
		TelNet telNet = new TelNet(7);
		telNet.addTelKnoten(1, 1);
		telNet.addTelKnoten(3, 1);
		telNet.addTelKnoten(4, 2);
		telNet.addTelKnoten(3, 4);
		telNet.addTelKnoten(7, 5);
		telNet.addTelKnoten(2, 6);
		telNet.addTelKnoten(4, 7);
		telNet.computeOptTelNet();
		System.out.println("Kosten: " + telNet.getOptTelNetKosten());
		System.out.print(telNet.toString());
		telNet.drawOptTelNet(7, 7);
	}

	private static void test2() {
		TelNet telNet = new TelNet(100);
		telNet.generateRandomTelNet(1000, 1000, 1000);
		telNet.computeOptTelNet();
		System.out.println("Kosten: " + telNet.getOptTelNetKosten());
		System.out.print(telNet.toString());
		telNet.drawOptTelNet(1000, 1000);
	}

}
