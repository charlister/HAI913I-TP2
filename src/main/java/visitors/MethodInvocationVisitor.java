package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodInvocationVisitor extends ASTVisitor {
    List<MethodInvocation> methodInvocations;

    public MethodInvocationVisitor() {
        methodInvocations = new ArrayList<>();
    }

    @Override
    public boolean visit(MethodInvocation node) {
        methodInvocations.add(node);
        return super.visit(node);
    }

    public List<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }
}
