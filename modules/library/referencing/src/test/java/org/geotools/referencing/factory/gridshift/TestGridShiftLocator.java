package org.geotools.referencing.factory.gridshift;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.logging.Logging;
import org.opengis.metadata.citation.Citation;

/** Test helper that can load compressed grids */
public class TestGridShiftLocator extends AbstractFactory implements GridShiftLocator {

    static final Logger LOGGER = Logging.getLogger(TestGridShiftLocator.class);

    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    @Override
    public URL locateGrid(String grid) {

        try {
            URL compressed = getClass().getResource(grid + ".gz");
            if (compressed != null) {
                File out = new File("./target/" + grid);
                try (GZIPInputStream is = new GZIPInputStream(compressed.openStream());
                        FileOutputStream fos = new FileOutputStream(out)) {
                    byte[] buf = new byte[1024];
                    int read;
                    while ((read = is.read(buf)) > 0) {
                        fos.write(buf, 0, read);
                    }
                }
                return out.toURI().toURL();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to unpack the grid", e);
        }

        return null;
    }
}
