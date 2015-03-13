package trip;

import graph.Graphs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.regex.MatchResult;
import java.text.DecimalFormat;


/** Initial class for the 'trip' program.
 *  @author Yang Li
 */
public final class Main {

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }
        trip(mapFileName);
    }


    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        graph = new MapGraph();
        Scanner scan1;
        try {
            scan1 = new Scanner(new File(mapFileName));
            while (scan1.findWithinHorizon(PATTERN, 0) != null) {
                MatchResult mat = scan1.match();
                if (mat.group(1) != null && mat.group(1).equals("L")) {
                    double x = Double.parseDouble(mat.group(3));
                    double y = Double.parseDouble(mat.group(4));
                    VerInfo ver = new VerInfo(mat.group(2), x, y);
                    graph.Graph.Vertex vert = graph.add(ver);
                    graph.addName(mat.group(2), vert);

                } else {
                    Float d = Float.parseFloat(mat.group(8));
                    graph.Graph.Vertex city1 = graph.getVer(mat.group(6));
                    graph.Graph.Vertex city2 = graph.getVer(mat.group(10));
                    if (!graph.contains(mat.group(6)) || !graph.contains(mat
                        .group(10))) { usage(); } else {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(System.in);
        ArrayList<String> requests = new ArrayList<String>();
        while (scan.hasNext()) {
            String request = scan.next();
            request = request.replace(",", "");
            requests.add(request);
        }
        System.out.println("From " + requests.get(0) + ":");
        System.out.println();
        int index = 1;
        for (int i = 1; i < requests.size(); i++) {
            index = findPath(graph, requests.get(i - 1)
                , requests.get(i), index);
        }
        scan.close();
    }

    /** A distancer. */
    private static Distance h = new Distance();

    /** Return int INDEX Find path use GRA V1 V2. */
    private static int findPath(MapGraph gra, String v1, String v2, int index) {
        List<graph.Graph.Edge> result;
        DecimalFormat fmt = new DecimalFormat("0.0");
        String nextRoad, nextDir; double nextDist;
        graph.Graph.Vertex vert1 = gra.getVer(v1);
        graph.Graph.Vertex vert2 = gra.getVer(v2);
        result = Graphs.shortestPath(gra, vert1, vert2, h);
        for (int j = 0; j < result.size(); j++) {
            String road =  ((EdgInfo) result.get(j)
                    .getLabel()).getRoadName();
            String direction = getDirection(((EdgInfo) result
                    .get(j).getLabel()).getDirection());
            double distance = ((EdgInfo) result
                    .get(j).getLabel()).getDistance();
            String end = (((VerInfo) result.get(j).getV1()
                    .getLabel()).getName());
            while (j < result.size()) {
                if (j == result.size() - 1) {
                    break;
                }
                nextRoad = ((EdgInfo) result.get(j + 1)
                        .getLabel()).getRoadName();
                nextDir = getDirection(((EdgInfo) result
                        .get(j + 1).getLabel()).getDirection());
                nextDist = ((EdgInfo) result.get(j + 1)
                        .getLabel()).getDistance();
                if (direction.equals(nextDir) && road.equals(nextRoad)) {
                    distance += nextDist;
                    j++;
                } else {
                    break;
                }
            }
            end = (((VerInfo) result.get(j).getV1().getLabel()).getName());
            String output1 = Integer.toString(index) + ". "
                    + "Take " + road + direction + "for ";
            String output2 = fmt.format(distance) + " miles";
            String output3 = ".";
            if (j == result.size() - 1) {
                output3 = " to " + v2 + ".";
            }
            System.out.println(output1 + output2 + output3);
            index++;
        }
        return index;
    }

    /** RETURN string based on DIR. */
    private static String getDirection(String dir) {
        if (dir.equals("NS")) { return " south "; }
        if (dir.equals("SN")) { return " north "; }
        if (dir.equals("WE")) { return " east "; } else { return " west "; }
    }

    /** A pattern matches location. */
    private static final String PATTERN = "\\s*(L)\\s*([\\w-]+)"
        + "\\s+(-?\\d+\\.?\\d*)\\s*(-?\\d+\\.?\\d*)\\s*|\\s*(R)\\"
        + "s*([\\w-]+)\\s*([\\w-]*)\\s*"
        + "(\\d*\\.?\\d*)\\s*(NS|SN|EW|WE)\\s*([\\w-]+)\\s*";

    /** A concrete directed graph. */
    private static MapGraph graph;

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.out.println("The input is not correct.");
        System.exit(1);
    }

}
