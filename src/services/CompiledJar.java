package services;

import enums.Builders;
import geneticAlgorithm.GeneticAlgorithmConfig;
import geneticAlgorithm.RecombinationOperator;

public class CompiledJar {
    private String[] args;
    private boolean legalArguments = false;
    private int populationSize;
    private int totalIterations;
    private RecombinationOperator recombinationOperator = RecombinationOperator.OX1;
    private int mutationRate = 5;
    private Builders builders = Builders.NEAREST_NEIGHBOR;
    private int threads = 1;
    private String filename;
    private final String divider = "-------------------------------------";


    public CompiledJar(String[] args) {
        this.args = args;
    }

    public void readEntry() throws InterruptedException {
        parseArguments();
        if (legalArguments) {
            ProblemManager pm = new ProblemManager(this.filename);
            System.out.println(divider);
            System.out.println("Parâmetros: "+ this.filename+" "+this.populationSize+" "+this.totalIterations+" "+this.recombinationOperator.toString()+" "+this.mutationRate+" "+this.threads);
            GeneticAlgorithmConfig config = new GeneticAlgorithmConfig(
                    this.populationSize,
                    this.totalIterations,
                    this.recombinationOperator,
                    this.mutationRate,
                    this.builders,
                    this.threads
            );
            pm.runGenetic(config);
        }
    }

    private void parseArguments() {
        if (args.length < 3) {
            if (args.length == 1 && args[0].equals("-h")) {
                System.out.println("Sintaxe:");
                System.out.println("Operadores disponíveis: OX1, POS, PMX");
                System.out.println("<arquivo> <tamanho-da-populacao> <num-iteracoes> <operador-crossover> <taxa-mutação> <num-threads> ");
                System.out.println("Exemplos:");
                System.out.println("eil101.tsp 1000 5000 OX1 5 1");
                System.out.println("att48.tsp 5000 1000 OX1 5 5");
                System.out.println("a280.tsp 200 2000 OX1 5 5");
                System.out.println("Se preferir, é possível omitir os argumentos: <operador-crossover> <taxa-mutação> <num-threads>");
                System.out.println("Que assumem os valores padrões: OX1, 5, 1");
                System.out.println("Os parâmetros não acessíveis aqui, como: critério de seleção, critério de atualização, % de elitismo, tamanho do torneio, e outros");
                System.out.println("Estão disponíveis na execução a partir do código somente (por enquanto)");
            } else if (args.length == 1 && args[0].equals("-a")) {
                System.out.println("Diretório de problemas/casos: assets/tsp-files/");
                System.out.println("\tDevem ter extensão <.tsp>");
                System.out.println("Exemplo de um arquivo válido: assets/tsp-files/eil101.tsp");
                System.out.println("Ao criar o seu próprio arquivo, é necessário inserir solução ótima no arquivo");
                System.out.println("\"bestSolutions.txt\", no mesmo diretório");
                System.out.println("Deve seguir a sintaxe do arquivo:");
                System.out.println("eil101 : 629");
            } else {
                System.out.println("Parâmetros incorretos!");
                System.out.println("-h para ajuda de sintaxe");
                System.out.println("-a para ajuda com arquivos");
            }
        } else {
            if (args.length >= 4) {
                this.recombinationOperator = RecombinationOperator.valueOf(args[3].toUpperCase());
                legalArguments = true;
            }
            if (args.length >= 5) {
                this.mutationRate = Integer.parseInt(args[4]);
            }
            if (args.length >= 6) {
                this.threads = Integer.parseInt(args[5]);
            }
            this.filename = args[0];
            this.populationSize = Integer.parseInt(args[1]);
            this.totalIterations = Integer.parseInt(args[2]);
            legalArguments = true;
        }
    }
}
