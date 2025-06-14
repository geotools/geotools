/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.remote;

import java.io.IOException;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.image.WorldImageReader;

public class RemoteImageReader extends AbstractGridCoverage2DReader implements GridCoverage2DReader {

    private WorldImageReader delegate;

    public RemoteImageReader(WorldImageReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Format getFormat() {
        return new RemoteImageFormat();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException, IOException {
        return delegate.read(parameters);
    }
}
