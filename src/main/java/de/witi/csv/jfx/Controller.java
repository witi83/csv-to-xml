package de.witi.csv.jfx;

import de.witi.csv.CSV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class Controller {
    @FXML
    private TextField inputField;
    @FXML
    private TextField delimField;
    @FXML
    private TextField outputField;
    @FXML
    private Button inputButton;
    @FXML
    private Button outputButton;
    @FXML
    private Button exportButton;
    private Optional<File> fileOptional = Optional.empty();
    private Optional<File> directoryOptional = Optional.empty();

    public void input(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Importdatei auswählen");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        this.fileOptional = Optional.ofNullable(chooser.showOpenDialog(inputButton.getScene().getWindow()));
        this.fileOptional.ifPresent(file -> {
            exportButton.setDisable(!directoryOptional.isPresent());
            inputField.setText(file.getAbsolutePath());
        });
    }

    public void output(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Ausgabepfad auswählen");
        this.directoryOptional = Optional.ofNullable(chooser.showDialog(outputButton.getScene().getWindow()));
        this.directoryOptional.ifPresent(file -> {
            exportButton.setDisable(!fileOptional.isPresent());
            outputField.setText(file.getAbsolutePath());
        });
    }

    public void export(ActionEvent actionEvent) {
        Path output = directoryOptional.get().toPath().resolve("output.xml");
        Path input = fileOptional.get().toPath();
        String message;

        try {
            List<String> lines = Files.readAllLines(input);
            Files.write(output, CSV.toXML(lines, delimField.getText()).getBytes());
            message = "Datei erfolgreich erstellt";
        } catch (Exception e) {
            message = "Datei konnte nicht erstellt werden: " + e.getMessage();
        }

        Stage dialog = new Stage();
        dialog.setTitle("Nachricht");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(exportButton.getScene().getWindow());
        Button button = new Button("OK");
        button.setOnAction(event -> dialog.close());
        VBox vbox = new VBox(50, new Text(message), button);
        vbox.setAlignment(Pos.CENTER);
        dialog.setScene(new Scene(vbox));
        dialog.showAndWait();
    }
}
