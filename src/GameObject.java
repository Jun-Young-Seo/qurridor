import java.io.Serializable;

public interface GameObject extends Serializable {
    int getRow();
    int getCol();
    String getType();
}
