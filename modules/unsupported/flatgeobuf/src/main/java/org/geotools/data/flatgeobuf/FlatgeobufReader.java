/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import static com.google.flatbuffers.Constants.SIZE_PREFIX_LENGTH;

import com.google.flatbuffers.ByteBufferUtil;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.generated.GeometryType;
import org.wololo.flatgeobuf.generated.Header;
import org.wololo.flatgeobuf.geotools.ColumnMeta;
import org.wololo.flatgeobuf.geotools.FeatureConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

public class FlatgeobufReader {

    private final InputStream inputStream;

    private int offset = 0;

    private ByteBuffer byteBuffer;

    private Header header;

    private SimpleFeatureType featureType;

    private HeaderMeta headerMeta;

    private String name;

    private String geometryPropertyName;

    public FlatgeobufReader(String name, String geometryPropertyName, InputStream inputStream) {
        this.name = name;
        this.geometryPropertyName = geometryPropertyName;
        this.inputStream = inputStream;
    }

    private ByteBuffer getByteBuffer() throws IOException {
        if (byteBuffer == null) {
            this.byteBuffer = toByteBuffer(inputStream);
        }
        return this.byteBuffer;
    }

    private static ByteBuffer toByteBuffer(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }

    private Header getHeader() throws IOException {
        if (header == null) {
            ByteBuffer bb = getByteBuffer();
            if (bb.get() != Flatgeobuf.MAGIC_BYTES[0]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[1]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[2]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[3]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[4]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[5]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[6]
                    || bb.get() != Flatgeobuf.MAGIC_BYTES[7])
                throw new IOException("Not a FlatGeobuf file");
            bb.position(offset += Flatgeobuf.MAGIC_BYTES.length);
            int headerSize = ByteBufferUtil.getSizePrefix(bb);
            bb.position(offset += SIZE_PREFIX_LENGTH);
            this.header = Header.getRootAsHeader(bb);
            bb.position(offset += headerSize);
        }
        return header;
    }

    public SimpleFeatureType getFeatureType() throws IOException {
        if (featureType == null) {
            Header header = getHeader();
            int geometryType = header.geometryType();
            Class<?> geometryClass;
            switch (geometryType) {
                case GeometryType.Point:
                    geometryClass = Point.class;
                    break;
                case GeometryType.MultiPoint:
                    geometryClass = MultiPoint.class;
                    break;
                case GeometryType.LineString:
                    geometryClass = LineString.class;
                    break;
                case GeometryType.MultiLineString:
                    geometryClass = MultiLineString.class;
                    break;
                case GeometryType.Polygon:
                    geometryClass = Polygon.class;
                    break;
                case GeometryType.MultiPolygon:
                    geometryClass = MultiPolygon.class;
                    break;
                default:
                    throw new IOException("Unknown geometry type");
            }

            int columnsLength = header.columnsLength();
            ArrayList<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();
            for (int i = 0; i < columnsLength; i++) {
                ColumnMeta columnMeta = new ColumnMeta();
                columnMeta.name = header.columns(i).name();
                columnMeta.type = (byte) header.columns(i).type();
                columnMetas.add(columnMeta);
            }

            this.headerMeta = new HeaderMeta();
            headerMeta.columns = columnMetas;
            headerMeta.geometryType = (byte) geometryType;

            SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
            // @TODO remove hard coded name and geometry property name
            ftb.setName(this.name);
            ftb.add(this.geometryPropertyName, geometryClass);
            for (ColumnMeta columnMeta : columnMetas) {
                ftb.add(columnMeta.name, columnMeta.getBinding());
            }
            this.featureType = ftb.buildFeatureType();
        }
        return this.featureType;
    }

    public SimpleFeature getNextFeature() throws IOException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(getFeatureType());
        ByteBuffer bb = getByteBuffer();
        if (bb.hasRemaining()) {
            int featureSize = ByteBufferUtil.getSizePrefix(bb);
            bb.position(offset += SIZE_PREFIX_LENGTH);
            Feature feature = Feature.getRootAsFeature(bb);
            bb.position(offset += featureSize);
            SimpleFeature f = FeatureConversions.deserialize(feature, fb, headerMeta);
            return f;
        } else {
            return null;
        }
    }
}
