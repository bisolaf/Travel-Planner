package test;

import org.junit.Test;
import sol.*;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly, but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a, b, c, d, e, f;
    private SimpleGraph graph;

    private TravelGraph graph1;
    private City losAngeles,sacramento, sanFransisco, malibu;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     * TODO: create more setup methods!
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    private void createGraph2(){
        this.graph1 = new TravelGraph();

        this.losAngeles = new City("Los Angeles");
        this.sacramento = new City("Sacramento");
        this.sanFransisco = new City("San Fransisco");
        this.malibu = new City("Malibu");

        this.graph1.addVertex(this.losAngeles);
        this.graph1.addVertex(this.sacramento);
        this.graph1.addVertex(this.sanFransisco);
        this.graph1.addVertex(this.malibu);

        this.graph1.addEdge(this.losAngeles, new Transport(this.losAngeles, this.malibu, TransportType.BUS, 10, 80));
        this.graph1.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sanFransisco, TransportType.PLANE, 150, 120));
        this.graph1.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sanFransisco, TransportType.TRAIN, 80, 420));
        this.graph1.addEdge(this.losAngeles, new Transport(this.losAngeles, this.sacramento, TransportType.PLANE, 200, 140));
        this.graph1.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.losAngeles, TransportType.PLANE, 150, 120));
        this.graph1.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.losAngeles, TransportType.TRAIN, 80, 420));
        this.graph1.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.sacramento, TransportType.BUS, 10, 120));
        this.graph1.addEdge(this.sanFransisco, new Transport(this.sanFransisco, this.sacramento, TransportType.TRAIN, 30, 80));
        this.graph1.addEdge(this.sacramento, new Transport(this.sacramento, this.sanFransisco, TransportType.TRAIN, 30, 80));
        this.graph1.addEdge(this.sacramento, new Transport(this.sacramento, this.sanFransisco, TransportType.BUS, 10, 120));
    }

    /**
     * A sample test that tests BFS on a simple graph. Checks that running BFS gives us the path we expect.
     */
    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    @Test
    public void testGetPath(){
        this.createGraph2();
        TravelController cities3Controller = new TravelController();
        cities3Controller.load("data/cities3.csv", "data/transport3.csv");

        BFS<City, Transport> bfs = new BFS<>();

        //*************** MALIBU -> MALIBU (TEST WHERE START = END) **************
        List<Transport> noPath = bfs.getPath(this.graph1, this.malibu, this.malibu);
        assertTrue(noPath.isEmpty());

        //************** YOSEMITE -> LOS ANGELES (TEST WHERE CITY IS NOT IN GRAPH) ********
        List<Transport> noPath2 = cities3Controller.mostDirectRoute("Yosemite", "Malibu");
        assertTrue(noPath2.isEmpty());

        // ************* MALIBU -> SAN FRANSISCO (TEST WHERE PATH DOES NOT EXIST) ********
        List<Transport> noPath3 = bfs.getPath(this.graph1, this.malibu, this.sanFransisco);
        assertTrue(noPath3.isEmpty());

        // ************* SACRAMENTO -> LOS ANGELES(TEST WHERE PATH ONLY EXIST ONE WAY) ********
        List<Transport> noPath4 = cities3Controller.mostDirectRoute("Sacramento", "Los Angeles");
        //assertTrue(noPath4.isEmpty());

        // ************* TEST WHERE CITY IS IN MAP BUT NOT CONNECTED TO ANY OTHER VERTEX **************
        City yosemite = new City("Yosemite");
        this.graph1.addVertex(yosemite);
        List<Transport> noPath5 = bfs.getPath(this.graph1, yosemite, this.malibu);
        assertTrue(noPath5.isEmpty());

        // ************* LA -> MALIBU (TEST WHERE THERE IS ONLY ONE PATH) *************
        List<Transport> onePath = bfs.getPath(this.graph1, this.losAngeles, this.malibu);
        assertEquals(1, onePath.size());
        assertEquals("Malibu", onePath.get(0).getTarget().toString());

        //*************** SAN FRANSISCO -> LOS ANGELES (TEST WHERE THERE ARE MULTIPLE PATHS) **********
        List<Transport> sfToLa= bfs.getPath(this.graph1, this.sanFransisco, this.malibu);
        System.out.println(sfToLa);
        assertEquals(2, sfToLa.size());
        assertEquals("Los Angeles", sfToLa.get(0).getTarget().toString());
        assertEquals("Los Angeles", sfToLa.get(1).getSource().toString());
        assertEquals("Malibu", sfToLa.get(1).getTarget().toString());

        // LA -> SACRAMENTO
        List<Transport> laToSac= cities3Controller.mostDirectRoute("Los Angeles", "Sacramento");
        assertEquals(1, laToSac.size());
        assertEquals("Sacramento", laToSac.get(0).getTarget().toString());
    }


}
