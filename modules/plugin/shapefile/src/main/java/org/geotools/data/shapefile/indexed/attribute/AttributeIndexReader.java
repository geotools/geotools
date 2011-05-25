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
package org.geotools.data.shapefile.indexed.attribute;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

import org.geotools.data.shapefile.StreamLogging;
import org.geotools.resources.NIOUtilities;

/**
 * Class to read an attribute index file
 * 
 * @author Manuele Ventoruzzo
 *
 *
 * @source $URL$
 */
public class AttributeIndexReader {

    private int record_size;
    private String attribute;
    private int numRecords;
    private char attributeType;
    private StreamLogging streamLogger = new StreamLogging("AttributeIndexReader");
    private FileChannel readChannel;
    private ByteBuffer buffer;
    private int foundPos;

    /** Crea una nuova istanza di AttributeIndexReader */
    public AttributeIndexReader(String attribute, FileChannel readChannel) throws IOException {
        this.readChannel = readChannel;
        this.attribute = attribute;
        streamLogger.open();
        readHeader();
        allocateBuffers();
    }

    /**
     * Returns the number of attributes in this index
     */
    public int getCount() {
        return numRecords;
    }

    public void goTo(int recno) throws IOException {
        long newPos = AttributeIndexWriter.HEADER_SIZE + (recno * record_size);
        if (newPos > readChannel.size())
            throw new NoSuchElementException();
        readChannel.position(newPos);
        buffer.limit(buffer.capacity());
        buffer.position(buffer.limit());
    }

    public IndexRecord next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return getRecord();
    }

    /**
     * Check if there's more elements to read
     */
    public boolean hasNext() throws IOException {
        if (isEOF())
            return false;
        if (buffer.position() == buffer.limit()) {
            buffer.position(0);
            buffer.limit((int) Math.min(buffer.limit(),remainingInFile()));
            int read = readChannel.read(buffer);
            if (read!=0)
                buffer.position(0);
        }
        return buffer.remaining() != 0;
    }

    public boolean isEOF() throws IOException {
        return (buffer.position() == buffer.limit()) && (readChannel.position() == readChannel.size());
    }

    /**
     * Finds an attribute and returns its FIDs.
     * 
     * @param reqAttribute
     *                Attribute to find.
     * @return Collection of FID found (empty if nothing could be found). It can
     *         found more than one record if it isn't univocal.
     * @throws java.io.IOException
     */
    public Collection findFids(Object reqAttribute) throws IOException {
        IndexRecord rec = findRecord(reqAttribute);
        if (rec==null)
            return new ArrayList(0);
        goTo(foundPos+1);
        ArrayList l = new ArrayList();
        while (rec.getAttribute().equals(reqAttribute)) {
            l.add(new Long(rec.getFeatureID()));
            if (!hasNext())
                break;
            rec = next();
        }
        return l;
    }

    /**
     * Finds an attibute.
     * 
     * @param reqAttribute
     *                Attribute to find.
     * @return Record as in index file.
     * @throws java.io.IOException
     */
    public IndexRecord findRecord(Object reqAttribute) throws IOException {
        foundPos = 0;
        return search(reqAttribute,0,numRecords,(numRecords/2)-1);
    }

    private void readHeader() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(AttributeIndexWriter.HEADER_SIZE);
        readChannel.read(buf);
        buf.position(0);
        attributeType = (char) buf.get();
        record_size = buf.getInt();
        numRecords = buf.getInt();
    }

    private void allocateBuffers() throws IOException {
        buffer = NIOUtilities.allocate(record_size*1024);
        buffer.position(buffer.limit());
    }

    /**
     * Searches for the desired record.
     * 
     * @param desired
     *                the id of the desired record.
     * @param minRec
     *                the last record that is known to be <em>before</em> the
     *                desired record.
     * @param maxRec
     *                the first record that is known to be <em>after</em> the
     *                desired record.
     * @param predictedRec
     *                the record that is predicted to be the desired record.
     * 
     * @return returns the record found.
     * 
     * @throws IOException
     */
    private IndexRecord search(Object desired, int minRec, int maxRec,
                int predictedRec) throws IOException {
        if (maxRec == minRec) {
            goTo(minRec);
            buffer.limit(record_size);
            IndexRecord currentRecord = next();
            buffer.limit(buffer.capacity());
            return (currentRecord != null && currentRecord.getAttribute().equals(desired))
                    ? firstInstance(minRec, currentRecord) : null;
        }
        goTo(predictedRec);
        buffer.limit(record_size);
        IndexRecord currentRecord = next();
        buffer.limit(buffer.capacity());
        if (currentRecord == null)
            return null;
        int comparison = currentRecord.compareTo(desired);
        if (comparison == 0) {
            return firstInstance(predictedRec, currentRecord);
        }
        if (maxRec - minRec < 2) {
            int llimit = (minRec == predictedRec) ? minRec + 1 : minRec;
            int ulimit = (minRec == predictedRec) ? maxRec : maxRec - 1;
            int newPrediction = (minRec == predictedRec) ? llimit : ulimit;
            return search(desired, llimit, ulimit, newPrediction);
        }
        if (comparison < 1) {
            int newPrediction = (maxRec - predictedRec) / 2 + predictedRec;
            return search(desired, ++predictedRec, maxRec, newPrediction);
        } else {
            int newPrediction = (predictedRec - minRec) / 2 + minRec;
            return search(desired, minRec, --predictedRec, newPrediction);
        }
    }

    private IndexRecord getRecord() throws IOException {
        Comparable obj = null;
        switch (attributeType) {
            case 'N':
                if (record_size == 12) {
                    obj = new Integer(buffer.getInt());
                } else {
                    obj = new Long(buffer.getLong());
                }
                break;
            case 'F':
                obj = new Double(buffer.getDouble());
                break;
            case 'L':
                obj = (buffer.get() == (byte) 1) ? Boolean.TRUE : Boolean.FALSE;
                break;
            case 'D':
                obj = new Date(buffer.getLong());
                break;
            case 'C':
            default:
                byte[] b = new byte[record_size - 8];
                buffer.get(b);
                obj = (new String(b, "ISO-8859-1")).trim();
            }
        long id = buffer.getLong();
        return new IndexRecord(obj, id);
    }

    private IndexRecord firstInstance(int position, IndexRecord rec) throws IOException {
        if (rec == null)
            return null;
        IndexRecord current = rec;
        IndexRecord prev = rec;
        while (rec.compareTo(current) == 0 && position > 0) {
            goTo(--position);
            buffer.limit(record_size);
            prev = current;
            current = next();
            buffer.limit(buffer.capacity());
        }
        boolean b = (rec.compareTo(current) == 0);
        foundPos = b ? position : position + 1;
        return b ? current : prev;
    }

    private long remainingInFile() throws IOException {
        return readChannel.size() - readChannel.position();
    }

}
