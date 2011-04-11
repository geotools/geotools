package org.geotools.renderer.markwkt;

import org.geotools.renderer.style.MarkFactory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.logging.Logger;

import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.WKTReader2;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;
import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory to produce marks based on WKT representation of symbols
 * 
 * @author Luca Morandini lmorandini@ieee.org
 * @version $Id$
 * 
 *          TODO: test WKTReader2 with CIRCULARSTRING, COMPOUNDCURVE and CURVEPOLYGON
 */
public class WKTMarkFactory implements MarkFactory {

    private static final String WKT_PREFIX = "wkt://";

    private static final String WKTLIB_PREFIX = "wktlib://";

    private static final String WKT_SEPARATOR = "#";

    /** The logger for the rendering module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.rendering");

    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature)
            throws Exception {

        // cannot handle a null url
        if (symbolUrl == null) {
            return null;
        }

        // If it does not start with either prefix, it cannot create symbols
        String wellKnown = symbolUrl.evaluate(feature, String.class);
        if (!wellKnown.startsWith(WKT_PREFIX) && !wellKnown.startsWith(WKTLIB_PREFIX)) {
            return null;
        }

        // See if it is a simple WKT, so that i can extract the WKT symbols
        // directly
        String wkt = null;
        if (wellKnown.startsWith(WKT_PREFIX)) {
            wkt = wellKnown.substring(WKT_PREFIX.length());
        }

        // See if it is a WKT library reference
        if (wellKnown.startsWith(WKTLIB_PREFIX)) {
            String[] urlComponents = wellKnown.substring(WKTLIB_PREFIX.length()).split(
                    WKT_SEPARATOR);
            wkt = this.loadWKT(urlComponents[0], urlComponents[1]);
        }

        if (wkt == null) {
            LOGGER.info("This is not recognised a WKT symbol: " + wellKnown);
        }

        // Creates and return the symbol
        WKTReader2 reader = new WKTReader2();
        LiteShape shape = new LiteShape(reader.read(wkt), null, false);
        LOGGER.info("Created symbol from WKT " + wkt);
        return shape;
    }

    /**
     * Loads a WKT from a given properties file (the proeprties file is loaded
     * via the classloader, hence it should be on the classpath)
     * 
     * @param propName
     *            properties file path
     * @param wktName
     *            property name that contains the WKT
     * 
     */
    protected String loadWKT(String propName, String wktName) throws IOException {

        BufferedReader in = null;

        in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader()
                .getResourceAsStream(propName)));
        final Properties properties = new Properties();

        properties.load(in);
        String wkt = properties.getProperty(wktName);
        in.close();
        LOGGER.info("Retrieved WKT " + wktName + " from " + propName);
        return wkt;
    }
}