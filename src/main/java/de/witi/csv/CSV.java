package de.witi.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CSV {
    public static String toXML(List<String> inputLines, String delim) {
        List<String> header = new ArrayList<>(Arrays.asList(inputLines.get(0).split(delim)));
        String output = "<lines>" + inputLines.stream().skip(1)
                .map(line -> {
                    List<String> cells = Arrays.asList(line.split(delim));
                    return "<line>" + IntStream.range(0, cells.size())
                            .mapToObj(i -> "<" + header.get(i) + ">" + cells.get(i) + "</" + header.get(i) + ">")
                            .collect(Collectors.joining())
                            + "</line>";
                })
                .collect(Collectors.joining(System.lineSeparator()))
                .replaceAll("&", "&amp;")
                + "</lines>";
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + System.lineSeparator()
                + output
                + System.lineSeparator();
    }
}