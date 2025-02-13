package org.geotools.referencing.factory.epsg.hsql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;

/** Dumps the contents of the EPSG database out to a java property file for use by the wkt plugin. */
public class PropertyDumper {

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) throws Exception {
        String filename = "/tmp/wkt.properties";
        Properties diff = new Properties();
        if (args.length > 0) {
            filename = args[0];
        }

        File original;
        if (args.length > 1) {
            original = new File(args[1]);
        } else {
            original = new File("../epsg-wkt/src/main/resources/org/geotools/referencing/epsg/wkt/epsg.properties");
        }
        if (original.exists()) {
            try (InputStream in = new FileInputStream(original)) {
                diff.load(in);
            }
        }

        try (FileOutputStream out = new FileOutputStream(filename);
                Writer writer = new BufferedWriter(new OutputStreamWriter(out, "8859_1"))) {
            sortedDump(writer, diff);
        }
    }

    /**
     * Sorted version of the dump, for easier comparison of the differences
     *
     * @param writer
     * @param diff
     * @throws IOException
     */
    @SuppressWarnings("PMD.SystemPrintln")
    private static void sortedDump(Writer writer, Properties diff) throws IOException {
        writer.write("#Generated from EPSG database version " + ThreadedHsqlEpsgFactory.VERSION + "\n");
        writer.write("#" + new Date() + "\n");
        List<String> codes = new ArrayList<>(CRS.getSupportedCodes("EPSG"));
        Collections.sort(codes, (c1, c2) -> {
            try {
                Long n1 = Long.valueOf(c1);
                Long n2 = Long.valueOf(c2);
                return n1.compareTo(n2);
            } catch (NumberFormatException e) {
                return c1.compareTo(c2);
            }
        });
        for (String code : codes) {
            try {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + code, true);
                // use toString, it's more lenient that toWKT
                String wkt = crs.toString().replaceAll("[\\n\\r]+| {2,}", "");
                // make sure we can parse back what we generated
                CRS.parseWKT(wkt);
                writer.append(code).append("=").append(wkt).append("\n");
                diff.remove(code);

            } catch (Exception e) {
                // we cannot actually decode all codes, but let's list what we can't
                String desc = "";
                try {
                    desc = CRS.getAuthorityFactory(true)
                            .getDescriptionText("EPSG:" + code)
                            .toString(Locale.ENGLISH);
                } catch (Exception ex) {
                    // fine, it's just to have a nicer description of the error
                }
                System.out.println("#" + code + "(" + desc + ")" + " -> " + e.getMessage());
            }
        }
        writer.flush();
        if (!diff.isEmpty()) {
            writer.write("#Extra Definitions Supplied from Community \n");
            writer.write("#" + new Date() + "\n");
            TreeMap<String, String> sortedMap = diff.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue().toString(),
                            (oldValue, newValue) -> oldValue,
                            () -> new TreeMap<>(Comparator.comparingLong(Long::parseLong))));

            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
                writer.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("\n");
            }
        }
        writer.flush();
    }
}
