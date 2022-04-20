import models.Problem;
import services.CompiledJar;
import services.FileManager;

public class Main {

    public static void main(String[] args) {

        if (args.length != 0) {
            compiledJarFile(args);
        } else {
            FileManager fm = new FileManager("att48.tsp");
            Problem p = fm.load();
            System.out.println("ueba");
        }
    }

    private static void compiledJarFile(String[] s) {
        CompiledJar cj = new CompiledJar(s);
        cj.readEntry();
    }
}