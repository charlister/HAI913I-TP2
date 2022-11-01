package parser;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import exceptions.NotFoundPathProjectException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyParser {
    private File folder;

    /**
     * Constructeur de la classe {@link MyParser} destinée à créer un AST pour chacun des fichiers source du projet
     * @param projectPath un chemin vers le projet à analyser.
     * @throws NotFoundPathProjectException
     */
    public MyParser(String projectPath) throws NotFoundPathProjectException {
        this.folder = new File(projectPath);
        if (!this.folder.exists() || !this.folder.isDirectory()) {
            throw new NotFoundPathProjectException("Le dossier "+projectPath+" spécifié est introuvable.");
        }
    }

    /**
     *
     * @param folder
     * @return
     */
    public List<File> listJavaFilesForFolderBis(File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolderBis(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                System.out.println(fileEntry.getName());
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    /**
     * Obtenir la liste des fichiers sources (.java) que contient le projet
     * @return liste des fichiers sources (.java) du projet analysé
     */
    public List<File> listJavaFilesForFolder() {
        return listJavaFilesForFolderBis(this.folder);
    }

    /**
     * Obtenir un AST ComplexNode pour l'extrait de code à analyser.
     * @param classSource un tableau de caractères relatifs à l'extrait de code à analyser.
     * @return un AST ComplexNode pour la séquence de caractères représentant du code java.
     */
    public CompilationUnit parseSource(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        Map options = JavaCore.getOptions();
        parser.setBindingsRecovery(true);
        parser.setCompilerOptions(options);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setUnitName("");
        String[] sources = { "" };
        String[] classpath = { "" };
        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setSource(classSource);
        return (CompilationUnit) parser.createAST(null);
    }
}
