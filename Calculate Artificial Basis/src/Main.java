import java.io.FileNotFoundException;
import java.util.Vector;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String ERROR = "\u001B[31m"; // RED
    public static final String INPUT = "\u001B[32m"; // GREEN
    public static final String COMMENT = "\u001B[33m"; // YELLOW
    public static final String CHOOSE = "\u001B[34m"; // BLUE
    public static final String OUTPUT = "\u001B[35m"; // PURPLE
    public static final String HEADER_OUTPUT = "\u001B[36m"; // CYAN
    public static void main(String[] args) throws FileNotFoundException {
        String pathToValuesMatrix = "src/valuesMatrixInput.txt";
        String pathToObjFunction = "src/objFunctionInput.txt";
        String pathToConstraints = "src/constraintsInput.txt";

        CanonicalSimplexMatrix canonicalSimplex = new CanonicalSimplexMatrix(pathToValuesMatrix, pathToObjFunction, pathToConstraints);
        canonicalSimplex.setDeltaGrades(canonicalSimplex.calculateDeltaGrades());
        canonicalSimplex.printDeltaGrades();
    }
}