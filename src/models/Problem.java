package models;

import java.util.ArrayList;

public class Problem {
    private String name, comment, type, edgeWeighType;
    private int size;
    private double solution, bestSolution;
    private ArrayList<City> cities, solutionPath;

    public Problem(String name, String comment, String type, String edgeWeighType, int size, double bestSolution, ArrayList<City> cities) {
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.edgeWeighType = edgeWeighType;
        this.size = size;
        this.bestSolution = bestSolution;
        this.cities = cities;
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

    public String getEdgeWeighType() {
        return edgeWeighType;
    }

    public void setEdgeWeighType(String edgeWeighType) {
        this.edgeWeighType = edgeWeighType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public void setBestSolution(double bestSolution) {
        this.bestSolution = bestSolution;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<City> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(ArrayList<City> solutionPath) {
        this.solutionPath = solutionPath;
    }
}
