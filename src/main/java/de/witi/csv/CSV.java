package de.witi.csv;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CSV {
    private static final String TEMPLATE = new Scanner(Main.class.getResourceAsStream("/template.xml")).useDelimiter("\\A").next();
    private static final String POS = new Scanner(Main.class.getResourceAsStream("/pos.xml")).useDelimiter("\\A").next();
    private final List<String> header;

    public CSV(String header) {
        this.header = new ArrayList<>(Arrays.asList(header.split(";")));
    }

    public String toXML(List<String> inputLines) {
        String output = inputLines.stream().skip(1)
                .map(line -> Arrays.asList(line.split(";")))
                .collect(Collectors.groupingBy(w -> w.get(9)))
                .entrySet().stream().flatMap(this::convertEntry).collect(Collectors.joining())
                .replaceAll("%.*%", "").replaceAll("&", "&amp;");

        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                + System.lineSeparator()
                + "<tBestellungen>"
                + System.lineSeparator()
                + output
                + System.lineSeparator()
                + "</tBestellungen>";
    }

    private Stream<String> convertEntry(Map.Entry<String, List<List<String>>> entries) {
        List<List<String>> values = entries.getValue();
        String line = convertLine(values.get(0));
        String positions = values.stream().map(this::convertPos).collect(Collectors.joining());
        String delivery = convertDelivery(values.get(0).get(10));
        String allPositions = positions + delivery;
        return Collections.singletonList(line.replace("%twarenkorbpos%", allPositions)).stream();
    }

    private String convertLine(List<String> columns) {
        AtomicReference<String> template = new AtomicReference<>(TEMPLATE);
        AtomicInteger index = new AtomicInteger();
        columns.forEach(column -> template.updateAndGet(t -> t.replaceFirst("%" + header.get(index.getAndIncrement()) + "%", column)));
        return template.get();
    }

    private String convertPos(List<String> columns) {
        AtomicReference<String> pos = new AtomicReference<>(POS);
        AtomicInteger index = new AtomicInteger(22);
        columns.stream().skip(22).limit(11)
                .forEach(column -> pos.updateAndGet(t -> t.replaceFirst("%" + header.get(index.getAndIncrement()) + "%", column)));
        return pos.get();
    }

    private String convertDelivery(String delivery) {
        String t = POS;
        t = t.replaceFirst("%cName%", delivery);

        if ("Flat Rate".equals(delivery)) {
            t = t.replaceFirst("%fPreisEinzelNetto%", "4.11764");
            t = t.replaceFirst("%fPreis%", "4.90");
            t = t.replaceFirst("%fMwSt%", "19.00");
        } else {
            t = t.replaceFirst("%fPreisEinzelNetto%", "9.40");
            t = t.replaceFirst("%fPreis%", "9.40");
            t = t.replaceFirst("%fMwSt%", "0.00");
        }

        t = t.replaceFirst("%fAnzahl%", "1.0000");
        t = t.replaceFirst("%cPosTyp%", "versandkosten");
        return t.replaceFirst("%fRabatt%", "0.00");
    }
}