package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Graph {
    public class ComplexNode {
        private String packageName;
        private String className;

        public ComplexNode(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public ComplexNode(String className) {
            this.packageName = "?";
            this.className = className;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getClassName() {
            return className;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ComplexNode complexNode = (ComplexNode) o;
            return Objects.equals(packageName, complexNode.packageName) && Objects.equals(className, complexNode.className);
        }

        @Override
        public int hashCode() {
            return Objects.hash(packageName, className);
        }

        @Override
        public String toString() {
            return packageName + '.' + className;
        }
    }
    public class Edge {
        ComplexNode complexNode1;
        ComplexNode complexNode2;
        String node1;
        String node2;
        int weight;

        public Edge(String node1, String node2) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = 1;
        }

        public Edge(ComplexNode complexNode1, ComplexNode complexNode2) {
            this.complexNode1 = complexNode1;
            this.complexNode2 = complexNode2;
            this.weight = 1;
        }

        public int getWeight() {
            return weight;
        }

        public String getNode1() {
            return node1;
        }

        public String getNode2() {
            return node2;
        }

        public void setComplexNode1(ComplexNode complexNode1) {
            this.complexNode1 = complexNode1;
        }

        public void setComplexNode2(ComplexNode complexNode2) {
            this.complexNode2 = complexNode2;
        }

        public void incrWeight() {
            this.weight++;
        }
    }

    List<String> nodes;
    List<Edge> edges;

    public Graph() {
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

    public void addEdge(String node1, String node2) {
        Optional<Edge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2) || e.node1.equals(node2) && e.node2.equals(node1))
                .findFirst();
        if (!result.isPresent()) {
            this.edges.add(new Edge(node1, node2));
        }
        else {
            result.get().incrWeight();
        }
    }

    public Edge findEdge (String node1, String node2) {
        Optional<Edge> result = edges.stream()
                .filter(e -> e.node1.equals(node1) && e.node2.equals(node2) || e.node1.equals(node2) && e.node2.equals(node1))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
