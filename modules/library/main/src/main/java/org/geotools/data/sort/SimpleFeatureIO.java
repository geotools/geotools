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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
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

    // a feature buffer that grew past this size is dropped before the next feature, so one large feature
    // does not keep its memory until the file is closed
    private static final int MAX_KEPT_BUFFER_SIZE = 1024 * 1024;

    RandomAccessFile raf;

    SimpleFeatureType schema;

    SimpleFeatureBuilder builder;

    File file;

    WKBWriter writer = new WKBWriter();
    WKBReader reader = new WKBReader();
    ByteArrayInStream bis;
    ByteArrayOutStream bos;
    byte[] buffer = new byte[8192]; // reusable buffer for small geometries

    // Byte array streams holding one feature, so the file sees one call per feature instead of one per value
    FeatureOutputBuffer outBuffer = new FeatureOutputBuffer();
    DataOutputStream out = new DataOutputStream(outBuffer);
    FeatureInputBuffer inBuffer = new FeatureInputBuffer();
    DataInputStream in = new DataInputStream(inBuffer);

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
        List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
        outBuffer.startFeature();
        // write feature id
        out.writeUTF(sf.getID());
        // write the attributes
        for (AttributeDescriptor ad : attributes) {
            Object value = sf.getAttribute(ad.getLocalName());
            writeAttribute(ad, value);
        }
        outBuffer.writeFeature(raf);
    }

    void writeAttribute(AttributeDescriptor ad, Object value) throws IOException {
        if (value == null) {
            // null marker
            out.writeBoolean(true);
        } else {
            // not null, write the contents. This one requires some explanation. We are not
            // writing any type metadata in the stream for the types we can optimize (primitives,
            // numbers,
            // strings and the like). This means we have to be 100% sure the class we're writing is
            // actually the one we can optimize for, and not some subclass. Thus, we are authorized
            // to use identity comparison instead of isAssignableFrom or equality, when we read back
            // it must be as if we did not serialize stuff at all
            out.writeBoolean(false);
            Class<?> binding = ad.getType().getBinding();
            if (binding == Boolean.class) {
                out.writeBoolean((Boolean) value);
            } else if (binding == Byte.class || binding == byte.class) {
                out.writeByte((Byte) value);
            } else if (binding == Short.class || binding == short.class) {
                out.writeShort((Short) value);
            } else if (binding == Integer.class || binding == int.class) {
                out.writeInt((Integer) value);
            } else if (binding == Long.class || binding == long.class) {
                out.writeLong((Long) value);
            } else if (binding == Float.class || binding == float.class) {
                out.writeFloat((Float) value);
            } else if (binding == Double.class || binding == double.class) {
                out.writeDouble((Double) value);
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
                    out.writeInt(values.size());
                    // write every string chunk
                    for (String evalue : values) {
                        out.writeUTF(evalue);
                    }
                } else {
                    // normal string encoding
                    out.writeUTF((String) value);
                }
            } else if (binding == java.sql.Date.class
                    || binding == java.sql.Time.class
                    || binding == java.sql.Timestamp.class
                    || binding == java.util.Date.class) {
                out.writeLong(((Date) value).getTime());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                ByteArrayOutStream os = getOutStream(this.buffer);
                writer.write((Geometry) value, os);
                int len = os.getPosition();
                out.writeInt(len);
                out.write(os.getBuffer(), 0, len);
            } else {
                // can't optimize, in this case we use an ObjectOutputStream to write out
                // full metadata
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(value);
                oos.flush();
                byte[] bytes = bos.toByteArray();
                out.writeInt(bytes.length);
                out.write(bytes);
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
        inBuffer.readFeature(raf);
        String fid = in.readUTF();
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
        boolean isNull = in.readBoolean();
        if (isNull) {
            return null;
        } else {
            Class<?> binding = ad.getType().getBinding();
            if (binding == Boolean.class) {
                return in.readBoolean();
            } else if (binding == Byte.class || binding == byte.class) {
                return in.readByte();
            } else if (binding == Short.class || binding == short.class) {
                return in.readShort();
            } else if (binding == Integer.class || binding == int.class) {
                return in.readInt();
            } else if (binding == Long.class || binding == long.class) {
                return in.readLong();
            } else if (binding == Float.class || binding == float.class) {
                return in.readFloat();
            } else if (binding == Double.class || binding == double.class) {
                return in.readDouble();
            } else if (binding == String.class) {
                if (isBigString(ad)) {
                    // read total parts
                    int parts = in.readInt();
                    // read every part
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts; i++) {
                        sb.append(in.readUTF());
                    }
                    return sb.toString();
                } else {
                    return in.readUTF();
                }
            } else if (binding == java.sql.Date.class) {
                return new java.sql.Date(in.readLong());
            } else if (binding == java.sql.Time.class) {
                return new java.sql.Time(in.readLong());
            } else if (binding == java.sql.Timestamp.class) {
                return new java.sql.Timestamp(in.readLong());
            } else if (binding == java.util.Date.class) {
                return new java.util.Date(in.readLong());
            } else if (Geometry.class.isAssignableFrom(binding)) {
                int length = in.readInt();
                byte[] buffer = getByteBuffer(length);
                in.readFully(buffer, 0, length);
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
        int length = in.readInt();
        byte[] buffer = new byte[length];
        in.readFully(buffer);
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

    /** Byte array output stream for one feature, with its length in front */
    private static class FeatureOutputBuffer extends ByteArrayOutputStream {

        void startFeature() {
            if (buf.length > MAX_KEPT_BUFFER_SIZE) {
                buf = new byte[Integer.BYTES];
            }
            reset();
            count = Integer.BYTES;
        }

        void writeFeature(RandomAccessFile raf) throws IOException {
            ByteBuffer.wrap(buf).putInt(0, count - Integer.BYTES);
            raf.write(buf, 0, count);
        }
    }

    /** Byte array input stream for one feature read from the file */
    private static class FeatureInputBuffer extends ByteArrayInputStream {

        FeatureInputBuffer() {
            super(new byte[Integer.BYTES]);
        }

        void readFeature(RandomAccessFile raf) throws IOException {
            if (buf.length > MAX_KEPT_BUFFER_SIZE) {
                buf = new byte[Integer.BYTES];
            }
            raf.readFully(buf, 0, Integer.BYTES);
            int length = ByteBuffer.wrap(buf).getInt(0);
            if (buf.length < length) {
                buf = new byte[length];
            }
            raf.readFully(buf, 0, length);
            pos = 0;
            count = length;
            mark = 0;
        }
    }
}
