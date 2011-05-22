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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.AbstractAttributeIO;
import org.geotools.data.AttributeReader;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.indexed.RecordNumberTracker;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.renderer.ScreenMap;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Envelope;

/**
 * An AttributeReader implementation for Shapefile. Pretty straightforward.
 * <BR/>The default geometry is at position 0, and all dbf columns follow.
 * <BR/>The dbf file may not be necessary, if not, just pass null as the
 * DbaseFileReader
 *
 * @source $URL$
 */
public class ShapefileAttributeReader extends AbstractAttributeIO implements
        AttributeReader, RecordNumberTracker {

    protected ShapefileReader shp;
    protected DbaseFileReader dbf;
    protected DbaseFileReader.Row row;
    protected ShapefileReader.Record record;
    int cnt;
    protected int[] dbfindexes;
    protected Envelope targetBBox;
    protected double simplificationDistance;
    protected Object geometry;
    protected ScreenMap screenMap;
    protected boolean featureAvailable = false;
    protected boolean flatFeature = false;

    public ShapefileAttributeReader(List<AttributeDescriptor> atts,
            ShapefileReader shp, DbaseFileReader dbf) {
        this(atts.toArray(new AttributeDescriptor[0]), shp, dbf);
    }
    
    /**
     * Sets a search area. If the geometry does not fall into it
     * it won't be read and will return a null geometry instead 
     * @param envelope
     */
    public void setTargetBBox(Envelope envelope) {
        this.targetBBox = envelope;
    }
    
    public void setSimplificationDistance(double distance) {
        this.simplificationDistance = distance;
    }
    
    public void setScreenMap(ScreenMap screenMap) {
        this.screenMap = screenMap;        
    }

    /**
     * Create the shapefile reader
     * 
     * @param atts -
     *                the attributes that we are going to read.
     * @param shp -
     *                the shapefile reader, required
     * @param dbf -
     *                the dbf file reader. May be null, in this case no
     *                attributes will be read from the dbf file
     */
    public ShapefileAttributeReader(AttributeDescriptor[] atts,
            ShapefileReader shp, DbaseFileReader dbf) {
        super(atts);
        this.shp = shp;
        this.dbf = dbf;
        
        if(dbf != null) {
            dbfindexes = new int[atts.length];
            DbaseFileHeader head = dbf.getHeader();
            AT: for (int i = 0; i < atts.length; i++) {
                String attName = atts[i].getLocalName();
                int count = 0;
                if (atts[i].getUserData().get(ShapefileDataStore.ORIGINAL_FIELD_NAME) != null){
                	attName = (String)atts[i].getUserData().get(ShapefileDataStore.ORIGINAL_FIELD_NAME);
                	count = (Integer)atts[i].getUserData().get(ShapefileDataStore.ORIGINAL_FIELD_DUPLICITY_COUNT);
                }

                for(int j = 0; j < head.getNumFields(); j++) {
                    if(head.getFieldName(j).equals(attName) && count-- <= 0){
                        dbfindexes[i] = j;
                        continue AT;
                    }
                }
                dbfindexes[i] = -1; // geometry
            }
        }
    }

    public void close() throws IOException {
        try {
            if (shp != null) {
                shp.close();
            }

            if (dbf != null) {
                dbf.close();
            }
        } finally {
            row = null;
            record = null;
            shp = null;
            dbf = null;
        }
    }

    boolean internalReadersHaveNext() throws IOException {
        int n = shp.hasNext() ? 1 : 0;

        if (dbf != null) {
            n += (dbf.hasNext() ? 2 : 0);
        }

        if ((n == 3) || ((n == 1) && (dbf == null))) {
            return true;
        }

        if (n == 0) {
            return false;
        }

        throw new IOException(((n == 1) ? "Shp" : "Dbf") + " has extra record");
    }
    
    public boolean hasNext() throws IOException {
        while(!featureAvailable && internalReadersHaveNext()) {
            record = shp.nextRecord();
            
            // read the geometry, so that we can decide if this row is to be skipped or not
            Envelope envelope = record.envelope();
            boolean skip = false;
            // ... if geometry is out of the target bbox, skip both geom and row
            if (targetBBox != null && !targetBBox.isNull() && !targetBBox.intersects(envelope)) {
                geometry = null;
                skip = true;
            // ... if the geometry is awfully small avoid reading it (unless it's a point)
            } else if (simplificationDistance > 0 && envelope.getWidth() < simplificationDistance
                    && envelope.getHeight() < simplificationDistance) {
                try {
                    if(screenMap != null && screenMap.checkAndSet(envelope)) {
                        geometry = null;
                        skip = true;
                    } else {
                        // if we are using the screenmap better provide a slightly modified
                        // version of the geometry bounds or we'll end up with many holes
                        // in the rendering
                        geometry = record.getSimplifiedShape(screenMap);
                    }
                } catch(Exception e) {
                    geometry = record.getSimplifiedShape();
                }
            // ... otherwise business as usual
            } else {
                geometry = record.shape();
            }

            // read the dbf only if the geometry was not skipped
            if (dbf != null) {
                if(skip) {
                    dbf.skip();
                    row = null;
                } else {
                    row = dbf.readRow();
                }
            } else {
                row = null;
            }
            featureAvailable = !skip;
        }
        
        return featureAvailable;
    }

    public void next() throws IOException {
        if(!hasNext()) {
            throw new NoSuchElementException("hasNext() returned false");
        }
        featureAvailable = false;
    }

    public Object read(int param) throws IOException,
            java.lang.ArrayIndexOutOfBoundsException {
        int index = dbfindexes != null ? dbfindexes[param] : -1;
                   
        switch (index) {
        case -1:
            return geometry; // geometry is considered dbf index -1

        default:
            if (row != null) {
                return row.read( index );
            }
            return null;
        }
    }
    
    public int getRecordNumber() {
        return this.record.number;
    }

    
    
}
