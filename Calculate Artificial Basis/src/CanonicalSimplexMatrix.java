import java.util.*;
public class CanonicalSimplexMatrix {
    protected Matrix simplexMatrix;
    CanonicalSimplexMatrix()
    { this.simplexMatrix = new Matrix(); }
    public Matrix getSimplexMatrix()
    { return simplexMatrix; }
    public void setSimplexMatrix(Matrix simplexMatrix)
    { this.simplexMatrix = simplexMatrix; }
}
