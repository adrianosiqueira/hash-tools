module hash.tools {
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.engine;
    exports hashtools.core.model;
    exports hashtools.core.strategy.checksumextractor;
    exports hashtools.core.strategy.checksumidentifier;
    exports hashtools.core.strategy.formatter;
    exports hashtools.core.strategy.messagedigest;
    exports hashtools.core.threadpool;
    exports hashtools.module.checking.api;
    exports hashtools.module.checking.controller;
    exports hashtools.module.checking.facade;
    exports hashtools.module.checking.model;
    exports hashtools.module.checking.service;
    exports hashtools.module.comparison;
    exports hashtools.module.generation;
    exports hashtools.module.main.controller;
    exports hashtools.view;
    exports hashtools.view.dialog;

    opens hashtools;
    opens hashtools.core.engine;
    opens hashtools.core.model;
    opens hashtools.core.strategy.checksumextractor;
    opens hashtools.core.strategy.checksumidentifier;
    opens hashtools.core.strategy.formatter;
    opens hashtools.core.strategy.messagedigest;
    opens hashtools.core.threadpool;
    opens hashtools.module.checking.api;
    opens hashtools.module.checking.controller;
    opens hashtools.module.checking.facade;
    opens hashtools.module.checking.model;
    opens hashtools.module.checking.service;
    opens hashtools.module.comparison;
    opens hashtools.module.generation;
    opens hashtools.module.main.controller;
    opens hashtools.view;
    opens hashtools.view.dialog;
}
