/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import java.io.IOException;

import org.geotools.data.shapefile.ng.dbf.DbaseFileReader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileReader.Row;
import org.geotools.data.shapefile.ng.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.ng.index.CloseableIterator;
import org.geotools.data.shapefile.ng.index.Data;
import org.geotools.data.shapefile.ng.shp.ShapefileReader;
import org.geotools.data.shapefile.ng.shp.ShapefileReader.Record;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The indexed version of the shapefile feature reader, will only read the records specified in the
 * constructor
 * 
 * @source $URL$
 */
class IndexedShapefileFeatureReader extends ShapefileFeatureReader {

    protected CloseableIterator<Data> goodRecs;

    private Data next;

    /**
     * Create the shape reader
     * 
     * @param atts - the attributes that we are going to read.
     * @param shp - the shape reader, required
     * @param dbf - the dbf file reader. May be null, in this case no attributes will be read from
     *        the dbf file
     * @param goodRecs Collection of good indexes that match the query.
     */
    public IndexedShapefileFeatureReader(SimpleFeatureType schema, ShapefileReader shp,
            DbaseFileReader dbf, CloseableIterator<Data> goodRecs) throws IOException {
        super(schema, shp, dbf);
        this.goodRecs = goodRecs;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (goodRecs != null) {
                goodRecs.close();
            }
            goodRecs = null;
        }
    }

    public boolean hasNext() throws IOException {
        while (nextFeature == null && this.goodRecs.hasNext()) {
            next = (Data) goodRecs.next();

            Long l = (Long) next.getValue(1);
            shp.goTo((int) l.longValue());

            Record record = shp.nextRecord();

            // read the geometry, so that we can decide if this row is to be skipped or not
            Envelope envelope = record.envelope();
            Geometry geometry = null;

            if(schema.getGeometryDescriptor() != null) {
                // ... if geometry is out of the target bbox, skip both geom and row
                if (targetBBox != null && !targetBBox.isNull() && !targetBBox.intersects(envelope)) {
                    continue;
                    // ... if the geometry is awfully small avoid reading it (unless it's a point)
                } else if (simplificationDistance > 0 && envelope.getWidth() < simplificationDistance
                        && envelope.getHeight() < simplificationDistance) {
                    try {
                        if (screenMap != null && screenMap.checkAndSet(envelope)) {
                            continue;
                        } else {
                            // if we are using the screenmap better provide a slightly modified
                            // version of the geometry bounds or we'll end up with many holes
                            // in the rendering
                            geometry = (Geometry) record.getSimplifiedShape(screenMap);
                        }
                    } catch (Exception e) {
                        geometry = (Geometry) record.getSimplifiedShape();
                    }
                    // ... otherwise business as usual
                } else {
                    geometry = (Geometry) record.shape();
                }
            }

            // read the dbf only if the geometry was not skipped
            Row row;
            if (dbf != null) {
                ((IndexedDbaseFileReader) dbf).goTo(record.number);
                row = dbf.readRow();
            } else {
                row = null;
            }

            nextFeature = buildFeature(record.number, geometry, row);
        }

        return nextFeature != null;
    }

}
