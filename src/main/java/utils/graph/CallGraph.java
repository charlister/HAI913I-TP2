package utils.graph;

import java.util.Optional;

public class CallGraph extends AbstractGraph {
    public CallGraph() {
        super();
    }

    @Override
    public void addEdge(String node1, String node2) {
        Edge edge = this.findEdge(node1, node2);
        if (edge == null) {
            this.edges.add(new Edge(node1, node2));
        }
    }

    @Override
    public Edge findEdge(String node1, String node2) {
        Optional<Edge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
