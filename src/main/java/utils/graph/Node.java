package utils.graph;

import java.util.Objects;

public class Node {
    protected String packageName;
    protected String className;

    public Node(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return this.className;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return this.packageName.equals(((Node) obj).packageName) && this.className.equals(((Node) obj).className);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, className);
    }

    @Override
    public String toString() {
        return this.packageName+"."+this.className;
    }
}
