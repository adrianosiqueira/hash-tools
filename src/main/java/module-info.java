module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.controller;
    exports hashtools.domain.checksum;
    exports hashtools.domain.commom;
    exports hashtools.domain.file;
    exports hashtools.domain.parameter;
    exports hashtools.domain.result;
    exports hashtools.service;
    exports hashtools.strategy.checksumextraction;
    exports hashtools.strategy.generatorupdate;
    exports hashtools.strategy.identification;
    exports hashtools.strategy.problemdetection;
    exports hashtools.strategy.threadfactory;
    exports hashtools.window;

    opens hashtools;
    opens hashtools.controller;
    opens hashtools.domain.checksum;
    opens hashtools.domain.commom;
    opens hashtools.domain.file;
    opens hashtools.domain.parameter;
    opens hashtools.domain.result;
    opens hashtools.service;
    opens hashtools.strategy.checksumextraction;
    opens hashtools.strategy.generatorupdate;
    opens hashtools.strategy.identification;
    opens hashtools.strategy.problemdetection;
    opens hashtools.strategy.threadfactory;
    opens hashtools.window;
}
