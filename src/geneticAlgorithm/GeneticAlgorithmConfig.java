package geneticAlgorithm;


import enums.Builders;
import enums.Improvers;

public class GeneticAlgorithmConfig {
    public final int populationSize;                                                    //OK
    public final int totalIterations;                                                   //OK
    public SelectionCriteria selectionCriteria = SelectionCriteria.TOURNAMENT;            //OK
    public final int tournamentSize = 4;                                                //OK
    public PopulationCriteria populationCriteria = PopulationCriteria.ELITISM;    //OK
    public final int elitismPercent = 10;                                               //OK
    public RecombinationOperator recombinationOperator = RecombinationOperator.PMX;     //OK
    public final int mutationRate;                                                      //OK
    public final Builders constructiveAlgorithm = Builders.NEAREST_NEIGHBOR;            //OK
    public final Improvers improvementAlgorithm = Improvers.OPT2_FIRST_IMPROVEMENT;     //OK

    public GeneticAlgorithmConfig(int populationSize, int mutationRate, int totalIterations) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.totalIterations = totalIterations;
    }
}
