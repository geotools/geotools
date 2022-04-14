/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.logging;

import java.util.logging.Logger;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.CommonsLoggerFactory;
import org.geotools.util.logging.Logging;

public class Example4 {
    static final Logger LOGGER = defaultLogger();

    public static void main(String... args) {
        LOGGER.info("Example4 started - primo post!");
    }

    private static final Logger defaultLogger() {
        GeoTools.setLoggerFactory(CommonsLoggerFactory.getInstance());
        GeoTools.init();
        return Logging.getLogger(Example4.class);
    }
}
