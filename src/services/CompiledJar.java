package services;

import models.Problem;

import java.util.HashSet;

public class CompiledJar {
    private String[] args;
    private HashSet<String> legalParameters;
    private boolean legalArguments = false;

    public CompiledJar(String[] args) {
        this.args = args;
        addLegalParameters();
    }

    public void readEntry(){
        parseArguments();
        if (legalArguments){
            Problem problem = new FileManager(args[0]).load();
            System.out.println(problem.getName());
        }
    }

    private void parseArguments(){
        if (args.length != 3) {
            if (args.length == 1 && args[0].equals("-h")) {
                System.out.println("Sintaxe:");
                System.out.println("<nome> <builder> <improver>");
                System.out.println("Exemplos:");
                System.out.println("eil101 FARTHEST_INSERTION OPT3_FIRST_IMPROVEMENT");
                System.out.println("att48 NEAREST_NEIGHBOR OPT2_FIRST_IMPROVEMENT");
                System.out.println("a280 CLOSEST_INSERTION OPT2_BEST_IMPROVEMENT");
                System.out.println("Sintaxe abreviada:");
                System.out.println("eil101 -fi -opt3fi");
                System.out.println("att48 -nn -opt2fi");
                System.out.println("a280 -ci -opt2bi");
            } else if (args.length == 1 && args[0].equals("-a")){
                System.out.println("Diretório de problemas/casos: \"tsp\":");
                System.out.println("\t<diretorio-atual>/tsp/");
                System.out.println("\tDevem ter extensão <.tsp>");
                System.out.println("Exemplo de um arquivo válido: <diretorio-atual>/tsp/eil101.tsp");
                System.out.println("Ao criar o seu próprio arquivo, é necessário inserir solução ótima no arquivo");
                System.out.println("\"bestSolutions.txt\", no diretório atual");
                System.out.println("Deve seguir a sintaxe do arquivo:");
                System.out.println("eil101 : 629");
            } else {
                System.out.println("Parâmetros incorretos!");
                System.out.println("-h para ajuda de sintaxe");
                System.out.println("-a para ajuda com arquivos");
            }
        } else {
            switch (args[1]) {
                case "-fi":
                    args[1] = "FARTHEST_INSERTION";
                    break;
                case "-nn":
                    args[1] = "NEAREST_NEIGHBOR";
                    break;
                case "-ci":
                    args[1] = "CLOSEST_INSERTION";
                    break;
            }
            switch (args[2]) {
                case "-opt2fi":
                    args[2] = "OPT2_FIRST_IMPROVEMENT";
                    break;
                case "-opt2bi":
                    args[2] = "OPT2_BEST_IMPROVEMENT";
                    break;
                case "-opt3fi":
                    args[2] = "OPT3_FIRST_IMPROVEMENT";
                    break;
            }
            legalArguments = true;
        }
    }

    private void addLegalParameters(){
        legalParameters = new HashSet<>();
        legalParameters.add("-fi");
        legalParameters.add("-nn");
        legalParameters.add("-ci");
        legalParameters.add("-opt3fi");
        legalParameters.add("-opt2fi");
        legalParameters.add("-opt2bi");
        legalParameters.add("FARTHEST_INSERTION");
        legalParameters.add("CLOSEST_INSERTION");
        legalParameters.add("NEAREST_NEIGHBOR");
        legalParameters.add("OPT2_FIRST_IMPROVEMENT");
        legalParameters.add("OPT3_FIRST_IMPROVEMENT");
        legalParameters.add("OPT2_BEST_IMPROVEMENT");
    }
}
