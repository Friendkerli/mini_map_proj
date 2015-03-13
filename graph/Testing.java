package graph;

import graph.Graph.Edge;
import org.junit.Test;

import ucb.junit.textui;
import static org.junit.Assert.*;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class));
    }

    private Graph _dGraph;

    private Graph _undGraph;

    private Graph.Vertex a, b, c, d, e, f, g, h, i, j;

    private Graph.Edge ab, ad, be, bf, ha, hi, hj, jg;

    private DepthFirst _tra = new DepthFirst();

    private static class Traverse1 extends Traversal<Integer, Integer> {

        private String _trail = "";

        @Override
        protected void preVisit(Graph<Integer, Integer>.Edge e,
                                Graph<Integer, Integer>.Vertex v0) {
            _trail += v0.getLabel().toString();
        }

        @Override
        protected void visit(Graph<Integer, Integer>.Vertex v) {
            _trail += v.getLabel().toString();
        }

        @Override
        protected void postVisit(Graph<Integer, Integer>.Vertex v) {
            _trail += v.getLabel().toString();
        }

        String trail() {
            return _trail;
        }

    }

    private void buildDirected() {
        _dGraph = new DirectedGraph();
        a = _dGraph.add(0);
        b = _dGraph.add(1);
        c = _dGraph.add(2);
        d = _dGraph.add(3);
        e = _dGraph.add(4);
        f = _dGraph.add(5);
        g = _dGraph.add(6);
        h = _dGraph.add(7);
        i = _dGraph.add(8);
        j = _dGraph.add(9);
        ab = _dGraph.add(a, b, 0);
        ad = _dGraph.add(a, d, 2);
        be = _dGraph.add(b, e, 4);
        bf = _dGraph.add(b, f, 5);
        ha = _dGraph.add(h, a, 8);
        hi = _dGraph.add(h, i, 9);
        hj = _dGraph.add(h, j, 10);
        jg = _dGraph.add(j, g, 11);
    }

    private void buildUnDirected() {
        _undGraph = new UndirectedGraph();
        a = _undGraph.add(0);
        b = _undGraph.add(1);
        c = _undGraph.add(2);
        d = _undGraph.add(3);
        e = _undGraph.add(4);
        f = _undGraph.add(5);
        g = _undGraph.add(6);
        h = _undGraph.add(7);
        i = _undGraph.add(8);
        j = _undGraph.add(9);
        ab = _undGraph.add(a, b, 0);
        ad = _undGraph.add(a, d, 2);
        be = _undGraph.add(b, e, 4);
        bf = _undGraph.add(b, f, 5);
        ha = _undGraph.add(h, a, 8);
        hi = _undGraph.add(h, i, 9);
        hj = _undGraph.add(h, j, 10);
        jg = _undGraph.add(j, g, 11);
    }


    @Test
    public void undirectTestSize() {
        buildUnDirected();
        assertEquals("undirected graph has 10 vertices", 10, _undGraph
            .vertexSize());
        assertEquals("undirected graph has 8 edges", 8, _undGraph.edgeSize());
    }

    @Test
    public void undirectTestDegree() {
        buildUnDirected();
        assertEquals("Indegree is 1", 3, _undGraph.degree(a));
        assertEquals("Indegree is 2", 3, _undGraph.degree(b));
        assertEquals("Indegree is 0", 0, _undGraph.degree(c));
        assertEquals("Indegree is 1", 1, _undGraph.degree(d));
        assertEquals("Outdegree is 3", 3, _undGraph.outDegree(a));
        assertEquals("Outdegree is 1", 0, _undGraph.outDegree(c));
        assertEquals("Outdegree is 2", 1, _undGraph.outDegree(d));
        assertEquals("Outdegree is 1", 1, _undGraph.outDegree(e));
    }

    @Test
    public void undirectTestContain() {
        buildUnDirected();
        assertEquals("Graph contains ac", false, _undGraph.contains(a, c));
        assertEquals("Graph contains cb", false, _undGraph.contains(c, b));
        assertEquals("Graph not contains cd", false, _undGraph.contains(c, d));
        assertEquals("Graph contains ab", true, _undGraph.contains(a, b));
        assertEquals("Graph contains ab", true, _undGraph.contains(a, b, 0));

    }

    @Test
    public void undirectTestRemove() {
        buildUnDirected();
        _undGraph.remove(a);
        assertEquals("Only vertex C is left", 9, _undGraph.vertexSize());
        assertEquals("Only 5 edge left", 5, _undGraph.edgeSize());
    }

    @Test
    public void emptyGraph() {
        DirectedGraph w = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, w.vertexSize());
        assertEquals("Initial graph has edges", 0, w.edgeSize());
    }

    @Test
    public void directTestSize() {
        buildDirected();
        assertEquals("Directed graph has 10 vertices", 10, _dGraph
            .vertexSize());
        assertEquals("Directed graph has 8 edges", 8, _dGraph.edgeSize());
    }

    @Test
    public void directTestDegree() {
        buildDirected();
        assertEquals("Indegree is 1", 1, _dGraph.inDegree(b));
        assertEquals("Outdegree is 0", 0, _dGraph.outDegree(c));
    }

    @Test
    public void directTestContain() {
        buildDirected();
        assertEquals("Graph not contains ac", false, _dGraph.contains(a, c));
        assertEquals("Graph not contains cb", false, _dGraph.contains(c, b));
        assertEquals("Graph not contains ad", true, _dGraph.contains(a, d));
        assertEquals("Graph contains ad", true, _dGraph.contains(a, d, 2));
    }

    @Test
    public void directTestRemove() {
        _dGraph = new DirectedGraph();
        a = _dGraph.add("A");
        b = _dGraph.add("B");
        c = _dGraph.add("C");
        d = _dGraph.add("D");
        e = _dGraph.add("E");
        f = _dGraph.add("F");
        ab = _dGraph.add(a, b, 1);
        Edge ac = _dGraph.add(a, c, 2);
        ad = _dGraph.add(a, d, 3);
        Edge de = _dGraph.add(d, e, 4);
        Edge df = _dGraph.add(d, f, 5);
        Edge ce = _dGraph.add(c, e, 6);
        _dGraph.remove(d);
        assertEquals("Only vertex C is left", 5, _dGraph.vertexSize());
        assertEquals("Only 1 edge left", 3, _dGraph.edgeSize());
        buildDirected();
        _dGraph.remove(b, c);
        assertEquals("size 10", 10, _dGraph.vertexSize());
        assertEquals("a", 8, _dGraph.edgeSize());
    }

    @Test
    public void testDFT() {
        buildDirected();
        Traverse1 depthfirst = new Traverse1();
        depthfirst.depthFirstTraverse(_dGraph, a);
        String result = depthfirst.trail();
        assertEquals("path", "00011144551330", result);
    }

    @Test
    public void testDFT2() {
        _dGraph = new DirectedGraph();
        a = _dGraph.add(1);
        b = _dGraph.add(2);
        c = _dGraph.add(3);
        d = _dGraph.add(4);
        e = _dGraph.add(5);
        f = _dGraph.add(6);
        ab = _dGraph.add(a, b, 1);
        Edge ac = _dGraph.add(a, c, 2);
        ad = _dGraph.add(a, d, 3);
        Edge de = _dGraph.add(d, e, 4);
        Edge df = _dGraph.add(d, f, 5);
        Edge ce = _dGraph.add(c, e, 6);
        Traverse1 depthfirst = new Traverse1();
        depthfirst.depthFirstTraverse(_dGraph, a);
        String result = depthfirst.trail();
        assertEquals("path", "11112233553446641", result);
    }
}
