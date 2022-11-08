package utils.graph;

import java.util.Objects;
import java.util.Optional;

public class CallGraph extends AbstractGraph<CallGraph.NodeCallGraph, Edge> {
    public static class NodeCallGraph extends Node {
        private String methodName;

        public NodeCallGraph(String packageName, String className, String methodName) {
            super(packageName, className);
            this.methodName = methodName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            NodeCallGraph that = (NodeCallGraph) o;
            return methodName.equals(that.methodName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), methodName);
        }

        @Override
        public String toString() {
            return super.toString()+"::"+methodName;
        }
    }
    public CallGraph() {
        super();
    }

    @Override
    public void addEdge(NodeCallGraph node1, NodeCallGraph node2) {
        Edge edge = this.findEdge(node1, node2);
        if (edge == null) {
            this.edges.add(new Edge(node1, node2));
        }
    }

    @Override
    public Edge findEdge(NodeCallGraph node1, NodeCallGraph node2) {
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
