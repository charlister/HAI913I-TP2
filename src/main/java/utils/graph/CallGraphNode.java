package utils.graph;

import java.util.Objects;

public class CallGraphNode extends Node {
    private String methodName;

    public CallGraphNode(String packageName, String className, String methodName) {
        super(packageName, className);
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CallGraphNode that = (CallGraphNode) o;
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