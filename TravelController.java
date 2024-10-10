package sol;

import src.ITravelController;
import src.TransportType;
import src.TravelCSVParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation for TravelController
 */
public class TravelController implements ITravelController<City, Transport> {
    private TravelGraph graph;

    /**
     * Constructor for TravelController
     */
    public TravelController() {
    }

    /**
     * Loads CSVs into the app.
     *
     * @param citiesFile    the filename of the cities csv
     * @param transportFile the filename of the transportations csv
     * @return an informative message to be printed in the REPL
     */
    @Override
    public String load(String citiesFile, String transportFile) {
        this.graph = new TravelGraph();
        TravelCSVParser parser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null; // need explicit return null to account for Void type
        };

        Function<Map<String, String>, Void> addEdge = map -> {
            Transport transp = new Transport(this.graph.getCity(map.get("origin")),
                    this.graph.getCity(map.get("destination")),
                    TransportType.fromString(map.get("type")),
                    Double.parseDouble(map.get("price")),
                    Double.parseDouble(map.get("duration")));
            this.graph.addEdge(transp.getSource(), transp);
            return null;
        };

        try {
            parser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }
        try {
            parser.parseTransportation(transportFile, addEdge);
        } catch (IOException e) {
            return "Error parsing file: " + transportFile;
        }
        return "Successfully loaded cities and transportation files.";
    }


    /**
     * Finds the fastest route in between two cities
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Function<Transport, Double> getMin = transportEdge -> transportEdge.getMinutes();
        Dijkstra<City,Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source),this.graph.getCity(destination), getMin);
    }

    /**
     * Finds the cheapest route in between two cities
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Function<Transport, Double> getPr = transportEdge -> transportEdge.getPrice();
        Dijkstra<City,Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source),this.graph.getCity(destination), getPr);
    }

    /**
     * Finds the most direct route in between two cities
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS<City, Transport> bfs = new BFS<>();
        return bfs.getPath(this.graph,this.graph.getCity(source), this.graph.getCity(destination));
    }

    /**
     * Returns the graph stored by the controller
     *
     * NOTE: You __should not__ be using this in your implementation, this is purely meant to be used for the visualizer
     *
     * @return The TravelGraph currently stored in the TravelController
     */
    public TravelGraph getGraph() {
        return this.graph;
    }
}
