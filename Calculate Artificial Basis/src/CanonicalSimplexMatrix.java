import java.io.*;
import java.util.*;
public class CanonicalSimplexMatrix {
    protected final double M = 1E5;
    protected Matrix simplexMatrix;
    protected CanonicalSimplexMatrix prevSimplexMatrix;
    protected CanonicalSimplexMatrix nextSimplexMatrix;
    protected Vector<Double> objectiveFunction;
    protected Vector<Double> constraints;
    protected Vector<Double> basisValue;
    protected ArrayList<String> basis;
    protected Vector<String> variables;
    protected Vector<Double> deltaGrades;
    protected HashMap<String, Double> solution;
    protected double objectiveFunctionValue = -1;
    protected String solutionExplain;
    CanonicalSimplexMatrix(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException {
        this.simplexMatrix = this.readSimplexMatrixFromFile(pathToValuesMatrix);
        this.objectiveFunction = this.readVectorFromFile(pathToObjFunction);
        this.constraints = this.readVectorFromFile(pathToConstraints);
        this.prevSimplexMatrix = null;
        this.nextSimplexMatrix = new CanonicalSimplexMatrix(this);
        this.basisValue = new Vector<>();
        this.basis = new ArrayList<>();
        this.variables = new Vector<>();
        this.setVariables();
        this.deltaGrades = new Vector<>();
        this.solution = new HashMap<>();
        this.setSolutionVariables();
        this.solutionExplain = this.getMessage("Not");
    }
    CanonicalSimplexMatrix(Matrix simplexMatrix, CanonicalSimplexMatrix prevSimplexMatrix, Vector<Double> constraint, ArrayList<String> basis, Vector<Double> basisValue)
    {
        this.simplexMatrix = simplexMatrix;
        this.prevSimplexMatrix = prevSimplexMatrix;
        this.nextSimplexMatrix = null;
        this.objectiveFunction = prevSimplexMatrix.getObjectiveFunction();
        this.constraints = constraint;
        this.basisValue = basisValue;
        this.basis = basis;
        this.variables = new Vector<>();
        this.setVariables();
        this.deltaGrades = new Vector<>();
        this.solution = prevSimplexMatrix.getSolution();
        this.solutionExplain = prevSimplexMatrix.getSolutionExplain();
    }
    CanonicalSimplexMatrix(Matrix simplexMatrix, Vector<Double> objectiveFunction, Vector<Double> constraint, ArrayList<String> basis, Vector<Double> basisValue)
    {
        this.simplexMatrix = simplexMatrix;
        this.prevSimplexMatrix = null;
        this.nextSimplexMatrix = null;
        this.objectiveFunction = objectiveFunction;
        this.constraints = constraint;
        this.basisValue = basisValue;
        this.basis = basis;
        this.variables = new Vector<>();
        this.setVariables();
        this.deltaGrades = new Vector<>();
        this.solution = new HashMap<>();
        this.setSolutionVariables();
        this.solutionExplain = this.getMessage("Not");
    }
    CanonicalSimplexMatrix(CanonicalSimplexMatrix prevSimplexMatrix)
    {
        this.simplexMatrix = new Matrix(prevSimplexMatrix.getSimplexMatrix().getRowsCount(), prevSimplexMatrix.getSimplexMatrix().getColumnsCount());
        this.prevSimplexMatrix = prevSimplexMatrix;
        this.nextSimplexMatrix = null;
        this.objectiveFunction = new Vector<>();
        this.constraints = new Vector<>();
        this.basisValue = new Vector<>();
        this.basis = new ArrayList<>();
        this.variables = new Vector<>();
        this.deltaGrades = new Vector<>();
        this.solution = new HashMap<>();
        this.setSolutionVariables();
        this.solutionExplain = this.getMessage("Not");
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
    public Vector<Double> getConstraints() {
        return constraints;
    }
    public Vector<Double> getBasisValue() {
        return basisValue;
    }
    public ArrayList<String> getBasis() {
        return basis;
    }
    public Vector<String> getVariables() {
        return variables;
    }
    public HashMap<String, Double> getSolution() {
        return solution;
    }
    public double getObjectiveFunctionValue() {
        return objectiveFunctionValue;
    }
    public String getSolutionExplain() {
        return solutionExplain;
    }
    public Vector<Double> getDeltaGrades() {
        return deltaGrades;
    }
    public String getMessage(String name) {
        HashMap<String, String> messages = new HashMap<>(){{
            put("Not", "Решений нет");
            put("Error", "Решение не допустимо\n Либо не допустимы входные данные");
            put("No Limit", "Целевая Функция не ограничена полученное Опорное Решение Не Оптимально");
            put("Optimal", "Получено Оптимальное Опорное Решение");
        }};
        return Main.OUTPUT + messages.get(name) + Main.RESET;
    }
    public void setSimplexMatrix(Matrix simplexMatrix) {
        this.simplexMatrix = simplexMatrix;
    }
    public void setPrevSimplexMatrix(CanonicalSimplexMatrix prevSimplexMatrix) {
        this.prevSimplexMatrix = prevSimplexMatrix;
    }
    public void setNextSimplexMatrix(CanonicalSimplexMatrix nextSimplexMatrix) {
        this.nextSimplexMatrix = nextSimplexMatrix;
        this.nextSimplexMatrix.setPrevSimplexMatrix(this);
    }
    public void setObjectiveFunction(Vector<Double> objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }
    public void setConstraints(Vector<Double> constraints) {
        this.constraints = constraints;
    }
    public void setBasis(ArrayList<String> basis) {
        this.basis = basis;
    }
    public void setBasisValue(Vector<Double> basisValue) {
        this.basisValue = basisValue;
    }
    public void setVariables() {
        for (int i = 0; i < this.simplexMatrix.getColumnsCount(); i++)
            this.variables.add(i, "X" + (i + 1));
    }
    public void setSolution(HashMap<String, Double> solution) {
        this.solution = solution;
    }
    public void setSolutionVariables()
    {
        for (String var : this.variables)
            this.solution.put(var, 0.0);
    }
    public void setObjectiveFunctionValue(double objectiveFunctionValue) {
        this.objectiveFunctionValue = objectiveFunctionValue;
    }
    public void setSolutionExplain(String solutionExplain) {
        this.solutionExplain = solutionExplain;
    }
    public void setDeltaGrades(Vector<Double> deltaGrades) {
        this.deltaGrades = deltaGrades;
    }
    public void setPrevDataForNextObject(CanonicalSimplexMatrix prevSimplexMatrix)
    {
        this.simplexMatrix = prevSimplexMatrix.getSimplexMatrix();
        this.objectiveFunction = prevSimplexMatrix.getObjectiveFunction();
        this.constraints = prevSimplexMatrix.getConstraints();
        this.basisValue = prevSimplexMatrix.getBasisValue();
        this.basis = prevSimplexMatrix.getBasis();
        this.variables = prevSimplexMatrix.getVariables();
        this.deltaGrades = prevSimplexMatrix.getDeltaGrades();
        this.solution = prevSimplexMatrix.getSolution();
        this.solutionExplain = prevSimplexMatrix.getSolutionExplain();
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
        for (int i = 0; i < this.constraints.size(); i++)
            if (this.basisValue.get(i) != -this.M)
                delta += this.constraints.get(i) * this.basisValue.get(i);
        calculationDeltaGrade.add(delta);
        for (int colInd = 0; colInd < this.simplexMatrix.getColumnsCount(); colInd++)
        {
            delta = 0;
            for (int rowInd = 0; rowInd < this.simplexMatrix.getRowsCount(); rowInd++)
                if (this.basisValue.get(rowInd) != -this.M)
                    delta += this.simplexMatrix.getItem(rowInd, colInd) * this.basisValue.get(rowInd);
            delta -= this.objectiveFunction.get(colInd);
            calculationDeltaGrade.add(delta);
        }
        return calculationDeltaGrade;
    }
    public void printDeltaGrades()
    {
        System.out.println(Main.HEADER_OUTPUT + "Дельта Оценки:");
        for (double delta : this.deltaGrades)
            System.out.println(Main.OUTPUT + delta + Main.RESET);
    }
}
