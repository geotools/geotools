package org.geotools.ysld;

import java.io.InputStream;

/**
 * Test class for loading SLD data.
 */
public class YsldTests {
    /**
     * Loads a test SLD.
     *
     * @param dir One of point, line, polygon, raster.
     * @param file The filename.
     *
     * @return The input stream for the SLD file.
     */
    public static InputStream sld(String dir, String file) {
        return YsldTests.class.getResourceAsStream("sld/"+dir+"/"+file);
    }
    
    public static InputStream ysld(String dir, String file) {
        return YsldTests.class.getResourceAsStream(""+dir+"/"+file);
    }
}
