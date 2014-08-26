package de.witi.csv;

import de.witi.csv.jfx.App;
import javafx.application.Application;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class Main {
    public static void main(String... args) throws Exception {
        if (args.length < 3) {
            Application.launch(App.class, args); // start GUI
            return;
        }

        Path output = Paths.get(args[0]);
        Path input = Paths.get(args[1]);
        String delim = args[2];
        List<String> lines = Files.readAllLines(input);
        Files.write(output, CSV.toXML(lines, delim).getBytes());
    }
}