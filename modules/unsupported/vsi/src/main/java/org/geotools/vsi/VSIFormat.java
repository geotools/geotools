/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vsi;

import it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi;
import java.io.IOException;
import java.util.logging.Logger;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.MismatchedDimensionException;

public final class VSIFormat extends BaseGDALGridFormat implements Format {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(VSIFormat.class);

    /** Creates an instance and sets the metadata. */
    public VSIFormat() {
        super(new VRTImageReaderSpi());

        LOGGER.fine("Creating a new VSIFormat.");

        setInfo();
    }

    @Override
    protected void setInfo() {
        setInfo(new InfoWrapper("VSI Virtual File System", "VSI"));
    }

    @Override
    public VSIReader getReader(Object source, Hints hints) {
        try {
            return new VSIReader(source, hints);
        } catch (MismatchedDimensionException | IOException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
}
