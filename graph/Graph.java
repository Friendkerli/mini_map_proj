package graph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make methods in Graph abstract, if you want
 * different implementations in DirectedGraph and UndirectedGraph.  You may
 * add bodies to abstract methods, modify existing bodies, or override
 * inherited methods. */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Yang Li
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The Vlabel on this vertex. */
        private final VLabel _label;

    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /** Represents one of my inedges. */
    private HashMap<Vertex, ArrayList<Edge>> _inList =
        new HashMap<Vertex, ArrayList<Edge>>();

    /** Represents one of my outedges. */
    private HashMap<Vertex, ArrayList<Edge>> _outList =
        new HashMap<Vertex, ArrayList<Edge>>();

    /** Represents one of my edges. */
    private ArrayList<Edge> _edges = new ArrayList<Edge>();

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _outList.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        if (_outList.get(v) == null) {
            return 0;
        }
        return _outList.get(v).size();
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        if (_inList.get(v) == null) {
            return 0;
        }
        return _inList.get(v).size();
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        if (isDirected()) {
            for (Edge edge: _edges) {
                try {
                    if (edge.getV0().equals(u) && edge.getV1().equals(v)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) { continue; }
            }
        } else {
            for (Edge edge: _edges) {
                try {
                    if (edge.getV(v).equals(u)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) { continue; }
            }
        }
        return false;
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        if (isDirected()) {
            for (Edge edge: _edges) {
                try {
                    if (edge.getV0().equals(u) && edge.getV1().equals(v)
                        && edge.getLabel().equals(label)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) { continue; }
            }
        } else {
            for (Edge edge: _edges) {
                try {
                    if (edge.getV(v).equals(u)
                        && edge.getLabel().equals(label)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) { continue; }
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex result = new Vertex(label);
        _inList.put(result, null);
        _outList.put(result, null);
        return result;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge edg = new Edge(from, to, label);
        ArrayList<Edge> in = new ArrayList<Edge>();
        ArrayList<Edge> out = new ArrayList<Edge>();
        if (isDirected()) {
            if (_outList.get(from) != null) {
                out.addAll(_outList.get(from));
            }
            if (_inList.get(to) != null) {
                in.addAll(_inList.get(to));
            }
            in.add(edg); out.add(edg);
            _inList.put(to, in); _outList.put(from, out);
        } else {
            if (_outList.get(from) != null) {
                out.addAll(_outList.get(from));
            }
            if (_outList.get(to) != null) {
                in.addAll(_outList.get(to));
            }
            in.add(edg); out.add(edg);
            _outList.put(from, out); _outList.put(to, in);
        }
        _edges.add(edg);
        return edg;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        ArrayList<Edge> in = new ArrayList<Edge>();
        ArrayList<Edge> out = new ArrayList<Edge>();
        Vertex target;
        if (isDirected()) {
            in = _inList.get(v);
            out = _outList.get(v);
            for (Edge edg: in) {
                target = edg.getV0();
                _outList.get(target).remove(edg);
                _edges.remove(edg);
            }
            for (Edge edg: out) {
                target = edg.getV1();
                _inList.get(target).remove(edg);
                _edges.remove(edg);
            }
        } else {
            if (_outList.get(v) != null) {
                out.addAll(_outList.get(v));
                in.addAll(_outList.get(v));
                for (Edge edg: out) {
                    target = edg.getV0();
                    _outList.get(target).remove(edg);
                    _edges.remove(edg);
                }
                for (Edge edg: in) {
                    target = edg.getV1();
                    _outList.get(target).remove(edg);
                    _edges.remove(edg);
                }
            }
        }
        _outList.remove(v);
        _inList.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        Vertex from = e.getV0();
        Vertex to = e.getV1();
        if (isDirected()) {
            _outList.get(from).remove(e);
            _inList.get(to).remove(e);
        } else {
            _outList.get(from).remove(e);
            _outList.get(to).remove(e);
        }
        _edges.remove(e);
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        ArrayList<Edge> in1 = new ArrayList<Edge>();
        ArrayList<Edge> in2 = new ArrayList<Edge>();
        ArrayList<Edge> out1 = new ArrayList<Edge>();
        ArrayList<Edge> out2 = new ArrayList<Edge>();
        try {
            if (isDirected()) {
                if (_inList.get(v1) != null) {
                    in1.addAll(_inList.get(v1));
                    for (Edge edge: in1) {
                        if (edge.getV(v1).equals(v2)) {
                            _inList.get(v1).remove(edge);
                            _edges.remove(edge);
                        }
                    }
                }
                if (_inList.get(v2) != null) {
                    in2 .addAll(_inList.get(v2));
                    for (Edge edge: in2) {
                        if (edge.getV(v2).equals(v1)) {
                            _inList.get(v2).remove(edge);
                            _edges.remove(edge);
                        }
                    }
                }
            }
            if (_outList.get(v1) != null) {
                out1.addAll(_outList.get(v1));
                for (Edge edge: out1) {
                    if (edge.getV(v1).equals(v2)) {
                        _outList.get(v1).remove(edge);
                        _edges.remove(edge);
                    }
                }
            }
            if (_outList.get(v2) != null) {
                out2.addAll(_outList.get(v2));
                for (Edge edge: out2) {
                    if (edge.getV(v2).equals(v1)) {
                        _outList.get(v2).remove(edge);
                        _edges.remove(edge);
                    }
                }
            }
        } catch (IllegalArgumentException e) { e = e; }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(_inList.keySet());
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> successors = new ArrayList<Vertex>();
        if (_outList.get(v) == null) {
            return Iteration.iteration(successors);
        }
        if (isDirected()) {
            for (Edge edg: _outList.get(v)) {
                successors.add(edg.getV1());
            }
        } else {
            for (Edge edg: _outList.get(v)) {
                successors.add(edg.getV(v));
            }
        }
        return Iteration.iteration(successors.iterator());
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        ArrayList<Vertex> successors = new ArrayList<Vertex>();
        if (_inList.get(v) == null) {
            return Iteration.iteration(successors);
        }
        if (isDirected()) {
            for (Edge edg: _inList.get(v)) {
                successors.add(edg.getV0());
            }
        } else {
            for (Edge edg: _outList.get(v)) {
                successors.add(edg.getV(v));
            }
        }
        return Iteration.iteration(successors.iterator());
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(_edges);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        if (_outList.get(v) == null) {
            return Iteration.iteration(edges);
        }
        edges.addAll(_outList.get(v));
        return Iteration.iteration(edges);
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        edges.addAll(_inList.get(v));
        return Iteration.iteration(edges);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if stringComp = Graph.<Integer>naturalOrder(), then
     *  stringComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }
    /** The label on this vertex. */
    private Comparator<ELabel> _comparator;


    /** Cause subsequent traversals and calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        _comparator = comparator;
        Collections.sort(_edges, new Newcomparator());
    }

    /** Comparator label on this vertex. */
    private class Newcomparator implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return _comparator.compare(e1.getLabel(), e2.getLabel());
        }
    }

}
