package main;

import exceptions.EmptyProjectException;
import exceptions.NotFoundPathProjectException;
import processor.Processor;
import utils.cluster.Cluster;
import utils.cluster.ICluster;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// afficher le graphe
// calculer le couplage
public class Application {
    private Scanner sc;
    private StringBuilder menu;

    /**
     * Constructeur par défaut pour la classe {@link Application}
     */
    public Application() {
        sc = new Scanner(System.in);
        buildMenu();
    }

    /**
     * cette méthode sert à initialiser l'attribut menu.
     */
    private void buildMenu() {
        menu = new StringBuilder();
        menu.append("==============================   MENU   ==============================\n");
        menu.append("0. Analyser un nouveau projet.\n");
        menu.append("1. Générer un graphe d'appel.\n");
        menu.append("2. Calculer le couplage entre deux classes.\n");
        menu.append("3. Générer un graphe de couplage pondéré.\n");
        menu.append("4. Générer un dendrogramme.\n");
        menu.append("5. Identification de modules.\n");
        menu.append("q. Quitter l’application.\n");
    }

    /**
     * Cette méthode sert à afficher le menu de l'application.
     */
    private void displayMenu() {
        System.out.print(menu);
    }

    /**
     * Cette méthode permet à l'utilisateur d'interagir avec l'application.
     * @param processor cet objet permet d'accéder à l'ensemble des données recueillies lors de l'analyse du projet.
     * @throws IOException
     * @throws InterruptedException
     * @throws EmptyProjectException
     * @throws NotFoundPathProjectException
     */
    private void chooseAFeatures(Processor processor) throws IOException, InterruptedException, EmptyProjectException, NotFoundPathProjectException {
        String choice = "";
        String graphName;
        String classe1;
        String classe2;
        ICluster dendrogramme = null;
        while (!choice.equals("q")) {
            displayMenu();
            Thread.sleep(500);
            System.out.print("\nCHOISIR UNE OPTION : ");
            choice = sc.nextLine();
            switch (choice.trim()) {
                case "0":
                    System.out.print("Veuillez indiquer le repertoire vers le nouveau projet à analyser : ");
                    String projectPath = sc.nextLine();
                    Processor newProcessor = new Processor();
                    newProcessor.makeAnalysis(projectPath);
                    dendrogramme = null;
                    chooseAFeatures(newProcessor);
                    break;
                case "1":
                    System.err.println("Génération du graphe d'appel ...");
                    Thread.sleep(500);
                    System.out.print("Nom du graphe d'appel : ");
                    graphName = sc.nextLine().trim();
                    processor.writeCallGraphInDotFile(graphName);
                    Thread.sleep(500);
                    System.out.println("Graphe d'appel généré !");
                    Thread.sleep(500);
                    break;
                case "2":
                    System.err.println("Saisissez les deux classes pour lesquelles vous souhaitez déterminer un couplage : ");
                    Thread.sleep(500);
                    System.out.print("classe1 : ");
                    classe1 = sc.nextLine().trim();
                    System.out.print("classe2 : ");
                    classe2 = sc.nextLine().trim();
                    System.out.println(String.format("Couplage (%s, %s) = %f.", classe1, classe2, processor.couplage(classe1, classe2)));
                    Thread.sleep(500);
                    break;
                case "3":
                    System.err.println("Génération du graphe de couplage pondéré ...");
                    Thread.sleep(500);
                    System.out.print("Nom du graphe de couplage pondéré : ");
                    graphName = sc.nextLine().trim();
                    processor.writeWeightedCouplingGraphInDotFile(graphName);
                    Thread.sleep(500);
                    break;
                case "4":
                    dendrogramme = processor.clusteringHierarchic();
                    System.out.println(dendrogramme);
                    Thread.sleep(500);
                    break;
                case "5":
                    if (dendrogramme == null) {
                        System.err.println("Générer le dendrogramme à partir de l'option 4 avant d'employer l'option 5.");
                        break;
                    }
                    System.err.print("Préciser la valeur de CP : ");
                    float CP = sc.nextFloat();
                    List<ICluster> modules = processor.identifyModules(dendrogramme, CP);

                    for (ICluster groupe : modules) {
                        System.out.println("??? "+groupe.toString());
                    }
                    Thread.sleep(1000);
                    break;
                case "q":
                    break;
                default:
                    System.err.println("Choix incorrect ... Veuillez recommencer !");
                    Thread.sleep(1000);
                    break;
            }
        }
    }

    /**
     * Cette méthode permet démarrer l'application.
     * @throws IOException
     * @throws InterruptedException
     * @throws EmptyProjectException
     * @throws NotFoundPathProjectException
     */
    private void launch() throws IOException, InterruptedException, EmptyProjectException, NotFoundPathProjectException {
        System.out.print("Veuillez indiquer le repertoire vers le projet à analyser : ");
        String projectPath = sc.nextLine().trim();

        Processor processor = new Processor();
        processor.makeAnalysis(projectPath);

        chooseAFeatures(processor);
    }

    public static void main(String[] args) throws IOException, InterruptedException, EmptyProjectException, NotFoundPathProjectException {
        System.out.println
                (
                        " -----------------------------------------------\n" +
                                "| CETTE INTERFACE MET A VOTRE DISPOSITION UN    |\n" +
                                "| ENSEMBLE DE FONCTIONNALITES VOUS PERMETTANT   |\n" +
                                "| DE REALISER UNE ANALYSE STATIQUE D'UN PROJET. |\n" +
                                " -----------------------------------------------\n"
                );

        Application application = new Application();
        application.launch();
    }
}
