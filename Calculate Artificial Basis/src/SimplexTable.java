import java.io.*;
import java.util.*;
public class SimplexTable {
    protected Vector<SimplexTableColumn> simplexTable;
    protected ArrayList<Vector<String>> simplexTableContent;
    protected SimplexTable prevSimplexTable;
    protected SimplexTable nextSimplexTable;
    protected ArtificialBasisExpansion simplexMatrix;
    SimplexTable(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException
    {
        this.simplexMatrix = new ArtificialBasisExpansion(pathToValuesMatrix, pathToObjFunction, pathToConstraints);
        this.simplexTableContent = this.simplexMatrix.convertToCorrectStringFormat();
        this.prevSimplexTable = new SimplexTable((ArtificialBasisExpansion) this.simplexMatrix.getPrevSimplexMatrix());
        this.nextSimplexTable = new SimplexTable((ArtificialBasisExpansion) this.simplexMatrix.getNextSimplexMatrix());
    }
    SimplexTable(ArtificialBasisExpansion simplexMatrix)
    {
        this.simplexMatrix = simplexMatrix;
        this.simplexTableContent = this.simplexMatrix.convertToCorrectStringFormat();
        this.prevSimplexTable = new SimplexTable((ArtificialBasisExpansion) this.simplexMatrix.getPrevSimplexMatrix());
        this.nextSimplexTable = new SimplexTable((ArtificialBasisExpansion) this.simplexMatrix.getNextSimplexMatrix());
    }
    public Vector<SimplexTableColumn> getSimplexTable() {
        return simplexTable;
    }
    public ArrayList<Vector<String>> getSimplexTableContent() {
        return simplexTableContent;
    }
    public SimplexTable getPrevSimplexTable() {
        return prevSimplexTable;
    }
    public SimplexTable getNextSimplexTable() {
        return nextSimplexTable;
    }
    public CanonicalSimplexMatrix getSimplexMatrix() {
        return simplexMatrix;
    }
    public void setSimplexTable(Vector<SimplexTableColumn> simplexTable) {
        this.simplexTable = simplexTable;
    }
    public void setSimplexTableContent(ArrayList<Vector<String>> simplexTableContent) {
        this.simplexTableContent = simplexTableContent;
    }
    public void setPrevSimplexTable(SimplexTable prevSimplexTable) {
        this.prevSimplexTable = prevSimplexTable;
    }
    public void setNextSimplexTable(SimplexTable nextSimplexTable) {
        this.nextSimplexTable = nextSimplexTable;
    }
    public void setSimplexMatrix(ArtificialBasisExpansion simplexMatrix) {
        this.simplexMatrix = simplexMatrix;
    }
    public int calculateBlockSize(int index)
    {
        int size = 0;
        for (String item : this.simplexTableContent.get(index))
            size = Math.max(item.length(), size);
        if (index == 0)
            return size + 2;
        return size + 1;
    }
    public String setHeaderItem(int colInd)
    {
        HashMap<Integer, String> headers = new HashMap<>();
        headers.put(0, "N"); headers.put(1, "Базис"); headers.put(2, "С Базис"); headers.put(3, "B");
        for (int i = 0; i < this.simplexMatrix.getVariables().size(); i++)
            headers.put(i + 4, this.simplexMatrix.getVariables().get(i));
        for (int i = 0; i < this.simplexMatrix.getBasis().size(); i++)
            headers.put(i + this.simplexMatrix.getVariables().size() + 4, this.simplexMatrix.getBasis().get(i));
        return headers.get(colInd);
    }
    public void createTable()
    {
        for (int i = 0; i < this.simplexTableContent.size(); i++)
            this.simplexTable.add(new SimplexTableColumn(i, this.simplexTableContent.get(i)));
    }
    public void printTable()
    {
        for (SimplexTableColumn column : this.simplexTable)
            System.out.print(column.getColumn());
    }
}
