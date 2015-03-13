package graph;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/** Assorted graph algorithms.
 *  @author Yang Li
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 final Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h,
                 final Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        ArrayList<Graph<VLabel, ELabel>.Edge> result =
            new ArrayList<Graph<VLabel, ELabel>.Edge>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> fringe =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.
            Edge> parents = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        for (Graph<VLabel, ELabel>.Vertex v: G.vertices()) {
            vweighter.setWeight(v.getLabel(), Double.MAX_VALUE);
            fringe.add(v);
        }
        vweighter.setWeight(V0.getLabel(), 0);
        while (!fringe.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex v = fringe.get(0);
            for (Graph<VLabel, ELabel>.Vertex ver: fringe) {
                if (vweighter.weight(ver.getLabel())
                    + h.dist(V1.getLabel(), ver.getLabel())
                    < vweighter.weight(v.getLabel())
                    + h.dist(v.getLabel(), v.getLabel())) {
                    v = ver;
                }
            }
            fringe.remove(v);
            if (v.equals(V1)) {
                while (parents.containsKey(v)) {
                    result.add(parents.get(v));
                    v = parents.get(v).getV(v);
                }
                Collections.reverse(result);
                return result;
            } else {
                for (Graph<VLabel, ELabel>.Edge edg: G.outEdges(v)) {
                    Graph<VLabel, ELabel>.Vertex w = edg.getV(v);
                    double distW = vweighter.weight(w.getLabel());
                    double distV = vweighter.weight(v.getLabel());
                    double n = eweighter.weight(edg.getLabel());
                    if (distW > n + distV) {
                        vweighter.setWeight(w.getLabel(), n + distV);
                        parents.put(w, edg);
                    }
                }
            }
        }
        return null;
    }


    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 final Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h) {
        ArrayList<Graph<VLabel, ELabel>.Edge> result =
            new ArrayList<Graph<VLabel, ELabel>.Edge>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> fringe =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel,
            ELabel>.Edge> parents = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        for (Graph<VLabel, ELabel>.Vertex v: G.vertices()) {
            v.getLabel().setWeight(Double.MAX_VALUE);
            fringe.add(v);
        }
        V0.getLabel().setWeight(0);
        while (!fringe.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex v = fringe.get(0);
            for (Graph<VLabel, ELabel>.Vertex ver: fringe) {
                if (ver.getLabel().weight() + h.dist(ver.getLabel(),
                    V1.getLabel()) < v.getLabel().weight()
                    + h.dist(v.getLabel(), V1.getLabel())) {
                    v = ver;
                }
            }
            fringe.remove(v);
            if (v.equals(V1)) {
                while (parents.containsKey(v)) {
                    result.add(parents.get(v));
                    v = parents.get(v).getV(v);
                }
                Collections.reverse(result);
                return result;
            } else {
                for (Graph<VLabel, ELabel>.Edge edg: G.outEdges(v)) {
                    Graph<VLabel, ELabel>.Vertex w = edg.getV(v);
                    double distW = w.getLabel().weight();
                    double distV = v.getLabel().weight();
                    double n = edg.getLabel().weight();
                    if (distW > n + distV) {
                        w.getLabel().setWeight(n + distV);
                        parents.put(w, edg);
                    }
                }
            }
        }
        return null;
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
