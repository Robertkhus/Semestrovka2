module org.example.semestrovka2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.semestrovka2.game to javafx.fxml;
     // Если требуется экспортировать весь пакет
    exports org.example.semestrovka2.game;
    opens org.example.semestrovka2.controllers to javafx.fxml;

}