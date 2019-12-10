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

import com.google.flatbuffers.FlatBufferBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.wololo.flatgeobuf.generated.Column;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.GeometryType;
import org.wololo.flatgeobuf.generated.Header;
import org.wololo.flatgeobuf.geotools.ColumnMeta;
import org.wololo.flatgeobuf.geotools.FeatureConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

public class FlatgeobufWriter {

    private final OutputStream outputStream;

    private HeaderMeta headerMeta;

    public FlatgeobufWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeFeature(SimpleFeature feature) throws IOException {
        byte[] bytes =
                FeatureConversions.serialize(feature, getHeaderMeta(feature.getFeatureType()));
        this.outputStream.write(bytes);
    }

    private HeaderMeta getHeaderMeta(SimpleFeatureType featureType) throws IOException {
        if (this.headerMeta == null) {
            List<AttributeDescriptor> types = featureType.getAttributeDescriptors();
            List<ColumnMeta> columns = new ArrayList<ColumnMeta>();

            for (int i = 0; i < types.size(); i++) {
                AttributeDescriptor ad = types.get(i);
                if (!(ad instanceof GeometryDescriptor)) {
                    String key = ad.getLocalName();
                    Class<?> binding = ad.getType().getBinding();
                    ColumnMeta column = new ColumnMeta();
                    column.name = key;
                    if (binding.isAssignableFrom(Boolean.class)) column.type = ColumnType.Bool;
                    else if (binding.isAssignableFrom(Integer.class)) column.type = ColumnType.Int;
                    else if (binding.isAssignableFrom(Long.class)) column.type = ColumnType.Long;
                    else if (binding.isAssignableFrom(Double.class))
                        column.type = ColumnType.Double;
                    else if (binding.isAssignableFrom(String.class))
                        column.type = ColumnType.String;
                    else throw new IOException("Unknown type");
                    columns.add(column);
                }
            }

            byte geometryType =
                    toGeometryType(featureType.getGeometryDescriptor().getType().getBinding());

            this.headerMeta = new HeaderMeta();
            headerMeta.featuresCount = 0;
            headerMeta.geometryType = geometryType;
            headerMeta.columns = columns;
        }
        return this.headerMeta;
    }

    public void writeFeatureType(SimpleFeatureType featureType) throws IOException {
        outputStream.write(Flatgeobuf.MAGIC_BYTES);
        byte[] headerBuffer = buildHeader(getHeaderMeta(featureType));
        outputStream.write(headerBuffer);
    }

    private byte toGeometryType(Class<?> geometryClass) throws IOException {
        if (geometryClass.isAssignableFrom(MultiPoint.class)) return GeometryType.MultiPoint;
        else if (geometryClass.isAssignableFrom(Point.class)) return GeometryType.Point;
        else if (geometryClass.isAssignableFrom(MultiLineString.class))
            return GeometryType.MultiLineString;
        else if (geometryClass.isAssignableFrom(LineString.class)) return GeometryType.LineString;
        else if (geometryClass.isAssignableFrom(MultiPolygon.class))
            return GeometryType.MultiPolygon;
        else if (geometryClass.isAssignableFrom(Polygon.class)) return GeometryType.Polygon;
        else throw new IOException("Unknown geometry type");
    }

    private byte[] buildHeader(HeaderMeta headerMeta) {
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);

        int[] columnsArray =
                headerMeta
                        .columns
                        .stream()
                        .mapToInt(
                                c -> {
                                    int nameOffset = builder.createString(c.name);
                                    int type = c.type;
                                    return Column.createColumn(builder, nameOffset, type);
                                })
                        .toArray();
        int columnsOffset = Header.createColumnsVector(builder, columnsArray);

        Header.startHeader(builder);
        Header.addGeometryType(builder, headerMeta.geometryType);
        Header.addIndexNodeSize(builder, 0);
        Header.addColumns(builder, columnsOffset);
        Header.addFeaturesCount(builder, headerMeta.featuresCount);
        int offset = Header.endHeader(builder);

        builder.finishSizePrefixed(offset);

        return builder.sizedByteArray();
    }
}
