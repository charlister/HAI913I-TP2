package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
    private List<TypeDeclaration> typeDeclarationList;
    private TypeDeclaration typeDeclaration;

    public TypeDeclarationVisitor() {
        this.typeDeclarationList = new ArrayList<>();
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        if(!node.isInterface() /* for not considerate interfaces */ /* && !node.isMemberTypeDeclaration() /* to ignore nested classes */) {
            typeDeclarationList.add(node);
            if(!node.isMemberTypeDeclaration())
                typeDeclaration = node;
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

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    public int isClassNameAppearingInTypeDeclarations (String className) {
        int result = -1;
        for (TypeDeclaration typeDeclaration: typeDeclarationList) {
            if(typeDeclaration.getName().toString().equals(className)) {
                if (typeDeclaration.isMemberTypeDeclaration())
                    result = 0;
                else
                    result = 1;
                break;
            }
        }
        return result;
    }
}
