import algorithms.ConstructiveAlgorithms;
import models.Problem;
import services.CompiledJar;
import services.FileManager;

public class Main {

    public static void main(String[] args) {

        if (args.length != 0) {
            CompiledJar cj = new CompiledJar(args);
            cj.readEntry();
        } else {
            FileManager fm = new FileManager("eil101.tsp");
            Problem p = fm.load();
            ConstructiveAlgorithms ca = new ConstructiveAlgorithms();
            ca.farthestInsertion(p);
        }
    }
/*DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
            for (Double d: solutions
                 ) {
                dss.accept(d);
            }*/

}