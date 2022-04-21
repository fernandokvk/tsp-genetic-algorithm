import algorithms.ConstructiveAlgorithms;
import algorithms.ImprovementAlgorithms;
import models.Problem;
import services.CompiledJar;
import services.FileManager;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LongSummaryStatistics;

public class Main {

    public static void main(String[] args) {

        if (args.length != 0) {
            CompiledJar cj = new CompiledJar(args);
            cj.readEntry();
        } else {
            FileManager fm = new FileManager("pla7397.tsp");
            Problem p = fm.load();
            ArrayList<Double> solutions  = new ArrayList<>(), improvedSolutions = new ArrayList<>();
            ArrayList<Long> timeMeasures = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Instant start = Instant.now();
                ConstructiveAlgorithms ca = new ConstructiveAlgorithms();
                ca.nearestNeighbor(p);
                ImprovementAlgorithms ia = new ImprovementAlgorithms();
                ia.opt2first(p);
                Instant end = Instant.now();
                timeMeasures.add(Duration.between(start, end).toMillis()/1000);
                solutions.add(p.getSolution());
                improvedSolutions.add(p.getImprovedSolution());
            }

            DoubleSummaryStatistics solutionsStats = new DoubleSummaryStatistics();
            DoubleSummaryStatistics improvedSolutionsStats = new DoubleSummaryStatistics();
            LongSummaryStatistics timeStats = new LongSummaryStatistics();

            for (Double d: solutions
                 ) {
                solutionsStats.accept(d);
            }
            for (Double d: improvedSolutions){
                improvedSolutionsStats.accept(d);
            }
            for (Long l: timeMeasures){
                timeStats.accept(l);
            }

            System.out.println(solutionsStats);
            System.out.println(improvedSolutionsStats);
            System.out.println(timeStats);
        }
    }
/*DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
            for (Double d: solutions
                 ) {
                dss.accept(d);
            }*/

}