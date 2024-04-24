module project.persistence {
    exports persistence;

    requires transitive com.google.gson;
    requires transitive project.types;

    opens persistence to com.google.gson;
}
