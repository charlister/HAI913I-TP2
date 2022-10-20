package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.HashSet;
import java.util.Set;

public class MethodInvocationVisitor extends ASTVisitor {
    Set<MethodInvocation> methodInvocations;

    public MethodInvocationVisitor() {
        methodInvocations = new HashSet<>();
    }

    @Override
    public boolean visit(MethodInvocation node) {
        methodInvocations.add(node);
        return super.visit(node);
    }

    public Set<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }
}
