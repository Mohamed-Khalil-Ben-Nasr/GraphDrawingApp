module edu.lawrence.graphdrawing {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.lawrence.graphdrawing to javafx.fxml;
    exports edu.lawrence.graphdrawing;
}
