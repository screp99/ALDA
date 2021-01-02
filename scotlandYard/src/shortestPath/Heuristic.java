/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shortestPath;

/**
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public interface Heuristic<V> {
	/**
	 * Schaetzt die Kosten (Distanz) von u nach v ab.
	 * @param u
	 * @param v
	 * @return Geschaetzte Kosten.
	 */
	double estimatedCost(V u, V v);
}
