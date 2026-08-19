module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.controller;
    exports hashtools.domain.algorithm;
    exports hashtools.domain.checksum;
    exports hashtools.domain.file;
    exports hashtools.domain.result;
    exports hashtools.service;
    exports hashtools.strategy.checksumextraction;
    exports hashtools.strategy.checksumsource;
    exports hashtools.strategy.generatorupdate;
    exports hashtools.strategy.identification;
    exports hashtools.strategy.inputsource;
    exports hashtools.strategy.problemdetection;
    exports hashtools.strategy.threadfactory;
    exports hashtools.window;

    opens hashtools;
    opens hashtools.controller;
    opens hashtools.domain.algorithm;
    opens hashtools.domain.checksum;
    opens hashtools.domain.file;
    opens hashtools.domain.result;
    opens hashtools.service;
    opens hashtools.strategy.checksumextraction;
    opens hashtools.strategy.checksumsource;
    opens hashtools.strategy.generatorupdate;
    opens hashtools.strategy.identification;
    opens hashtools.strategy.inputsource;
    opens hashtools.strategy.problemdetection;
    opens hashtools.strategy.threadfactory;
    opens hashtools.window;
}
