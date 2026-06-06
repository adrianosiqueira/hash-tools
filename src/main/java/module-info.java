module hash.tools {
    requires ch.qos.logback.classic;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.checksum;
    exports hashtools.core.formatter.header;
    exports hashtools.core.problem;
    exports hashtools.core.source.checksum;
    exports hashtools.core.source.input;
    exports hashtools.core.threadpool;
    exports hashtools.domain.checker.controller;
    exports hashtools.domain.checker.model;
    exports hashtools.domain.checker.service;
    exports hashtools.domain.main.controller;
    exports hashtools.view;
    exports hashtools.view.dialog;
    exports hashtools.view.javafx;

    opens hashtools;
    opens hashtools.core.checksum;
    opens hashtools.core.formatter.header;
    opens hashtools.core.problem;
    opens hashtools.core.source.checksum;
    opens hashtools.core.source.input;
    opens hashtools.core.threadpool;
    opens hashtools.domain.checker.controller;
    opens hashtools.domain.checker.model;
    opens hashtools.domain.checker.service;
    opens hashtools.domain.main.controller;
    opens hashtools.view;
    opens hashtools.view.dialog;
    opens hashtools.view.javafx;
}
