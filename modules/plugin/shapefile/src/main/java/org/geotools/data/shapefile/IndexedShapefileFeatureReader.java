/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.io.IOException;
import org.geotools.data.CloseableIterator;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader.Row;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.fid.IndexedFidReader;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * The indexed version of the shapefile feature reader, will only read the records specified in the
 * constructor
 *
 * @source $URL$
 */
class IndexedShapefileFeatureReader extends ShapefileFeatureReader {

    protected CloseableIterator<Data> goodRecs;

    private Data next;

    private IndexedFidReader fidReader;

    /**
     * Create the shape reader
     *
     * @param atts - the attributes that we are going to read.
     * @param shp - the shape reader, required
     * @param dbf - the dbf file reader. May be null, in this case no attributes will be read from
     *     the dbf file
     * @param goodRecs Collection of good indexes that match the query.
     */
    public IndexedShapefileFeatureReader(
            SimpleFeatureType schema,
            ShapefileReader shp,
            DbaseFileReader dbf,
            IndexedFidReader fidReader,
            CloseableIterator<Data> goodRecs)
            throws IOException {
        super(schema, shp, dbf, fidReader);
        this.goodRecs = goodRecs;
        this.fidReader = fidReader;
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
            next = goodRecs.next();

            Long l = (Long) next.getValue(1);
            shp.goTo((int) l.longValue());

            Record record = shp.nextRecord();

            // read the geometry, so that we can decide if this row is to be skipped or not
            Geometry geometry = getGeometry(record);
            if (geometry == SKIP) {
                continue;
            }

            // read the dbf only if the geometry was not skipped
            Row row;
            if (dbf != null) {
                ((IndexedDbaseFileReader) dbf).goTo(record.number);
                row = dbf.readRow();
            } else {
                row = null;
            }

            nextFeature = buildFeature(record.number, geometry, row, record.envelope());
        }

        return nextFeature != null;
    }
}
