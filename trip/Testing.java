package trip;

import graph.Graphs;

import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the trip package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.Testing.class));
    }

    private static String a;

    private static String b;

    @Test
    public void test() {
        setupInput();
        trip(a);
        findPath(graph, "B_erkeley", "SF");
        assertEquals("Right path", "2. Take c_ave west for 5.0 miles to SF."
            , output1 + output2 + output3);
    }

    private void setupInput() {
        a = "L O_kland 1.0 0 ";
        a += "L B_erkeley 2.0 0 ";
        a += "L SF 0 2.0 ";
        a += "R B_erkeley a_ave 1 NS O_kland ";
        a += "R B_erkeley b_ave 10 EW SF ";
        a += "R SF c_ave 5 WE O_kland ";
    }

    private void setupInput2() {
        b = "L Syracuse -4195.47 2969.63 ";
        b += "L Nedrow -4195.09 2964.51 ";
        b += "L Tully -4193.58 2953.08 ";
        b += "L Lafayette -4193.5 2959.51 ";
        b += "R Syracuse I-81 6.5165 NS Nedrow ";
        b += "R Nedrow I-81 6.8302 NS Lafayette ";
        b += "R Tully I-81 6.7594 SN Lafayette ";
    }

    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        graph = new MapGraph();
        Scanner scan1 = new Scanner(mapFileName);
        while (scan1.findWithinHorizon(L_PATTERN
            + "|" + R_PATTERN, 0) != null) {
            MatchResult mat = scan1.match();
            if (mat.group(1) != null && mat.group(1).equals("L")) {
                double x = Double.parseDouble(mat.group(3));
                double y = Double.parseDouble(mat.group(4));
                VerInfo ver = new VerInfo(mat.group(2), x, y);
                graph.Graph.Vertex vert = graph.add(ver);
                graph.addName(mat.group(2), vert);

            } else {
                Double d = Double.parseDouble(mat.group(8));
                graph.Graph.Vertex city1 = graph.getVer(mat.group(6));
                graph.Graph.Vertex city2 = graph.getVer(mat.group(10));
                if (!graph.contains(mat.group(6)) || !graph
                    .contains(mat.group(10))) { usage(); } else {
                    String road = mat.group(7);
                    String direction = mat.group(9);
                    String reverse = direction.substring(1, 2)
                        + direction.substring(0, 1);
                    EdgInfo e1 = new EdgInfo(d, direction, road);
                    EdgInfo e2 = new EdgInfo(d, reverse, road);
                    graph.add(city1, city2, e1);
                    graph.add(city2, city1, e2);
                }
            }
        }
    }

    private static Distance h = new Distance();

    private static void findPath(MapGraph gra, String v1, String v2) {
        List<graph.Graph.Edge> result;
        graph.Graph.Vertex vert1 = gra.getVer(v1);
        graph.Graph.Vertex vert2 = gra.getVer(v2);
        result = Graphs.shortestPath(gra, vert1, vert2, h);
        System.out.println("From " + v1 + ":");
        System.out.println();
        int i = 1;
        for (int j = 0; j < result.size(); j++) {
            String road =  ((EdgInfo) result.get(j).getLabel()).getRoadName();
            String direction = getDirection(((EdgInfo) result
                .get(j).getLabel()).getDirection());
            double distance = ((EdgInfo) result
                .get(j).getLabel()).getDistance();
            output1 = Integer.toString(i) + ". "
                + "Take " + road + direction + "for ";
            output2 = Double.toString(distance) + " miles";
            output3 = ".";
            String end = (((VerInfo) result.get(j)
                    .getV1().getLabel()).getName());
            if (j + 1 < result.size()) {
                String nextRoad = ((EdgInfo) result.get(j + 1)
                            .getLabel()).getRoadName();
                String nextDir = getDirection(((EdgInfo) result.get(j + 1)
                            .getLabel()).getDirection());
                double nextDist = ((EdgInfo) result.get(j + 1)
                        .getLabel()).getDistance();
                if (direction.equals(nextDir) && road.equals(nextRoad)) {
                    j += 1;
                    output2 = Double.toString(distance + nextDist) + " miles";
                }
            }
            if (end.equals(v2)) {
                output3 = " to " + v2 + ".";
            }
            System.out.println(output1 + output2 + output3);
            i++;
        }
    }

    private static String output1, output2, output3;

    private static String getDirection(String dir) {
        if (dir.equals("NS")) { return " south "; }
        if (dir.equals("SN")) { return " north "; }
        if (dir.equals("WE")) { return " east "; } else { return " west "; }
    }

    private static final String L_PATTERN = "\\s*(L)\\s*([a-zA-Z_0-9]"
        + "+)\\s+(\\d+\\.?"
        + "\\d*)\\s*(-?\\d+\\.?\\d*)\\s*";

    private static final String R_PATTERN = "\\s*(R)\\s*([a-zA-Z_0"
        + "-9]+)\\s*([a-zA-Z_"
        + "0-9]*)\\s*(\\d*\\.?"
        + "\\d*)\\s*(NS|SN|EW|WE)\\s*([a-zA-Z_0-9]+)\\s*";

    private static MapGraph graph;

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.out.println("The input is not correct.");
        System.exit(1);
    }

}
