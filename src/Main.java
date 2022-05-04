import enums.Builders;
import enums.Improvers;
import geneticAlgorithm.GeneticAlgorithmConfig;
import geneticAlgorithm.PopulationCriteria;
import geneticAlgorithm.RecombinationOperator;
import geneticAlgorithm.SelectionCriteria;
import services.ProblemManager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

       /* CompiledJar cj = new CompiledJar(args);
        cj.readEntry();*/


        runSmallProblems();
    }

    public static void runSmallProblems() throws InterruptedException {
        List<ProblemManager> smallProblems = new ArrayList<>();
        GeneticAlgorithmConfig smallConfigOX1 = new GeneticAlgorithmConfig(
                1000,
                10000,
                RecombinationOperator.OX1,
                5,
                Builders.NEAREST_NEIGHBOR,
                10);
        GeneticAlgorithmConfig smallConfigPOS = new GeneticAlgorithmConfig(
                1000,
                50000,
                RecombinationOperator.POS,
                5,
                Builders.NEAREST_NEIGHBOR,
                10);
        ProblemManager p1 = new ProblemManager("att48.tsp");
        ProblemManager p2 = new ProblemManager("eil101.tsp");
        ProblemManager p3 = new ProblemManager("kroA150.tsp");
        ProblemManager p4 = new ProblemManager("gil262.tsp");
        ProblemManager p5 = new ProblemManager("a280.tsp");
        ProblemManager p6 = new ProblemManager("lin318.tsp");
        ProblemManager p7 = new ProblemManager("pr439.tsp");
        ProblemManager p8 = new ProblemManager("rat575.tsp");
        ProblemManager p9 = new ProblemManager("rat783.tsp");
        smallProblems.add(p1);
        smallProblems.add(p2);
        smallProblems.add(p3);
        smallProblems.add(p4);
        smallProblems.add(p5);
        smallProblems.add(p6);
        smallProblems.add(p7);
        smallProblems.add(p8);
        smallProblems.add(p9);
        for (ProblemManager p: smallProblems){
            p.runGenetic(smallConfigOX1);
        } for (ProblemManager p: smallProblems){
            p.runGenetic(smallConfigPOS);
        }
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