package make;

import graph.DirectedGraph;
import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;


/** Initial class for the 'make' program.
 *  @author Yang Li
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
        makeGraph();
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        _tar = targets;
        String[] line; String comset = ""; graph.Graph.Vertex target = null;
        try {
            Scanner first = new Scanner(new File(makefileName));
            while (first.hasNextLine()) {
                HashSet<String> prerequisites =
                    new HashSet<String>();
                String next = first.nextLine();
                if (next.contains(":")) {
                    line = next.split(" ");
                    String tarS = line[0].substring(0, line[0].length() - 1);
                    if (makeData.get(tarS) != null) {
                        prerequisites = makeData.get(tarS);
                    }
                    for (int i = 1; i < line.length; i++) {
                        prerequisites.add(line[i]);
                        if (!data.containsKey(line[i])) {
                            Graph<VerInfo, E>.Vertex vert =
                                _graph.add(new VerInfo(line[i], null));
                            data.put(line[i], vert);
                        }
                    }
                    if (data.containsKey(tarS)) {
                        target = data.get(tarS);
                    } else {
                        target = _graph.add(new VerInfo(tarS, prerequisites));
                        data.put(tarS, target);
                    }
                    makeData.put(tarS, prerequisites);
                } else if (next.equals("") && first.hasNextLine()
                    || next.contains("#")) {
                    continue;
                } else {
                    String tarComset = ((VerInfo) target.getLabel())
                        .getComset();
                    if (tarComset == null) {
                        ((VerInfo) target.getLabel()).addComset(next);
                    } else {
                        _errMessage = "wrong file"; usage();
                    }
                }
            }
            readInfo(fileInfoName);
        } catch (FileNotFoundException e) {
            usage();
        }
    }
    /** Read FILEINFONAME. */
    private static void readInfo(String fileInfoName)
        throws FileNotFoundException {
        Scanner second = new Scanner(new File(fileInfoName));
        while (second.hasNextLine()) {
            String next = second.nextLine();
            if (next.matches("\\d+")) {
                _currTime = Integer.parseInt(next);
            } else {
                String[] line = next.split(" ");
                int time = Integer.parseInt(line[1]);
                if (data.get(line[0]) == null) {
                    continue;
                } else {
                    ((VerInfo) data.get(line[0]).getLabel()).addTime(time);
                }
            }
        }
    }

    /** Create current graph. */
    private static void makeGraph() {
        for (String target: makeData.keySet()) {
            ArrayList<Graph<VerInfo, E>.Vertex> build =
                new ArrayList<Graph<VerInfo, E>.Vertex>();
            for (String prereq: makeData.get(target)) {
                build.add(data.get(prereq));
            }
            for (Graph<VerInfo, E>.Vertex tar: build) {
                _graph.add(data.get(target), tar);
            }
        }
        _deTraver.setTime(_currTime);
        for (String tar: _tar) {
            graph.Graph.Vertex vert = data.get(tar);
            _deTraver.depthFirstTraverse(_graph, vert);
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    static void usage() {
        System.err.println(_errMessage);
        System.exit(1);
    }
    /** target list. */
    private static List<String> _tar;
    /** string of all error message. */
    private static DeTraver _deTraver = new DeTraver();
    /** string of all error message. */
    private static String _errMessage = "error";
    /** Data of all vertices. */
    private static HashMap<String, Graph<VerInfo, E>.Vertex> data =
        new HashMap<String, Graph<VerInfo, E>.Vertex>();
    /** Data for make. */
    private static HashMap<String, HashSet<String>> makeData =
        new HashMap<String, HashSet<String>>();
    /** Int represent current time. */
    private static int _currTime;
    /** represent graph. */
    private static Graph<VerInfo, E> _graph =
        new DirectedGraph<VerInfo, E>();
    /** represent graph. */
    private static Graph<VerInfo, E> _graphb =
        new DirectedGraph<VerInfo, E>();

}
