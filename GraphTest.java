package test;

import org.junit.Test;
import sol.City;
import sol.Transport;
import sol.TravelController;
import sol.TravelGraph;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class GraphTest {
    private SimpleGraph graph;
    private SimpleVertex a, b, c;
    private SimpleEdge edgeAB, edgeBC, edgeCA, edgeAC;

    private TravelGraph graph1;
    private City losAngeles, sacramento, sanFransisco, malibu;
    private Transport sacToSf1, sacToSf2, sfToSac1, sfToSac2, sfToLa1, sfToLa2, laToSf1,laToSf2, laToMalibu, laToSac;


    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    private void createGraph1() {
        this.graph1 = new TravelGraph();

        this.losAngeles = new City("Los Angeles");
        this.sacramento = new City("Sacramento");
        this.sanFransisco = new City("San Fransisco");
        this.malibu = new City("Malibu");

        this.sacToSf1 = new Transport(this.sacramento, this.sanFransisco, TransportType.TRAIN, 30, 80);
        this.sacToSf2 = new Transport(this.sacramento, this.sanFransisco, TransportType.BUS, 10, 120);
        this.sfToSac1 = new Transport(this.sanFransisco, this.sacramento, TransportType.BUS, 10, 120);
        this.sfToSac2 = new Transport(this.sanFransisco, this.sacramento, TransportType.TRAIN, 30, 80);
        this.sfToLa1 = new Transport(this.sanFransisco, this.losAngeles, TransportType.PLANE, 150, 120);
        this.sfToLa2 = new Transport(this.sanFransisco, this.losAngeles, TransportType.TRAIN, 80, 420);
        this.laToSf1 = new Transport(this.losAngeles, this.sanFransisco, TransportType.PLANE, 150, 120);
        this.laToSf2 = new Transport(this.losAngeles, this.sanFransisco, TransportType.TRAIN, 80, 420);
        this.laToMalibu = new Transport(this.losAngeles, this.malibu, TransportType.BUS, 10, 80);
        this.laToSac = new Transport(this.losAngeles, this.sacramento, TransportType.PLANE, 200, 140);


        this.graph1.addVertex(this.losAngeles);
        this.graph1.addVertex(this.sacramento);
        this.graph1.addVertex(this.sanFransisco);
        this.graph1.addVertex(this.malibu);

        this.graph1.addEdge(this.losAngeles, this.laToMalibu);
        this.graph1.addEdge(this.losAngeles, this.laToSac);
        this.graph1.addEdge(this.losAngeles, this.laToSf1);
        this.graph1.addEdge(this.losAngeles, this.laToSf2);
        this.graph1.addEdge(this.sacramento, this.sacToSf1);
        this.graph1.addEdge(this.sacramento, this.sacToSf2);
        this.graph1.addEdge(this.sanFransisco, this.sfToLa1);
        this.graph1.addEdge(this.sanFransisco, this.sfToLa2);
        this.graph1.addEdge(this.sanFransisco, this.sfToSac1);
        this.graph1.addEdge(this.sanFransisco, this.sfToSac2);
    }


    /**
     * Sample test for the graph. Tests that the number of vertices and the vertices in the graph are what we expect.
     */
    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        // test getVertices to check this method AND insertVertex
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    /**
     * Test for graph1 (cities 3 and transport 3 CSV)
     */
    @Test
    public void testGraph1(){
        this.createGraph1();
        assertEquals(this.graph1.getVertices().size(), 4);
        assertTrue(this.graph1.getVertices().contains(this.losAngeles));
        assertTrue(this.graph1.getVertices().contains(this.malibu));
        assertTrue(this.graph1.getVertices().contains(this.sanFransisco));
        assertTrue(this.graph1.getVertices().contains(this.sacramento));

        City yosemite = new City("Yosemite");
        this.graph1.addVertex(yosemite);
        assertEquals(this.graph1.getVertices().size(), 5);
        assertTrue(this.graph1.getOutgoingEdges(yosemite).isEmpty());
        Transport laToYosemite = new Transport(this.losAngeles, yosemite, TransportType.BUS, 100, 480);
        this.graph1.addEdge(this.losAngeles, laToYosemite);
        assertEquals(5, this.graph1.getOutgoingEdges(this.losAngeles).size());
    }




}
