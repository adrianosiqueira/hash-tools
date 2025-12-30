module hash.tools {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports hash_tools;
    exports hash_tools.backend.checksum.extractor;
    exports hash_tools.backend.checksum.source;
    exports hash_tools.backend.checksum;
    exports hash_tools.backend.request.processor;
    exports hash_tools.backend.request;
    exports hash_tools.backend.result;
    exports hash_tools.frontend.abstraction;
    exports hash_tools.frontend.dialog;
    exports hash_tools.frontend.javafx;
    exports hash_tools.frontend.screen.checker;
    exports hash_tools.frontend.screen.comparator;
    exports hash_tools.frontend.screen.generator;
    exports hash_tools.frontend.screen.start;
    exports hash_tools.frontend.window;

    opens hash_tools;
    opens hash_tools.backend.checksum.extractor;
    opens hash_tools.backend.checksum.source;
    opens hash_tools.backend.checksum;
    opens hash_tools.backend.request.processor;
    opens hash_tools.backend.request;
    opens hash_tools.backend.result;
    opens hash_tools.frontend.abstraction;
    opens hash_tools.frontend.dialog;
    opens hash_tools.frontend.javafx;
    opens hash_tools.frontend.screen.checker;
    opens hash_tools.frontend.screen.comparator;
    opens hash_tools.frontend.screen.generator;
    opens hash_tools.frontend.screen.start;
    opens hash_tools.frontend.window;
}
