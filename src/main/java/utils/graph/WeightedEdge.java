package utils.graph;

public class WeightedEdge extends Edge<Node>{
    private int weight;

    public WeightedEdge(Node node1, Node node2) {
        super(node1, node2);
        this.weight = 1;
    }

    public int getWeight() {
        return weight;
    }

    public void incrWeight() {
        this.weight++;
    }

    @Override
    public String toString() {
        return "("+node1+")----"+weight+"----("+node2+")";
    }
}