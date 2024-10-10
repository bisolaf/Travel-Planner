package sol;

import src.IGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation for TravelGraph
 */
public class TravelGraph implements IGraph<City, Transport> {
    HashMap<String, City> data; //this maps the name of the city to a city class

    /**
     * Constructor for TravelGraph
     */
    public TravelGraph (){
        this.data = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the vertex
     */
    @Override
    public void addVertex(City vertex) {
        this.data.put(vertex.toString(), vertex);
        //this adds the vertex to the hashmap
    }

    /**
     * Adds an edge to the graph.
     *
     * @param origin the origin of the edge.
     * @param edge edge you want to add
     */
    @Override
    public void addEdge(City origin, Transport edge) {
        System.out.println(origin.toString());
        origin.addOut(edge);
        //this adds edges to the city class
    }

    /**
     * Gets a set of vertices in the graph.
     *
     * @return the set of vertices
     */
    @Override
    public Set<City> getVertices() {
        return new HashSet<>(this.data.values());
        // this returns the city vertices
    }

    /**
     * Gets the source of an edge.
     *
     * @param edge the edge
     * @return the source of the edge
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
        //this returns the original node/parent node of the edge
    }

    /**
     * Gets the target of an edge.
     *
     * @param edge the edge
     * @return the target of the edge
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();

    }

    /**
     * Gets the outgoing edges of a vertex.
     *
     * @param fromVertex the vertex
     * @return the outgoing edges from that vertex
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    /**
     * Gets a city class from a string name
     * @param cityname - the name of city you want to get as a String
     * @return the inputted city as a City class
     */

    public City getCity(String cityname){
        if (!this.data.containsKey(cityname)){
           City  citynew = new City(cityname);
           this.addVertex(citynew);
        }
        else if (this.data.get(cityname) == null){
            throw new RuntimeException("Not Found");
        }
        return this.data.get(cityname);
    }
}