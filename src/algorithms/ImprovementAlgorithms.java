package algorithms;

import models.City;
import models.Problem;
import services.Util;

import java.util.ArrayList;
import java.util.Collections;

public class ImprovementAlgorithms extends Util {

    public ArrayList<City> opt2first(ArrayList<City> solutionPath) {
        boolean canImprove = true;
        double originalSolution = sumSolutionPath(solutionPath);

        while (canImprove) {
            double improvedSolution = originalSolution;

            for (int i = 1; i < solutionPath.size() - 2; i++) {
                for (int j = i + 1; j < solutionPath.size() - 1; j++) {

                    double beforeSwap = getDistance(solutionPath.get(i - 1), solutionPath.get(i))
                            + getDistance(solutionPath.get(j), solutionPath.get(j + 1));

                    double afterSwap = getDistance(solutionPath.get(i - 1), solutionPath.get(j))
                            + getDistance(solutionPath.get(i), solutionPath.get(j + 1));

                    if (afterSwap < beforeSwap) {
                        Collections.reverse(solutionPath.subList(i, j + 1));
                        improvedSolution -= (beforeSwap - afterSwap);
                    }

                }
            }
            if (improvedSolution == originalSolution) canImprove = false;
        }
        return solutionPath;
    }

    public void opt3first(Problem p) {
        ArrayList<City> solutionPath = new ArrayList<>(p.getSolutionPath());
        p.setImprovedSolution(p.getSolution());
        boolean canImprove = true;
        int solutionPathSize = solutionPath.size(); // Assigning value now so we don't have to evaluate every loop

        while (canImprove) {
            double originalSolution = p.getImprovedSolution();

            for (int i = 1; i < solutionPathSize - 2; i++) {
                for (int j = i + 1; j < solutionPathSize - 1; j++) {
                    for (int k = j + 1; k < solutionPathSize; k++) {

                        double newSolution = opt3Swap(solutionPath, originalSolution, i, j, k);

                        if (newSolution < originalSolution) {
                            originalSolution = newSolution;
                        }

                    }
                }

            }
            if (originalSolution == p.getImprovedSolution()) canImprove = false;
            p.setImprovedSolution(originalSolution);

        }

        p.setSolutionPath(solutionPath);
    }

    private double opt3Swap(ArrayList<City> solutionPath, double originalSolution, int i, int j, int k) {
        // sublist:
        // 1st: [i...j-1]
        // 2nd: [j...k-1]
        // considering vertexes: (i-1, i) (j-1, j) (k-1, k)

        ArrayList<Double> swapCases = new ArrayList<>(5);

        double base = getDistance(solutionPath.get(i - 1), solutionPath.get(i))
                + getDistance(solutionPath.get(j - 1), solutionPath.get(j))
                + getDistance(solutionPath.get(k - 1), solutionPath.get(k));
        swapCases.add(base);

        double case1 = getDistance(solutionPath.get(i - 1), solutionPath.get(j - 1))
                + getDistance(solutionPath.get(i), solutionPath.get(k - 1))
                + getDistance(solutionPath.get(j), solutionPath.get(k));
        swapCases.add(case1);

        double case2 = getDistance(solutionPath.get(i - 1), solutionPath.get(j))
                + getDistance(solutionPath.get(k - 1), solutionPath.get(i))
                + getDistance(solutionPath.get(j - 1), solutionPath.get(k));
        swapCases.add(case2);

        double case3 = getDistance(solutionPath.get(i - 1), solutionPath.get(j))
                + getDistance(solutionPath.get(k - 1), solutionPath.get(j - 1))
                + getDistance(solutionPath.get(i), solutionPath.get(k));
        swapCases.add(case3);

        double case4 = getDistance(solutionPath.get(i - 1), solutionPath.get(k - 1))
                + getDistance(solutionPath.get(j), solutionPath.get(i))
                + getDistance(solutionPath.get(j - 1), solutionPath.get(k));
        swapCases.add(case4);

        int bestSwapIndex = swapCases.indexOf(Collections.min(swapCases));

        if (bestSwapIndex != 0) { //bestSwap isn't base case
            int rotateSize = solutionPath.subList(j, k).size();
            switch (bestSwapIndex) {
                case 1:
                    Collections.reverse(solutionPath.subList(i, j));
                    Collections.reverse(solutionPath.subList(j, k));
                    break;
                case 2:
                    Collections.rotate(solutionPath.subList(i, k), rotateSize);
                    break;
                case 3:
                    Collections.reverse(solutionPath.subList(i, j));
                    Collections.rotate(solutionPath.subList(i, k), rotateSize);
                    break;
                case 4:
                    Collections.reverse(solutionPath.subList(j, k));
                    Collections.rotate(solutionPath.subList(i, k), rotateSize);
                    break;
            }
            double difference = swapCases.get(0) - swapCases.get(bestSwapIndex);
            return (originalSolution - difference);

        }
        return originalSolution;
    }

}
