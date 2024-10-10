package sol;

import src.IDijkstra;
import src.IEdge;
import src.IGraph;
import src.IVertex;

import java.util.*;
import java.util.function.Function;

/**
 * Implementation for Dijkstra's algorithm
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
public class Dijkstra<V extends IVertex<E>, E extends IEdge<V>> implements IDijkstra<V, E> {
    HashMap<V, Double> dist; // toCheckPQ
    HashMap<V, E> cameFrom;

    /**
     *Constructor for Dijkstra
     */
    public Dijkstra() {
        this.dist = new HashMap<>();
        this.cameFrom = new HashMap<>();
    }

    /**
     * Finds the lowest cost path from source to destination.
     *
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight  the weight of an edge
     * @return a list of edges from source to destination
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        this.getshortestPathhelper(graph, source, edgeWeight);
        List<E> path = new LinkedList<>();
        V dest = destination;
        E oldcity = this.cameFrom.get(dest);
        while (oldcity != null) {
            path.add(0, oldcity);
            dest = graph.getEdgeSource(oldcity);
            oldcity = this.cameFrom.get(dest);
        }
        return path;
    }
    /**
     * This helper function updates the hashmaps came from and uses a priority queue to check neighbors
     *
     * @param graph- the graph
     * @param source- the start
     *@param edgeWeight- the values to compare
     */
    public void getshortestPathhelper(IGraph<V, E> graph, V source,
                                      Function<E, Double> edgeWeight) {
        Comparator<V> weight = (edg1, edg2) -> { //checks why I understand as weight size
            return Double.compare(this.dist.get(edg1), this.dist.get(edg2));
        };
        PriorityQueue<V> priorityQueue = new PriorityQueue<>(weight);
        for (V vertex : graph.getVertices()) {
            this.dist.put(vertex, Double.POSITIVE_INFINITY);//every vertex should be infinity
            this.cameFrom.put(vertex, null);
        }
        this.dist.put(source, 0.0); //changes source to 0
        priorityQueue.addAll(graph.getVertices()); //adds all the vertices
        while (!priorityQueue.isEmpty()) {
            V checkingCity = priorityQueue.poll(); //removes the first element at the queue
            for (E outgoing : graph.getOutgoingEdges(checkingCity)) { //for outgoing edges, we want to check the edges of the source that was removed
                V neighbor = graph.getEdgeTarget(outgoing); //makes the neighbour from the list of outgoing edges
                double newDistance = this.dist.get(checkingCity) + edgeWeight.apply(outgoing); //replaces the E values of thisv to outgoing which is first in the set
                if (newDistance < this.dist.get(neighbor)) { // checks which distance is shorter
                    this.dist.put(neighbor, newDistance); // updates the toCheckPQ with the shortest distance
                    this.cameFrom.put(neighbor, outgoing); // updates the path Hashmap
                    priorityQueue.remove(neighbor);
                    priorityQueue.add(neighbor);
                }
            }
        }
    }
}

