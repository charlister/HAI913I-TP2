package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.ArrayList;
import java.util.List;

public class PackageDeclarationVisitor extends ASTVisitor {
    private String packageDeclarationName = "";
    PackageDeclaration packageDeclaration;

    @Override
    public boolean visit(PackageDeclaration node) {
        packageDeclaration = node;
        packageDeclarationName = node.getName().toString();
        return super.visit(node);
    }

    @Override
    public void endVisit(PackageDeclaration node) {
        System.out.println("( PACKAGE ) " + packageDeclarationName + " [ VISITED ]");
        super.endVisit(node);
    }

    public List<String> buildSubPackages() {
        List<String> result = new ArrayList<>();
        if (!packageDeclarationName.equals("")) {
            if (packageDeclarationName.contains(".")) {
                String[] packageSplit = packageDeclarationName.split("\\.");

                String str = "";
                for (String s : packageSplit) {
                    str += ((str.equals("") ? "" : ".") + s);
                    result.add(str);
                }
            }
            else result.add(packageDeclarationName);
        }

        return result;
    }

    public PackageDeclaration getPackageDeclaration() {
        return packageDeclaration;
    }
}
