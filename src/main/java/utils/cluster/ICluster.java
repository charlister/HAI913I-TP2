package utils.cluster;

import java.util.List;

public interface ICluster {
    List<String> getClusterClasses();
    List<ICluster> getSubClusters();
}
