package geneticAlgorithm;


import enums.Builders;
import enums.Improvers;

public class GeneticAlgorithmConfig {
    public final int populationSize;
    public SelectionCriteria selectionCriteria = SelectionCriteria.ROULETTE;
    public final int tournamentSize = 4;
    public final String criterioSobrevivenciaCromossomos;
    public final int mutationRate;
    public final String criterioParada;
    public final Builders constructiveAlgorithm = Builders.NEAREST_NEIGHBOR;
    public final Improvers improvementAlgorithm = Improvers.OPT2_FIRST_IMPROVEMENT;


    public GeneticAlgorithmConfig(int populationSize) {
        this.populationSize = populationSize;
        this.criterioSobrevivenciaCromossomos = "";
        this.mutationRate = 0;
        this.criterioParada = "";
    }


}
