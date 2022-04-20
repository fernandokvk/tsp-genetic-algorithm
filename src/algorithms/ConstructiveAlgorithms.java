package algorithms;

import models.City;
import models.Problem;

import java.util.LinkedList;

public class ConstructiveAlgorithms {

    public void farthestInsertion(Problem p) {
        p.setSolutionPath(new LinkedList<>());
        LinkedList<City> availableCities = new LinkedList<>(p.getCities());
        int initialSize = availableCities.size();
        initializeCycle(availableCities, p.getSolutionPath(), 3);

        while (p.getSolutionPath().size() < initialSize) {
            City city_k = findFarthestToSolution(p.getSolutionPath(), availableCities);
            int insertAt = findInsertIndex(city_k, p.getSolutionPath());
            p.getSolutionPath().add(insertAt, city_k);
            availableCities.remove(city_k);
        }
        p.getSolutionPath().add(p.getSolutionPath().peekFirst());
        p.setSolution(sumSolutionPath(p.getSolutionPath()));
    }

    public void nearestNeighbor(Problem p) {
        p.setSolutionPath(new LinkedList<>());
        LinkedList<City> availableCities = new LinkedList<>(p.getCities());

        City firstCity = addRandomCity(availableCities, p.getSolutionPath());
        int initialSize = availableCities.size();
        availableCities.remove(firstCity);

        while (p.getSolutionPath().size() < initialSize) {
            City city_k = findNearestNeighbor(p.getSolutionPath().getLast(), availableCities);
            p.getSolutionPath().add(city_k);
        }

        p.getSolutionPath().add(firstCity);
        p.setSolution(sumSolutionPath(p.getSolutionPath()));
    }

    private int findInsertIndex(City city_k, LinkedList<City> solutionPath) {
        double minCost = Double.MAX_VALUE;
        int insertAt = -1;

        for (int i = 0; i < solutionPath.size() - 1; i++) {
            City city_i = solutionPath.get(i);
            City city_j = solutionPath.get(i + 1);
            double dist_i_k = getDistance(city_i, city_k);
            double dist_j_k = getDistance(city_j, city_k);
            double dist_i_j = getDistance(city_i, city_j);

            double cost = dist_i_k + dist_j_k - dist_i_j;
            if (cost < minCost) {
                minCost = cost;
                insertAt = solutionPath.indexOf(city_j);
            }
        }
        return insertAt;
    }

    private City findFarthestToSolution(LinkedList<City> solutionPath, LinkedList<City> availableCities) {
        double maxDistance = 0;
        int index = 0;
        int solutionPathSize = solutionPath.size();
        int availableCitiesSize = availableCities.size(); // Values are assigned now, so we don't have to evaluate them every loop

        for (int i = 0; i < solutionPathSize; i++) {
            for (int j = 0; j < availableCitiesSize; j++) {
                double distance = getDistance(solutionPath.get(i), availableCities.get(j));
                if (distance > maxDistance) {
                    maxDistance = distance;
                    index = j;
                }
            }
        }
        return availableCities.get(index);
    }

    private void initializeCycle(LinkedList<City> availableCities, LinkedList<City> solutionPath, int initialCycleSize) {
        City firstCity = addRandomCity(availableCities, solutionPath);
        availableCities.remove(firstCity);
        for (int i = 0; i < (initialCycleSize - 1); i++) {
            City city_k = findNearestNeighbor(firstCity, availableCities);
            solutionPath.add(city_k);
        }

        for (City c : solutionPath) {
            availableCities.remove(c);
        }
    }

    private double sumSolutionPath(LinkedList<City> solutionPath) {
        double sum = 0;

        for (int i = 0; i < solutionPath.size() - 1; i++) {
            sum += getDistance(solutionPath.get(i), solutionPath.get(i + 1));
        }
        return sum;
    }

    private City findNearestNeighbor(City city, LinkedList<City> availableCities) {
        double minDist = Double.MAX_VALUE;
        int minDistIndex = 0;

        for (int i = 0; i < availableCities.size(); i++) {
            double distance = getDistance(city, availableCities.get(i));
            if (distance < minDist) {
                minDist = distance;
                minDistIndex = i;
            }
        }

        City city_k = availableCities.get(minDistIndex);
        availableCities.remove(city_k);
        return city_k;
    }

    private double getDistance(City a, City b) {
        double dist, difX, difY;
        difX = a.getX() - b.getX();
        difY = a.getY() - b.getY();
        dist = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        return dist;
    }

    private City addRandomCity(LinkedList<City> availableCities, LinkedList<City> solutionPath) {
        boolean added = false;
        City addedCity = null;
        while (!added) {
            int randomIndex = (int) Math.round(Math.random() * (availableCities.size() - 1));

            if (!solutionPath.contains(availableCities.get(randomIndex))) {
                addedCity = availableCities.get(randomIndex);
                solutionPath.add(addedCity);
                added = true;
            }
        }
        return addedCity;
    }
}
