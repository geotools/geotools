/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.InStream;
import org.locationtech.jts.io.OutStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

/**
 * Allows writing and reading features to/from the given file
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SimpleFeatureIO {

    public static final int MAX_BYTES_LENGTH = 65535;
    public static final String BIG_STRING = "bigString";

    public static final String ENABLE_DESERIALIZATION = "geotools.simplefeatureio.deserialization";

    RandomAccessFile raf;

    SimpleFeatureType schema;

    SimpleFeatureBuilder builder;

    File file;

    WKBWriter writer = new WKBWriter();
    WKBReader reader = new WKBReader();
    ByteArrayInStream bis;
    ByteArrayOutStream bos;
    byte[] buffer = new byte[8192]; // reusable buffer for small geometries

    private final boolean initialFileEmpty;
    private final String enableDeserialization;

    public SimpleFeatureIO(File file, SimpleFeatureType schema) throws FileNotFoundException {
        this.file = file;
        this.raf = new RandomAccessFile(file, "rw");
        this.schema = schema;
        this.builder = new SimpleFeatureBuilder(schema);
        this.initialFileEmpty = this.file.length() <= 0;
        this.enableDeserialization = System.getProperty(ENABLE_DESERIALIZATION);
    }

    /** Writes the feature to the file */
    public void write(SimpleFeature sf) throws IOException {
        // write each attribute in the random access file
        List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
        // write feature id
        raf.writeUTF(sf.getID());
        // write the attributes
        for (AttributeDescriptor ad : attributes) {
            Object value = sf.getAttribute(ad.getLocalName());
            writeAttribute(ad, value);
        }
    }

    void writeAttribute(AttributeDescriptor ad, Object value) throws IOException {
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
                if (isBigString(ad)) {
                    // if attribute descriptor marked as Big String
                    String strVal = (String) value;
                    List<String> values = new ArrayList<>();
                    // if bytes length is over the maximum allowed, calculate parts
                    if (strVal.getBytes(StandardCharsets.UTF_8).length >= MAX_BYTES_LENGTH) {
                        values.addAll(split(strVal, 32767));
                    } else {
                        values.add(strVal);
                    }
                    // write total parts
                    raf.writeInt(values.size());
                    // write every string chunk
                    for (String evalue : values) {
                        raf.writeUTF(evalue);
                    }
                } else {
                    // normal string encoding
                    raf.writeUTF((String) value);
                }
            } else if (binding == java.sql.Date.class
                    || binding == java.sql.Time.class
                    || binding == java.sql.Timestamp.class
                    || binding == java.util.Date.class) {
                raf.writeLong(((Date) value).getTime());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                ByteArrayOutStream os = getOutStream(this.buffer);
                writer.write((Geometry) value, os);
                int len = os.getPosition();
                raf.writeInt(len);
                raf.write(os.getBuffer(), 0, len);
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

    private boolean isBigString(AttributeDescriptor ad) {
        return ad.getUserData() != null
                && ad.getUserData().containsKey(BIG_STRING)
                && ad.getUserData().get(BIG_STRING) instanceof Boolean
                && (Boolean) ad.getUserData().get(BIG_STRING);
    }

    /** Reads the next feature form the file */
    public SimpleFeature read() throws IOException {
        // read the fid, check for file end
        String fid = raf.readUTF();
        // read the other attributes, build the feature
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            Object att = readAttribute(ad);
            builder.add(att);
        }

        // return the feature
        return builder.buildFeature(fid);
    }

    /** Reads the attributes. */
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
                return raf.readInt();
            } else if (binding == Long.class || binding == long.class) {
                return raf.readLong();
            } else if (binding == Float.class || binding == float.class) {
                return raf.readFloat();
            } else if (binding == Double.class || binding == double.class) {
                return raf.readDouble();
            } else if (binding == String.class) {
                if (isBigString(ad)) {
                    // read total parts
                    int parts = raf.readInt();
                    // read every part
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts; i++) {
                        sb.append(raf.readUTF());
                    }
                    return sb.toString();
                } else {
                    return raf.readUTF();
                }
            } else if (binding == java.sql.Date.class) {
                return new java.sql.Date(raf.readLong());
            } else if (binding == java.sql.Time.class) {
                return new java.sql.Time(raf.readLong());
            } else if (binding == java.sql.Timestamp.class) {
                return new java.sql.Timestamp(raf.readLong());
            } else if (binding == java.util.Date.class) {
                return new java.util.Date(raf.readLong());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                int length = raf.readInt();
                byte[] buffer = getByteBuffer(length);
                raf.read(buffer, 0, length);
                try {
                    return reader.read(getInStream(buffer));
                } catch (ParseException e) {
                    throw new IOException("Failed to parse the geometry WKB", e);
                }
            } else {
                return readObject();
            }
        }
    }

    private ByteArrayOutStream getOutStream(byte[] buffer) {
        if (bos == null) {
            bos = new ByteArrayOutStream(buffer);
        } else {
            bos.setBuffer(buffer);
        }
        return bos;
    }

    private InStream getInStream(byte[] buffer) {
        if (bis == null) {
            bis = new ByteArrayInStream(buffer);
        } else {
            bis.setBytes(buffer);
        }
        return bis;
    }

    private byte[] getByteBuffer(int length) {
        byte[] buffer;
        if (length < this.buffer.length) {
            buffer = this.buffer;
        } else {
            buffer = new byte[length];
        }
        return buffer;
    }

    @SuppressWarnings("BanSerializableRead")
    private Object readObject() throws IOException {
        if ("false".equalsIgnoreCase(this.enableDeserialization)) {
            throw new IllegalStateException("Object deserialization is not allowed");
        } else if (!this.initialFileEmpty && !"true".equalsIgnoreCase(this.enableDeserialization)) {
            throw new IllegalStateException("Object deserialization is only allowed when created with an empty file");
        }
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

    /** Moves the IO to the specified offset in the file */
    public void seek(long offset) throws IOException {
        raf.seek(offset);
    }

    /** Returns the current reading position in the file */
    public long getOffset() throws IOException {
        return raf.getFilePointer();
    }

    /** Returns true if the end of file has been reached */
    public boolean endOfFile() throws IOException {
        return getOffset() >= raf.length();
    }

    /** Closes the IO, eventually deleting the file in the process */
    public void close(boolean deleteFile) throws IOException {
        try {
            raf.close();
        } finally {
            if (deleteFile) {
                file.delete();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SimpleFeatureIO [schema=" + schema + ", file=" + file + "]";
    }

    private Collection<String> split(String value, int charSize) {
        List<String> strings = new ArrayList<>();
        int index = 0;
        while (index < value.length()) {
            strings.add(value.substring(index, Math.min(index + charSize, value.length())));
            index += charSize;
        }
        return strings;
    }

    /**
     * Straight OutStream implementation on top of a byte[], can reuse the one given, or allocate a larger one if needed
     */
    private static class ByteArrayOutStream implements OutStream {

        byte[] buffer;
        int position;

        public ByteArrayOutStream(byte[] buffer) {
            setBuffer(buffer);
        }

        public void setBuffer(byte[] buffer) {
            this.buffer = buffer;
            this.position = 0;
        }

        @Override
        public void write(byte[] bytes, int len) throws IOException {
            if (position + len >= buffer.length) {
                grow(position + len);
            }
            System.arraycopy(bytes, 0, this.buffer, position, len);
            position += len;
        }

        private void grow(int minCapacity) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            int oldCapacity = buffer.length;
            int newCapacity = oldCapacity << 1;
            if (newCapacity - minCapacity < 0) newCapacity = minCapacity;
            this.buffer = Arrays.copyOf(buffer, newCapacity);
        }

        public int getPosition() {
            return position;
        }

        public byte[] getBuffer() {
            return buffer;
        }
    }
}
