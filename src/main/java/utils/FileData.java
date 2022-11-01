package utils;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import visitors.ImportDeclarationVisitor;
import visitors.PackageDeclarationVisitor;
import visitors.TypeDeclarationVisitor;

import java.util.ArrayList;
import java.util.List;

public class FileData {
    private PackageDeclaration packageDeclaration;
    private List<ImportDeclaration> importDeclarations;
    private TypeDeclaration typeDeclaration;
    ImportDeclarationVisitor visitorImport;
    PackageDeclarationVisitor visitorPackage;
    TypeDeclarationVisitor visitorClass;

    public FileData(CompilationUnit cu) {
        this.visitorPackage = new PackageDeclarationVisitor();
        cu.accept(visitorPackage);
        this.packageDeclaration = visitorPackage.getPackageDeclaration();

        this.visitorImport = new ImportDeclarationVisitor();
        cu.accept(visitorImport);
        this.importDeclarations = visitorImport.getImportDeclarations();

        this.visitorClass = new TypeDeclarationVisitor();
        cu.accept(visitorClass);
        this.typeDeclaration = visitorClass.getTypeDeclaration();

        System.out.println(this);
    }

    public PackageDeclaration getPackageDeclaration() {
        return packageDeclaration;
    }

    public List<ImportDeclaration> getImportDeclarations() {
        return importDeclarations;
    }

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    public String getPackageDeclarationName() {
        return packageDeclaration.getName().toString();
    }

    public List<String> getImportDeclarationsName() {
        List<String> result = new ArrayList<>();
        for (ImportDeclaration importDeclaration : importDeclarations) {
            result.add(importDeclaration.getName().toString());
        }
        return result;
    }

    public String getTypeDeclarationName() {
        return typeDeclaration.getName().toString();
    }

    public String getClassPackage(String className) {
        String result = "?";
        switch (visitorClass.isClassNameAppearingInTypeDeclarations(className)) {
            case -1:
                result = visitorImport.findPackageClassFromImportDeclarations(className);
                break;
            case 0:
                result = getPackageDeclarationName()+"."+getTypeDeclarationName();
                break;
            case 1:
                result = getPackageDeclarationName();
                break;
            default:
                break;
        }
        return result;
    }

    public String getFullClassName(String className) {
        return getClassPackage(className) + "." + className;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getPackageDeclarationName()+"\n");
        for (String importName : this.getImportDeclarationsName()) {
            result.append("\t"+importName+"\n");
        }
        result.append("\t\t"+this.getTypeDeclarationName()+"\n");
        return result.toString();
    }
}