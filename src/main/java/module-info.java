module com.example.confesstime {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens Program.Confession to javafx.fxml;
    exports Program.Confession;
    exports Program.Compare.Comparators;
    opens Program.Compare.Comparators to javafx.fxml;
    exports Program.Compare;
    opens Program.Compare to javafx.fxml;
    exports Program.Utility;
    opens Program.Utility to javafx.fxml;
    exports com;
    opens com to javafx.fxml;
}