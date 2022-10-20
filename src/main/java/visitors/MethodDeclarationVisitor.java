package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
    private List<MethodDeclaration> methodDeclarationList;

    public MethodDeclarationVisitor() {
        this.methodDeclarationList = new ArrayList<>();
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        methodDeclarationList.add(node);
        return super.visit(node);
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        System.out.println("( METHOD ) " + node.getName() + " [ VISITED ]");
        super.endVisit(node);
    }
    
    public List<MethodDeclaration> getMethodDeclarationList() {
        return methodDeclarationList;
    }
}
