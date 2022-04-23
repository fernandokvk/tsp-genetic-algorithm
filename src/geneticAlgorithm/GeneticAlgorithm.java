package geneticAlgorithm;

/*
    Cromossomo: um indivíduo (solução)
    Gene: um elemento do cromossomo
    População: um conjunto de cromossomos
    Operadores genéticos:
        Seleção: selecionar os cromossomos (pais) para a reprodução;
        Reprodução (crossover): executar uma combinação (cruzamento) dos cromossomos pais para gerar novos cromossomos filhos
        Mutação: é uma modificação arbitrária de uma parte (pequena) do cromossomo.

 */

import models.City;
import models.Problem;
import services.ProblemManager;
import services.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GeneticAlgorithm extends Util {
    GeneticAlgorithmConfig gac;
    Problem problem;
    ArrayList<Chromosome> population = new ArrayList<>();
    ArrayList<Chromosome> parents = new ArrayList<>();
    ArrayList<Chromosome> offsprings = new ArrayList<>();

    private static class Chromosome implements Comparable<Chromosome> {
        public ArrayList<City> solutionPath;
        public double solution;

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

    public void run() {

        generateInitialPopulation();

        for (int i = 0; i < gac.totalIterations; i++) {
            double gap = ((population.get(0).solution - problem.getBestSolution()) / problem.getBestSolution()) * 100;
            System.out.printf("%.2f - Generation: %d - Gap: %.2f%% \n", population.get(0).solution, i+1, gap);
            evaluation();
            selection();
            crossover();
            mutation();
            localSearch();
            update();
        }
        Collections.sort(population);
        System.out.println("GeneticAlgorithm.run");


    }

    private void generateInitialPopulation() {
        ProblemManager pm = new ProblemManager();

        for (int i = 0; i < gac.populationSize; i++) {
            ArrayList<City> solutionPath = pm.buildSolution(problem, gac.constructiveAlgorithm);
            population.add(new Chromosome(solutionPath));
        }
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

    private void selectionTournament() {

        for (int i = 0; i < 2; i++) {
            ArrayList<Chromosome> tournament = new ArrayList<>(gac.tournamentSize);
            int index = (int) Math.round(Math.random() * (gac.populationSize - 1));
            for (int j = 0; j < gac.tournamentSize; j++) {
                tournament.add(population.get(index % gac.populationSize));
                index++;
            }
            Chromosome c = Collections.min(tournament);
            parents.add(c);
        }
    }

    private void selectionRanking() {
        Collections.sort(population);
        parents.add(population.get(0));
        parents.add(population.get(1));
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

        // Adding 2 parents randomly, we don't remove the 1st parent, so it can happen that it is the same
        for (int i = 0; i < 2; i++) {
            double random = Math.random();
            int j = 0;
            while (cumulativeValues.get(j) < random) {
                j++;
            }
            parents.add(population.get(j));
        }
    }

    /**
     * Executa o cruzamento dos reprodutores
     */
    private void crossover() {
        switch (gac.recombinationOperator) {
            case PMX:
                crossoverPMX();
                break;
            case CX:
                break;
            case OX1:
                break;
            case OX2:
                break;
            case POS:
                break;
        }
    }

    private void crossoverPMX() {

        while (offsprings.size() < gac.populationSize) {
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

            offspringA.solution = sumSolutionPath(offspringA.solutionPath);
            offspringB.solution = sumSolutionPath(offspringB.solutionPath);

            offsprings.add(offspringA);
            offsprings.add(offspringB);
        }


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
     * Gerar mutações da população
     */
    private void mutation() {
        int mutationRate = gac.mutationRate;
        for (int i = 0; i < gac.populationSize; i++) {
            int random = ((int) Math.round(Math.random() * 100)) +1;
            if (random <= mutationRate) mutate(population.get(i));
        }
    }

    private void mutate(Chromosome c) {

        int j = (int) Math.round(Math.random() * (problem.getSize() - 2)) + 1;
        int i = (int) Math.round((Math.random() * (j - 1)));
        Collections.reverse(c.solutionPath.subList(i, j+1));


    }

    /**
     * Busca local na população
     */
    private void localSearch() {
        ProblemManager pm = new ProblemManager();

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

    /**
     * Atualizar a população
     */
    private void update() {
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
        Collections.sort(offsprings);
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
