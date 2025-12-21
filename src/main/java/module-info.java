module hash.tools {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports hash_tools;
    exports hash_tools.domain.checksum;
    exports hash_tools.domain.checksum_extractor;
    exports hash_tools.domain.checksum_source;
    exports hash_tools.domain.request;
    exports hash_tools.domain.request_processor;
    exports hash_tools.domain.result;
    exports hash_tools.frontend.abstraction;
    exports hash_tools.frontend.checker_screen;
    exports hash_tools.frontend.dialog;
    exports hash_tools.frontend.main_screen;

    opens hash_tools;
    opens hash_tools.domain.checksum;
    opens hash_tools.domain.checksum_extractor;
    opens hash_tools.domain.checksum_source;
    opens hash_tools.domain.request;
    opens hash_tools.domain.request_processor;
    opens hash_tools.domain.result;
    opens hash_tools.frontend.abstraction;
    opens hash_tools.frontend.checker_screen;
    opens hash_tools.frontend.dialog;
    opens hash_tools.frontend.main_screen;
}
