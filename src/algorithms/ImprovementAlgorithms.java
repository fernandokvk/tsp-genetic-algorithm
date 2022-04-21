package algorithms;

import models.City;
import models.Problem;

import java.util.ArrayList;
import java.util.Collections;

public class ImprovementAlgorithms {

    public void opt2first(Problem p) {
        ArrayList<City> solutionPath = new ArrayList<>(p.getSolutionPath());
        p.setImprovedSolution(p.getSolution());
        boolean canImprove = true;

        while (canImprove) {
            double originalSolution = p.getImprovedSolution();

            for (int i = 1; i < solutionPath.size() - 2; i++) {
                for (int j = i + 1; j < solutionPath.size() - 1; j++) {

                    double beforeSwap = getDistance(solutionPath.get(i - 1), solutionPath.get(i))
                            + getDistance(solutionPath.get(j), solutionPath.get(j + 1));

                    double afterSwap = getDistance(solutionPath.get(i - 1), solutionPath.get(j))
                            + getDistance(solutionPath.get(i), solutionPath.get(j + 1));

                    if (afterSwap < beforeSwap) {
                        Collections.reverse(solutionPath.subList(i, j + 1));
                        p.setImprovedSolution((p.getImprovedSolution()) - (beforeSwap - afterSwap));
                    }

                }
            }
            if (originalSolution == p.getImprovedSolution()) canImprove = false;
        }
        p.setSolutionPath(solutionPath);
    }

    private double getDistance(City a, City b) {
        double dist, difX, difY;
        difX = a.getX() - b.getX();
        difY = a.getY() - b.getY();
        dist = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        return dist;
    }


}
