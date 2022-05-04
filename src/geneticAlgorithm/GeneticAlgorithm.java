package geneticAlgorithm;

import models.City;
import models.Problem;
import services.FileManager;
import services.ProblemManager;
import services.Util;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class GeneticAlgorithm extends Util {
    GeneticAlgorithmConfig gac;
    Problem problem;
    ArrayList<Chromosome> population = new ArrayList<>();
    ArrayList<Chromosome> parents = new ArrayList<>();
    ArrayList<Chromosome> offsprings = new ArrayList<>();
    Chromosome currentBestSolution;

    public static class Chromosome implements Comparable<Chromosome> {
        public ArrayList<City> solutionPath;
        public double solution;

        public Chromosome(Chromosome chromosome) {
            this.solution = chromosome.solution;
            this.solutionPath = chromosome.solutionPath;
        }

        public Chromosome(ArrayList<City> solutionPath) {
            this.solutionPath = solutionPath;
        }

        @Override
        public int compareTo(Chromosome o) {
            return Double.compare(solution, o.solution);
        }
    }

    public GeneticAlgorithm(GeneticAlgorithmConfig gac, Problem problem) {
        this.gac = gac;
        this.problem = problem;
    }

    public void run() throws InterruptedException {
        ArrayList<Long> timestampsMilliseconds = new ArrayList<>(gac.totalIterations);
        ArrayList<Long> solutions = new ArrayList<>(gac.totalIterations);
        generateInitialPopulation();
        currentBestSolution = population.get(0);

        for (int i = 0; i < gac.totalIterations; i++) {
            double gap = ((population.get(0).solution - problem.getBestSolution()) / problem.getBestSolution()) * 100;
            System.out.printf("%.2f - Generation: %d - Gap: %.2f%% \n", population.get(0).solution, i + 1, gap);
            Instant start = Instant.now();

            evaluation();
            selection();
            crossover();
            mutation();
            localSearch();
            update();

            Instant end = Instant.now();
            timestampsMilliseconds.add(Duration.between(start, end).toMillis());
            if (population.get(0).solution < currentBestSolution.solution) {
                currentBestSolution = new Chromosome(population.get(0));
            }
            solutions.add(Math.round(currentBestSolution.solution));
        }
        FileManager fm = new FileManager();
        fm.outputToFile(problem, gac, timestampsMilliseconds, solutions, currentBestSolution.solutionPath, currentBestSolution.solution);
    }

    private void generateInitialPopulation() {
        ProblemManager pm = new ProblemManager();
        double oldPercent = 0;
        double percent;
        System.out.println("Building solutions:");
        for (int i = 0; i < gac.populationSize; i++) {
            percent = i;
            percent = ((percent + 1) / gac.populationSize) * 100;
            if (Math.round(percent) > Math.round(oldPercent) && Math.round(percent) % 10 == 0)
                System.out.printf("%02d%% ", Math.round(percent));
            ArrayList<City> solutionPath = pm.buildSolution(problem, gac.constructiveAlgorithm);
            population.add(new Chromosome(solutionPath));
            oldPercent = percent;
        }
        System.out.println("\nRunning genetic algorithm:");
    }

    /**
     * Calcula a qualidade dos cromossomos filhos
     */
    private void evaluation() {
        for (int i = 0; i < gac.populationSize; i++) {
            population.get(i).solution = sumSolutionPath(population.get(i).solutionPath);
        }
    }

    /**
     * Escolhe os cromossomos reprodutores
     */
    private void selection() {
        parents.clear();
        switch (gac.selectionCriteria) {
            case ROULETTE:
                selectionRoulette();
                break;
            case RANKING:
                selectionRanking();
                break;
            case TOURNAMENT:
                selectionTournament();
                break;
        }
    }

    private void selectionRoulette() {
        double sum = 0;
        ArrayList<Double> fitnessValues = new ArrayList<>(gac.populationSize);
        ArrayList<Double> cumulativeValues = new ArrayList<>(gac.populationSize);

        // This for loop assigns fitness values as (total distance)⁻1
        // The shorter the path, the more we favor them in the roulette
        for (int i = 0; i < gac.populationSize; i++) {
            double fitness = Math.pow(population.get(i).solution, -1);
            fitnessValues.add(fitness);
            sum += fitness;
        }

        // Here we evaluate their relative value to sum
        for (int i = 0; i < gac.populationSize; i++) {
            double relativeValue = fitnessValues.get(i) / sum;
            fitnessValues.set(i, relativeValue);
        }

        // Setting the cumulative values, which are intervals weighted by the solution fitness
        // Meaning that better solutions (higher fitness) have larger intervals
        cumulativeValues.add(fitnessValues.get(0));
        for (int i = 1; i < gac.populationSize; i++) {
            double cumulativeValue = fitnessValues.get(i) + cumulativeValues.get(i - 1);
            cumulativeValues.add(i, cumulativeValue);
        }

        // Adding 2 parents randomly, we don't remove the 1st parent, so it can happen that one is chosen twice
        for (int i = 0; i < 2; i++) {
            double random = Math.random();
            int j = 0;
            while (cumulativeValues.get(j) < random) {
                j++;
            }
            parents.add(population.get(j));
        }
    }

    private void selectionRanking() {
        Collections.sort(population);
        ArrayList<Integer> ranks = new ArrayList<>(gac.populationSize);
        ArrayList<Integer> cumulativeRanks = new ArrayList<>(gac.populationSize);

        //Generating the ranks
        for (int i = 0; i < gac.populationSize; i++) {
            ranks.add(gac.populationSize - i);
        }

        //Generating the cumulative ranks array. Could be done in one array, but for clarity this is better.
        cumulativeRanks.add(ranks.get(0));
        for (int i = 1; i < gac.populationSize; i++) {
            cumulativeRanks.add(cumulativeRanks.get(i - 1) + ranks.get(i));
        }

        // Adding 2 parents randomly, we don't remove the 1st parent, so it can happen that one is chosen twice
        for (int i = 0; i < 2; i++) {
            double random = Math.random() * Collections.max(cumulativeRanks);
            int j = 0;
            while (cumulativeRanks.get(j) < random) {
                j++;
            }
            parents.add(population.get(j));
        }

    }

    private void selectionTournament() {

        for (int i = 0; i < 2; i++) {
            ArrayList<Chromosome> tournament = new ArrayList<>(gac.tournamentSize);
            HashSet<Integer> participantsIndex = new HashSet<>(gac.tournamentSize);

            while (participantsIndex.size() < gac.tournamentSize) {
                int index = (int) Math.round(Math.random() * (gac.populationSize - 1));
                participantsIndex.add(index);
            }
            for (Integer j : participantsIndex) {
                tournament.add(population.get(j));
            }
            Chromosome champion = Collections.min(tournament);
            parents.add(champion);
        }
    }

    /**
     * Executa o cruzamento dos reprodutores
     */
    private void crossover() {

        while (offsprings.size() < gac.populationSize) {
            switch (gac.recombinationOperator) {
                case OX1:
                    crossoverOX1();
                    break;
                case POS:
                    crossoverPOS();
                    break;
                case PMX:
                    crossoverPMX();
                    break;
            }
            selection();
        }


    }

    private void crossoverOX1() {
        Chromosome offspringA = new Chromosome(new ArrayList<>());
        Chromosome offspringB = new Chromosome(new ArrayList<>());

        for (int i = 0; i < problem.getSize(); i++) {
            offspringA.solutionPath.add(i, new City());
            offspringB.solutionPath.add(i, new City());
        }

        int indexJ = (int) Math.round(Math.random() * (problem.getSize() - 2)) + 1; // indexJ cannot be first city, so (-1), must be inside bounds, so (-2)
        int indexI = (int) Math.round((Math.random() * (indexJ - 1)));

        for (int i = indexI; i < (indexJ + 1); i++) {
            offspringA.solutionPath.set(i, parents.get(1).solutionPath.get(i));
            offspringB.solutionPath.set(i, parents.get(0).solutionPath.get(i));
        }
        int elementsLeft = (problem.getSize() - 1) - (indexJ - indexI);

        for (int i = (indexJ + 1); i < (indexJ + elementsLeft + 1); i++) {
            City c = parents.get(1).solutionPath.get(i % problem.getSize());
            if (!offspringA.solutionPath.subList(indexI, indexJ).contains(c)) {
                offspringA.solutionPath.set((i % problem.getSize()), c);
            }
        }

        for (int i = (indexJ + 1); i < (indexJ + elementsLeft + 1); i++) {
            City c = parents.get(0).solutionPath.get(i % problem.getSize());
            if (!offspringB.solutionPath.subList(indexI, indexJ).contains(c)) {
                offspringB.solutionPath.set((i % problem.getSize()), c);
            }
        }
        offsprings.add(offspringA);
        offsprings.add(offspringB);
    }

    private void crossoverPOS() {

        int numberOfSwaps = (int) (Math.ceil(Math.random() * (problem.getSize() - 1)));
        HashSet<Integer> swapPositions = new HashSet<>(numberOfSwaps);
        while (swapPositions.size() < numberOfSwaps) {
            int swapPosition = (int) (Math.round((Math.random() * (problem.getSize() - 1))));
            swapPositions.add(swapPosition);
        }

        ArrayList<City> parentA = parents.get(0).solutionPath;
        ArrayList<City> parentB = parents.get(1).solutionPath;
        Chromosome offspringA = new Chromosome(parentA);
        Chromosome offspringB = new Chromosome(parentB);

        for (Integer i : swapPositions) {
            offspringA.solutionPath.set(i, parentB.get(i));
            offspringA.solutionPath.set(parentA.indexOf(offspringA.solutionPath.get(i)), parentA.get(i));

            offspringB.solutionPath.set(i, parentA.get(i));
            offspringB.solutionPath.set(parentB.indexOf(offspringB.solutionPath.get(i)), parentB.get(i));
        }
        offsprings.add(offspringA);
        offsprings.add(offspringB);
    }

    private void crossoverPMX() {

        Chromosome offspringA = new Chromosome(new ArrayList<>());
        Chromosome offspringB = new Chromosome(new ArrayList<>());
        Map<City, City> mappingA = new HashMap<>();
        Map<City, City> mappingB = new HashMap<>();

        for (int i = 0; i < problem.getSize(); i++) {
            offspringA.solutionPath.add(i, new City());
            offspringB.solutionPath.add(i, new City());
        }

        int indexJ = (int) Math.round(Math.random() * (problem.getSize() - 2)) + 1; // indexJ cannot be first city, so (-1), must be inside bounds, so (-2)
        int indexI = (int) Math.round((Math.random() * (indexJ - 1)));              //indexI cannot be indexJ, so (-1)

        for (int i = indexI; i < (indexJ + 1); i++) {
            offspringA.solutionPath.set(i, parents.get(1).solutionPath.get(i));
            offspringB.solutionPath.set(i, parents.get(0).solutionPath.get(i));

            mappingA.put(parents.get(1).solutionPath.get(i), parents.get(0).solutionPath.get(i));
            mappingB.put(parents.get(0).solutionPath.get(i), parents.get(1).solutionPath.get(i));
        }
        sortMappedElements(offspringA, parents.get(0), mappingA, indexI, indexJ);
        sortMappedElements(offspringB, parents.get(1), mappingB, indexI, indexJ);
        offsprings.add(offspringA);
        offsprings.add(offspringB);
    }

    private void sortMappedElements(Chromosome offspring, Chromosome parent, Map<City, City> mapping, int indexI, int indexJ) {
        for (int i = 0; i < problem.getSize() - 1; i++) {
            if (i < indexI || i > indexJ) {
                City c = parent.solutionPath.get(i);
                if (mapping.containsKey(c)) {
                    boolean sorted = false;
                    City aux = mapping.get(c);
                    while (!sorted) {
                        if (!mapping.containsKey(aux)) {
                            offspring.solutionPath.set(i, aux);
                            sorted = true;
                        } else {
                            aux = mapping.get(aux);
                        }
                    }
                } else {
                    offspring.solutionPath.set(i, parent.solutionPath.get(i));
                }
            }
        }
    }

    /**
     * Gerar mutações na população
     */
    private void mutation() {
        int mutationRate = gac.mutationRate;
        for (int i = 0; i < gac.populationSize; i++) {
            int random = ((int) Math.round(Math.random() * 100)) + 1;
            if (random <= mutationRate) {
                mutate(population.get(i));
                mutate(offsprings.get(i));
            }
        }
    }

    private void mutate(Chromosome c) {

        int j = (int) Math.round(Math.random() * (problem.getSize() - 2)) + 1;
        int i = (int) Math.round((Math.random() * (j - 1)));
        Collections.reverse(c.solutionPath.subList(i, j + 1));


    }

    /**
     * Busca local na população
     */
    private void localSearch() throws InterruptedException {
        ProblemManager pm = new ProblemManager();

        if (gac.threads >= 1) multiThreadLocalSearch();
        else {
            if (gac.populationCriteria == PopulationCriteria.POPULATIONAL) {
                for (int i = 0; i < gac.populationSize; i++) {
                    offsprings.get(i).solutionPath = pm.switchImprover(offsprings.get(i).solutionPath, gac.improvementAlgorithm);
                }
            } else {
                //STEADY-STATED OR ELITISM
                for (int i = 0; i < gac.populationSize; i++) {
                    offsprings.get(i).solutionPath = pm.switchImprover(offsprings.get(i).solutionPath, gac.improvementAlgorithm);
                    population.get(i).solutionPath = pm.switchImprover(population.get(i).solutionPath, gac.improvementAlgorithm);
                }
            }
        }
    }

    private synchronized void multiThreadLocalSearch() throws InterruptedException {
        int chromosomesPerThread = gac.populationSize / gac.threads;
        ArrayList<Integer> chromosomeIndexAllocation = new ArrayList<>(gac.threads + 1);
        ProblemManager pm = new ProblemManager();

        chromosomeIndexAllocation.add(0);
        chromosomeIndexAllocation.add(chromosomesPerThread);
        for (int i = 2; i < gac.threads + 1; i++) {
            chromosomeIndexAllocation.add(chromosomeIndexAllocation.get(i - 1) + chromosomesPerThread);
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < gac.threads; i++) {
            int lowerbound = chromosomeIndexAllocation.get(i);
            int upperbound = chromosomeIndexAllocation.get(i + 1);

            Thread t = new Thread(() -> {
                for (int j = lowerbound; j < upperbound; j++) {
                    offsprings.get(j).solutionPath = pm.switchImprover(offsprings.get(j).solutionPath, gac.improvementAlgorithm);
                }

            });
            threads.add(t);
            t.start();
        }
        for (Thread t: threads){
            t.join();
        }

        threads.clear();

        if (gac.populationCriteria != PopulationCriteria.POPULATIONAL){
            for (int i = 0; i < gac.threads; i++) {
                int lowerbound = chromosomeIndexAllocation.get(i);
                int upperbound = chromosomeIndexAllocation.get(i + 1);

                Thread t = new Thread(() -> {
                    for (int j = lowerbound; j < upperbound; j++) {
                        population.get(j).solutionPath = pm.switchImprover(population.get(j).solutionPath, gac.improvementAlgorithm);
                    }
                });
                threads.add(t);
                t.start();
            }
            for (Thread t: threads){
                t.join();
            }
        }

    }

    /**
     * Atualizar a população
     */
    private void update() {

        for (int i = 0; i < gac.populationSize; i++) {
            Chromosome ch = offsprings.get(i);
            ch.solution = sumSolutionPath(ch.solutionPath);
        }
        if (gac.populationCriteria != PopulationCriteria.POPULATIONAL) {
            for (int i = 0; i < gac.populationSize; i++) {
                Chromosome ch = population.get(i);
                ch.solution = sumSolutionPath(ch.solutionPath);
            }
        }

        switch (gac.populationCriteria) {
            case POPULATIONAL:
                updatePopulational();
                break;
            case ELITISM:
                updateElitism();
                break;
            case STEADY_STATED:
                updateSteadyStated();
                break;
        }
    }

    private void updatePopulational() {
        population = offsprings;
//        Collections.sort(offsprings);
    }

    private void updateElitism() {
        Collections.sort(population);
        Collections.sort(offsprings);
        ArrayList<Chromosome> newPopulation = new ArrayList<>();
        double elitismPercent = gac.elitismPercent;
        elitismPercent /= 100;
        int oldPopulationSize = (int) Math.round(gac.populationSize * elitismPercent);

        for (int i = 0; i < gac.populationSize; i++) {
            if (i < oldPopulationSize) newPopulation.add(population.get(i));
            else newPopulation.add(offsprings.get(i));
        }
        population = newPopulation;
    }

    private void updateSteadyStated() {
        Collections.sort(offsprings);
        Collections.sort(population);
        ArrayList<Chromosome> newPopulation = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (newPopulation.size() < gac.populationSize) {
            Chromosome offspring = offsprings.get(i);
            Chromosome old = population.get(j);

            if (offspring.solution < old.solution) {
                newPopulation.add(offspring);
                i++;
            } else {
                newPopulation.add(old);
                j++;
            }
        }
        population = newPopulation;
    }

}
