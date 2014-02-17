package org.geotools.geopkg.mosaic;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * 
 * @author Niels Charlier
 *
 */
public class GeoPackageFormatFactorySpi implements GridFormatFactorySpi {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public AbstractGridFormat createFormat() {
        return new GeoPackageFormat();
    }

}
