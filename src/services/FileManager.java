package services;

import geneticAlgorithm.GeneticAlgorithmConfig;
import models.City;
import models.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LongSummaryStatistics;
import java.util.Scanner;

class Path {
    public static final String TSP_FILES_FOLDER = "assets/tsp-files/";
    public static final String BEST_SOLUTIONS_FILE = "assets/bestSolutions.txt";
}

public class FileManager {

    private String filename;
    private Scanner scanner;
    private final String divider = "------------------------------------------------------------";

    public FileManager() {
    }

    public FileManager(String filename) {
        this.filename = parseFilename(filename);
    }

    public Problem load() {
        String filepath = Path.TSP_FILES_FOLDER.concat(filename);
        ArrayList<String> header;
        ArrayList<City> cities;

        try {
            File file = new File(filepath);
            scanner = new Scanner(file);
            header = readHeader();
            cities = readCities();
            double bestSolution = readBestSolution();
            return new Problem(header.get(0), header.get(1), header.get(2), header.get(3), cities.size(), bestSolution, cities);
        } catch (FileNotFoundException f) {
            throw new RuntimeException(f);
        }
    }

    private String parseFilename(String filename) {
        if (!filename.contains(".tsp")) {
            filename = filename.concat(".tsp");
        }
        return filename;
    }

    private double readBestSolution() {
        double bestSolution = -1;
        try {
            File file = new File(Path.BEST_SOLUTIONS_FILE);
            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                String problemName = filename.substring(0, filename.length() - 4);
                if (s.contains(problemName)) {
                    String[] strArr = s.split(":");
                    bestSolution = Double.parseDouble(strArr[1].trim());
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return bestSolution;
    }

    private ArrayList<City> readCities() {
        ArrayList<City> cities = new ArrayList<>();

        while (scanner.hasNextInt()) {
            int n, x, y;

            n = scanner.nextInt();
            x = scanner.nextInt();
            y = scanner.nextInt();
            cities.add(new City(n, x, y));
        }

        return cities;
    }

    private ArrayList<String> readHeader() {
        ArrayList<String> header = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            header.add(scanner.nextLine());
        }

        for (int i = 0; i < header.size() - 1; i++) {
            String[] strArr = header.get(i).split(":");
            header.set(i, strArr[1].trim());
        }

        return header;
    }


    public void outputToFile(Problem problem, GeneticAlgorithmConfig gac, ArrayList<Long> timestampsMilliseconds, ArrayList<Long> solutions, ArrayList<City> bestSolutionPath, double bestSolution) {

        LongSummaryStatistics solutionsStats = new LongSummaryStatistics();
        LongSummaryStatistics timeStatsMilliseconds = new LongSummaryStatistics();

        for (Long d : solutions) {
            solutionsStats.accept(d);
        }
        for (Long l : timestampsMilliseconds) {
            timeStatsMilliseconds.accept(l);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        Date date = new Date();
        String directory = (formatter.format(date)).concat("-").concat(problem.getName()).concat("/");

        try {
            if (new File("results/".concat(directory)).mkdir()) {
                directory = "results/".concat(directory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        outputSummary(directory, problem, gac, solutionsStats, timeStatsMilliseconds);
        outputResults(directory, solutions, timestampsMilliseconds);
        outputPath(directory, bestSolutionPath, bestSolution);


        System.out.println("Directory with results created at: " + directory);
    }

    private void outputSummary(String directory, Problem problem, GeneticAlgorithmConfig gac, LongSummaryStatistics solutionsStats, LongSummaryStatistics timeStats) {
        try {
            PrintStream fileStream = new PrintStream(directory.concat("summary.txt"));
            fileStream.printf("Problem: %s | Size: %s%n", problem.getName(), problem.getSize());
            fileStream.println("Best possible solution: " + problem.getBestSolution());
            fileStream.println(divider);
            fileStream.println("Genetic Algorithm Parameters");
            fileStream.println("populationSize: " + gac.populationSize);
            fileStream.println("totalIterations: " + gac.totalIterations);
            fileStream.println("SelectionCriteria: " + gac.selectionCriteria.toString());
            fileStream.println("tournamentSize: " + gac.tournamentSize);
            fileStream.println("PopulationCriteria: " + gac.populationCriteria.toString());
            fileStream.println("elitismPercent: " + gac.elitismPercent);
            fileStream.println("CrossoverOperator: " + gac.recombinationOperator);
            fileStream.println("mutationRatePercent: " + gac.mutationRate);
            fileStream.println("ConstructiveAlgorithm: " + gac.constructiveAlgorithm.toString());
            fileStream.println("ImprovementAlgorithm: " + gac.improvementAlgorithm.toString());
            fileStream.println(divider);
            fileStream.println("Results - Accuracy");
            fileStream.println("Best solution: " + solutionsStats.getMin());
            fileStream.println("Avg. solution: " + solutionsStats.getAverage());
            fileStream.println("Worst solution: " + solutionsStats.getMax());
            fileStream.printf("Best GAP%%: %.2f %n", gap(solutionsStats.getMin(), problem.getBestSolution()));
            fileStream.printf("Avg. GAP%%: %.2f %n", gap(solutionsStats.getAverage(), problem.getBestSolution()));
            fileStream.println(divider);
            fileStream.println("Results - Processing time");
            fileStream.printf("Fastest generation: %dms %s | %ds %n", timeStats.getMin(), "\t", timeStats.getMin() / 1000);
            fileStream.printf("Avg. generation: %.2fms %s | %.2fs %n", timeStats.getAverage(), "\t", timeStats.getAverage() / 1000);
            fileStream.println(divider);
            fileStream.printf("Total time elapsed: %ds %s | %.2fmin%n", timeStats.getSum() / 1000, "\t", ((double) timeStats.getSum()) / (1000 * 60));
            fileStream.printf("Threads used: %d", gac.threads);
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void outputResults(String directory, ArrayList<Long> solutions, ArrayList<Long> timestamps) {
        try {
            PrintStream fileStream = new PrintStream(directory.concat("results.csv"));
            fileStream.println("generation,solutionValue,timeMilliseconds");
            for (int i = 0; i < solutions.size() - 1; i++) {
                fileStream.println(i + 1 + "," + solutions.get(i) + "," + timestamps.get(i));
            }
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void outputPath(String directory, ArrayList<City> path, double bestSolution) {
        String bestSolutionString = String.format("(%.0f)", bestSolution);
        try {
            PrintStream fileStream = new PrintStream(directory.concat(bestSolutionString + "_path.csv"));
            fileStream.println("n,x,y");
            for (int i = 0; i < path.size() - 1; i++) {
                City c = path.get(i);
                fileStream.println(c.getN() + "," + c.getX() + "," + c.getY());
            }
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double gap(double value, double bestSolution) {
        return ((value - bestSolution) / bestSolution) * 100;
    }
}
