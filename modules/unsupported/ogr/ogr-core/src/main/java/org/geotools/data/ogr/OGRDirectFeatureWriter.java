/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * OGR feature writer leveraging OGR capabilities to rewrite a file using random access and in place
 * deletes
 *
 * @author Andrea Aime - GeoSolutions
 */
class OGRDirectFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    private FeatureReader<SimpleFeatureType, SimpleFeature> reader;

    private SimpleFeatureType featureType;

    private SimpleFeature original;

    private SimpleFeature live;

    private Object layer;

    private OGRDataSource dataSource;

    private FeatureMapper mapper;

    private boolean deletedFeatures;

    private Object layerDefinition;

    private OGR ogr;

    /** Creates a new direct OGR feature writer */
    public OGRDirectFeatureWriter(
            OGRDataSource dataSource,
            Object layer,
            FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            SimpleFeatureType originalSchema,
            GeometryFactory gf,
            OGR ogr) {
        this.reader = reader;
        this.featureType = reader.getFeatureType();
        this.dataSource = dataSource;
        this.layer = layer;
        this.layerDefinition = ogr.LayerGetLayerDefn(layer);
        this.mapper = new FeatureMapper(featureType, layer, gf, ogr);
        this.deletedFeatures = false;
        this.ogr = ogr;
    }

    public void close() throws IOException {
        if (reader != null) {
            try {
                original = null;
                live = null;
                Object driver = dataSource.getDriver();
                String driverName = ogr.DriverGetName(driver);

                if ("ESRI Shapefile".equals(driverName) && deletedFeatures) {
                    String layerName = ogr.LayerGetName(layer);
                    dataSource.executeSQL("REPACK " + layerName, null);
                }
                ogr.LayerSyncToDisk(layer);
            } finally {
                reader.close();
            }
        }
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public SimpleFeature next() throws IOException {
        if (live != null) {
            write();
        }

        if (reader.hasNext()) {
            original = reader.next();
            live = SimpleFeatureBuilder.copy(original);
        } else {
            original = null;
            live = SimpleFeatureBuilder.template(featureType, null);
        }

        return live;
    }

    public void remove() throws IOException {
        long ogrId = mapper.convertGTFID(original);
        if (!ogr.LayerDeleteFeature(layer, ogrId)) {
            throw new IOException(ogr.GetLastErrorMsg());
        }
        deletedFeatures = true;
    }

    public void write() throws IOException {
        if (live == null) throw new IOException("No current feature to write");

        // this will return true only in update mode, otherwise original is null
        boolean changed = !live.equals(original);
        if (changed || original == null) {
            if (original != null) {
                // not equals, we're updating an existing one
                Object ogrFeature = mapper.convertGTFeature(layerDefinition, live);
                ogr.CheckError(ogr.LayerSetFeature(layer, ogrFeature));
            } else {
                Object ogrFeature = mapper.convertGTFeature(layerDefinition, live);

                ogr.CheckError(ogr.LayerCreateFeature(layer, ogrFeature));
                String geotoolsId = mapper.convertOGRFID(featureType, ogrFeature);
                ((FeatureIdImpl) live.getIdentifier()).setID(geotoolsId);
                ogr.FeatureDestroy(ogrFeature);
            }
        }

        // reset state
        live = null;
        original = null;
    }
}
