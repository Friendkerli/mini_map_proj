package make;

import graph.Graph;
import graph.RejectException;
import graph.Traversal;
import graph.StopException;

/** Initial class for the 'make' program.
 *  @author Yang Li
 */
public class DeTraver extends Traversal<VerInfo, E> {

    /** int time. */
    private int _time;

    /** set int TIME. */
    void setTime(int time) {
        _time = time;
    }
    /** need rebuild?. */
    private boolean _outdated;

    @Override
    protected void preVisit(Graph<VerInfo, E>.Edge e,
                                Graph<VerInfo, E>.Vertex v0) {
        if (!_graph.neighbors(e.getV1()).hasNext()) {
            if (e.getV1().getLabel().getTime() != -1) {
                throw new RejectException();
            } else if (e.getV1().getLabel().getComset() == null) {
                System.err.println("the error");
                System.exit(1);
            }
        }
    }

    @Override
    protected void visit(Graph<VerInfo, E>.Vertex v) {
        for (Graph<VerInfo, E>.Edge vert: _graph.edges(v)) {
            try {
                CyTraver newTraver = new CyTraver();
                newTraver.setVertex(v);
                newTraver.depthFirstTraverse(_graph, vert.getV1());
            } catch (StopException error) {
                System.err.print(error);
                System.exit(1);
            }
        }
    }

    @Override
    protected void postVisit(Graph<VerInfo, E>.Vertex v) {
        int time1 = v.getLabel().getTime(); int time2;
        if (time1 == -1) { _outdated = true; }
        for (Graph<VerInfo, E>.Vertex vert: _graph.neighbors(v)) {
            time2 = vert.getLabel().getTime();
            if (time1 < time2 || time1 == -1) {
                _outdated = true;
            }
        }
        if (_outdated) {
            if (v.getLabel().getComset() != null) {
                System.out.println(v.getLabel().getComset());
            }
            _time++; v.getLabel().addTime(_time);
            _outdated = false;
        }
    }

}
