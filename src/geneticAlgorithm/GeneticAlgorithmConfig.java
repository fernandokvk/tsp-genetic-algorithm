package geneticAlgorithm;


import enums.Builders;
import enums.Improvers;

public class GeneticAlgorithmConfig {
    public final int populationSize;                                                     //OK
    public final int totalIterations;                                                    //OK
    public SelectionCriteria selectionCriteria = SelectionCriteria.RANKING;              //OK
    public final int tournamentSize = 4;                                                 //OK
    public PopulationCriteria populationCriteria = PopulationCriteria.POPULATIONAL;      //OK
    public int elitismPercent = 10;                                                      //OK
    public RecombinationOperator recombinationOperator = RecombinationOperator.PMX;      //OK
    public int mutationRate = 5;                                                         //OK
    public Builders constructiveAlgorithm = Builders.NEAREST_NEIGHBOR;            //OK
    public Improvers improvementAlgorithm = Improvers.OPT2_FIRST_IMPROVEMENT;     //OK

    public GeneticAlgorithmConfig(int populationSize, int totalIterations, int mutationRate) {
        this.populationSize = populationSize;
        this.totalIterations = totalIterations;
        this.mutationRate = mutationRate;
    }

    public GeneticAlgorithmConfig(int populationSize, int totalIterations, SelectionCriteria selectionCriteria, PopulationCriteria populationCriteria, int elitismPercent, RecombinationOperator recombinationOperator, int mutationRate) {
        this.populationSize = populationSize;
        this.totalIterations = totalIterations;
        this.selectionCriteria = selectionCriteria;
        this.populationCriteria = populationCriteria;
        this.elitismPercent = elitismPercent;
        this.recombinationOperator = recombinationOperator;
        this.mutationRate = mutationRate;
    }

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
}
