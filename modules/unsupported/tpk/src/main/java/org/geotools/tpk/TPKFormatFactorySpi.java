package org.geotools.tpk;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

public class TPKFormatFactorySpi implements GridFormatFactorySpi {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public AbstractGridFormat createFormat() {
        return new TPKFormat();
    }
}
