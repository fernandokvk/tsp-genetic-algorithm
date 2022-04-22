package models;

import java.util.ArrayList;

public class Problem {
    private String name;
    private String comment;
    private String type;
    private final String edgeWeighType;
    private int size;
    private double solution, improvedSolution;
    private final double bestSolution;
    private ArrayList<City> cities, solutionPath;

    public Problem(String name, String comment, String type, String edgeWeighType, int size, double bestSolution, ArrayList<City> cities) {
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.edgeWeighType = edgeWeighType;
        this.size = size;
        this.bestSolution = bestSolution;
        this.cities = cities;
        this.solutionPath = new ArrayList<>();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getImprovedSolution() {
        return improvedSolution;
    }

    public void setImprovedSolution(double improvedSolution) {
        this.improvedSolution = improvedSolution;
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


    public ArrayList<City> getCities() {
        return cities;
    }


    public ArrayList<City> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(ArrayList<City> solutionPath) {
        this.solutionPath = solutionPath;
    }
}
