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

  /*      CompiledJar cj = new CompiledJar(args);
        cj.readEntry();*/


        runRequiredProblems();
    }


    public static void runRequiredProblems() throws InterruptedException{
        List<ProblemManager> problems = new ArrayList<>();
        GeneticAlgorithmConfig OX1 = new GeneticAlgorithmConfig(
                200,
                1000,
                RecombinationOperator.OX1,
                1,
                Builders.NEAREST_NEIGHBOR,
                10);
        GeneticAlgorithmConfig PMX = new GeneticAlgorithmConfig(
                200,
                10000,
                RecombinationOperator.PMX,
                1,
                Builders.NEAREST_NEIGHBOR,
                10);

        ProblemManager p1 = new ProblemManager("pr1002.tsp");
        ProblemManager p2 = new ProblemManager("fnl4461.tsp");
        ProblemManager p3 = new ProblemManager("pla7397.tsp");
        ProblemManager p4 = new ProblemManager("brd14051.tsp");
        ProblemManager p5 = new ProblemManager("d15112.tsp");
        ProblemManager p6 = new ProblemManager("d18512.tsp");
        ProblemManager p7 = new ProblemManager("pla33810.tsp");
        ProblemManager p8 = new ProblemManager("pla85900.tsp");
        problems.add(p1);
        problems.add(p2);
        problems.add(p3);
        problems.add(p4);
        problems.add(p5);
        problems.add(p6);
        problems.add(p7);
        problems.add(p8);
        for (ProblemManager p: problems){
            p.runGenetic(OX1);
        } for (ProblemManager p: problems){
            p.runGenetic(PMX);
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