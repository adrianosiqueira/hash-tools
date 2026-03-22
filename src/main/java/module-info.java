module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;

    exports hashtools;
    exports hashtools.controller;
    exports hashtools.core.engine;
    exports hashtools.core.model;
    exports hashtools.core.strategy.checksumextractor;
    exports hashtools.core.strategy.checksumidentifier;
    exports hashtools.core.strategy.messagedigest;
    exports hashtools.core.threadpool;
    exports hashtools.module.checking;
    exports hashtools.module.comparison;
    exports hashtools.module.generation;
    exports hashtools.service;
    exports hashtools.view;

    opens hashtools;
    opens hashtools.controller;
    opens hashtools.core.engine;
    opens hashtools.core.model;
    opens hashtools.core.strategy.checksumextractor;
    opens hashtools.core.strategy.checksumidentifier;
    opens hashtools.core.strategy.messagedigest;
    opens hashtools.core.threadpool;
    opens hashtools.module.checking;
    opens hashtools.module.comparison;
    opens hashtools.module.generation;
    opens hashtools.service;
    opens hashtools.view;
}
