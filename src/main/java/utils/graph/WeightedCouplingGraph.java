package utils.graph;

import java.util.Optional;

public class WeightedCouplingGraph extends AbstractGraph {
    public WeightedCouplingGraph() {
        super();
    }

    @Override
    public void addEdge(String node1, String node2) {
        Edge edge = this.findEdge(node1, node2);
        if (edge == null) {
            this.edges.add(new WeightEdge(node1, node2));
        }
        else {
            ((WeightEdge) edge).incrWeight();
        }
    }

    @Override
    public Edge findEdge(String node1, String node2) {
        Optional<Edge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2) || e.node1.equals(node2) && e.node2.equals(node1))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
