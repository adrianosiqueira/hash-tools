module hash.tools {
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.model;
    exports hashtools.core.strategy.checksumsource;
    exports hashtools.core.strategy.formatter;
    exports hashtools.core.strategy.inputsource;
    exports hashtools.core.threadpool;
    exports hashtools.module.checking.controller;
    exports hashtools.module.checking.model;
    exports hashtools.module.checking.service;
    exports hashtools.module.main.controller;
    exports hashtools.view;
    exports hashtools.view.dialog;
    exports hashtools.view.javafx;

    opens hashtools;
    opens hashtools.core.model;
    opens hashtools.core.strategy.checksumsource;
    opens hashtools.core.strategy.formatter;
    opens hashtools.core.strategy.inputsource;
    opens hashtools.core.threadpool;
    opens hashtools.module.checking.controller;
    opens hashtools.module.checking.model;
    opens hashtools.module.checking.service;
    opens hashtools.module.main.controller;
    opens hashtools.view;
    opens hashtools.view.dialog;
    opens hashtools.view.javafx;
}
