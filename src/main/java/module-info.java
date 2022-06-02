module com.example.confesstime {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;

    opens Program.Confession to javafx.fxml;
    exports Program.Confession;
    exports Program.Compare.Comparators;
    opens Program.Compare.Comparators to javafx.fxml;
    exports Program.Compare;
    opens Program.Compare to javafx.fxml;
    exports Program.Utility;
    opens Program.Utility to javafx.fxml;
    exports GUI;
    opens GUI to javafx.fxml;
    exports Program.AdminUtility;
    opens Program.AdminUtility to javafx.fxml;
    exports Program.Sandbox;
    opens Program.Sandbox to javafx.fxml;
}