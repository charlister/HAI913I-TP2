package utils.graph;

public class WeightEdge extends Edge{
    private int weight;

    public WeightEdge(String node1, String node2) {
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
