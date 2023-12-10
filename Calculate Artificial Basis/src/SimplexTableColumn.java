import java.text.DecimalFormat;
import java.util.Vector;

public class SimplexTableColumn {
    protected String column;
    protected Vector<String> columnContent;
    protected int columnNumber;
    protected int columnSize;
    SimplexTableColumn(int columnNumber, Vector<String> columnContent)
    {
        this.columnNumber = columnNumber;
        this.columnContent = columnContent;
        this.createColumn();
    }
    public String getColumn() {
        return column;
    }
    public Vector<String> getColumnContent() {
        return columnContent;
    }
    public int getColumnNumber() {
        return columnNumber;
    }
    public int getColumnSize() {
        return columnSize;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public void setColumnContent(Vector<String> columnContent) {
        this.columnContent = columnContent;
    }
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }
    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }
    public void calculateColumnSize()
    {
        int size = this.columnContent.get(0).length();
        for (String item : this.columnContent)
            size = Math.max(size, item.length());
        if(this.columnNumber == 0)
            this.setColumnSize(size + 2);
        else this.setColumnSize(size + 1);
    }
    public void createColumn()
    {
        StringBuilder strColumn = new StringBuilder();
        this.calculateColumnSize();
        for (int i = 0; i < this.columnContent.size(); i++)
        {
            String item = this.columnContent.get(i);
            SimplexTableSingleBlock singleBlock = new SimplexTableSingleBlock(i, this.columnNumber, this.columnSize, item, 0);
            strColumn.append(singleBlock.getSingleBlock());
        }
        this.setColumn(strColumn.toString());
    }
}
