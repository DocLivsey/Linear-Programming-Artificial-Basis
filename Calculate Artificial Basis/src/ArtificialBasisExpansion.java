import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

public class ArtificialBasisExpansion extends CanonicalSimplexMatrix {
    protected Matrix artificialBasisMatrix;
    protected Vector<Double> artificialDeltaGrade;
    ArtificialBasisExpansion(String pathToValuesMatrix, String pathToObjFunction, String pathToConstraints) throws FileNotFoundException {
        super(pathToValuesMatrix, pathToObjFunction, pathToConstraints);
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        super.objectiveFunction.addAll(this.basisValue);
        this.artificialDeltaGrade = new Vector<>();
    }
    ArtificialBasisExpansion(Matrix simplexMatrix, ArtificialBasisExpansion prevSimplexMatrix, Vector<Double> constraint, Vector<String> basis, Vector<Double> basisValue)
    {
        super(simplexMatrix, prevSimplexMatrix, constraint, basis, basisValue);
        this.setArtificialBasisMatrix();
        this.setArtificialBasis();
        this.setArtificialBasisValue();
        this.artificialDeltaGrade = new Vector<>();
    }
    ArtificialBasisExpansion(ArtificialBasisExpansion prevSimplexMatrix)
    {
        super(prevSimplexMatrix);
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
    public void setArtificialBasisMatrix() {
        this.artificialBasisMatrix = Matrix.setIdentityMatrix(super.constraints.size());
    }
    public void setArtificialDeltaGrade(Vector<Double> artificialDeltaGrade) {
        this.artificialDeltaGrade = artificialDeltaGrade;
    }
    public void setArtificialBasis()
    {
        for (int i = 0; i < super.constraints.size(); i++)
            super.basis.add(i, "X" + (i + super.getVariables().size() + 1));
    }
    public void setArtificialBasisValue()
    {
        for (int i = 0; i < super.basis.size(); i++)
            super.basisValue.add(i, -1E10);
    }
    public ArrayList<Vector<String>> convertToCorrectStringFormat()
    {
        int width = super.constraints.size();
        ArrayList<Vector<String>> table = new ArrayList<>();
        for (int i = 0; i < width; i++)
        {
            Vector<String> row = new Vector<>();
            row.add(super.basis.get(i));
            DecimalFormat formattedOut = new DecimalFormat("#.####");
            String result = formattedOut.format(super.basisValue.get(i));
            row.add(result);
            row.addAll(this.simplexMatrix.rowToStringVector(i));
            row.addAll(this.artificialBasisMatrix.rowToStringVector(i));
            table.add(row);
        }
        return table;
    }
}
