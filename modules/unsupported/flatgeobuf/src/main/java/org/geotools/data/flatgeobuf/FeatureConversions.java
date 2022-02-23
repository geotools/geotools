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
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.NIOUtilities;
import org.opengis.feature.simple.SimpleFeature;
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
                StandardCharsets.UTF_8
                        .newEncoder()
                        .onMalformedInput(REPLACE)
                        .onUnmappableCharacter(REPLACE);

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
            SimpleFeature feature,
            HeaderMeta headerMeta,
            final OutputStream to,
            FlatBufferBuilder builder)
            throws IOException {

        final int propertiesOffset = createProperiesVector(feature, builder, headerMeta);
        org.locationtech.jts.geom.Geometry geometry =
                (org.locationtech.jts.geom.Geometry) feature.getDefaultGeometry();
        final int geometryOffset =
                geometry == null
                        ? 0
                        : GeometryConversions.serialize(builder, geometry, headerMeta.geometryType);
        int featureOffset = Feature.createFeature(builder, geometryOffset, propertiesOffset, 0);
        builder.finishSizePrefixed(featureOffset);

        // Closing is caller responsibility
        @SuppressWarnings("PMD.CloseResource")
        WritableByteChannel channel = Channels.newChannel(to);
        ByteBuffer dataBuffer = builder.dataBuffer();
        while (dataBuffer.hasRemaining()) {
            channel.write(dataBuffer);
        }
    }

    protected static int buildGeometry(
            SimpleFeature feature, FlatBufferBuilder builder, byte geometryType)
            throws IOException {

        org.locationtech.jts.geom.Geometry geometry =
                (org.locationtech.jts.geom.Geometry) feature.getDefaultGeometry();

        int geometryOffset = GeometryConversions.serialize(builder, geometry, geometryType);

        return geometryOffset;
    }

    /** Writes the properties vector to {@code builder} and returns its offset */
    private static int createProperiesVector(
            SimpleFeature feature, FlatBufferBuilder builder, HeaderMeta headerMeta) {

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
        throw new IllegalStateException(
                "Unable to write properties vector of feature "
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
    private static void buildPropertiesVector(
            SimpleFeature feature, HeaderMeta headerMeta, ByteBuffer target) {

        target.order(ByteOrder.LITTLE_ENDIAN);
        for (short i = 0; i < headerMeta.columns.size(); i++) {
            ColumnMeta column = headerMeta.columns.get(i);
            byte type = column.type;
            Object value = feature.getAttribute(column.name);
            if (value == null) {
                continue;
            }
            target.putShort(i);
            if (type == ColumnType.Bool) {
                target.put((byte) ((boolean) value ? 1 : 0));
            } else if (type == ColumnType.Byte) {
                target.put((byte) value);
            } else if (type == ColumnType.Short) {
                target.putShort((short) value);
            } else if (type == ColumnType.Int) {
                target.putInt((int) value);
            } else if (type == ColumnType.Long)
                if (value instanceof Long) {
                    target.putLong((long) value);
                } else if (value instanceof BigInteger) {
                    target.putLong(((BigInteger) value).longValue());
                } else {
                    target.putLong((long) value);
                }
            else if (type == ColumnType.Double)
                if (value instanceof Double) {
                    target.putDouble((double) value);
                } else if (value instanceof BigDecimal) {
                    target.putDouble(((BigDecimal) value).doubleValue());
                } else {
                    target.putDouble((double) value);
                }
            else if (type == ColumnType.DateTime) {
                String isoDateTime = "";
                if (value instanceof LocalDateTime) {
                    isoDateTime = ((LocalDateTime) value).toString();
                } else if (value instanceof LocalDate) {
                    isoDateTime = ((LocalDate) value).toString();
                } else if (value instanceof LocalTime) {
                    isoDateTime = ((LocalTime) value).toString();
                } else if (value instanceof OffsetDateTime) {
                    isoDateTime = ((OffsetDateTime) value).toString();
                } else if (value instanceof OffsetTime) {
                    isoDateTime = ((OffsetTime) value).toString();
                } else if (value instanceof java.sql.Date) {
                    isoDateTime = ((java.sql.Date) value).toString();
                } else if (value instanceof java.sql.Time) {
                    isoDateTime = ((java.sql.Time) value).toString();
                } else if (value instanceof java.sql.Timestamp) {
                    isoDateTime = ((java.sql.Timestamp) value).toString();
                } else {
                    throw new RuntimeException(
                            "Cannot handle datetime type " + value.getClass().getName());
                }
                writeString(target, isoDateTime);
            } else if (type == ColumnType.String) {
                writeString(target, (String) value);
            } else {
                throw new RuntimeException("Cannot handle type " + value.getClass().getName());
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
            Feature feature, SimpleFeatureBuilder fb, HeaderMeta headerMeta, long fid) {
        Geometry geometry = feature.geometry();
        byte geometryType = headerMeta.geometryType;
        if (geometry != null) {
            if (geometryType == GeometryType.Unknown) {
                geometryType = (byte) geometry.type();
            }
            org.locationtech.jts.geom.Geometry jtsGeometry =
                    GeometryConversions.deserialize(geometry, geometryType);
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
                if (type == ColumnType.Bool) fb.set(name, bb.get() > 0 ? true : false);
                else if (type == ColumnType.Byte) fb.set(name, bb.get());
                else if (type == ColumnType.Short) fb.set(name, bb.getShort());
                else if (type == ColumnType.Int) fb.set(name, bb.getInt());
                else if (type == ColumnType.Long) fb.set(name, bb.getLong());
                else if (type == ColumnType.Double) fb.set(name, bb.getDouble());
                else if (type == ColumnType.DateTime) fb.set(name, readString(bb, name));
                else if (type == ColumnType.String) fb.set(name, readString(bb, name));
                else throw new RuntimeException("Unknown type");
            }
        }
        SimpleFeature f = fb.buildFeature(fb.getFeatureType().getTypeName() + "." + fid);
        return f;
    }
}
