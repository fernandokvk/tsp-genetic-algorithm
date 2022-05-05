package geneticAlgorithm;


import enums.Builders;
import enums.Improvers;

public class GeneticAlgorithmConfig {
    public final int populationSize;
    public final int totalIterations;
    public SelectionCriteria selectionCriteria = SelectionCriteria.ROULETTE;
    public final int tournamentSize = 4;
    public PopulationCriteria populationCriteria = PopulationCriteria.STEADY_STATED;
    public int elitismPercent = 25;
    public RecombinationOperator recombinationOperator = RecombinationOperator.OX1;
    public int mutationRate = 1;
    public Builders constructiveAlgorithm = Builders.NEAREST_NEIGHBOR;
    public Improvers improvementAlgorithm = Improvers.OPT2_FIRST_IMPROVEMENT;
    public int threads = 1;
    public long timeLimit = 1;

    public GeneticAlgorithmConfig(int populationSize, int totalIterations, SelectionCriteria selectionCriteria, PopulationCriteria populationCriteria, int elitismPercent, RecombinationOperator recombinationOperator, int mutationRate, Builders constructiveAlgorithm, Improvers improvementAlgorithm) {
        this.populationSize = populationSize;
        this.totalIterations = totalIterations;
        this.selectionCriteria = selectionCriteria;
        this.populationCriteria = populationCriteria;
        this.elitismPercent = elitismPercent;
        this.recombinationOperator = recombinationOperator;
        this.mutationRate = mutationRate;
        this.constructiveAlgorithm = constructiveAlgorithm;
        this.improvementAlgorithm = improvementAlgorithm;
    }

    public GeneticAlgorithmConfig(int populationSize, int totalIterations, RecombinationOperator recombinationOperator, int mutationRate, Builders constructiveAlgorithm, int threadsAmount) {
        this.populationSize = populationSize;
        this.totalIterations = totalIterations;
        this.recombinationOperator = recombinationOperator;
        this.mutationRate = mutationRate;
        this.constructiveAlgorithm = constructiveAlgorithm;
        this.threads = threadsAmount;
    }
}
