/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.s3.geotiff;

import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.s3.S3ImageInputStreamImpl;
import org.geotools.s3.S3ImageInputStreamImplSpi;
import org.geotools.util.factory.Hints;

/**
 * Very simple wrapper around GeoTIff reader in order to support S3 geotiff. The goal is to have
 * this go away eventually and to be able to support S3 with just the original GeoTiff reader
 */
public class S3GeoTiffReader extends GeoTiffReader {
    public S3GeoTiffReader(Object input) throws DataSourceException {
        super(input);

        /*
         * Because S3 geotiff is always instantiated with an S3ImageInputStreamImpl the SPI never
         * gets set (because the reader doesn't need to look for it). We set it hear so that
         * subsequent calls that rely on it pass.
         */
        this.inStreamSPI = new S3ImageInputStreamImplSpi();
        // Needs close me, since we're using a stream and it should not be reused.
        closeMe = true;
    }

    public S3GeoTiffReader(Object input, Hints uHints) throws DataSourceException {
        super(input, uHints);
        closeMe = true;
        this.inStreamSPI = new S3ImageInputStreamImplSpi();
        if (input instanceof S3ImageInputStreamImpl) {
            String fileName = ((S3ImageInputStreamImpl) input).getFileName();
            final int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex != fileName.length()) {
                this.coverageName = fileName.substring(0, dotIndex);
            }
        }
    }
}
