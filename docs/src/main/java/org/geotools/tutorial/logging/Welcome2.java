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
import org.geotools.util.logging.Logging;

class Welcome2 {
    static {
        Logging.GEOTOOLS.setLoggerFactory(org.geotools.util.logging.CommonsLoggerFactory.getInstance());
    }

    public static final Logger LOGGER = Logging.getLogger(Welcome2.class);

    public static void main(String... args) {
        LOGGER.info("Welcome");
    }
}
