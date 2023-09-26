module com.heshanthenura.fileshredder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.heshanthenura.fileshredder to javafx.fxml;
    exports com.heshanthenura.fileshredder;
    exports com.heshanthenura.fileshredder.Controllers;
    opens com.heshanthenura.fileshredder.Controllers to javafx.fxml;
}