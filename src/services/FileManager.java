package services;

import models.City;
import models.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Path {
    public static final String TSP_FILES_FOLDER = "assets/tsp-files/";
    public static final String BEST_SOLUTIONS_FILE = "assets/bestSolutions.txt";
}

public class FileManager {

    private final String filename;
    private Scanner scanner;

    public FileManager(String filename) {
        this.filename = parseFilename(filename);
    }

    public Problem load() {
        String filepath = Path.TSP_FILES_FOLDER.concat(filename);
        ArrayList<String> header;
        ArrayList<City> cities;

        try {
            File file = new File(filepath);
            scanner = new Scanner(file);
            header = readHeader();
            cities = readCities();
            double bestSolution = readBestSolution();
            return new Problem(header.get(0), header.get(1), header.get(2), header.get(3), cities.size(), bestSolution, cities);
        } catch (FileNotFoundException f) {
            throw new RuntimeException(f);
        }
    }

    private String parseFilename(String filename){
        if (!filename.contains(".tsp")){
            filename = filename.concat(".tsp");
        }
        return filename;
    }

    private double readBestSolution(){
        double bestSolution = -1;
        try {
            File file = new File(Path.BEST_SOLUTIONS_FILE);
            scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                String problemName = filename.substring(0, filename.length()-4);
                if (s.contains(problemName)){
                    String[] strArr = s.split(":");
                    bestSolution = Double.parseDouble(strArr[1].trim());
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return bestSolution;
    }

    private ArrayList<City> readCities(){
        ArrayList<City> cities = new ArrayList<>();

        while(scanner.hasNextInt()){
            int n, x, y;

            n = scanner.nextInt();
            x = scanner.nextInt();
            y = scanner.nextInt();
            cities.add(new City(n, x, y));
        }

        return cities;
    }

    private ArrayList<String> readHeader() {
        ArrayList<String> header = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            header.add(scanner.nextLine());
        }

        for (int i = 0; i < header.size() - 1; i++) {
            String[] strArr = header.get(i).split(":");
            header.set(i, strArr[1].trim());
        }

        return header;
    }


}
