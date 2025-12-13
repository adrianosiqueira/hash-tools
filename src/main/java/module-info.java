module hash.tools {
    exports hash_tools;
    exports hash_tools.domain.checksum;
    exports hash_tools.domain.checksum_source;
    exports hash_tools.domain.request;

    opens hash_tools;
    opens hash_tools.domain.checksum;
    opens hash_tools.domain.checksum_source;
    opens hash_tools.domain.request;
}
