package services;

import algorithms.ConstructiveAlgorithms;
import enums.Builders;
import models.City;
import models.Problem;

import java.util.ArrayList;

public class ProblemManager {

    private Problem problem;

    public ProblemManager(Problem problem) {
        this.problem = problem;
    }

    public ArrayList<City> buildSolution(Builders constructiveAlgorithm) {
        ConstructiveAlgorithms ca = new ConstructiveAlgorithms();

        switch (constructiveAlgorithm){
            case NEAREST_NEIGHBOR:
                return ca.nearestNeighbor(problem);
            case FARTHEST_INSERTION:
                return ca.farthestInsertion(problem);
            default:
                throw new IllegalStateException("Unexpected value: " + constructiveAlgorithm);
        }
    }
}
