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
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.geotools.FeatureConversions;
import org.wololo.flatgeobuf.geotools.FeatureTypeConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

public class FlatgeobufReader {

    private final InputStream inputStream;

    private int offset = 0;

    private ByteBuffer byteBuffer;

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

    public SimpleFeatureType getFeatureType() throws IOException {
        if (featureType == null) {
            ByteBuffer bb = getByteBuffer();
            this.headerMeta = FeatureTypeConversions.deserialize(bb, name, geometryPropertyName);
            this.offset = this.headerMeta.offset;
            this.featureType = this.headerMeta.featureType;
        }
        return this.featureType;
    }

    private int featureId = 1;

    public SimpleFeature getNextFeature() throws IOException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(getFeatureType());
        ByteBuffer bb = getByteBuffer();
        if (bb.hasRemaining()) {
            int featureSize = ByteBufferUtil.getSizePrefix(bb);
            bb.position(offset += SIZE_PREFIX_LENGTH);
            Feature feature = Feature.getRootAsFeature(bb);
            bb.position(offset += featureSize);
            SimpleFeature f =
                    FeatureConversions.deserialize(
                            feature, fb, headerMeta, String.valueOf(featureId++));
            return f;
        } else {
            return null;
        }
    }
}
