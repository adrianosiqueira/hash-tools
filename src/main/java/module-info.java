module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.checksum;
    exports hashtools.core.strategy.identification;

    opens hashtools;
    opens hashtools.core.checksum;
    opens hashtools.core.strategy.identification;
}
