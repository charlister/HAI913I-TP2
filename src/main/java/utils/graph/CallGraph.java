package utils.graph;

import java.util.Objects;
import java.util.Optional;

public class CallGraph extends AbstractGraph<CallGraphNode, Edge> {
    public CallGraph() {
        super();
    }

    @Override
    public void addEdge(CallGraphNode node1, CallGraphNode node2) {
        Edge edge = this.findEdge(node1, node2);
        if (edge == null) {
            this.edges.add(new Edge(node1, node2));
        }
    }

    @Override
    public Edge findEdge(CallGraphNode node1, CallGraphNode node2) {
        Optional<Edge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
    
    public WeightedCouplingGraph convertCallGraphToWeightedCouplingGraph() {
        WeightedCouplingGraph newWeightedCouplingGraph = new WeightedCouplingGraph();

        for (Node node : this.nodes) {
            newWeightedCouplingGraph.addNode(new Node(node.packageName, node.className));
        }
        for (Edge edge : this.edges) {
            newWeightedCouplingGraph.addEdge(new Node(edge.node1.packageName, edge.node1.className), new Node(edge.node2.packageName, edge.node2.className));
        }
        
        return newWeightedCouplingGraph;
    }
}
