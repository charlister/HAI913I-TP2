package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ImportDeclarationVisitor extends ASTVisitor {
    List<ImportDeclaration> importDeclarations;

    public ImportDeclarationVisitor() {
        this.importDeclarations = new ArrayList<>();
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        this.importDeclarations.add(node);
        return super.visit(node);
    }

    @Override
    public void endVisit(ImportDeclaration node) {
        System.out.println("( IMPORT ) " + node.getName() + " [ VISITED ]");
        super.endVisit(node);
    }

    public List<ImportDeclaration> getImportDeclarations() {
        return importDeclarations;
    }

    public String findPackageClassFromImportDeclarations(String className) {
        String result = "?";
        for (ImportDeclaration importDeclaration :
                importDeclarations) {
            if (importDeclaration.getName().toString().contains(format(".%s\0", className))) {
                result = importDeclaration.getName().toString();
                break;
            }
        }
        return result;
    }
}
