module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.controller;
    exports hashtools.domain.algorithm;
    exports hashtools.domain.checksum;
    exports hashtools.domain.container;
    exports hashtools.domain.context;
    exports hashtools.domain.file;
    exports hashtools.domain.result;
    exports hashtools.service;
    exports hashtools.strategy.algorithmsource;
    exports hashtools.strategy.checksumsource;
    exports hashtools.strategy.inputsource;
    exports hashtools.strategy.problemdetection;
    exports hashtools.strategy.thread;
    exports hashtools.window;

    opens hashtools;
    opens hashtools.controller;
    opens hashtools.domain.algorithm;
    opens hashtools.domain.checksum;
    opens hashtools.domain.container;
    opens hashtools.domain.context;
    opens hashtools.domain.file;
    opens hashtools.domain.result;
    opens hashtools.service;
    opens hashtools.strategy.algorithmsource;
    opens hashtools.strategy.checksumsource;
    opens hashtools.strategy.inputsource;
    opens hashtools.strategy.problemdetection;
    opens hashtools.strategy.thread;
    opens hashtools.window;
}
