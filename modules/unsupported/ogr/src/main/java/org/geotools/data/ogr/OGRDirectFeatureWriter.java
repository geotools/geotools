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

import org.gdal.gdal.gdal;
import org.gdal.ogr.Layer;
import org.geotools.data.FeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * OGR feature writer leveraging OGR capabilities to rewrite a file using random access and in place
 * deletes
 * 
 * @author aaime
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/OGRDirectFeatureWriter.java $
 */
public class OGRDirectFeatureWriter implements FeatureWriter {

    private OGRFeatureReader reader;

    private SimpleFeatureType featureType;

    private SimpleFeature original;

    private SimpleFeature live;

    private Layer layer;

    private FeatureMapper mapper;

    private boolean deletedFeatures;

    /**
     * Creates a new direct OGR feature writer, with the specified OGRFeatureReader and destination
     * layer (it may be a different layer from the one the reader is working against)
     * 
     * @param reader
     * @param featureType
     * @param layer
     */
    public OGRDirectFeatureWriter(OGRFeatureReader reader) {
        this.reader = reader;
        this.featureType = reader.getFeatureType();
        this.layer = reader.layer;
        this.mapper = new FeatureMapper(featureType, new GeometryFactory());
        this.deletedFeatures = false;
    }

    public void close() throws IOException {
        if (reader != null) {
            original = null;
            live = null;
            if ("ESRI Shapefile".equals(reader.ds.GetDriver().getName()) && deletedFeatures)
                reader.ds.ExecuteSQL("REPACK " + reader.layer.GetName(), null, null);
            reader.layer.SyncToDisk();
            reader.close();
        }
    }

    public FeatureType getFeatureType() {
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
        int ogrId = mapper.convertGTFID(original);
        if (layer.DeleteFeature(ogrId) != 0) {
            throw new IOException(gdal.GetLastErrorMsg());
        }
        deletedFeatures = true;
    }

    public void write() throws IOException {
        if (live == null)
            throw new IOException("No current feature to write");

        // this will return true only in update mode, otherwise original is null
        boolean changed = live.equals(original);
        if (!changed && original != null && layer == reader.layer) {
            // nothing to do, just skip
        } else if (changed && original != null && layer == reader.layer) {
            // not equals, we're updating an existing one
            layer.SetFeature(mapper.convertGTFeature(layer.GetLayerDefn(), live));
        } else {
            org.gdal.ogr.Feature ogrFeature = mapper.convertGTFeature(layer.GetLayerDefn(),
                    original != null ? original : live);
            layer.CreateFeature(ogrFeature);
            ((FeatureIdImpl) live.getIdentifier()).setID(mapper.convertOGRFID(featureType, ogrFeature));
            ogrFeature.delete();
        }

        // reset state
        live = null;
        original = null;
    }

}
