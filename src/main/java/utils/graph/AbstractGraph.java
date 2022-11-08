package utils.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGraph<N extends Node, E extends Edge> {
//    protected List<String> nodes;
    protected List<N> nodes;
    protected List<E> edges;

    public AbstractGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public List<N> getNodes() {
        return nodes;
    }

    public List<E> getEdges() {
        return edges;
    }

    public void addNode(N node) {
        Optional<N> result = nodes.stream().filter(e -> e.equals(node)).findFirst();
        if (!result.isPresent()) {
            this.nodes.add(node);
        }
    }

    public abstract void addEdge(N node1, N node2) ;

    public abstract E findEdge (N node1, N node2);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("nodes : \n");
        for (N node : nodes) {
            stringBuilder.append("\t- " + node + "\n");
        }
        for (E edge : edges) {
            stringBuilder.append(edge.toString()+"\n");
        }
        return stringBuilder.toString();
    }
}
