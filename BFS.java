package sol;

import src.IBFS;
import src.IEdge;
import src.IGraph;
import src.IVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation for BFS, implements IBFS interface
 * @param <V> The type of the vertices
 * @param <E> The type of the edges
 */
public class BFS<V extends IVertex<E>, E extends IEdge<V>> implements IBFS<V, E> {
    IGraph<V, E> graph;
    LinkedList<E> edges;
    HashMap<V, E> path;

    /**
     * Constructor for BFS
     */
    public BFS() {
        this.path = new HashMap<>();
    }

    /**
     * Returns whether path exists or if there is a way to reach the end
     *
     * @param start the start vertex
     * @param end   the end vertex
     * @return a boolean to be called in getPath
     */

    public boolean getPathHelper(V start, V end) {
        HashSet<E> visited = new HashSet<E>();
        LinkedList<E> toCheck = new LinkedList<E>(this.graph.getOutgoingEdges(start));

        while (!toCheck.isEmpty()) {
            E edge = toCheck.removeFirst();
            V target = this.graph.getEdgeTarget(edge);

            if (visited.contains(edge)) {
                visited.add(edge);
            }
            if (!this.path.containsKey(target)) {
                this.path.put(target, edge);
            }
            if ((target.equals(end))) {
                return true;
            } else {
                toCheck.addAll(this.graph.getOutgoingEdges(target));
            }
        }
        return false;
    }

    /**
     * Returns the path from start to end.
     *
     * @param graph the graph including the vertices
     * @param start the start vertex
     * @param end   the end vertex
     * @return a list of edges starting from the start to the end
     */

    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        // HashMap<V, E> mapPath = this.getPathHelper(graph, start, end); // cameFrom HashMap
        this.graph = graph;
        this.edges = new LinkedList<E>();

        if (this.getPathHelper(start, end)) {
            V city = end;
            while (!city.equals(start)) {
                E edge = this.path.get(city);
                V starter = this.graph.getEdgeSource(edge);
                this.edges.addFirst(edge);
                city = starter;
            }
        }
        return this.edges;
    }
}
