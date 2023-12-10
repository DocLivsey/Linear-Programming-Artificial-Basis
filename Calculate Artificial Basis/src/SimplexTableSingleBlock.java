import java.util.*;
public class SimplexTableSingleBlock {
    protected String singleBlock;
    protected String topBorder;
    protected String bottomBorder;
    protected String leftBorder;
    protected String rightBorder;
    protected String singleBlockContent;
    protected int blockSize;
    protected int blockRowNumber;
    protected int blockColumnNumber;
    SimplexTableSingleBlock(int rowNumber, int columnNumber, int blockSize, String item, int blocksCount)
    {
        this.blockRowNumber = rowNumber;
        this.blockColumnNumber = columnNumber;
        this.blockSize = blockSize;
        this.singleBlockContent = item;
        this.createSingleBlock(blocksCount);
    }
    public String getSingleBlock() {
        return singleBlock;
    }
    public String getTopBorder() {
        return topBorder;
    }
    public String getBottomBorder() {
        return bottomBorder;
    }
    public String getLeftBorder() {
        return leftBorder;
    }
    public String getRightBorder() {
        return rightBorder;
    }
    public String getSingleBlockContent() {
        return singleBlockContent;
    }
    public int getBlockSize() {
        return blockSize;
    }
    public int getBlockRowNumber() {
        return blockRowNumber;
    }
    public int getBlockColumnNumber() {
        return blockColumnNumber;
    }
    public void setSingleBlock(String singleBlock) {
        this.singleBlock = singleBlock;
    }
    public void setTopBorder(String topBorder) {
        this.topBorder = topBorder;
    }
    public void setBottomBorder(String bottomBorder) {
        this.bottomBorder = bottomBorder;
    }
    public void setLeftBorder(String leftBorder) {
        this.leftBorder = leftBorder;
    }
    public void setRightBorder(String rightBorder) {
        this.rightBorder = rightBorder;
    }
    public void setSingleBlockContent(String singleBlockContent) {
        this.singleBlockContent = singleBlockContent;
    }
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
    public void setBlockRowNumber(int blockRowNumber) {
        this.blockRowNumber = blockRowNumber;
    }
    public void setBlockColumnNumber(int blockColumnNumber) {
        this.blockColumnNumber = blockColumnNumber;
    }
    public void createSingleBlock(int blocksCount)
    {
        int spacesCount;
        String spaces, strBlock;
        if (this.blockRowNumber == 0 && this.blockColumnNumber == 0)
        {
            spacesCount = this.blockSize - (this.singleBlockContent.length() + 2);
            spaces = " ".repeat(spacesCount / 2);
            this.setTopBorder("|" + "—".repeat(this.blockSize - 2) + "|");
            this.setBottomBorder("|" + "—".repeat(this.blockSize - 2) + "|");
            strBlock = "|" + spaces + this.singleBlockContent + spaces + "|";
            this.setSingleBlock(this.topBorder + strBlock + this.bottomBorder);
        }
        else if (this.blockRowNumber == 0)
        {

            spacesCount = this.blockSize - (this.singleBlockContent.length() + 1);
            spaces = " ".repeat(spacesCount / 2);
            this.setTopBorder("—".repeat(this.blockSize - 1) + "|");
            this.setBottomBorder("—".repeat(this.blockSize - 1) + "|");
            strBlock = spaces + this.singleBlockContent + spaces + "|";
            this.setSingleBlock(this.topBorder + strBlock + this.bottomBorder);
        }
        else if (this.blockColumnNumber == 0)
        {
            spacesCount = this.blockSize - (this.singleBlockContent.length() + 1);
            spaces = " ".repeat(spacesCount / 2);
            this.setBottomBorder("|" + "—".repeat(this.blockSize - 2) + "|");
            strBlock = "|" + spaces + this.singleBlockContent + spaces + "|";
            this.setSingleBlock(strBlock + this.bottomBorder);
        }
        else if (this.blockRowNumber == blocksCount - 1)
        {
            spacesCount = this.blockSize - (this.singleBlockContent.length() + 1);
            spaces = " ".repeat(spacesCount / 2);
            this.setBottomBorder("—".repeat(this.blockSize - 1) + "|");
            strBlock = spaces + this.singleBlockContent + spaces + "|";
        }
        else
        {
            spacesCount = this.blockSize - (this.singleBlockContent.length() + 1);
            spaces = " ".repeat(spacesCount / 2);
            this.setBottomBorder("—".repeat(this.blockSize - 1) + "|");
            strBlock = spaces + this.singleBlockContent + spaces + "|";
            this.setSingleBlock(strBlock + this.bottomBorder);
        }
    }
}
