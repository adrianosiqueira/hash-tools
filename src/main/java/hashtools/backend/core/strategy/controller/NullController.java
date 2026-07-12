package hashtools.backend.core.strategy.controller;

import hashtools.backend.core.interfaces.Controller;

import java.net.URL;
import java.util.ResourceBundle;

public class NullController implements Controller {

    @Override
    public void close() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
