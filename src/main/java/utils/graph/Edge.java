package utils.graph;

public class Edge<N extends Node> {
    protected N node1;
    protected N node2;

    public Edge(N node1, N node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public N getNode1() {
        return node1;
    }

    public N getNode2() {
        return node2;
    }

    @Override
    public String toString() {
        return "("+node1+")--------("+node2+")";
    }
}

