package models;

import java.util.LinkedList;

public class Problem {
    private String name;
    private String comment;
    private String type;
    private final String edgeWeighType;
    private final int size;
    private double solution;
    private final double bestSolution;
    private final LinkedList<City> cities;
    private LinkedList<City> solutionPath;

    public Problem(String name, String comment, String type, String edgeWeighType, int size, double bestSolution, LinkedList<City> cities) {
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.edgeWeighType = edgeWeighType;
        this.size = size;
        this.bestSolution = bestSolution;
        this.cities = cities;
        this.solutionPath = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }



    public double getSolution() {
        return solution;
    }

    public void setSolution(double solution) {
        this.solution = solution;
    }

    public double getBestSolution() {
        return bestSolution;
    }


    public LinkedList<City> getCities() {
        return cities;
    }


    public LinkedList<City> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(LinkedList<City> solutionPath) {
        this.solutionPath = solutionPath;
    }
}
