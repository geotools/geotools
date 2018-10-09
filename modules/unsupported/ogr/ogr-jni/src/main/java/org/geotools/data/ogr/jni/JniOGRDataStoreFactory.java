package org.geotools.data.ogr.jni;

import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.OGRDataStoreFactory;
import org.geotools.util.logging.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;


public class JniOGRDataStoreFactory extends OGRDataStoreFactory {

    private static final Logger LOGGER = Logging.getLogger(JniOGRDataStoreFactory.class);

    @Override
    protected OGR createOGR() {
        return new JniOGR();
    }

    @Override
    protected boolean doIsAvailable() throws Throwable {
        try {
            // GDAL version >= 2.3.0
            System.loadLibrary("gdalalljni");
        } catch (UnsatisfiedLinkError e) {
            LOGGER.log(Level.FINE,  "Error initializing GDAL/OGR library from \"gdalalljni\". " +
                    "Falling back to \"gdaljni\"", e);
            System.loadLibrary("gdaljni");
        }
        return true;
    }
}
