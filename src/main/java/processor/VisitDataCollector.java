package processor;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import exceptions.EmptyProjectException;
import exceptions.NotFoundPathProjectException;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import parser.MyParser;
import visitors.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class VisitDataCollector {
    private List<String> listEdges;
    private List<TypeDeclaration> typeDeclarations;
    private DefaultDirectedGraph<String, DefaultEdge> graphJGraphT;

    /**
     * Constructeur par défaut pour la classe {@link VisitDataCollector}
     */
    public VisitDataCollector() {
        this.listEdges = new ArrayList<>();
        this.typeDeclarations = new ArrayList<>();
        this.graphJGraphT = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

//    BUILD GRAPH
    private void collectTypeDeclarations(CompilationUnit cu) {
        TypeDeclarationVisitor visitorClass = new TypeDeclarationVisitor();
        cu.accept(visitorClass);
        typeDeclarations.addAll(visitorClass.getTypeDeclarationList());
    }

    private void collectMethodInvocationsOfProject() {
        boolean isMethodNodeAdded;

        for (TypeDeclaration nodeClass : typeDeclarations) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);

            Map<MethodDeclaration, Set<MethodInvocation>> mapMethodDeclarationInvocation = new HashMap<>();
            String caller;

            for (MethodDeclaration nodeMethod : visitorMethod.getMethodDeclarationList()) {
                nodeMethod.resolveBinding();
                MethodInvocationVisitor visitorMethodInvocation = new MethodInvocationVisitor();
                nodeMethod.accept(visitorMethodInvocation);
                mapMethodDeclarationInvocation.put(nodeMethod, visitorMethodInvocation.getMethodInvocations());

                caller = nodeClass.getName().toString()+"::"+nodeMethod.getName();

                isMethodNodeAdded = false;

                for (MethodInvocation methodInvocation : visitorMethodInvocation.getMethodInvocations()) {

                    String callee;

                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                            callee = methodInvocation.getExpression().resolveTypeBinding().getName()+"::"+methodInvocation.getName();

                            if (!isMethodNodeAdded) {
                                graphJGraphT.addVertex(caller);
                                isMethodNodeAdded = true;
                            }
                            graphJGraphT.addVertex(callee);
                            graphJGraphT.addEdge(caller, callee);

                            if (!listEdges.contains(caller+"->"+callee)) { // comme un Set, car on évite les doublons
                                listEdges.add(caller+"->"+callee);
                            }
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        callee = methodInvocation.resolveMethodBinding().getDeclaringClass().getName()+"::"+methodInvocation.getName();

                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(caller);
                            isMethodNodeAdded = true;
                        }
                        graphJGraphT.addVertex(callee);
                        graphJGraphT.addEdge(caller, callee);

                        if (!listEdges.contains(caller+"->"+callee)) {
                            listEdges.add(caller+"->"+callee);
                        }
                    }
                    else {
                        callee = nodeClass.getName()+"::"+methodInvocation.getName();

                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(caller);
                            isMethodNodeAdded = true;
                        }
                        graphJGraphT.addVertex(callee);
                        graphJGraphT.addEdge(caller, callee);

                        if (!listEdges.contains(caller+"->"+callee)) {
                            listEdges.add(caller+"->"+callee);
                        }
                    }
                }
            }
        }
    }

    private int countNumberOfMethodInvocationsBetween2Classes(String classe1, String classe2) {
        List<String> selection = new ArrayList<>();
        int result = 0;

        for (TypeDeclaration nodeClass : typeDeclarations) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);

            Map<MethodDeclaration, Set<MethodInvocation>> mapMethodDeclarationInvocation = new HashMap<>();
            String caller;

            for (MethodDeclaration nodeMethod : visitorMethod.getMethodDeclarationList()) {
                nodeMethod.resolveBinding();
                MethodInvocationVisitor visitorMethodInvocation = new MethodInvocationVisitor();
                nodeMethod.accept(visitorMethodInvocation);
                mapMethodDeclarationInvocation.put(nodeMethod, visitorMethodInvocation.getMethodInvocations());

                caller = nodeClass.getName().toString()+"::"+nodeMethod.getName();

                for (MethodInvocation methodInvocation : visitorMethodInvocation.getMethodInvocations()) {

                    String callee;

                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                            callee = methodInvocation.getExpression().resolveTypeBinding().getName()+"::"+methodInvocation.getName();
                            if (!selection.contains(caller+"->"+callee)) {
                                if (classe1.equals(nodeClass.getName().toString()) &&  classe2.equals(methodInvocation.getExpression().resolveTypeBinding().getName())
                                || classe2.equals(nodeClass.getName().toString()) && classe1.equals(methodInvocation.getExpression().resolveTypeBinding().getName())) {
                                    selection.add(caller+"->"+callee);
                                    result++;
                                }
                            }
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        callee = methodInvocation.resolveMethodBinding().getDeclaringClass().getName()+"::"+methodInvocation.getName();
                        if (!selection.contains(caller+"->"+callee)) {
                            if (classe1.equals(nodeClass.getName().toString()) && classe2.equals(methodInvocation.resolveMethodBinding().getDeclaringClass().getName())
                            || classe2.equals(nodeClass.getName().toString()) && classe1.equals(methodInvocation.resolveMethodBinding().getDeclaringClass().getName())) {
                                selection.add(caller+"->"+callee);
                                result++;
                            }
                        }
                    }
                    else {
                        callee = nodeClass.getName()+"::"+methodInvocation.getName();
                        if (!selection.contains(caller+"->"+callee)) {
                            if (classe1.equals(nodeClass.getName().toString()) && classe2.equals(nodeClass.getName())
                                    || classe2.equals(nodeClass.getName().toString()) && classe1.equals(nodeClass.getName())) {
                                selection.add(caller+"->"+callee);
                                result++;
                            }
                        }
                    }
                }
            }
            System.out.println("SELECTION : "+selection);
        }
        return result;
    }

    public float couplage(String classe1, String classe2) {
        return (float)countNumberOfMethodInvocationsBetween2Classes(classe1, classe2)/listEdges.size();
    }

    // Draw/Display graph

    /**
     * Afficher en mode console le graphe d'appel.
     * @param aCallGraph
     */
    public static void displayTheCAllGraph(Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> aCallGraph) {
        Set<Map.Entry<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>>> set = aCallGraph.entrySet();
        String callee;
        for (Map.Entry<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> mapEntry1 : set) {
            System.out.println("(CLASS) : " + mapEntry1.getKey().getName());
            for (Map.Entry<MethodDeclaration, Set<MethodInvocation>> mapEntry2: mapEntry1.getValue().entrySet()) {
                System.out.println("\t(METHOD) : " + mapEntry2.getKey().getName());
                for (MethodInvocation methodInvocation : mapEntry2.getValue()) {
                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                            callee = methodInvocation.getExpression().resolveTypeBinding().getName()+"::"+methodInvocation.getName();
                            System.out.println("\t\t(CALL) : " + callee);
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        callee = methodInvocation.resolveMethodBinding().getDeclaringClass().getName()+"::"+methodInvocation.getName();
                        System.out.println("\t\t(CALL) : " + callee);
                    }
                }
            }
        }
    }

    /**
     * Réaliser un affichage graphique avec JGraphT
     * @throws IOException
     */
    public void buildGraphWithJGraphT() throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graphJGraphT);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("graph_jgrapht.png");
        if (imgFile.exists())
            imgFile.delete();

        ImageIO.write(image, "PNG", imgFile);

        if (!imgFile.exists()) {
            System.err.println("Le fichier "+imgFile.getName()+" n'a pas pu être créé !");
        }
        else {
            System.out.println(imgFile.getAbsolutePath());
        }
    }

//    COLLECT DATA PROJECT

    /**
     * collecter l'ensemble des données souhaitées pour le TP.
     * @param pathProject chemin vers le projet à analyser.
     * @throws IOException
     * @throws EmptyProjectException
     * @throws NotFoundPathProjectException
     */
    public void makeAnalysis(String pathProject) throws IOException, EmptyProjectException, NotFoundPathProjectException {
        MyParser parser = new MyParser(pathProject);
        List<File> javaFiles = parser.listJavaFilesForFolder();
        if (javaFiles.isEmpty()) {
            throw new EmptyProjectException("Le project "+pathProject+" ne contient aucun fichier source.");
        }
        for(File javaFile : javaFiles) {
            String content = FileUtils.readFileToString(javaFile);
            CompilationUnit cu = parser.parseSource(content.toCharArray());

            collectTypeDeclarations(cu);
        }
        collectMethodInvocationsOfProject();
    }
}
