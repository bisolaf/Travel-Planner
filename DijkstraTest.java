package test;

import org.junit.Test;
import sol.*;
import src.IDijkstra;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly, but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 */
public class DijkstraTest {
    private static final double DELTA = 0.001;
    private SimpleGraph graph;
    private SimpleVertex a, b, c, d, e;
    private TravelController cities1Controller, cities3Controller;
    private TravelGraph graph1, graph2;
    private City boston, providence, newYork, losAngeles, sacramento, sanFransisco, malibu;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     * TODO: create more setup methods!
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    private void createGraph1() {
        this.graph1 = new TravelGraph();
        this.cities1Controller = new TravelController();


        this.boston = new City("Boston");
        this.providence = new City("Providence");
        this.newYork = new City("New York City");


        this.graph1.addVertex(this.boston);
        this.graph1.addVertex(this.providence);
        this.graph1.addVertex(this.newYork);


        this.graph1.addEdge(this.boston, new Transport(this.boston, this.providence, TransportType.TRAIN, 13, 80));
        this.graph1.addEdge(this.boston, new Transport(this.boston, this.providence, TransportType.BUS, 7, 150));
        this.graph1.addEdge(this.providence, new Transport(this.providence,this.boston, TransportType.TRAIN, 13, 80));
        this.graph1.addEdge(this.providence, new Transport(this.providence, this.boston, TransportType.BUS, 7, 150));
        this.graph1.addEdge(this.boston, new Transport(this.boston, this.newYork, TransportType.PLANE, 267, 50));
        this.graph1.addEdge(this.newYork, new Transport(this.newYork, this.boston, TransportType.PLANE, 267, 50));
        this.graph1.addEdge(this.newYork, new Transport(this.newYork, this.providence, TransportType.BUS, 40, 225));
    }

    /**
     * Creates graph for Transport 3 CSV
     */

    private void createGraph2(){
        this.graph2 = new TravelGraph();
        this.cities3Controller = new TravelController();

        this.losAngeles = new City("Los Angeles");
        this.sacramento = new City("Sacramento");
        this.sanFransisco = new City("San Fransisco");
        this.malibu = new City("Malibu");

        this.graph2.addVertex(this.losAngeles);
        this.graph2.addVertex(this.sacramento);
        this.graph2.addVertex(this.sanFransisco);
        this.graph2.addVertex(this.malibu);

        this.graph2.addEdge(this.losAngeles, new Transport(this.losAngeles, this.malibu, TransportType.BUS, 10, 80));
        this.graph2.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sanFransisco, TransportType.PLANE, 150, 120));
        this.graph2.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sanFransisco, TransportType.TRAIN, 80, 420));
        this.graph2.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sacramento, TransportType.PLANE, 200, 140));
        this.graph2.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.losAngeles, TransportType.PLANE, 150, 120));
        this.graph2.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.losAngeles, TransportType.TRAIN, 80, 420));
        this.graph2.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.sacramento, TransportType.BUS, 10, 120));
        this.graph2.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.sacramento, TransportType.TRAIN, 30, 80));
        this.graph2.addEdge(this.sacramento, new Transport(this.sacramento, this.sanFransisco, TransportType.TRAIN, 30, 80));
        this.graph2.addEdge(this.sacramento, new Transport(this.sacramento, this.sanFransisco, TransportType.BUS, 10, 120));
    }


    /**
     * A sample test that tests Dijkstra's on a simple graph. Checks that the shortest path using Dijkstra's is what we
     * expect.
     */
    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
                dijkstra.getShortestPath(this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        List<SimpleEdge> path2 = dijkstra.getShortestPath(this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path2), DELTA);
        assertEquals(3, path.size());
    }

    /**
     * Test Dijkstra and methods in Travel Controller for Cities 1 and Transport 1 CSV
     */
    @Test
    public void testGraph1() {
        this.createGraph1();
        this.cities1Controller = new TravelController();
        this.cities1Controller.load("data/cities1.csv","data/transport1.csv");
        assertEquals(3, this.graph1.getVertices().size());

        IDijkstra<City, Transport> dijkstra = new Dijkstra<>();
        Function<Transport, Double> timeEdgeWeight = e -> e.getMinutes();
        Function<Transport, Double> priceEdgeWeight = e -> e.getPrice();

        // ************* TEST DIJKSTRA DIRECTLY **********
        List<Transport> timePath =
                dijkstra.getShortestPath(this.graph1, this.boston, this.providence, timeEdgeWeight);
        assertEquals(1, timePath.size());

        // ********* TEST CITY THAT DOES NOT EXIST IN MAP ***********
        List<Transport> noCity = this.cities1Controller.cheapestRoute("New Haven", "Providence");
        assertTrue(noCity.isEmpty());

        // ******* TEST WHEN SOURCE AND DEST ARE THE SAME *********
        List<Transport> noPath =
                dijkstra.getShortestPath(this.graph1, this.providence, this.providence, priceEdgeWeight);
        assertTrue(noPath.isEmpty());

        // ********** BOS -> PVD (TEST ONE ROUTE BUT DIFFERENT TRANSPORT TYPES BASED ON EDGE WEIGHT) **********
        List<Transport> cheapestRoute =  this.cities1Controller.cheapestRoute("Boston", "Providence");
        assertEquals("Boston", cheapestRoute.get(0).getSource().toString());
        assertEquals("Providence", cheapestRoute.get(0).getTarget().toString());
        // Cheapest = BUS
        assertEquals("BUS", cheapestRoute.get(0).getType());
        List<Transport> fastestRoute = this.cities1Controller.fastestRoute("Boston", "Providence");
        // Fastest = TRAIN
        assertEquals("TRAIN", fastestRoute.get(0).getType());
        assertEquals(1, fastestRoute.size());

        //***************   NY -> PVD (TEST MULTIPLE ROUTES BASED ON EDGE WEIGHT)   **************
        // Fastest = NY -> Boston + Boston -> PVD (TRAIN)
        List<Transport> fastestRouteNY = this.cities1Controller.fastestRoute("New York City", "Providence");
        System.out.println(fastestRouteNY);
        assertEquals(2, fastestRouteNY.size());
        assertEquals("Boston", fastestRouteNY.get(0).getTarget().toString());
        assertEquals("TRAIN", fastestRouteNY.get(1).getType());

        // Cheapest = NY -> PVD (BUS)
        List<Transport> cheapestRouteNY = this.cities1Controller.cheapestRoute("New York City", "Providence");
        assertEquals(1, cheapestRouteNY.size());
        assertEquals("Providence", cheapestRouteNY.get(0).getTarget().toString());
        assertEquals("BUS", cheapestRouteNY.get(0).getType());
    }

    /**
     * Test Dijkstra and methods in TravelController for the Cities 3 and Transport 3 CSV
     */
    @Test
    public void testGraph2() {
        this.createGraph2();
        this.cities3Controller = new TravelController();
        this.cities3Controller.load("data/cities3.csv", "data/transport3.csv");

        IDijkstra<City, Transport> dijkstra = new Dijkstra<>();
        Function<Transport, Double> timeEdgeWeight = e -> e.getMinutes();
        Function<Transport, Double> priceEdgeWeight = e -> e.getPrice();


        // ************** TEST WHEN SOURCE AND DEST ARE SAME CITY *************
        List<Transport> noPath = dijkstra.getShortestPath(this.graph2, this.malibu, this.malibu, priceEdgeWeight);
        assertTrue(noPath.isEmpty());

        // **************  MALIBU -> SAN FRANSISCO (TEST FOR A ROUTE THAT DOES NOT EXIST)  *************
        List<Transport> noRoute = this.cities3Controller.cheapestRoute("Malibu", "San Fransisco");
        assertTrue(noRoute.isEmpty());

        // ****************  TEST CITY THAT DOES EXIST IN GRAPH BUT HAS NO PATHS TO ANY OTHER CITY **************
        City yosemite = new City("Yosemite");
        this.graph2.addVertex(yosemite);
        List<Transport> noRoute2 = this.cities3Controller.cheapestRoute("Yosemite", "Sacramento");
        assertTrue(noRoute2.isEmpty());

        // ***************  TEST WHEN CITY DOES NOT EXIST IN GRAPH AT ALL ******************
        List<Transport> noRoute3 = this.cities3Controller.cheapestRoute("Hollywood", "Malibu");
        assertTrue(noRoute3.isEmpty());

        // ****************  LA -> MALIBU (TEST WHEN THERE IS ONE DIRECT PATH)  **************
        List<Transport> onePath =
                dijkstra.getShortestPath(this.graph2, this.losAngeles, this.malibu, timeEdgeWeight);
        assertEquals(1, onePath.size());

        // *************  SAN FRANSISCO -> MALIBU (TEST ONE ROUTE BUT DIFFERENT PATHS BASED ON EDGE WEIGHT  *********

        // Route =  San Fransisco -> LA -> Malibu
        // Cheapest Route =  San Fransisco -> LA (Train)
        List<Transport> cheapestRoute1 = this.cities3Controller.cheapestRoute("San Fransisco", "Malibu");
        System.out.println(cheapestRoute1);
        assertEquals(2, cheapestRoute1.size());
        assertEquals("TRAIN", cheapestRoute1.get(0).getType());
        assertEquals("Malibu", cheapestRoute1.get(1).getTarget().toString());

        // Fastest route =  San Fransisco -> LA (Plane)
        List<Transport> fastestRoute1 = this.cities3Controller.fastestRoute("San Fransisco", "Malibu");
        System.out.println(fastestRoute1);
        assertEquals(2, fastestRoute1.size());
        assertEquals("PLANE", fastestRoute1.get(0).getType());
        assertEquals("Malibu", fastestRoute1.get(1).getTarget().toString());
        assertNotEquals(fastestRoute1, cheapestRoute1); // should give different routes although source and dest are the same

        // **************** LA -> SACRAMENTO (TEST MULTIPLE DIFFERENT ROUTES)  ****************

        // Fastest Route = LA -> Sacramento (Plane)
        List<Transport> fastestRoute2 = this.cities3Controller.fastestRoute("Los Angeles", "Sacramento");
        assertEquals(1, fastestRoute2.size());

        // Cheapest Route = LA -> San Fransisco (Train) + San Fransisco -> Sacramento (Bus)
        List<Transport> cheapestRoute2 = this.cities3Controller.cheapestRoute("Los Angeles", "Sacramento");
        assertEquals(2, cheapestRoute2.size());

        assertEquals("Los Angeles", cheapestRoute2.get(0).getSource().toString());
        assertEquals("San Fransisco", cheapestRoute2.get(0).getTarget().toString());
        assertEquals("San Fransisco", cheapestRoute2.get(1).getSource().toString());

        assertEquals("TRAIN", cheapestRoute2.get(0).getType());
        assertEquals("BUS", cheapestRoute2.get(1).getType());

    }

    }


