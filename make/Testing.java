package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import graph.DirectedGraph;
import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the make package. */
public class Testing {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.Testing.class));
    }

    private static String a, b;

    private static final String NL = System.getProperty("line.separator");

    private void setupMake() {
        a = "foo.o: foo.c foo.h";
        a += NL;
        a += "        gcc -g -c foo.o foo.c";
        a += NL;
        a += "foo: foo.o";
        a += NL;
        a += "  gcc -o foo foo.o";
        a += NL;
        a += NL;
        a += "foo.c: foo.y";
        a += NL;
        a += "  yacc -o foo.c foo.y";
    }

    private void setupInfo() {
        b = "100";
        b += NL;
        b += "foo 90";
        b += NL;
        b += "foo.y 50";
        b += NL;
        b += "foo.h 10";
    }

    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        _tar = targets;
        String[] line; String comset = ""; graph.Graph.Vertex target = null;
        Scanner first = new Scanner(makefileName);
        while (first.hasNextLine()) {
            HashSet<String> prerequisites =
                new HashSet<String>();
            String next = first.nextLine();
            System.out.println(next);
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
        Scanner second = new Scanner(fileInfoName);
        while (second.hasNextLine()) {
            String next = second.nextLine();
            if (next.matches("\\d+")) {
                _currTime = Integer.parseInt(next);
            } else {
                line = next.split(" ");
                int time = Integer.parseInt(line[1]);
                if (data.get(line[0]) == null) { usage(); }
                ((VerInfo) data.get(line[0]).getLabel()).addTime(time);
            }
        }
    }

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

    static void usage() {
        System.out.println(_errMessage);
        System.exit(1);
    }

    private static List<String> _tar;

    private static DeTraver _deTraver = new DeTraver();

    private static String _errMessage = "error";

    private static HashMap<String, Graph<VerInfo, E>.Vertex> data =
        new HashMap<String, Graph<VerInfo, E>.Vertex>();

    private static HashMap<String, HashSet<String>> makeData =
        new HashMap<String, HashSet<String>>();

    private static int _currTime;

    private static Graph<VerInfo, E> _graph =
        new DirectedGraph<VerInfo, E>();

    private static Graph<VerInfo, E> _graphb =
        new DirectedGraph<VerInfo, E>();

    @Test
    public void test() {
        _tar = new ArrayList<String>();
        _tar.add("foo.o"); _tar.add("foo.c"); _tar.add("foo");
        setupMake();
        setupInfo();
        make(a, b, _tar);
        makeGraph();
    }
}
