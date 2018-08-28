/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.io.footprint.FootprintLoader;
import org.geotools.coverage.grid.io.footprint.FootprintLoaderSpi;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.URLs;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

public class ShapefileLoaderSPI implements FootprintLoaderSpi {

    @Override
    public FootprintLoader createLoader() {
        return new ShapefileLoader();
    }

    /**
     * Loads footprints from a sidecar shepefile with a single record, will complain if more than
     * one is found
     */
    public class ShapefileLoader implements FootprintLoader {

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".shp");
            if (file.exists()) {
                ShapefileDataStore ds = new ShapefileDataStore(URLs.fileToUrl(file));
                SimpleFeatureIterator fi = null;
                try {
                    fi = ds.getFeatureSource().getFeatures().features();
                    if (!fi.hasNext()) {
                        return null;
                    } else {
                        SimpleFeature sf = fi.next();
                        Geometry result = (Geometry) sf.getDefaultGeometry();
                        if (fi.hasNext()) {
                            throw new IOException(
                                    "Found more than one footprint record in the shapefile "
                                            + file.getCanonicalPath());
                        }
                        return result;
                    }
                } finally {
                    if (fi != null) {
                        fi.close();
                    }
                    ds.dispose();
                }
            }

            return null;
        }
    }
}
