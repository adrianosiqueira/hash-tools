module hash.tools {
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.model;
    exports hashtools.core.source.checksum;
    exports hashtools.core.source.input;
    exports hashtools.core.strategy.checksumextractor;
    exports hashtools.core.strategy.formatter;
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
    opens hashtools.core.source.checksum;
    opens hashtools.core.source.input;
    opens hashtools.core.strategy.checksumextractor;
    opens hashtools.core.strategy.formatter;
    opens hashtools.core.threadpool;
    opens hashtools.module.checking.controller;
    opens hashtools.module.checking.model;
    opens hashtools.module.checking.service;
    opens hashtools.module.main.controller;
    opens hashtools.view;
    opens hashtools.view.dialog;
    opens hashtools.view.javafx;
}
