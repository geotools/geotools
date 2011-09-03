/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate.sort;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

/**
 * Reads the features stored in the specified block of a {@link RandomAccessFile}
 * 
 * @author Andrea Aime - GeoSolutions
 */
class FeatureBlockReader {

    RandomAccessFile raf;

    SimpleFeatureType schema;

    SimpleFeatureBuilder builder;

    SimpleFeature curr;

    long offset;

    int count;

    public FeatureBlockReader(RandomAccessFile raf, long start, int count, SimpleFeatureType schema) {
        this.raf = raf;
        this.offset = start;
        this.count = count;
        this.schema = schema;
        this.builder = new SimpleFeatureBuilder(schema);
    }

    public SimpleFeature feature() throws IOException {
        if (curr == null && count > 0) {
            curr = readNextFeature();
        }
        return curr;
    }

    public SimpleFeature next() throws IOException {
        curr = readNextFeature();
        return curr;
    }

    private SimpleFeature readNextFeature() throws IOException {
        if (count <= 0) {
            return null;
        }

        // move to the next feature offset
        raf.seek(offset);

        // read the fid, check for file end
        String fid = raf.readUTF();
        // read the other attributes, build the feature
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            Object att = readAttribute(ad);
            builder.add(att);
        }
        // update the offset for the next feature
        offset = raf.getFilePointer();
        count--;

        // return the feature
        return builder.buildFeature(fid);
    }

    /**
     * Reads the attributes.
     * 
     * @param ad
     * @return
     * @throws IOException
     */
    Object readAttribute(AttributeDescriptor ad) throws IOException {
        // See the comments in {@link MergeSortDumper#writeAttribute(RandomAccessFile,
        // AttributeDescriptor, Object)} to get an insight on why the method is built like this
        boolean isNull = raf.readBoolean();
        if (isNull) {
            return null;
        } else {
            Class<?> binding = ad.getType().getBinding();
            if (binding == Boolean.class) {
                return raf.readBoolean();
            } else if (binding == Byte.class || binding == byte.class) {
                return raf.readByte();
            } else if (binding == Short.class || binding == short.class) {
                return raf.readShort();
            } else if (binding == Integer.class || binding == int.class) {
                return raf.readByte();
            } else if (binding == Long.class || binding == long.class) {
                return raf.readLong();
            } else if (binding == Float.class || binding == float.class) {
                return raf.readFloat();
            } else if (binding == Double.class || binding == double.class) {
                return raf.readDouble();
            } else if (binding == String.class) {
                return raf.readUTF();
            } else if (binding == java.sql.Date.class) {
                return new java.sql.Date(raf.readLong());
            } else if (binding == java.sql.Time.class) {
                return new java.sql.Time(raf.readLong());
            } else if (binding == java.sql.Timestamp.class) {
                return new java.sql.Timestamp(raf.readLong());
            } else if (binding == java.util.Date.class) {
                return new java.util.Date(raf.readLong());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                WKBReader reader = new WKBReader();
                int length = raf.readInt();
                byte[] buffer = new byte[length];
                raf.read(buffer);
                try {
                    return reader.read(buffer);
                } catch (ParseException e) {
                    throw new IOException("Failed to parse the geometry WKB", e);
                }
            } else {
                int length = raf.readInt();
                byte[] buffer = new byte[length];
                raf.read(buffer);
                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(bis);
                try {
                    return ois.readObject();
                } catch (ClassNotFoundException e) {
                    throw new IOException("Could not read back object", e);
                }
            }
        }
    }

}
