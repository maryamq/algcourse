import java.util.ArrayList;
import java.util.List;

public class SAP {
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
       int ancestor = ancestor(v,w);
        if (ancestor == -1) {
            return -1;
        }
        BreadthFirstDirectedPaths bfsV = new  BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new  BreadthFirstDirectedPaths(graph, w);
        return bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new  BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new  BreadthFirstDirectedPaths(graph, w);
        int pathLen = -1;
        int ancestor = -1;
        for (int vertex = 0; vertex < this.graph.V(); vertex++) {
            if (!bfsV.hasPathTo(vertex) || !bfsW.hasPathTo(vertex)){
                continue;
            }

            int pathV = bfsV.distTo(vertex);
            int pathW = bfsW.distTo(vertex);
            int sumPath = pathV + pathW;
            if (sumPath < pathLen || pathLen < 0) {
                pathLen = sumPath;
                ancestor = vertex;
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for(Integer vertexV : v) {
            for (Integer vertexW: w) {
                int pathLen = this.length(vertexV, vertexW);
                if (pathLen < shortest) {
                    shortest = pathLen;
                    ancestor = this.ancestor(vertexV, vertexW);
                }
            }
        }
        if (ancestor == -1) {
            return -1;
        }
        return shortest;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for(Integer vertexV : v) {
            for (Integer vertexW: w) {
                int pathLen = this.length(vertexV, vertexW);
                if (pathLen < shortest) {
                    shortest = pathLen;
                    ancestor = this.ancestor(vertexV, vertexW);
                }
            }
        }
        if (ancestor == -1) {
            return -1;
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}