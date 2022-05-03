package services;

import models.City;

import java.util.ArrayList;

public abstract class Util {

    public static double sumSolutionPath(ArrayList<City> solutionPath) {
        double sum = 0;

        for (int i = 0; i < solutionPath.size() - 1; i++) {
            sum += getDistance(solutionPath.get(i), solutionPath.get(i + 1));
        }
        sum += getDistance(solutionPath.get(solutionPath.size() -1), solutionPath.get(0));
        return sum;
    }

    public static double getDistance(City a, City b) {
        double dist, difX, difY;
        difX = a.getX() - b.getX();
        difY = a.getY() - b.getY();
        dist = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        return dist;
    }
}
