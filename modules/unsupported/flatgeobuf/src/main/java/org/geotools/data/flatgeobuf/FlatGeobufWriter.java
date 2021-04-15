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

import com.google.flatbuffers.FlatBufferBuilder;
import java.io.IOException;
import java.io.OutputStream;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.flatgeobuf.Constants;
import org.wololo.flatgeobuf.HeaderMeta;

public class FlatGeobufWriter {

    private final OutputStream outputStream;
    private final FlatBufferBuilder builder;

    private HeaderMeta headerMeta;

    public FlatGeobufWriter(OutputStream outputStream, FlatBufferBuilder builder) {
        this.outputStream = outputStream;
        this.builder = builder;
    }

    public void writeFeature(SimpleFeature feature) throws IOException {
        FeatureConversions.serialize(feature, this.headerMeta, this.outputStream, this.builder);
    }

    public void writeFeatureType(SimpleFeatureType featureType) throws IOException {
        outputStream.write(Constants.MAGIC_BYTES);
        headerMeta = HeaderMetaUtil.fromFeatureType(featureType, 0);
        HeaderMeta.write(headerMeta, outputStream, builder);
    }
}
