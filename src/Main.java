import enums.Builders;
import enums.Improvers;
import geneticAlgorithm.GeneticAlgorithmConfig;
import services.CompiledJar;
import services.ProblemManager;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LongSummaryStatistics;

public class Main {

    public static void main(String[] args) {
//        printStats("att48.tsp", 500, Builders.NEAREST_NEIGHBOR, Improvers.OPT2_FIRST_IMPROVEMENT);

       if (args.length != 0) {
            CompiledJar cj = new CompiledJar(args);
            cj.readEntry();
        } else {
           ProblemManager pm = new ProblemManager();
           GeneticAlgorithmConfig gac = new GeneticAlgorithmConfig(5000, 15, 200);
           pm.runGenetic("eil101.tsp", gac);
        }
        System.out.println("Main.main");
    }

    public static void printStats(String filename, int runs, Builders constructiveAlgorithm, Improvers improvementAlgorithm){
        ArrayList<Double> solutions  = new ArrayList<>(), improvedSolutions = new ArrayList<>();
        ArrayList<Long> timeMeasures = new ArrayList<>();

        ProblemManager pm = new ProblemManager();
        pm.load(filename);

        for (int i = 0; i < runs; i++) {
            Instant start = Instant.now();

            pm.buildSolution(constructiveAlgorithm);
            pm.improveSolution(improvementAlgorithm);

            Instant end = Instant.now();

            timeMeasures.add(Duration.between(start, end).toMillis());
            solutions.add(pm.getProblem().getSolution());
            improvedSolutions.add(pm.getProblem().getImprovedSolution());
        }

        DoubleSummaryStatistics solutionsStats = new DoubleSummaryStatistics();
        DoubleSummaryStatistics improvedSolutionsStats = new DoubleSummaryStatistics();
        LongSummaryStatistics timeStats = new LongSummaryStatistics();

        for (Double d: solutions) {solutionsStats.accept(d);}
        for (Double d: improvedSolutions){improvedSolutionsStats.accept(d);}
        for (Long l: timeMeasures){timeStats.accept(l);}

        System.out.println(solutionsStats);
        System.out.println(improvedSolutionsStats);
        System.out.println(timeStats);
    }

}