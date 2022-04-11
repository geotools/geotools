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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

class Welcome {

    public static final Logger LOGGER = initDefaultLogger();

    public static void main(String... args) {
        LOGGER.info("Welcome");
    }
    /**
     * Setup DEFAULT Logger (taking care to initialize GeoTools with preferred logging framework).
     *
     * @return Logger for this application, and the net.fun.example package.
     */
    private static Logger initDefaultLogger() {
        Throwable troubleSettingUpLogging = null;
        try {
            Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4J2LoggerFactory");
        } catch (Throwable trouble) {
            troubleSettingUpLogging = trouble;
        }
        Logger logger = Logging.getLogger(Welcome.class);
        logger.info("Default Logger:" + logger);
        if (troubleSettingUpLogging != null) {
            logger.log(
                    Level.WARNING,
                    "Unable to setup \"org.geotools.util.logging.Log4J2LoggerFactory\", is log4j2 available?",
                    troubleSettingUpLogging);
        }
        return logger;
    }
}
