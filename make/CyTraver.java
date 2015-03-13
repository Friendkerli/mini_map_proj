package make;


import graph.Graph;
import graph.Traversal;

/** Initial class for the 'make' program.
 *  @author Yang Li
 */
public class CyTraver extends graph.Traversal<VerInfo, E> {

    /** need rebuild?. */
    private Graph<VerInfo, E>.Vertex _vertex;

    /** set VER. */
    void setVertex(Graph<VerInfo, E>.Vertex ver) {
        _vertex = ver;
    }

    @Override
    protected void visit(Graph<VerInfo, E>.Vertex v) {
        if (_vertex == v) {
            System.err.println("visit CyTraver");
            System.exit(1);
        }
    }

}
