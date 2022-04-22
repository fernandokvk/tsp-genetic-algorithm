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

import java.util.ArrayList;
import java.util.Collections;


public class GeneticAlgorithm {
    GeneticAlgorithmConfig gac;
    Problem problem;
    ArrayList<Chromosome> population = new ArrayList<>();
    ArrayList<Chromosome> parents = new ArrayList<>();

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

        generatePopulation();

        gac.selectionCriteria = SelectionCriteria.TOURNAMENT;
        evaluation();
        selection();
        System.out.println("GeneticAlgorithm.run");
//        reproduction();
//        mutation();
//        localSearch();
//        update();

    }

    private void generatePopulation() {
        ProblemManager pm = new ProblemManager(problem);

        for (int i = 0; i < gac.populationSize; i++) {
            ArrayList<City> solutionPath = pm.buildSolution(gac.constructiveAlgorithm);
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
        switch (gac.selectionCriteria){
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
            int index = (int) Math.round(Math.random()*(gac.populationSize - 1));
            index = gac.populationSize -1;
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

    private void selectionRoulette(){
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
        for (int i = 0; i < gac.populationSize ; i++) {
            double relativeValue = fitnessValues.get(i) / sum;
            fitnessValues.set(i, relativeValue);
        }

        // Setting the cumulative values, which are intervals weighted by the solution fitness
        // Meaning that better solutions (higher fitness) have larger intervals
        cumulativeValues.add(fitnessValues.get(0));
        for (int i = 1; i < gac.populationSize; i++) {
            double cumulativeValue = fitnessValues.get(i) + cumulativeValues.get(i-1);
            cumulativeValues.add(i, cumulativeValue);
        }

        // Adding 2 parents randomly, we don't remove the 1st parent, so it can happen that it is the same
        for (int i = 0; i < 2; i++) {
            double random = Math.random();
            int j = 0;
            while (cumulativeValues.get(j) < random){
                j++;
            }
            parents.add(population.get(j));
        }
    }

    /**
     * Executa o cruzamento dos reprodutores
     */
    private void reproduction() {
    }

    /**
     * Gerar mutações da população
     */
    private void mutation() {
    }

    /**
     * Busca local na população
     */
    private void localSearch() {

    }

    /**
     * Atualizar a população
     */
    private void update() {
    }

    private double sumSolutionPath(ArrayList<City> solutionPath) {
        double sum = 0;

        for (int i = 0; i < solutionPath.size() - 1; i++) {
            sum += getDistance(solutionPath.get(i), solutionPath.get(i + 1));
        }
        return sum;
    }
    private double getDistance(City a, City b) {
        double dist, difX, difY;
        difX = a.getX() - b.getX();
        difY = a.getY() - b.getY();
        dist = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        return dist;
    }


}
