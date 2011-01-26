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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.shapefile.ShapefileAttributeReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.index.CloseableCollection;
import org.geotools.index.Data;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * An AttributeReader implementation for shape. Pretty straightforward. <BR/>The
 * default geometry is at position 0, and all dbf columns follow. <BR/>The dbf
 * file may not be necessary, if not, just pass null as the DbaseFileReader
 *
 * @source $URL$
 */
public class IndexedShapefileAttributeReader extends ShapefileAttributeReader
        implements RecordNumberTracker {

    protected Iterator<Data> goodRecs;

    private int recno;

    private Data next;

    private CloseableCollection<Data> closeableCollection;

    public IndexedShapefileAttributeReader(
            List<AttributeDescriptor> attributes, ShapefileReader shp,
            IndexedDbaseFileReader dbf, CloseableCollection<Data> goodRecs) {
        this(attributes.toArray(new AttributeDescriptor[0]), shp, dbf, goodRecs);
    }

    /**
     * Create the shape reader
     * 
     * @param atts -
     *                the attributes that we are going to read.
     * @param shp -
     *                the shape reader, required
     * @param dbf -
     *                the dbf file reader. May be null, in this case no
     *                attributes will be read from the dbf file
     * @param goodRecs
     *                Collection of good indexes that match the query.
     */
    public IndexedShapefileAttributeReader(AttributeDescriptor[] atts,
            ShapefileReader shp, IndexedDbaseFileReader dbf,
            CloseableCollection<Data> goodRecs) {
        super(atts, shp, dbf);
        if (goodRecs != null)
            this.goodRecs = goodRecs.iterator();
        this.closeableCollection = goodRecs;
        this.recno = 0;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if( closeableCollection!=null ){
                closeableCollection.closeIterator(goodRecs);
                closeableCollection.close();
            }
            goodRecs = null;
        }
    }

    public boolean hasNext() throws IOException {
        if (this.goodRecs != null) {
            if (next != null)
                return true;
            if (this.goodRecs.hasNext()) {

                next = (Data) goodRecs.next();
                this.recno = ((Integer) next.getValue(0)).intValue();
                return true;
            }
            return false;
        }

        return super.hasNext();
    }

    public void next() throws IOException {
        if (!hasNext())
            throw new IndexOutOfBoundsException("No more features in reader");
        if (this.goodRecs != null) {
            this.recno = ((Integer) next.getValue(0)).intValue();

            if (dbf != null) {
                ((IndexedDbaseFileReader) dbf).goTo(this.recno);
            }

            Long l = (Long) next.getValue(1);
            shp.goTo((int) l.longValue());
            next = null;
        } else {
            this.recno++;
        }

        super.next();
    }

    public int getRecordNumber() {
        return this.recno;
    }

}
