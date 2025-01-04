module com.workshopsoftware {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires com.github.librepdf.openpdf;
    requires java.desktop;
    requires org.apache.commons.configuration2;
    requires com.h2database;

    opens com.simpleworkshopsoftware to javafx.fxml;
    opens com.simpleworkshopsoftware.entities to javafx.base;
    exports com.simpleworkshopsoftware;
    exports com.simpleworkshopsoftware.controller;
    opens com.simpleworkshopsoftware.controller to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.repo;
    exports com.simpleworkshopsoftware.entities;
    opens com.simpleworkshopsoftware.repo to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.dto;
    opens com.simpleworkshopsoftware.dto to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.validators;
    opens com.simpleworkshopsoftware.validators to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.factories;
    opens com.simpleworkshopsoftware.factories to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.controller.tableControllers;
    opens com.simpleworkshopsoftware.controller.tableControllers to javafx.base, javafx.fxml;
    opens com.simpleworkshopsoftware.utils to javafx.base, javafx.fxml;
    exports com.simpleworkshopsoftware.utils;
    exports com.simpleworkshopsoftware.mapper;
}