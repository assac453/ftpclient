module com.example.ftpclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.apache.commons.net;

    opens com.example.ftpclient to javafx.fxml, javafx.base, javafx.controls;
    opens Controllers to javafx.fxml, javafx.base, javafx.controls;
    opens com.example.ftpclient.Global to javafx.fxml, javafx.base, javafx.controls;
    exports com.example.ftpclient;
    exports com.example.ftpclient.Global;
    exports Controllers;
}