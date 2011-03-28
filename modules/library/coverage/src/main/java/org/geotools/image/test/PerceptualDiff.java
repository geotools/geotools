package org.geotools.image.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    };

    static {
        try {
            String result = run(Arrays.asList("perceptualdiff"));
            AVAILABLE = result.contains("PerceptualDiff");
        } catch (Exception e) {

            AVAILABLE = false;
        }
    }

    /**
     * Compares two images (either png or tiffs)
     * 
     * @param image1
     *            A png/tiff file
     * @param image2
     *            A png/tiff file
     * @param threshold
     *            The number of pixels to be visually different in order to consider the test a
     *            failure, if negative the PerceptualDiff default value will be used
     * @return
     */
    public static Difference compareImages(File image1, File image2, int threshold) {
        if (!AVAILABLE) {
            LOGGER.severe("perceptualdiff is not available, can't compare " + image1
                    + " with image2");
            return new Difference(false, "Perceptual diff not available...");
        }

        try {
            // run it
            List<String> args = new ArrayList<String>();
            args.add("perceptualdiff");
            args.add(image1.getAbsolutePath());
            args.add(image2.getAbsolutePath());
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

    /**
     * Runs the specified command and returns the output as a string
     * 
     * @param cmd
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    static String run(List<String> cmd) throws IOException, InterruptedException {
        // run the process and grab the output for error reporting purposes
        ProcessBuilder builder = new ProcessBuilder(cmd);
        StringBuilder sb = new StringBuilder();
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (sb != null) {
                sb.append("\n");
                sb.append(line);
            }
        }
        p.waitFor();

        return sb.toString();
    }
}
