package utils.cluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements ICluster {
    private List<ICluster> subClusters;

    public Cluster() {
        this.subClusters = new ArrayList<>();
    }

    @Override
    public List<String> getClusterClasses() {
        List<String> clustersComponents = new ArrayList<>();
        for (ICluster cluster :
                this.subClusters) {
            clustersComponents.addAll(cluster.getClusterClasses());
        }
        return clustersComponents;
    }

    public void addCluster (ICluster cluster) {
        this.subClusters.add(cluster);
    }

    public List<ICluster> getSubClusters() {
        return subClusters;
    }

    @Override
    public String toString() {
        StringBuilder clusterStrBuilder = new StringBuilder();
        clusterStrBuilder.append("( ");
        int clusterSize = this.subClusters.size();
        for (int i = 0; i < clusterSize; i++) {
            clusterStrBuilder.append((i != 0 ? ", " : "") + this.subClusters.get(i).toString());
        }
        clusterStrBuilder.append(" )");
        return  clusterStrBuilder.toString();
    }
}
