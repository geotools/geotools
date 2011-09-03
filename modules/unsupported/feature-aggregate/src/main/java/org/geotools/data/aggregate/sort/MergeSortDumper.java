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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;

class MergeSortDumper {

    static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        if (sortBy == SortBy.UNSORTED) {
            return true;
        }

        // check all attributes are serializable
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            Class<?> binding = ad.getType().getBinding();
            if (!Serializable.class.isAssignableFrom(binding)) {
                return false;
            }
        }

        // check all sorting attributes are comparable
        for (SortBy sb : sortBy) {
            if (sb != SortBy.NATURAL_ORDER && sb != SortBy.REVERSE_ORDER) {
                AttributeDescriptor ad = schema.getDescriptor(sb.getPropertyName()
                        .getPropertyName());
                if (ad == null || !Comparable.class.isAssignableFrom(ad.getType().getBinding())) {
                    return false;
                }
            }
        }

        return true;
    }

    static SimpleFeatureReader getDelegateReader(SimpleFeatureReader reader, SortBy[] sortBy,
            int maxFeatures) throws IOException {
        Comparator<SimpleFeature> comparator = getComparator(sortBy);

        // easy case, no sorting needed
        if (comparator == null) {
            return reader;
        }

        // double check
        SimpleFeatureType schema = reader.getFeatureType();
        if (!canSort(schema, sortBy)) {
            throw new IllegalArgumentException(
                    "The specified reader cannot be sorted, either the "
                            + "sorting properties are not comparable or the attributes are not serializable");
        }

        int count = 0;
        File file = null;
        RandomAccessFile raf = null;
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        List<FeatureBlockReader> readers = new ArrayList<FeatureBlockReader>();
        boolean cleanFile = true;
        try {
            // read and store into files as necessary
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                features.add(f);
                count++;

                if (count > maxFeatures) {
                    Collections.sort(features, comparator);
                    if (raf == null) {
                        file = File.createTempFile("sorted", ".features");
                        file.delete();
                        raf = new RandomAccessFile(file, "rw");
                    }
                    FeatureBlockReader fbr = storeToFile(raf, features, schema);
                    readers.add(fbr);
                    count = 0;
                    features.clear();
                }
            }

            // return the appropriate reader
            if (raf == null) {
                // simple case, we managed to keep everything in memory, sort and return a
                // reader based on the collection contents
                Collections.sort(features, comparator);

                SimpleFeatureIterator fi = new ListFeatureCollection(schema, features).features();
                return new DelegateSimpleFeatureReader(schema, fi);
            } else {
                // go merge-sort
                cleanFile = false;
                return new MergeSortReader(schema, raf, file, readers, comparator);
            }

        } finally {
            if (cleanFile && raf != null) {
                raf.close();
                file.delete();
            }

            reader.close();
        }
    }

    /**
     * Writes the feature attributes to a binary file
     * 
     * @param features
     * @return
     * @throws IOException
     */
    static FeatureBlockReader storeToFile(RandomAccessFile raf, List<SimpleFeature> features,
            SimpleFeatureType schema) throws IOException {
        long start = raf.getFilePointer();

        // write each attribute in the random access file
        List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
        for (SimpleFeature sf : features) {
            // write feature id
            raf.writeUTF(sf.getID());
            // write the attributes
            for (AttributeDescriptor ad : attributes) {
                Object value = sf.getAttribute(ad.getLocalName());
                writeAttribute(raf, ad, value);
            }
        }

        return new FeatureBlockReader(raf, start, features.size(), schema);
    }

    static void writeAttribute(RandomAccessFile raf, AttributeDescriptor ad, Object value)
            throws IOException {
        if (value == null) {
            // null marker
            raf.writeBoolean(true);
        } else {
            // not null, write the contents. This one requires some explanation. We are not
            // writing any type metadata in the stream for the types we can optimize (primitives,
            // numbers,
            // strings and the like). This means we have to be 100% sure the class we're writing is
            // actually the one we can optimize for, and not some subclass. Thus, we are authorized
            // to use identity comparison instead of isAssignableFrom or equality, when we read back
            // it must be as if we did not serialize stuff at all
            raf.writeBoolean(false);
            Class<?> binding = ad.getType().getBinding();
            if (binding == Boolean.class) {
                raf.writeBoolean((Boolean) value);
            } else if (binding == Byte.class || binding == byte.class) {
                raf.writeByte((Byte) value);
            } else if (binding == Short.class || binding == short.class) {
                raf.writeShort((Short) value);
            } else if (binding == Integer.class || binding == int.class) {
                raf.writeInt((Integer) value);
            } else if (binding == Long.class || binding == long.class) {
                raf.writeLong((Long) value);
            } else if (binding == Float.class || binding == float.class) {
                raf.writeFloat((Float) value);
            } else if (binding == Double.class || binding == double.class) {
                raf.writeDouble((Double) value);
            } else if (binding == String.class) {
                raf.writeUTF((String) value);
            } else if (binding == java.sql.Date.class || binding == java.sql.Time.class
                    || binding == java.sql.Timestamp.class || binding == java.util.Date.class) {
                raf.writeLong(((Date) value).getTime());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                WKBWriter writer = new WKBWriter();
                byte[] buffer = writer.write((Geometry) value);
                int length = buffer.length;
                raf.writeInt(length);
                raf.write(buffer);
            } else {
                // can't optimize, in this case we use an ObjectOutputStream to write out
                // full metadata
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(value);
                oos.flush();
                byte[] bytes = bos.toByteArray();
                raf.writeInt(bytes.length);
                raf.write(bytes);
            }
        }
    }

    /**
     * Builds a comparator out of the sortBy list
     * 
     * @param sortBy
     * @return
     */
    static Comparator<SimpleFeature> getComparator(SortBy[] sortBy) {
        // handle the easy cases, no sorting or natural sorting
        if (sortBy == SortBy.UNSORTED || sortBy == null) {
            return null;
        }

        // build a list of comparators
        List<Comparator<SimpleFeature>> comparators = new ArrayList<Comparator<SimpleFeature>>();
        for (SortBy sb : sortBy) {
            if (sb == SortBy.NATURAL_ORDER) {
                comparators.add(new FidComparator(true));
            } else if (sb == SortBy.REVERSE_ORDER) {
                comparators.add(new FidComparator(false));
            } else {
                String name = sb.getPropertyName().getPropertyName();
                boolean ascending = sb.getSortOrder() == SortOrder.ASCENDING;
                comparators.add(new PropertyComparator(name, ascending));
            }
        }

        // return the final comparator
        if (comparators.size() == 1) {
            return comparators.get(0);
        } else {
            return new CompositeComparator(comparators);
        }

    }
}
