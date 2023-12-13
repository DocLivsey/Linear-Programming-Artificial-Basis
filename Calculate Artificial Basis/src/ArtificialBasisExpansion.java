import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

public class ArtificialBasisExpansion extends CanonicalSimplexMatrix {
    protected Matrix artificialBasisMatrix;
    protected Vector<Double> artificialDeltaGrade;
    protected ArtificialBasisExpansion prevExpandedSimplexMatrix;
    protected ArtificialBasisExpansion nextExpandedSimplexMatrix;
    ArtificialBasisExpansion(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException {
        super(pathToValuesMatrix, pathToObjFunction, pathToConstraints);
        this.setArtificialVariables();
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        super.objectiveFunction.addAll(super.basisValue);
        this.artificialDeltaGrade = new Vector<>();
        this.calculateArtificialDeltaGrades();
    }
    ArtificialBasisExpansion(Matrix simplexMatrix, ArtificialBasisExpansion prevSimplexMatrix, Vector<Double> constraint, ArrayList<String> basis, Vector<Double> basisValue)
    {
        super(simplexMatrix, prevSimplexMatrix, constraint, basis, basisValue);
        this.prevExpandedSimplexMatrix = prevSimplexMatrix;
        this.nextExpandedSimplexMatrix = null;
        this.setArtificialVariables();
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        this.artificialDeltaGrade = new Vector<>();
        this.calculateArtificialDeltaGrades();
    }
    ArtificialBasisExpansion(Matrix simplexMatrix, Vector<Double> objectiveFunction, Vector<Double> constraint, ArrayList<String> basis, Vector<Double> basisValue)
    {
        super(simplexMatrix, objectiveFunction, constraint, basis, basisValue);
        this.prevExpandedSimplexMatrix = null;
        this.nextExpandedSimplexMatrix = null;
        this.setArtificialVariables();
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        this.artificialDeltaGrade = new Vector<>();
        this.calculateArtificialDeltaGrades();
    }
    ArtificialBasisExpansion(ArtificialBasisExpansion prevSimplexMatrix)
    {
        super(prevSimplexMatrix);
        this.prevExpandedSimplexMatrix = prevSimplexMatrix;
        this.nextExpandedSimplexMatrix = null;
        this.setArtificialVariables();
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        this.artificialDeltaGrade = new Vector<>();
    }
    public Matrix getArtificialBasisMatrix() {
        return artificialBasisMatrix;
    }
    public Vector<Double> getArtificialDeltaGrade() {
        return artificialDeltaGrade;
    }
    public ArtificialBasisExpansion getPrevExpandedSimplexMatrix() {
        return prevExpandedSimplexMatrix;
    }
    public ArtificialBasisExpansion getNextExpandedSimplexMatrix() {
        return nextExpandedSimplexMatrix;
    }
    public void setArtificialBasisMatrix() {
        this.artificialBasisMatrix = Matrix.setIdentityMatrix(super.constraints.size());
    }
    public void setArtificialVariables() {
        for (int i = 0; i < super.constraints.size(); i++)
            super.variables.add("X" + (i + super.simplexMatrix.getColumnsCount() + 1));
    }
    public void setArtificialDeltaGrade(Vector<Double> artificialDeltaGrade) {
        this.artificialDeltaGrade = artificialDeltaGrade;
    }
    public void setArtificialBasis()
    {
        if (super.basis.isEmpty())
            for (int i = 0; i < super.constraints.size(); i++)
                super.basis.add("X" + (i + super.simplexMatrix.getColumnsCount() + 1));
    }
    public void setArtificialBasisValue()
    {
        if (this.basisValue.isEmpty())
            for (int i = 0; i < super.basis.size(); i++)
                super.basisValue.add(i, -this.M);
    }
    public void setPrevExpandedSimplexMatrix(ArtificialBasisExpansion prevExpandedSimplexMatrix) {
        this.prevExpandedSimplexMatrix = prevExpandedSimplexMatrix;
        this.nextExpandedSimplexMatrix.setPrevExpandedSimplexMatrix(this);
    }
    public void setNextExpandedSimplexMatrix(ArtificialBasisExpansion nextExpandedSimplexMatrix) {
        this.nextExpandedSimplexMatrix = nextExpandedSimplexMatrix;
    }
    public void setPrevDataForNextObject(ArtificialBasisExpansion extendedSimplexMatrix)
    {
        super.setPrevDataForNextObject(extendedSimplexMatrix);
        this.artificialBasisMatrix = prevExpandedSimplexMatrix.getArtificialBasisMatrix();
        this.artificialDeltaGrade = prevExpandedSimplexMatrix.getArtificialDeltaGrade();
        this.calculateArtificialDeltaGrades();
    }
    public Vector<Vector<String>> convertToCorrectStringFormat()
    {
        DecimalFormat formattedOut = new DecimalFormat("#.####");
        int width = super.constraints.size();
        Vector<Vector<String>> table = new Vector<>();
        Vector<String> row = new Vector<>(List.of(new String[]{"N", "Базис", "С Базис", "В"}));
        row.addAll(this.variables);
        table.add(row);
        for (int i = 0; i < width; i++)
        {
            row = new Vector<>();
            row.add((i + 1) + "");
            row.add(super.basis.get(i));
            row.add(formattedOut.format(super.basisValue.get(i)));
            row.add(formattedOut.format(super.constraints.get(i)));
            row.addAll(this.simplexMatrix.rowToStringVector(i));
            row.addAll(this.artificialBasisMatrix.rowToStringVector(i));
            table.add(row);
        }
        Vector<String> grades = new Vector<>();
        grades.add((width + 1) + ""); grades.add(""); grades.add("Δ'");
        for (double i : super.deltaGrades)
            grades.add(formattedOut.format(i));
        table.add(grades);
        grades = new Vector<>();
        grades.add((width + 2) + ""); grades.add(""); grades.add("Δ''");
        for (double i : this.artificialDeltaGrade)
            grades.add(formattedOut.format(i));
        table.add(grades);
        return table;
    }
    public Vector<Vector<String>> transposeConvertToCorrectStringFormat()
    {
        DecimalFormat formattedOut = new DecimalFormat("#.####");
        Vector<Vector<String>> table = new Vector<>();
        Vector<String> strVector = new Vector<>();
        strVector.add("N");
        for (int i = 0; i < super.constraints.size(); i++)
            strVector.add(formattedOut.format(i));
        strVector.add(super.constraints.size() + "");
        strVector.add((super.constraints.size() + 1) + "");
        table.add(strVector);
        strVector = new Vector<>();
        strVector.add("Базис");
        strVector.addAll(super.basis);
        strVector.add("");
        strVector.add("");
        table.add(strVector);
        strVector = new Vector<>();
        strVector.add("C Базис");
        for (double i : this.basisValue)
            strVector.add(formattedOut.format(i));
        strVector.add("Δ'");
        strVector.add("Δ''");
        table.add(strVector);
        strVector = new Vector<>();
        strVector.add("B");
        for (double i : this.constraints)
            strVector.add(formattedOut.format(i));
        strVector.add(formattedOut.format(super.deltaGrades.get(0)));
        strVector.add(formattedOut.format(this.artificialDeltaGrade.get(0)));
        table.add(strVector);
        for (int i = 0; i < super.getSimplexMatrix().getColumnsCount(); i++)
        {
            strVector = new Vector<>();
            strVector.add(super.variables.get(i));
            for (int j = 0; j < super.getSimplexMatrix().getRowsCount(); j++)
                strVector.add(formattedOut.format(super.getSimplexMatrix().getItem(j, i)));
            strVector.add(formattedOut.format(super.deltaGrades.get(i + 1)));
            strVector.add(formattedOut.format(this.artificialDeltaGrade.get(i + 1)));
            table.add(strVector);
        }
        for (int i = 0; i < this.getArtificialBasisMatrix().getColumnsCount(); i++)
        {
            strVector = new Vector<>();
            for (int j = 0; j < this.getArtificialBasisMatrix().getRowsCount(); j++)
                strVector.add(formattedOut.format(this.getArtificialBasisMatrix().getItem(j, i)));
            strVector.add(formattedOut.format(super.getDeltaGrades().get(i + this.getSimplexMatrix().getColumnsCount() + 1)));
            strVector.add(formattedOut.format(this.artificialDeltaGrade.get(i + this.getSimplexMatrix().getColumnsCount() + 1)));
            table.add(strVector);
        }
        return table;
    }
    public void calculateArtificialDeltaGrades()
    {
        Vector<Double> calculationDeltaGrade;
        Vector<Double> calculationArtificialDeltaGrade = new Vector<>();
        calculationDeltaGrade = super.calculateDeltaGrades();
        double delta = 0;
        for (int colInd = 0; colInd < this.artificialBasisMatrix.getColumnsCount(); colInd++)
        {
            for (int rowInd = 0; rowInd < this.artificialBasisMatrix.getRowsCount(); rowInd++)
                if (this.basisValue.get(rowInd) != -this.M)
                    delta += this.artificialBasisMatrix.getItem(rowInd, colInd) * this.basisValue.get(rowInd);
            if (this.objectiveFunction.get(colInd + this.simplexMatrix.getColumnsCount()) != -this.M)
                delta -= this.objectiveFunction.get(colInd + this.simplexMatrix.getColumnsCount());
            calculationDeltaGrade.add(delta);
        }
        super.setDeltaGrades(calculationDeltaGrade);
        delta = 0;
        for (int i = 0; i < this.constraints.size(); i++)
            if (this.basisValue.get(i) == -this.M)
                delta += this.constraints.get(i) * this.basisValue.get(i);
        calculationArtificialDeltaGrade.add(delta);
        for (int colInd = 0; colInd < this.simplexMatrix.getColumnsCount(); colInd++)
        {
            delta = 0;
            for (int rowInd = 0; rowInd < this.simplexMatrix.getRowsCount(); rowInd++)
                if (this.basisValue.get(rowInd) == -this.M)
                    delta += this.simplexMatrix.getItem(rowInd, colInd) * this.basisValue.get(rowInd);
            calculationArtificialDeltaGrade.add(delta);
        }
        for (int colInd = 0; colInd < this.artificialBasisMatrix.getColumnsCount(); colInd++)
        {
            delta = 0;
            for (int rowInd = 0; rowInd < this.artificialBasisMatrix.getRowsCount(); rowInd++)
                if (this.basisValue.get(rowInd) == -this.M)
                    delta += this.artificialBasisMatrix.getItem(rowInd, colInd) * this.basisValue.get(rowInd);
            calculationArtificialDeltaGrade.add(delta);
        }
        this.setArtificialDeltaGrade(calculationArtificialDeltaGrade);
    }
    public void printAllDeltaGrades()
    {
        int number = 1;
        System.out.println(Main.HEADER_OUTPUT + "Дельта Оценки:" + Main.RESET);
        for (double delta : super.deltaGrades)
        {
            System.out.print(Main.OUTPUT + "Δ'" + number + " = " + delta + ", " + Main.RESET);
            number++;
        }
        System.out.println();
        number = 1;
        for (double delta : this.artificialDeltaGrade)
        {
            System.out.print(Main.OUTPUT + "Δ''" + number + " = " + delta + ", " + Main.RESET);
            number++;
        }
        System.out.println();
    }
    public boolean isValuesValid()
    {
        for (Double constraint : super.constraints)
            if (constraint < 0)
            {
                super.setSolutionExplain(super.getMessage("Error"));
                return false;
            }
        return true;
    }
    public boolean isDeltaPositive()
    {
        for (int i = 0; i < super.deltaGrades.size(); i++)
            if ((super.deltaGrades.get(i) + this.artificialDeltaGrade.get(i)) < 0)
                return false;
        super.setObjectiveFunctionValue(super.deltaGrades.get(0));
        super.setSolutionExplain(super.getMessage("Optimal"));
        for (int i = 0; i < super.basis.size(); i ++)
            super.solution.put(super.basis.get(i), super.constraints.get(i));
        return true;
    }
    public boolean isZeroArtificialDeltas()
    {
        for (double delta : this.artificialDeltaGrade)
            if (delta != 0)
                return false;
        return true;
    }
    public int findMinDeltaIndex()
    {
        int indexOfMin = 1;
        double min = this.M;
        for (int i = 1; i < super.deltaGrades.size(); i++)
            if (super.deltaGrades.get(i) + this.artificialDeltaGrade.get(i) < min)
            {
                indexOfMin = i;
                min = super.deltaGrades.get(i) + this.artificialDeltaGrade.get(i);
            }
        return indexOfMin - 1;
    }
    public int findIndexOfMinRelation(int colIndex)
    {
        if (this.isSolutionPossible(colIndex))
        {
            int indexOfMin = 0;
            double minRelation = this.M;
            for (int i = 0; i < this.constraints.size(); i++)
                if (this.constraints.get(i) / this.simplexMatrix.getItem(i, colIndex) < minRelation && this.simplexMatrix.getItem(i, colIndex) > 0)
                {
                    indexOfMin = i;
                    minRelation = this.constraints.get(i) / this.simplexMatrix.getItem(i, colIndex);
                }
            return indexOfMin;
        }
        else
        {
            super.setSolutionExplain(super.getMessage("No Limit"));
            return -1;
        }
    }
    public void gaussianTransformAndChangeMatrix(int rowIndex, int colIndex)
    {
        super.basis.set(rowIndex, super.variables.get(colIndex));
        super.basisValue.set(rowIndex, super.objectiveFunction.get(colIndex));
        double setDivision = super.simplexMatrix.getItem(rowIndex, colIndex);
        super.constraints.set(rowIndex, super.constraints.get(rowIndex) / setDivision);
        for (int i = 0; i < super.simplexMatrix.getColumnsCount(); i++)
        {
            double currently = super.simplexMatrix.getItem(rowIndex, i);
            super.simplexMatrix.setItem(rowIndex, i, currently / setDivision);
        }
        for (int i = 0; i < this.artificialBasisMatrix.getColumnsCount(); i++)
        {
            double currently = this.artificialBasisMatrix.getItem(rowIndex, i);
            this.artificialBasisMatrix.setItem(rowIndex, i, currently / setDivision);
        }
        double multiplier, selectedLine, changeLine;
        for (int i = 0; i < super.constraints.size(); i++)
            if (i != rowIndex)
            {
                multiplier = -super.simplexMatrix.getItem(i, colIndex);
                super.constraints.set(i, super.constraints.get(i) + super.constraints.get(rowIndex) * multiplier);
                for (int j = 0; j < super.simplexMatrix.getColumnsCount(); j++)
                {
                    selectedLine = super.simplexMatrix.getItem(rowIndex, j);
                    changeLine = super.simplexMatrix.getItem(i, j);
                    super.simplexMatrix.setItem(i, j, changeLine + multiplier * selectedLine);
                }
                for (int j = 0; j < this.artificialBasisMatrix.getColumnsCount(); j++)
                {
                    selectedLine = this.artificialBasisMatrix.getItem(rowIndex, j);
                    changeLine = this.artificialBasisMatrix.getItem(i, j);
                    this.artificialBasisMatrix.setItem(i, j, changeLine + multiplier * selectedLine);
                }
            }
    }
    public boolean isSolutionPossible(int colInd)
    {
        for (int i = 0; i < super.simplexMatrix.getRowsCount(); i++)
            if (super.simplexMatrix.getItem(i, colInd) > 0)
                return true;
        super.setObjectiveFunctionValue(super.deltaGrades.get(0));
        super.setSolutionExplain("No Limit");
        for (int i = 0; i < super.basis.size(); i ++)
            super.solution.put(super.basis.get(i), super.constraints.get(i));
        return false;
    }
    public void ArtificialBasisMethod() {
        if (this.isValuesValid())
        {
            int it = 0;
            SimplexTable table = new SimplexTable(this);
            table.printTable();
            ArtificialBasisExpansion cycleSimplexMatrix = this.cloneExpandedSimplexMatrix();
            while ((!cycleSimplexMatrix.isDeltaPositive() || !cycleSimplexMatrix.isValuesValid()) && it < 10)
            {
                it++; System.out.println(Main.ERROR + "iteration " + it + Main.RESET);
                this.setNextExpandedSimplexMatrix(cycleSimplexMatrix);
                System.out.println(Main.HEADER_OUTPUT + "TABLE BEFORE" + Main.RESET);
                table = new SimplexTable(this.nextExpandedSimplexMatrix);
                table.printTable();

                int colInd = this.nextExpandedSimplexMatrix.findMinDeltaIndex();
                int rowInd = this.nextExpandedSimplexMatrix.findIndexOfMinRelation(colInd);

                this.nextExpandedSimplexMatrix.gaussianTransformAndChangeMatrix(rowInd, colInd);
                System.out.println(Main.HEADER_OUTPUT + "TABLE AFTER" + Main.RESET);
                this.nextExpandedSimplexMatrix.calculateArtificialDeltaGrades();
                table = new SimplexTable(this.nextExpandedSimplexMatrix);
                table.printTable();

                cycleSimplexMatrix = this.getNextExpandedSimplexMatrix();
            }
            System.out.println(cycleSimplexMatrix.getSolutionExplain());
            System.out.println(cycleSimplexMatrix.getSolution());
            System.out.println("F = " + cycleSimplexMatrix.getObjectiveFunctionValue());
        }
        System.out.println(super.solutionExplain);
    }
    public ArtificialBasisExpansion cloneExpandedSimplexMatrix()
    {
        if (this.prevExpandedSimplexMatrix != null && super.prevSimplexMatrix != null)
            return new ArtificialBasisExpansion(this.simplexMatrix.cloneMatrix(), this.prevExpandedSimplexMatrix, new Vector<>(this.constraints), new ArrayList<>(this.basis), new Vector<>(this.basisValue));
        return new ArtificialBasisExpansion(this.simplexMatrix.cloneMatrix(), new Vector<>(super.objectiveFunction), new Vector<>(super.constraints), new ArrayList<>(super.basis), new Vector<>(super.basisValue));
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
