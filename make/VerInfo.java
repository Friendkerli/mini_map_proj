package make;

import java.util.HashSet;

/** EdgInfo represents the ELABEL.
 *  @author Yang Li
 */
public class VerInfo {
    /** Constructor use NAME PREREQUISITES COMSET TIME. */
    VerInfo(String name, Object prerequisites) {
        _name = name;
        _prerequisites = (HashSet<String>) prerequisites;
    }

    /** String represent name. */
    private String _name = "";

    /** String represent comset. */
    private String _comset = null;

    /** Int represent _time. */
    private int _time = -1;

    /** ArrayList represent prerequisites. */
    private HashSet<String> _prerequisites;

    /** Return the name. */
    String getName() {
        return _name;
    }

    /** Add COMS to comset. */
    void addComset(String coms) {
        _comset = coms;
    }
    /** add VERT ArrayList represent name. */
    void addPrereq(HashSet<String> vert) {
        _prerequisites = vert;
    }
    /** RETURN ArrayList represent name. */
    HashSet<String> getPrereq() {
        return _prerequisites;
    }

    /** RETURN String represent name. */
    String getComset() {
        return _comset;
    }

    /** add TIME to me. */
    void addTime(int time) {
        _time = time;
    }

    /** RETURN Int represent name. */
    int getTime() {
        return _time;
    }
}
