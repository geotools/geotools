package org.geotools.gce.imagemosaic.remote;

import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.image.WorldImageReader;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.GeneralParameterValue;

public class RemoteImageReader  extends AbstractGridCoverage2DReader
    implements GridCoverage2DReader {
    
    private WorldImageReader delegate;
    
    public RemoteImageReader(WorldImageReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Format getFormat() {
        return new RemoteImageFormat();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {
        return delegate.read(parameters);
    }

}
