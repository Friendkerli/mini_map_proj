package trip;

import java.util.HashMap;

/** A distancer. */

/** Concrete class for MapGraph.
 *  @author Yang Li
 */
final class MapGraph extends graph.DirectedGraph<VerInfo, EdgInfo> {

    /** The data. */
    private static HashMap<String, graph.Graph.Vertex> _data =
        new HashMap<String, graph.Graph.Vertex>();

    /** RETURN NAME. */
    boolean contains(String name) {
        return _data.containsKey(name);
    }

    /** addname NAME V. */
    void addName(String name, graph.Graph.Vertex v) {
        _data.put(name, v);
    }

    /** RETURN NAME. */
    static graph.Graph.Vertex getVer(String name) {
        return _data.get(name);
    }

}
