module hash.tools {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;

    exports hashtools;
    exports hashtools.core.checksum;
    exports hashtools.core.file;
    exports hashtools.core.source;
    exports hashtools.core.strategy.extraction;
    exports hashtools.core.strategy.identification;
    exports hashtools.core.strategy.messagedigest;
    exports hashtools.core.strategy.problem;
    exports hashtools.core.threadpool;
    exports hashtools.module.application.controller;
    exports hashtools.module.application.service;
    exports hashtools.module.checker.controller;
    exports hashtools.module.checker.domain;
    exports hashtools.module.checker.service;
    exports hashtools.module.comparator.domain;
    exports hashtools.view.util;
    exports hashtools.view.window;

    opens hashtools;
    opens hashtools.core.checksum;
    opens hashtools.core.file;
    opens hashtools.core.source;
    opens hashtools.core.strategy.extraction;
    opens hashtools.core.strategy.identification;
    opens hashtools.core.strategy.messagedigest;
    opens hashtools.core.strategy.problem;
    opens hashtools.core.threadpool;
    opens hashtools.module.application.controller;
    opens hashtools.module.application.service;
    opens hashtools.module.checker.controller;
    opens hashtools.module.checker.domain;
    opens hashtools.module.checker.service;
    opens hashtools.module.comparator.domain;
    opens hashtools.view.util;
    opens hashtools.view.window;
}
