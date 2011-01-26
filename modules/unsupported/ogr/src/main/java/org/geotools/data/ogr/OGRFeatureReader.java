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
import java.util.NoSuchElementException;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * An OGR feature reader, reads data from the provided layer.<br>
 * It assumes eventual filters have already been set on it, and will extract only the
 * 
 * @author aaime
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/OGRFeatureReader.java $
 */
public class OGRFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    DataSource ds;

    Layer layer;

    SimpleFeatureType schema;

    org.gdal.ogr.Feature curr;

    private FeatureMapper mapper;

    boolean layerCompleted;

    public OGRFeatureReader(DataSource ds, Layer layer, SimpleFeatureType schema) {
        this.ds = ds;
        this.layer = layer;
        this.schema = schema;
        this.layer.ResetReading();
        this.layerCompleted = false;
        // TODO: use the most appropriate Geometry Factory once the SPI system
        // allows us to provide such an hint
        this.mapper = new FeatureMapper(schema, new GeometryFactory());
    }

    public void close() throws IOException {
        if (curr != null) {
            curr.delete();
            curr = null;
        }
        if (layer != null) {
            layer.delete();
            layer = null;
        }
        if (ds != null) {
            ds.delete();
            ds = null;
        }
        schema = null;
    }

    protected void finalize() throws Throwable {
        close();
    }

    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    public boolean hasNext() throws IOException {
        // ugly, but necessary to close the reader when getting to the end, because
        // it would break feature appending otherwise (the reader is used in feature
        // writing too)
        if (layerCompleted)
            return false;

        if (curr != null)
            return true;
        boolean hasNext = (curr = layer.GetNextFeature()) != null;
        if (!hasNext)
            layerCompleted = true;
        return hasNext;
    }

    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext())
            throw new NoSuchElementException("There are no more Features to be read");

        SimpleFeature f = mapper.convertOgrFeature(curr);

        // .. nullify curr, so that we can move to the next one
        curr.delete();
        curr = null;

        return f;
    }

}
