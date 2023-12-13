import java.io.*;
import java.util.*;
public class SimplexTable {
    protected Vector<SimplexTableRow> simplexTable;
    protected HashMap<Integer, Integer> columnsWidth;
    protected int tableHeight;
    protected Vector<Vector<String>> simplexTableContent;
    protected ArtificialBasisExpansion simplexMatrix;
    SimplexTable(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException
    {
        this.columnsWidth = new HashMap<>();
        this.simplexMatrix = new ArtificialBasisExpansion(pathToValuesMatrix, pathToObjFunction, pathToConstraints);
        this.tableHeight = simplexMatrix.getConstraints().size() + 3;
        this.simplexTableContent = new Vector<>();
        this.createTable();
    }
    SimplexTable(ArtificialBasisExpansion simplexMatrix)
    {
        this.columnsWidth = new HashMap<>();
        this.simplexMatrix = simplexMatrix;
        this.tableHeight = simplexMatrix.getConstraints().size() + 3;
        this.simplexTableContent = this.simplexMatrix.transposeConvertToCorrectStringFormat();
        this.createTable();
    }
    public Vector<SimplexTableRow> getSimplexTable() {
        return simplexTable;
    }
    public HashMap<Integer, Integer> getColumnsWidth() {
        return columnsWidth;
    }
    public int getTableHeight() {
        return tableHeight;
    }
    public Integer getColumnWidthByIndex(int index) {
        return this.columnsWidth.get(index);
    }
    public Vector<Vector<String>> getSimplexTableContent() {
        return simplexTableContent;
    }

    public ArtificialBasisExpansion getSimplexMatrix() {
        return simplexMatrix;
    }
    public void setSimplexTable(Vector<SimplexTableRow> simplexTable) {
        this.simplexTable = simplexTable;
    }
    public void setColumnsWidth(HashMap<Integer, Integer> columnsWidth) {
        this.columnsWidth = columnsWidth;
    }
    public void setTableHeight(int tableHeight) {
        this.tableHeight = tableHeight;
    }
    public void setColumnWidthByIndex(int index, int width) {
        this.columnsWidth.put(index, width);
    }
    public void setSimplexTableContent(Vector<Vector<String>> simplexTableContent) {
        this.simplexTableContent = simplexTableContent;
    }

    public void setSimplexMatrix(ArtificialBasisExpansion simplexMatrix) {
        this.simplexMatrix = simplexMatrix;
    }
    public void calculateBlocksSize()
    {
        this.setSimplexTableContent(this.simplexMatrix.transposeConvertToCorrectStringFormat());
        for (int i = 0; i < this.simplexTableContent.size(); i++)
        {
            Vector<String> currentCol = this.simplexTableContent.get(i);
            int len = currentCol.get(0).length();
            for (String item : currentCol)
                len = Math.max(len, item.length());
            this.setColumnWidthByIndex(i, len);
        }
    }
    public void calculateMaxBlocksSize()
    {
        int maxLen = 0;
        for (Vector<String> currentCol : this.simplexTableContent)
            for (String item : currentCol)
                maxLen = Math.max(maxLen, item.length());
        for (int i = 0; i < this.columnsWidth.size(); i++)
            this.setColumnWidthByIndex(i, maxLen);
    }
    public void createTable()
    {
        this.calculateBlocksSize();
        this.setSimplexTableContent(this.simplexMatrix.convertToCorrectStringFormat());
        this.simplexTable = new Vector<>();
        for (int i = 0; i < this.tableHeight; i++)
        {
            SimplexTableRow tableRow = new SimplexTableRow(i, this.simplexTableContent.get(i), this.columnsWidth);
            this.simplexTable.add(tableRow);
        }
    }
    public void printTable()
    {
        for (SimplexTableRow tableRow : this.simplexTable)
            System.out.print(tableRow.getRow());
    }
}
