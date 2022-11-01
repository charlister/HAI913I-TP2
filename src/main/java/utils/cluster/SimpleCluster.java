package utils.cluster;

import java.util.ArrayList;
import java.util.List;

public class SimpleCluster implements ICluster {
    private String cluster;

    public SimpleCluster(String cluster) {
        this.cluster = cluster;
    }

    @Override
    public List<String> getClusterComponents() {
        List<String> clusterComponents = new ArrayList<>();
        clusterComponents.add(this.cluster);
        return clusterComponents;
    }

    @Override
    public String toString() {
        return cluster;
    }
}
