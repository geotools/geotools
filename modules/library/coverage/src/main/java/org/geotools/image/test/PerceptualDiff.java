/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Wrapper around the PerceptualDiff command line utility http://pdiff.sourceforge.net/
 *
 * @author Andrea Aime - GeoSolutions
 */
class PerceptualDiff {

    static final Logger LOGGER = Logging.getLogger(PerceptualDiff.class);

    static boolean AVAILABLE;

    static class Difference {
        boolean imagesDifferent;
        String output;

        public Difference(boolean different, String output) {
            this.imagesDifferent = different;
            this.output = output;
        }
    }

    static {
        if (Boolean.getBoolean("org.geotools.image.test.enabled")) {
            try {
                String result = run(Arrays.asList("perceptualdiff"));
                AVAILABLE = result.contains("PerceptualDiff");
            } catch (Exception e) {
                AVAILABLE = false;
            }
        } else {
            AVAILABLE = false;
        }
    }

    /**
     * Compares two images (either png or tiffs)
     *
     * @param image1 A png/tiff file
     * @param image2 A png/tiff file
     * @param threshold The number of pixels to be visually different in order to consider the test a failure, if
     *     negative the PerceptualDiff default value will be used
     */
    public static Difference compareImages(File image1, File image2, int threshold) {
        if (!AVAILABLE) {
            LOGGER.severe("perceptualdiff is not available, can't compare " + image1 + " with image2");
            return new Difference(false, "Perceptual diff not available...");
        }

        try {
            // run it
            List<String> args = new ArrayList<>();
            args.add("perceptualdiff");
            args.add(image1.getAbsolutePath());
            args.add(image2.getAbsolutePath());
            args.add("-verbose");
            args.add("-fov");
            args.add("89.9");
            if (threshold > 0) {
                args.add("-threshold");
                args.add(String.valueOf(threshold));
            }
            String result = run(args);

            // check the results
            if (result.contains("PASS")) {
                return new Difference(false, result);
            } else {
                return new Difference(true, result);
            }
        } catch (Exception e) {
            throw new RuntimeException("PerceptualDiff call failed!!", e);
        }
    }

    /** Runs the specified command and returns the output as a string */
    static String run(List<String> cmd) throws IOException, InterruptedException {
        // run the process and grab the output for error reporting purposes
        ProcessBuilder builder = new ProcessBuilder(cmd);
        StringBuilder sb = new StringBuilder();
        builder.redirectErrorStream(true);
        Process p = builder.start();
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (sb != null) {
                    sb.append("\n");
                    sb.append(line);
                }
            }
            p.waitFor();
        }

        return sb.toString();
    }
}
