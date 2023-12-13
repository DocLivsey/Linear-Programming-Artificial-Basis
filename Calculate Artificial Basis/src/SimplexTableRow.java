import java.text.DecimalFormat;
import java.util.*;
public class SimplexTableRow {
    protected String row;
    protected String topBorder;
    protected String bottomBorder;
    protected Vector<String> rowContent;
    protected int rowNumber;
    SimplexTableRow(int rowNumber, Vector<String> rowContent, HashMap<Integer, Integer> colWidth)
    {
        this.rowNumber = rowNumber;
        this.rowContent = rowContent;
        this.createRow(colWidth);
    }
    public String getRow()
    { return this.row; }
    public Vector<String> getRowContent()
    { return this.rowContent; }
    public int getRowNumber()
    { return this.rowNumber; }

    public void setRow(String row)
    { this.row = row; }
    public void setRowContent(Vector<String> rowContent)
    { this.rowContent = rowContent; }
    public void setRowNumber(int rowNumber)
    { this.rowNumber = rowNumber; }
    public void createRow(HashMap<Integer, Integer> colWidth)
    {
        String singleItem, leftSpaces, rightSpaces;
        StringBuilder row = new StringBuilder();
        StringBuilder border = new StringBuilder(Main.COMMENT + "|" + Main.RESET);
        for (int i = 0; i < colWidth.size(); i++)
        {
            String item = this.rowContent.get(i);
            int lengthSpaces = Math.abs(colWidth.get(i) - item.length());
            border.append(Main.COMMENT).append("—".repeat(colWidth.get(i))).append("|" + Main.RESET);
            if (lengthSpaces % 2 != 0)
            {
                leftSpaces = " ".repeat(lengthSpaces / 2 + 1);
                rightSpaces = " ".repeat(lengthSpaces / 2);
            }
            else if (lengthSpaces == 0)
            { leftSpaces = ""; rightSpaces = leftSpaces; }
            else
            {
                leftSpaces = " ".repeat(lengthSpaces / 2);
                rightSpaces = " ".repeat(lengthSpaces / 2);
            }
            singleItem = Main.COMMENT + "|" + Main.OUTPUT + leftSpaces + item + rightSpaces + Main.RESET;
            row.append(singleItem);
        }
        row.append(Main.COMMENT + "|" + Main.RESET);
        if (this.rowNumber == 0)
            this.setRow(border + "\n" + row + "\n" + border + "\n");
        else this.setRow(row + "\n" + border + "\n");
    }
}
