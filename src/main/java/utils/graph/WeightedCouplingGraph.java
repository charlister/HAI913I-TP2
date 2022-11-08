package utils.graph;

import java.util.Optional;

public class WeightedCouplingGraph extends AbstractGraph<Node, WeightedEdge> {
    public WeightedCouplingGraph() {
        super();
    }

    @Override
    public void addEdge(Node node1, Node node2) {
        Edge edge = this.findEdge(node1, node2);
        if (edge == null) {
            this.edges.add(new WeightedEdge(node1, node2));
        }
        else {
            ((WeightedEdge) edge).incrWeight();
        }
    }

    @Override
    public WeightedEdge findEdge(Node node1, Node node2) {
        Optional<WeightedEdge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2) || e.node1.equals(node2) && e.node2.equals(node1))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public WeightedEdge findEdge(String node1, String node2) {
        Optional<WeightedEdge> result = edges.stream()
                .filter(e -> e.node1.toString().equals(node1) && e.node2.toString().equals(node2) || e.node1.toString().equals(node2) && e.node2.toString().equals(node1))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
