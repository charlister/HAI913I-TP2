package utils.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGraph {
    protected List<String> nodes;
    protected List<Edge> edges;

    public AbstractGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addNode(String node) {
        Optional<String> result = nodes.stream().filter(e -> e.equals(node)).findFirst();
        if (!result.isPresent()) {
            this.nodes.add(node);
        }
    }

    public abstract void addEdge(String node1, String node2) ;

    public abstract Edge findEdge (String node1, String node2);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("nodes : \n");
        for (String node : nodes) {
            stringBuilder.append("\t- " + node + "\n");
        }
        for (Edge edge : edges) {
            stringBuilder.append(edge.toString()+"\n");
        }
        return stringBuilder.toString();
    }
}
