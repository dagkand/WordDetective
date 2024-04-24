module project.core {
    exports core;

    requires transitive com.google.gson;
    requires transitive project.persistence;
    requires transitive project.types;

    opens core to com.google.gson;
}
