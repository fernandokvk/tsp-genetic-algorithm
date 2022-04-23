package services;

import algorithms.ConstructiveAlgorithms;
import algorithms.ImprovementAlgorithms;
import enums.Builders;
import enums.Improvers;
import geneticAlgorithm.GeneticAlgorithm;
import geneticAlgorithm.GeneticAlgorithmConfig;
import models.City;
import models.Problem;

import java.util.ArrayList;

public class ProblemManager extends Util {

    Problem problem;

    public ProblemManager() {
    }

    public ProblemManager(String filename) {
        load(filename);
    }

    public ArrayList<City> buildSolution(Problem problem, Builders constructiveAlgorithm) {
        ConstructiveAlgorithms ca = new ConstructiveAlgorithms();

        switch (constructiveAlgorithm) {
            case NEAREST_NEIGHBOR:
                return ca.nearestNeighbor(problem);
            case FARTHEST_INSERTION:
                return ca.farthestInsertion(problem);
            default:
                throw new IllegalStateException("Unexpected value: " + constructiveAlgorithm);
        }
    }

    public void buildSolution(Builders constructiveAlgorithm){
        ConstructiveAlgorithms ca = new ConstructiveAlgorithms();
        switch (constructiveAlgorithm) {
            case NEAREST_NEIGHBOR:
                problem.setSolutionPath(ca.nearestNeighbor(problem));
                break;
            case FARTHEST_INSERTION:
                problem.setSolutionPath(ca.farthestInsertion(problem));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + constructiveAlgorithm);
        }
    }

    public void improveSolution(Improvers improvementAlgorithm){
        problem.setSolutionPath(switchImprover(problem.getSolutionPath(), improvementAlgorithm));
        problem.setImprovedSolution(sumSolutionPath(problem.getSolutionPath()));
    }


    public void improveSolution(Problem p, Improvers improvementAlgorithm){
        p.setSolutionPath(switchImprover(p.getSolutionPath(), improvementAlgorithm));
        p.setImprovedSolution(sumSolutionPath(p.getSolutionPath()));
    }

    public ArrayList<City> switchImprover(ArrayList<City> solutionPath, Improvers improvementAlgorithm) {
        ImprovementAlgorithms ia = new ImprovementAlgorithms();
        switch (improvementAlgorithm){
            case OPT2_FIRST_IMPROVEMENT:
                return ia.opt2first(solutionPath);
            case OPT3_FIRST_IMPROVEMENT:
                return ia.opt3first(solutionPath);
            case NONE:
                return solutionPath;
        }
        return solutionPath;
    }

    public void load(String filename) {
        FileManager fm = new FileManager(filename);
        problem = fm.load();
    }

    public Problem getProblem() {
        return problem;
    }

    public void runGenetic(GeneticAlgorithmConfig gac){
        GeneticAlgorithm ga = new GeneticAlgorithm(gac, problem);
        ga.run();
    }

    public static ArrayList<City> mockSolution(int[] order, Problem problem){
        ArrayList<City> array = new ArrayList<>(order.length);

        for (int i = 0; i < order.length ; i++) {
            for (int j = 0; j < problem.getSize(); j++) {
                if (problem.getCities().get(j).getN() == order[i]){
                    array.add(problem.getCities().get(j));
                }
            }
        }

        return array;
    }

}
