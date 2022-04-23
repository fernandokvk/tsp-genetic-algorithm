import enums.Builders;
import enums.Improvers;
import geneticAlgorithm.GeneticAlgorithmConfig;
import geneticAlgorithm.PopulationCriteria;
import geneticAlgorithm.RecombinationOperator;
import geneticAlgorithm.SelectionCriteria;
import services.CompiledJar;
import services.ProblemManager;

public class Main {

    public static void main(String[] args){
//        printStats("att48.tsp", 500, Builders.NEAREST_NEIGHBOR, Improvers.OPT2_FIRST_IMPROVEMENT);

        if (args.length != 0) {
            CompiledJar cj = new CompiledJar(args);
            cj.readEntry();
        } else {
            ProblemManager pm = new ProblemManager("att48.tsp");
            GeneticAlgorithmConfig config = new GeneticAlgorithmConfig(
                    10000,
                    10,
                    RecombinationOperator.OX1,
                    5,
                    Builders.NEAREST_NEIGHBOR);
            pm.runGenetic(config);
        }
        System.out.println("Main.main");
    }

    public static void printStats(String filename, int runs, Builders constructiveAlgorithm, Improvers improvementAlgorithm) {

    }

    public static void savedConfigs() {
        GeneticAlgorithmConfig config_att48 =
                new GeneticAlgorithmConfig(100,
                        50000,
                        SelectionCriteria.TOURNAMENT,
                        PopulationCriteria.ELITISM,
                        50,
                        RecombinationOperator.PMX,
                        1,
                        Builders.FARTHEST_INSERTION,
                        Improvers.OPT3_FIRST_IMPROVEMENT);

        GeneticAlgorithmConfig config_tcc = new GeneticAlgorithmConfig(
                600,
                50000,
                SelectionCriteria.ROULETTE,
                PopulationCriteria.STEADY_STATED,
                50,
                RecombinationOperator.PMX,
                1,
                Builders.FARTHEST_INSERTION,
                Improvers.OPT2_FIRST_IMPROVEMENT);

    }

}