package hashtools.backend.core.interfaces;

import javafx.fxml.Initializable;

public interface Controller extends AutoCloseable, Initializable {

    void close();
}
