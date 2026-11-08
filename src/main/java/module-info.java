module hash.tools {
    requires org.slf4j;



    exports hashtools.checker.controller;
    opens hashtools.checker.controller;

    exports hashtools.checker.domain;
    opens hashtools.checker.domain;

    exports hashtools.checker.service;
    opens hashtools.checker.service;

    exports hashtools.checker.strategy;
    opens hashtools.checker.strategy;



    exports hashtools.comparator.controller;
    opens hashtools.comparator.controller;

    exports hashtools.comparator.domain;
    opens hashtools.comparator.domain;

    exports hashtools.comparator.service;
    opens hashtools.comparator.service;



    exports hashtools.core.domain;
    opens hashtools.core.domain;

    exports hashtools.core.strategy;
    opens hashtools.core.strategy;



    exports hashtools.generator.controller;
    opens hashtools.generator.controller;

    exports hashtools.generator.domain;
    opens hashtools.generator.domain;

    exports hashtools.generator.service;
    opens hashtools.generator.service;
}
