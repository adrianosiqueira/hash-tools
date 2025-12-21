module hash.tools {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports hash_tools;
    exports hash_tools._interface.abstraction;
    exports hash_tools._interface.checker_screen;
    exports hash_tools._interface.main_screen;
    exports hash_tools.domain.checksum;
    exports hash_tools.domain.checksum_extractor;
    exports hash_tools.domain.checksum_source;
    exports hash_tools.domain.request;
    exports hash_tools.domain.request_processor;
    exports hash_tools.domain.result;

    opens hash_tools;
    opens hash_tools._interface.abstraction;
    opens hash_tools._interface.checker_screen;
    opens hash_tools._interface.main_screen;
    opens hash_tools.domain.checksum;
    opens hash_tools.domain.checksum_extractor;
    opens hash_tools.domain.checksum_source;
    opens hash_tools.domain.request;
    opens hash_tools.domain.request_processor;
    opens hash_tools.domain.result;
}
