/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.geojson;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** Not a test class, but a load test that can be used to profile the module */
public class GeoJSONReaderRunner {

    static final Logger LOGGER = Logging.getLogger(GeoJSONReaderRunner.class);

    File source;
    int threads = Runtime.getRuntime().availableProcessors() * 2;
    int count = threads * 10;

    public GeoJSONReaderRunner(File source) {
        this.source = source;
        if (!source.exists()) throw new RuntimeException("File does not exist");
    }

    public void execute() throws Exception {
        List<Callable<Void>> runners = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            runners.add(new ParsingCallable());
        }

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            executor.invokeAll(runners);
        } finally {
            executor.shutdownNow();
        }
    }

    private class ParsingCallable implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            try (FileInputStream fos = new FileInputStream(source);
                    GeoJSONReader reader = new GeoJSONReader(fos)) {
                reader.getFeatures();

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to read GeoJSON", e);
            }
            return null;
        }
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println(GeoJSONReaderRunner.class.getSimpleName() + " <file>");
            System.exit(-1);
        }

        File source = new File(args[0]);
        long start = System.currentTimeMillis();
        GeoJSONReaderRunner runner = new GeoJSONReaderRunner(source);
        runner.execute();
        long end = System.currentTimeMillis();
        System.out.println("Time to execute: " + (end - start) / 1000d);
    }
}
