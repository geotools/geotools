/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import static java.nio.charset.CodingErrorAction.REPLACE;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_TIME;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.flatbuffers.FlatBufferBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.NIOUtilities;
import org.wololo.flatgeobuf.ColumnMeta;
import org.wololo.flatgeobuf.GeometryConversions;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.generated.Geometry;
import org.wololo.flatgeobuf.generated.GeometryType;

public class FeatureConversions {

    private static void writeString(ByteBuffer target, String value) {

        CharsetEncoder encoder =
                StandardCharsets.UTF_8.newEncoder().onMalformedInput(REPLACE).onUnmappableCharacter(REPLACE);

        // save current position to write the string length later
        final int lengthPosition = target.position();
        // and leave room for it
        target.position(lengthPosition + Integer.BYTES);

        final int startStrPos = target.position();
        final boolean endOfInput = true;
        encoder.encode(CharBuffer.wrap(value), target, endOfInput);

        final int endStrPos = target.position();
        final int encodedLength = endStrPos - startStrPos;

        // absolute put, doesn't change the current position
        target.putInt(lengthPosition, encodedLength);
    }

    public static void serialize(
            SimpleFeature feature, HeaderMeta headerMeta, final OutputStream to, FlatBufferBuilder builder)
            throws IOException {

        final int propertiesOffset = createProperiesVector(feature, builder, headerMeta);
        org.locationtech.jts.geom.Geometry geometry = (org.locationtech.jts.geom.Geometry) feature.getDefaultGeometry();
        final int geometryOffset =
                geometry == null ? 0 : GeometryConversions.serialize(builder, geometry, headerMeta.geometryType);
        int featureOffset = Feature.createFeature(builder, geometryOffset, propertiesOffset, 0);
        builder.finishSizePrefixed(featureOffset);

        // Closing is caller responsibility
        WritableByteChannel channel = Channels.newChannel(to);
        ByteBuffer dataBuffer = builder.dataBuffer();
        while (dataBuffer.hasRemaining()) channel.write(dataBuffer);
    }

    protected static int buildGeometry(SimpleFeature feature, FlatBufferBuilder builder, byte geometryType)
            throws IOException {

        org.locationtech.jts.geom.Geometry geometry = (org.locationtech.jts.geom.Geometry) feature.getDefaultGeometry();

        int geometryOffset = GeometryConversions.serialize(builder, geometry, geometryType);

        return geometryOffset;
    }

    /** Writes the properties vector to {@code builder} and returns its offset */
    private static int createProperiesVector(SimpleFeature feature, FlatBufferBuilder builder, HeaderMeta headerMeta) {

        final int minPow = 16; // 2^16 = 64KiB
        final int maxPow = 20; // 2^20 = 1MiB
        int size = 0;
        for (int pow = minPow; pow <= maxPow; pow += 2) {
            size = (int) Math.pow(2, pow);
            ByteBuffer bb = NIOUtilities.allocate(size);
            try {
                buildPropertiesVector(feature, headerMeta, bb);
            } catch (BufferOverflowException overflow) {
                // return buffer and retry with a bigger one
                NIOUtilities.returnToCache(bb);
                continue;
            }

            int propertiesOffset = 0;
            if (bb.position() > 0) {
                bb.flip();
                propertiesOffset = Feature.createPropertiesVector(builder, bb);
            }
            NIOUtilities.returnToCache(bb);
            return propertiesOffset;
        }
        throw new IllegalStateException("Unable to write properties vector of feature "
                + feature.getID()
                + ". Buffer overflowed at maximum capacity of "
                + size
                + " bytes");
    }

    /**
     * Builds the {@code feature} properties vector onto the {@code target} byte buffer, throws
     * {@link BufferOverflowException} if {@code target} is too small
     *
     * @param feature the feature whose properties to encode
     * @param headerMeta feature type metadata
     * @param target the buffer where to encode the feature properties vector
     * @throws BufferOverflowException if {@code target} couldn't hold the encoded properties
     */
    private static void buildPropertiesVector(SimpleFeature feature, HeaderMeta headerMeta, ByteBuffer target) {

        target.order(ByteOrder.LITTLE_ENDIAN);
        for (short i = 0; i < headerMeta.columns.size(); i++) {
            ColumnMeta column = headerMeta.columns.get(i);
            byte type = column.type;
            Object value = feature.getAttribute(column.name);
            if (value == null) continue;
            target.putShort(i);
            switch (type) {
                case ColumnType.Bool -> target.put((byte) ((boolean) value ? 1 : 0));
                case ColumnType.Byte -> target.put((byte) value);
                case ColumnType.Short -> target.putShort((short) value);
                case ColumnType.Int -> target.putInt((int) value);
                case ColumnType.Long -> {
                    if (value instanceof Long long1) {
                        target.putLong(long1);
                    } else if (value instanceof BigInteger integer) {
                        target.putLong(integer.longValue());
                    } else {
                        target.putLong((long) value);
                    }
                }
                case ColumnType.Double -> {
                    if (value instanceof Double double1) {
                        target.putDouble(double1);
                    } else if (value instanceof BigDecimal bigDecimal) {
                        target.putDouble(bigDecimal.doubleValue());
                    } else {
                        target.putDouble((double) value);
                    }
                }
                case ColumnType.DateTime -> {
                    String isoDateTime = "";
                    if (value instanceof LocalDateTime localDateTime) {
                        isoDateTime = ISO_LOCAL_DATE_TIME.format(localDateTime);
                    } else if (value instanceof LocalDate localDate) {
                        isoDateTime = ISO_LOCAL_DATE.format(localDate);
                    } else if (value instanceof LocalTime localTime) {
                        isoDateTime = ISO_LOCAL_TIME.format(localTime);
                    } else if (value instanceof OffsetDateTime offsetDateTime) {
                        isoDateTime = ISO_OFFSET_DATE_TIME.format(offsetDateTime);
                    } else if (value instanceof OffsetTime offsetTime) {
                        isoDateTime = ISO_OFFSET_TIME.format(offsetTime);
                    } else if (value instanceof java.sql.Date date) {
                        isoDateTime = ISO_LOCAL_DATE.format(date.toLocalDate());
                    } else if (value instanceof java.sql.Time time) {
                        isoDateTime = ISO_LOCAL_TIME.format(time.toLocalTime());
                    } else if (value instanceof java.sql.Timestamp timestamp) {
                        isoDateTime = ISO_LOCAL_DATE_TIME.format(timestamp.toLocalDateTime());
                    } else if (value instanceof java.util.Date date) {
                        isoDateTime = ISO_INSTANT.format(date.toInstant());
                    } else {
                        throw new RuntimeException("Cannot handle datetime type "
                                + value.getClass().getName());
                    }
                    writeString(target, isoDateTime);
                }

                case ColumnType.String -> writeString(target, (String) value);
                default ->
                    throw new RuntimeException(
                            "Cannot handle type " + value.getClass().getName());
            }
        }
    }

    private static String readString(ByteBuffer bb, String name) {
        int length = bb.getInt();
        byte[] stringBytes = new byte[length];
        bb.get(stringBytes, 0, length);
        String value = new String(stringBytes, StandardCharsets.UTF_8);
        return value;
    }

    public static SimpleFeature deserialize(
            LittleEndianDataInputStream data, SimpleFeatureBuilder fb, HeaderMeta headerMeta, long fid)
            throws IOException {
        int featureSize = data.readInt();
        SimpleFeature feature = deserialize(data, fb, headerMeta, fid, featureSize);
        return feature;
    }

    public static SimpleFeature deserialize(
            LittleEndianDataInputStream data, SimpleFeatureBuilder fb, HeaderMeta headerMeta, long fid, int featureSize)
            throws IOException {
        byte[] bytes = new byte[featureSize];
        data.readFully(bytes);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        Feature f = Feature.getRootAsFeature(bb);
        SimpleFeature feature = FeatureConversions.deserialize(f, fb, headerMeta, fid);
        return feature;
    }

    public static SimpleFeature deserialize(Feature feature, SimpleFeatureBuilder fb, HeaderMeta headerMeta, long fid) {
        Geometry geometry = feature.geometry();
        byte geometryType = headerMeta.geometryType;
        if (geometry != null) {
            if (geometryType == GeometryType.Unknown) geometryType = (byte) geometry.type();
            org.locationtech.jts.geom.Geometry jtsGeometry = GeometryConversions.deserialize(geometry, geometryType);
            fb.add(jtsGeometry);
        }
        int propertiesLength = feature.propertiesLength();
        if (propertiesLength > 0) {
            ByteBuffer bb = feature.propertiesAsByteBuffer();
            while (bb.hasRemaining()) {
                short i = bb.getShort();
                ColumnMeta columnMeta = headerMeta.columns.get(i);
                String name = columnMeta.name;
                byte type = columnMeta.type;
                switch (type) {
                    case ColumnType.Bool -> fb.set(name, (bb.get() > 0));
                    case ColumnType.Byte -> fb.set(name, bb.get());
                    case ColumnType.Short -> fb.set(name, bb.getShort());
                    case ColumnType.Int -> fb.set(name, bb.getInt());
                    case ColumnType.Long -> fb.set(name, bb.getLong());
                    case ColumnType.Double -> fb.set(name, bb.getDouble());
                    case ColumnType.DateTime -> fb.set(name, readString(bb, name));
                    case ColumnType.String -> fb.set(name, readString(bb, name));
                    default -> throw new RuntimeException("Unknown type");
                }
            }
        }
        SimpleFeature f = fb.buildFeature(fb.getFeatureType().getTypeName() + "." + fid);
        return f;
    }
}
