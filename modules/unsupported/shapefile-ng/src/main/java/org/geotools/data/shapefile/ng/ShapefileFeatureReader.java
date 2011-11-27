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
package org.geotools.data.shapefile.ng;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileReader;
import org.geotools.data.shapefile.ng.dbf.DbaseFileReader.Row;
import org.geotools.data.shapefile.ng.fid.IndexedFidReader;
import org.geotools.data.shapefile.ng.shp.ShapeType;
import org.geotools.data.shapefile.ng.shp.ShapefileReader;
import org.geotools.data.shapefile.ng.shp.ShapefileReader.Record;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.renderer.ScreenMap;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

class ShapefileFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

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

    public ShapefileFeatureReader(SimpleFeatureType schema, ShapefileReader shp, DbaseFileReader dbf, IndexedFidReader fidReader)
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
                        count = (Integer) userData
                                .get(ShapefileDataStore.ORIGINAL_FIELD_DUPLICITY_COUNT);
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
                        throw new IOException("Could not find attribute " + attName
                                + " (mul count: " + count);
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
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if (hasNext()) {
            SimpleFeature result = nextFeature;
            nextFeature = null;
            return result;
        } else {
            throw new NoSuchElementException("hasNext() returned false");
        }
    }

    /**
     * Returns true if the lower level readers, shp and dbf, have one more record to read
     * 
     * @return
     * @throws IOException
     */
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

            // read the geometry, so that we can decide if this row is to be skipped or not
            Envelope envelope = record.envelope();
            boolean skip = false;
            Geometry geometry = null;
            if(schema.getGeometryDescriptor() != null) {
                // ... if geometry is out of the target bbox, skip both geom and row
                if (targetBBox != null && !targetBBox.isNull() && !targetBBox.intersects(envelope)) {
                    skip = true;
                    // ... if the geometry is awfully small avoid reading it (unless it's a point)
                } else if (simplificationDistance > 0 && envelope.getWidth() < simplificationDistance
                        && envelope.getHeight() < simplificationDistance) {
                    try {
                        if (screenMap != null && screenMap.checkAndSet(envelope)) {
                            geometry = null;
                            skip = true;
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

            if (!skip) {
                // also grab the dbf row
                Row row;
                if (dbf != null) {
                    row = dbf.readRow();
                } else {
                    row = null;
                }

                nextFeature = buildFeature(record.number, geometry, row);
            } else {
                if (dbf != null) {
                    dbf.skip();
                }
            }
        }

        return nextFeature != null;
    }

    SimpleFeature buildFeature(int number, Geometry geometry, Row row) throws IOException {
        if (dbfindexes != null) {
            for (int i = 0; i < dbfindexes.length; i++) {
                if (dbfindexes[i] == -1) {
                    builder.add(geometry);
                } else {
                    builder.add(row.read(dbfindexes[i]));
                }
            }
        } else if(geometry != null) {
            builder.add(geometry);
        }
        // build the feature id
        String featureId = buildFeatureId(number);
        return builder.buildFeature(featureId);
    }
    
    protected String buildFeatureId(int number) throws IOException {
        if(fidReader == null) {
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
                shp = null;
                dbf = null;
            }
        }

    }

    /**
     * Sets the target bbox, will be used to skip over features we do not need
     * 
     * @param targetBBox
     */
    public void setTargetBBox(Envelope targetBBox) {
        this.targetBBox = targetBBox;
    }

    /**
     * Sets the simplification distance, the reader will subsample pixels on the go
     * 
     * @param simplificationDistance
     */
    public void setSimplificationDistance(double simplificationDistance) {
        this.simplificationDistance = simplificationDistance;
    }

    /**
     * Sets the screen map, will be used to skip over features that are too small
     * 
     * @param screenMap
     */
    public void setScreenMap(ScreenMap screenMap) {
        this.screenMap = screenMap;
    }

    void disableShxUsage() throws IOException {
        this.shp.disableShxUsage();

    }

    ShapeType getShapeType() {
        return shp.getHeader().getShapeType();
    }

}
