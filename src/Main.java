import enums.Builders;
import enums.Improvers;
import geneticAlgorithm.GeneticAlgorithmConfig;
import geneticAlgorithm.PopulationCriteria;
import geneticAlgorithm.RecombinationOperator;
import geneticAlgorithm.SelectionCriteria;
import services.CompiledJar;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CompiledJar cj = new CompiledJar(args);
        cj.readEntry();

      /*  ProblemManager pm = new ProblemManager("att48.tsp");
        GeneticAlgorithmConfig config = new GeneticAlgorithmConfig(
                1000,
                10000,
                RecombinationOperator.OX1,
                5,
                Builders.NEAREST_NEIGHBOR,
                12);
        pm.runGenetic(config);*/

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