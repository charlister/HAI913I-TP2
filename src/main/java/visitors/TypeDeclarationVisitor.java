package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
    private List<TypeDeclaration> typeDeclarationList;

    public TypeDeclarationVisitor() {
        this.typeDeclarationList = new ArrayList<>();
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        if(!node.isInterface() /* for not considerate interfaces */ /* && !node.isMemberTypeDeclaration() /* to ignore nested classes */) {
            typeDeclarationList.add(node);
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        System.out.println("( CLASS ) " + node.getName() + " [ VISITED ]");
        super.endVisit(node);
    }

    public List<TypeDeclaration> getTypeDeclarationList() {
        return typeDeclarationList;
    }
}
