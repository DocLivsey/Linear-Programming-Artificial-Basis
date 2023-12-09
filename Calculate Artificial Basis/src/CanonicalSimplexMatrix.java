import java.io.*;
import java.util.*;
public class CanonicalSimplexMatrix {
    protected Matrix simplexMatrix;
    protected CanonicalSimplexMatrix prevSimplexMatrix;
    protected CanonicalSimplexMatrix nextSimplexMatrix;
    protected Vector<Double> objectiveFunction;
    protected Vector<Double> constraint;
    protected Vector<Double> basisValue;
    protected Vector<String> basis;
    protected Vector<Double> deltaGrades;
    protected Vector<String> solution;
    protected String solutionExplain;
    CanonicalSimplexMatrix(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException {
        this.simplexMatrix = this.readSimplexMatrixFromFile(pathToValuesMatrix);
        this.objectiveFunction = this.readVectorFromFile(pathToObjFunction);
        this.constraint = this.readVectorFromFile(pathToConstraints);
        this.prevSimplexMatrix = null;
        this.nextSimplexMatrix = new CanonicalSimplexMatrix(this);
        this.basisValue = new Vector<>();
        this.basis = new Vector<>();
        this.deltaGrades = new Vector<>();
        this.solution = new Vector<>();
        this.solutionExplain = "Решений нет";
    }
    CanonicalSimplexMatrix(Matrix simplexMatrix, CanonicalSimplexMatrix prevSimplexMatrix, Vector<Double> constraint, Vector<String> basis, Vector<Double> basisValue)
    {
        this.simplexMatrix = simplexMatrix;
        this.prevSimplexMatrix = prevSimplexMatrix;
        this.nextSimplexMatrix = null;
        this.objectiveFunction = prevSimplexMatrix.getObjectiveFunction();
        this.constraint = constraint;
        this.basisValue = basisValue;
        this.basis = basis;
        this.deltaGrades = new Vector<>();
        this.solution = prevSimplexMatrix.getSolution();
        this.solutionExplain = prevSimplexMatrix.getSolutionExplain();
    }
    CanonicalSimplexMatrix(CanonicalSimplexMatrix prevSimplexMatrix)
    {
        this.simplexMatrix = new Matrix(prevSimplexMatrix.getSimplexMatrix().getRowsCount(), prevSimplexMatrix.getSimplexMatrix().getColumnsCount());
        this.prevSimplexMatrix = prevSimplexMatrix;
        this.nextSimplexMatrix = null;
        this.objectiveFunction = new Vector<>();
        this.constraint = new Vector<>();
        this.basisValue = new Vector<>();
        this.basis = new Vector<>();
        this.deltaGrades = new Vector<>();
        this.solution = new Vector<>();
        this.solutionExplain = "Решений нет";
    }
    public Matrix getSimplexMatrix() {
        return simplexMatrix;
    }
    public CanonicalSimplexMatrix getPrevSimplexMatrix() {
        return prevSimplexMatrix;
    }
    public CanonicalSimplexMatrix getNextSimplexMatrix() {
        return nextSimplexMatrix;
    }
    public Vector<Double> getObjectiveFunction() {
        return objectiveFunction;
    }
    public Vector<Double> getConstraint() {
        return constraint;
    }
    public Vector<Double> getBasisValue() {
        return basisValue;
    }
    public Vector<String> getBasis() {
        return basis;
    }
    public Vector<String> getSolution() {
        return solution;
    }
    public String getSolutionExplain() {
        return solutionExplain;
    }
    public Vector<Double> getDeltaGrades() {
        return deltaGrades;
    }
    public void setSimplexMatrix(Matrix simplexMatrix) {
        this.simplexMatrix = simplexMatrix;
    }
    public void setPrevSimplexMatrix(CanonicalSimplexMatrix prevSimplexMatrix) {
        this.prevSimplexMatrix = prevSimplexMatrix;
    }
    public void setNextSimplexMatrix(CanonicalSimplexMatrix nextSimplexMatrix) {
        this.nextSimplexMatrix = nextSimplexMatrix;
    }
    public void setObjectiveFunction(Vector<Double> objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }
    public void setConstraint(Vector<Double> constraint) {
        this.constraint = constraint;
    }
    public void setBasis(Vector<String> basis) {
        this.basis = basis;
    }
    public void setBasisValue(Vector<Double> basisValue) {
        this.basisValue = basisValue;
    }
    public void setSolution(Vector<String> solution) {
        this.solution = solution;
    }
    public void setSolutionExplain(String solutionExplain) {
        this.solutionExplain = solutionExplain;
    }
    public void setDeltaGrades(Vector<Double> deltaGrades) {
        this.deltaGrades = deltaGrades;
    }
    public Matrix readSimplexMatrixFromFile(String pathToFile) throws FileNotFoundException {
        return new Matrix(pathToFile);
    }
    public Vector<Double> readVectorFromFile(String pathToFile) throws  FileNotFoundException {
        Vector<Double> returnVector = new Vector<>();
        File input = new File(pathToFile);
        Scanner fileScanner = new Scanner(input);
        String[] strArr = fileScanner.nextLine().trim().split("\\s+");
        for (String str : strArr)
            returnVector.add(Double.parseDouble(str));
        return returnVector;
    }
    public Vector<Double> calculateDeltaGrades()
    {
        Vector<Double> calculationDeltaGrade = new Vector<>(this.simplexMatrix.getColumnsCount() + 1);
        double delta = 0;
        for (double constraint : this.constraint)
            delta += constraint;
        calculationDeltaGrade.add(0, delta);
        for (int colInd = 0; colInd < this.simplexMatrix.getColumnsCount(); colInd++)
        {
            delta = 0;
            for (int rowInd = 0; rowInd < this.simplexMatrix.getRowsCount(); rowInd++)
                delta += this.simplexMatrix.getItem(rowInd, colInd);
            delta -= this.objectiveFunction.get(colInd);
            calculationDeltaGrade.add(colInd + 1, delta);
        }
        System.out.println("size = " + calculationDeltaGrade.size());
        return calculationDeltaGrade;
    }
    public void printDeltaGrades()
    {
        System.out.println(Main.HEADER_OUTPUT + "Дельта Оценки:");
        for (double delta : this.deltaGrades)
            System.out.println(Main.OUTPUT + delta + Main.RESET);
    }
}
