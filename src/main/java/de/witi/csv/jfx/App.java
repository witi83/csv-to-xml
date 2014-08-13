package de.witi.csv.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/GUI.fxml"));
        Parent root = loader.load();
        stage.setTitle("CSV2XML");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
