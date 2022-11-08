package utils.graph;

public class Edge {
    protected String node1;
    protected String node2;

    public Edge(String node1, String node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    @Override
    public String toString() {
        return "("+node1+")--------("+node2+")";
    }
}

