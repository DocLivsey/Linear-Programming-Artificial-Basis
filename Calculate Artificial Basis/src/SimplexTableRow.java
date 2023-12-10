import java.util.*;
public class SimplexTableRow {
    protected String row;
    protected Vector<String> rowContent;
    protected int rowNumber;
    protected int rowSize;
    SimplexTableRow(int rowNumber, int width, int rowSize, ArtificialBasisExpansion simplexMatrix)
    {
        this.rowNumber = rowNumber;
        this.rowSize = rowSize;
    }
    public String getRow()
    { return this.row; }
    public Vector<String> getRowContent()
    { return this.rowContent; }
    public int getRowNumber()
    { return this.rowNumber; }
    public int getRowSize()
    { return this.rowSize; }
    public void setRow(String row)
    { this.row = row; }
    public void setRowContent(Vector<String> rowContent)
    { this.rowContent = rowContent; }
    public void setRowNumber(int rowNumber)
    { this.rowNumber = rowNumber; }
    public void setRowSize(int rowSize)
    { this.rowSize = rowSize; }
    public void setDeltaRow(ArtificialBasisExpansion simplexMatrix)
    {
        Vector<String> content = new Vector<>();
        content.add("");
        content.add("");
        content.add("Δ'");
        for (double i : simplexMatrix.calculateDeltaGrades())
            content.add("" + i);
        this.setRowContent(content);
    }
    public void createRow()
    {

    }
}
