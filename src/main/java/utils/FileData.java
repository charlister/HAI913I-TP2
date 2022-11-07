package utils;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import visitors.PackageDeclarationVisitor;
import visitors.TypeDeclarationVisitor;

public class FileData {
    private PackageDeclaration packageDeclaration;
    private TypeDeclaration typeDeclaration;
    PackageDeclarationVisitor visitorPackage;
    TypeDeclarationVisitor visitorClass;

    public FileData(CompilationUnit cu) {
        this.visitorPackage = new PackageDeclarationVisitor();
        cu.accept(visitorPackage);
        this.packageDeclaration = visitorPackage.getPackageDeclaration();

        this.visitorClass = new TypeDeclarationVisitor();
        cu.accept(visitorClass);
        this.typeDeclaration = visitorClass.getTypeDeclaration();
    }

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    public String getPackageDeclarationName() {
        return packageDeclaration.getName().toString();
    }

    public String getTypeDeclarationName() {
        return typeDeclaration.getName().toString();
    }

    public String getFullClassName() {
        return getPackageDeclarationName() + "." + getTypeDeclarationName();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getPackageDeclarationName()+"\n");
        result.append("\t"+this.getTypeDeclarationName()+"\n");
        return result.toString();
    }
}