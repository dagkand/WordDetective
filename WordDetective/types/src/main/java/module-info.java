module project.types {
    exports types;

    requires com.google.gson;

    opens types to com.google.gson;
}
