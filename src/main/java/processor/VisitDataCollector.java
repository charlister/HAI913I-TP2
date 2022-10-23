package processor;

import graph.Graph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Renderer;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import exceptions.EmptyProjectException;
import exceptions.NotFoundPathProjectException;
import parser.MyParser;
import visitors.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class VisitDataCollector {
    private Graph graph;
    private Set<String> linksCallGraph;
    int numberOfMethodInvocationsBetween2Classes;

    /**
     * Constructeur par défaut pour la classe {@link VisitDataCollector}
     */
    public VisitDataCollector() {
        this.graph = new Graph();
        this.linksCallGraph = new HashSet<>();
        this.numberOfMethodInvocationsBetween2Classes = 0;
    }

//    BUILD GRAPH
    private void collectGraphData(CompilationUnit cu) {
        String callerClass = "", calleeClass = "";

        TypeDeclarationVisitor visitorClass = new TypeDeclarationVisitor();
        cu.accept(visitorClass);

        for (TypeDeclaration nodeClass : visitorClass.getTypeDeclarationList()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);

            Map<MethodDeclaration, Set<MethodInvocation>> mapMethodDeclarationInvocation = new HashMap<>();
            String caller;
            callerClass = nodeClass.getName().toString();
            this.graph.addNode(callerClass);

            for (MethodDeclaration nodeMethod : visitorMethod.getMethodDeclarationList()) {
                nodeMethod.resolveBinding();
                MethodInvocationVisitor visitorMethodInvocation = new MethodInvocationVisitor();
                nodeMethod.accept(visitorMethodInvocation);
                mapMethodDeclarationInvocation.put(nodeMethod, visitorMethodInvocation.getMethodInvocations());

                caller = callerClass+"::"+nodeMethod.getName();

                for (MethodInvocation methodInvocation : visitorMethodInvocation.getMethodInvocations()) {

                    String callee;

                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                            calleeClass = methodInvocation.getExpression().resolveTypeBinding().getName();
                            callee = calleeClass+"::"+methodInvocation.getName();

                            linksCallGraph.add("\t\""+caller+"\"->\""+callee+"\"\n");
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        calleeClass = methodInvocation.resolveMethodBinding().getDeclaringClass().getName();
                        callee = calleeClass+"::"+methodInvocation.getName();

                        linksCallGraph.add("\t\""+caller+"\"->\""+callee+"\"\n");
                    }
                    else {
                        calleeClass = callerClass;
                        callee = calleeClass+"::"+methodInvocation.getName();

                        linksCallGraph.add("\t\""+caller+"\"->\""+callee+"\"\n");
                    }

                    this.graph.addNode(calleeClass);
                    this.graph.addEdge(callerClass, calleeClass);
                }
            }
        }
    }

    public float couplage(String classe1, String classe2) {
        Graph.Edge edge = graph.findEdge(classe1, classe2);
        float a = edge == null ? 0 : edge.getWeight();
        float b = 0;
        for (Graph.Edge e :
                graph.getEdges()) {
            b += e.getWeight();
        }
        return a/b;
    }

    // Draw/Display graph

    public void writeCallGraphInDotFile(String fileGraphPath) throws IOException {
        FileWriter fW = new FileWriter(fileGraphPath);
        fW.write("digraph G {\n");
        for (String link : linksCallGraph) {
            fW.write(link);
        }
        fW.write("}");
        fW.close();
        convertDotToSVG(fileGraphPath);
    }

    public void writeWeightedCouplingGraphInDotFile(String fileGraphPath) throws IOException {
        FileWriter fW = new FileWriter(fileGraphPath);
        fW.write("digraph G {\n");
        for (Graph.Edge edge : graph.getEdges()) {
            fW.write(edge.getNode1()+"->"+edge.getNode2()+String.format(" [ label=\"%s\" ]", edge.getWeight())+"\n");
        }
        fW.write("}");
        fW.close();
        convertDotToSVG(fileGraphPath);
    }

    /**
     * Convertir un fichier .dot au format .svg
     * @param fileGraphPath chemin vers le fichier .dot à convertir en svg.
     * @throws IOException
     */
    private void convertDotToSVG(String fileGraphPath) throws IOException {
        Parser p = new Parser();
        MutableGraph g = p.read(new File(fileGraphPath));
        Renderer render = Graphviz.fromGraph(g).render(Format.SVG);
        File imgFile = new File(fileGraphPath+".svg");
        if (imgFile.exists())
            imgFile.delete();
        render.toFile(imgFile);
        if (imgFile.exists())
            System.out.println(imgFile.getAbsolutePath());
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

            collectGraphData(cu);
        }
    }
}
