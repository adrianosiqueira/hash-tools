module HashTools {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires ASLib;
    requires java.logging;

    opens hashtools.gui.screen.about;
    opens hashtools.gui.screen.application;
    opens hashtools.gui.screen.manual;

    exports hashtools.gui.screen.about;
    exports hashtools.gui.screen.application;
    exports hashtools.gui.screen.manual;
    exports hashtools.main;
}
