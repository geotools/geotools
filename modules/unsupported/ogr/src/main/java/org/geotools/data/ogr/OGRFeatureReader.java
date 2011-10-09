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

import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.bridj.Pointer;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * An OGR feature reader, reads data from the provided layer.<br>
 * It assumes eventual filters have already been set on it, and will extract only the
 * 
 * @author Andrea Aime - OpenGeo
 * 
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/OGRFeatureReader.java $
 */
public class OGRFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    Pointer<?> dataSource;

    Pointer<?> layer;

    SimpleFeatureType schema;

    Pointer<?> curr;

    private FeatureMapper mapper;

    boolean layerCompleted;

    public OGRFeatureReader(Pointer<?> dataSource, Pointer<?> layer,
            SimpleFeatureType targetSchema, SimpleFeatureType originalSchema, GeometryFactory gf) {
        this.dataSource = dataSource;
        this.layer = layer;
        this.schema = targetSchema;
        OGR_L_ResetReading(layer);
        this.layerCompleted = false;
        this.mapper = new FeatureMapper(targetSchema, originalSchema, gf);

        // TODO: mark as ignored all the fields we don't want to handle, as well as ignoring
        // the per feature style, assuming the caps say we can
    }

    public void close() throws IOException {
        if (curr != null) {
            OGR_F_Destroy(curr);
            curr = null;
        }
        if (layer != null) {
            OGRUtils.releaseLayer(layer);
            layer = null;
        }
        if (dataSource != null) {
            OGRUtils.releaseDataSource(dataSource);
            dataSource = null;
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
        if (layerCompleted) {
            return false;
        }

        if (curr == null) {
            curr = OGR_L_GetNextFeature(layer);
        }
        if (curr != null) {
            return true;
        } else {
            layerCompleted = true;
            return false;
        }
    }

    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext())
            throw new NoSuchElementException("There are no more Features to be read");

        SimpleFeature f = mapper.convertOgrFeature(curr);

        // .. nullify curr, so that we can move to the next one
        curr.release();
        // OGR_F_Destroy(curr);
        curr = null;

        return f;
    }

}
