/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader.Row;
import org.geotools.data.shapefile.fid.IndexedFidReader;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.data.util.ScreenMap;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.TransformException;

class ShapefileFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    static final Logger LOGGER = Logging.getLogger(ShapefileFeatureReader.class);

    protected static final Geometry SKIP =
            new Point(new LiteCoordinateSequence(Double.NaN, Double.NaN), new GeometryFactory()) {
                private static final long serialVersionUID = 6311215718936799001L;

                public String toString() {
                    return "SKIP";
                };
            };

    SimpleFeatureType schema;

    ShapefileReader shp;

    DbaseFileReader dbf;

    int[] dbfindexes;

    SimpleFeatureBuilder builder;

    SimpleFeature nextFeature;

    Envelope targetBBox;

    double simplificationDistance;

    ScreenMap screenMap;

    StringBuffer idxBuffer;

    int idxBaseLen;

    IndexedFidReader fidReader;

    Filter filter;

    public ShapefileFeatureReader(
            SimpleFeatureType schema,
            ShapefileReader shp,
            DbaseFileReader dbf,
            IndexedFidReader fidReader)
            throws IOException {
        this.schema = schema;
        this.shp = shp;
        this.dbf = dbf;
        this.fidReader = fidReader;
        this.builder = new SimpleFeatureBuilder(schema);

        idxBuffer = new StringBuffer(schema.getTypeName());
        idxBuffer.append('.');
        idxBaseLen = idxBuffer.length();

        if (dbf != null) {
            // build the list of dbf indexes we have to read taking into consideration the
            // duplicated dbf field names issue
            List<AttributeDescriptor> atts = schema.getAttributeDescriptors();
            dbfindexes = new int[atts.size()];
            DbaseFileHeader head = dbf.getHeader();
            for (int i = 0; i < atts.size(); i++) {
                AttributeDescriptor att = atts.get(i);
                if (att instanceof GeometryDescriptor) {
                    dbfindexes[i] = -1;
                } else {
                    String attName = att.getLocalName();
                    int count = 0;
                    Map<Object, Object> userData = att.getUserData();
                    if (userData.get(ShapefileDataStore.ORIGINAL_FIELD_NAME) != null) {
                        attName = (String) userData.get(ShapefileDataStore.ORIGINAL_FIELD_NAME);
                        count =
                                (Integer)
                                        userData.get(
                                                ShapefileDataStore.ORIGINAL_FIELD_DUPLICITY_COUNT);
                    }

                    boolean found = false;
                    for (int j = 0; j < head.getNumFields(); j++) {
                        if (head.getFieldName(j).equals(attName) && count-- <= 0) {
                            dbfindexes[i] = j;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IOException(
                                "Could not find attribute " + attName + " (mul count: " + count);
                    }
                }
            }
        }
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (hasNext()) {
            SimpleFeature result = nextFeature;
            nextFeature = null;
            return result;
        } else {
            throw new NoSuchElementException("hasNext() returned false");
        }
    }

    /** Returns true if the lower level readers, shp and dbf, have one more record to read */
    boolean filesHaveMore() throws IOException {
        if (dbf == null) {
            return shp.hasNext();
        } else {
            boolean dbfHasNext = dbf.hasNext();
            boolean shpHasNext = shp.hasNext();
            if (dbfHasNext && shpHasNext) {
                return true;
            } else if (dbfHasNext || shpHasNext) {
                throw new IOException(((shpHasNext) ? "Shp" : "Dbf") + " has extra record");
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        while (nextFeature == null && filesHaveMore()) {
            Record record = shp.nextRecord();

            Geometry geometry = getGeometry(record);
            if (geometry != SKIP) {
                // also grab the dbf row
                Row row;
                if (dbf != null) {
                    row = dbf.readRow();
                    if (row.isDeleted()) {
                        continue;
                    }
                } else {
                    row = null;
                }

                nextFeature = buildFeature(record.number, geometry, row, record.envelope());
            } else {
                if (dbf != null) {
                    dbf.skip();
                }
            }
        }

        return nextFeature != null;
    }

    /**
     * Reads the geometry, it will return {@link #SKIP} if the records is to be skipped because of
     * the screenmap or because it does not match the target bbox
     */
    protected Geometry getGeometry(Record record) {
        // read the geometry, so that we can decide if this row is to be skipped or not
        Envelope envelope = record.envelope();
        Geometry geometry = null;
        if (schema.getGeometryDescriptor() != null) {
            // ... if geometry is out of the target bbox, skip both geom and row
            if (targetBBox != null && !targetBBox.isNull() && !targetBBox.intersects(envelope)) {
                geometry = SKIP;
                // ... if the geometry is awfully small avoid reading it (unless it's a point)
            } else if (simplificationDistance > 0
                    && envelope.getWidth() < simplificationDistance
                    && envelope.getHeight() < simplificationDistance) {
                try {
                    // if we have the screenmap, we either have no filter, and we
                    // can directly alter the screenmap, or we have a filter, in that
                    // case we just check if the screenmap is already busy
                    if (screenMap != null && screenMap.get(envelope)) {
                        geometry = SKIP;
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
        return geometry;
    }

    SimpleFeature buildFeature(int number, Geometry geometry, Row row, Envelope envelope)
            throws IOException {
        if (dbfindexes != null) {
            for (int i = 0; i < dbfindexes.length; i++) {
                if (dbfindexes[i] == -1) {
                    builder.add(geometry);
                } else {
                    builder.add(row.read(dbfindexes[i]));
                }
            }
        } else if (geometry != null) {
            builder.add(geometry);
        }
        // build the feature id
        String featureId = buildFeatureId(number);
        SimpleFeature feature = builder.buildFeature(featureId);
        if (filter != null) {
            // if we should not return the feature, just drop it and continue reading
            if (!filter.evaluate(feature)) {
                return null;
            }
        }

        // update screenmap if present, now that we have the certainty
        // that the record is to be returned and will be displayed
        if (screenMap != null) {
            // we are going to keep the feature, if we have the screenmap do update
            // it (if we got here, we already checked the screenmap was not busy)
            try {
                screenMap.checkAndSet(envelope);
            } catch (TransformException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Failed to set screenmap", e);
                }
            }
        }

        return feature;
    }

    protected String buildFeatureId(int number) throws IOException {
        if (fidReader == null) {
            idxBuffer.delete(idxBaseLen, idxBuffer.length());
            idxBuffer.append(number);
            return idxBuffer.toString();
        } else {
            fidReader.goTo(number - 1);
            return fidReader.next();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (shp != null) {
                shp.close();
            }
        } finally {
            try {
                if (dbf != null) {
                    dbf.close();
                }
            } finally {
                try {
                    if (fidReader != null) {
                        fidReader.close();
                    }
                } finally {
                    shp = null;
                    dbf = null;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        if (shp != null) {
            LOGGER.log(
                    Level.WARNING,
                    "There is code leaving shapefile readers unclosed, "
                            + "this might result in file system locks not being cleared. File is: "
                            + schema.getTypeName());
            close();
        }
    }

    /** Sets the target bbox, will be used to skip over features we do not need */
    public void setTargetBBox(Envelope targetBBox) {
        this.targetBBox = targetBBox;
    }

    /** Sets the simplification distance, the reader will subsample pixels on the go */
    public void setSimplificationDistance(double simplificationDistance) {
        this.simplificationDistance = simplificationDistance;
    }

    /** Sets the screen map, will be used to skip over features that are too small */
    public void setScreenMap(ScreenMap screenMap) {
        this.screenMap = screenMap;
    }

    void disableShxUsage() throws IOException {
        this.shp.disableShxUsage();
    }

    ShapeType getShapeType() {
        return shp.getHeader().getShapeType();
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
