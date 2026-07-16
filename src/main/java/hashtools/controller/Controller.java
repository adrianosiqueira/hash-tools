package hashtools.controller;

import javafx.fxml.Initializable;

public interface Controller extends AutoCloseable, Initializable {

    void close();
}
