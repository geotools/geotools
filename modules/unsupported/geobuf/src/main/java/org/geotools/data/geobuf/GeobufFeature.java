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
package org.geotools.data.geobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * GeobufFeature encodes and decodes SimpleFeatures
 *
 * @author Jared Erickson
 */
public class GeobufFeature {

    private GeobufGeometry geobufGeometry;

    public GeobufFeature() {
        this(new GeobufGeometry());
    }

    public GeobufFeature(GeobufGeometry geobufGeometry) {
        this.geobufGeometry = geobufGeometry;
    }

    public GeobufGeometry getGeobufGeometry() {
        return this.geobufGeometry;
    }

    public void encode(SimpleFeature feature, OutputStream out) throws IOException {
        Geobuf.Data.Builder dataBuilder = Geobuf.Data.newBuilder();
        for (AttributeDescriptor descriptor : feature.getFeatureType().getAttributeDescriptors()) {
            if (!(descriptor instanceof GeometryDescriptor)) {
                dataBuilder.addKeys(descriptor.getLocalName());
            }
        }
        dataBuilder.setDimensions(geobufGeometry.getDimension());
        dataBuilder.setPrecision(geobufGeometry.getPrecision());
        dataBuilder.setFeature(encode(feature));
        dataBuilder.build().writeTo(out);
    }

    public SimpleFeature decode(InputStream in) throws IOException {
        Geobuf.Data data = Geobuf.Data.parseFrom(in);
        GeobufFeatureType geobufFeatureType = new GeobufFeatureType();
        return decode(
                data, new SimpleFeatureBuilder(geobufFeatureType.getFeatureType("features", data)));
    }

    protected Geobuf.Data.Feature encode(SimpleFeature feature) {
        Geobuf.Data.Feature.Builder featureBuilder = Geobuf.Data.Feature.newBuilder();
        int i = 0;
        for (AttributeDescriptor attributeDescriptor :
                feature.getFeatureType().getAttributeDescriptors()) {
            Object value = feature.getAttribute(attributeDescriptor.getName());
            if (attributeDescriptor instanceof GeometryDescriptor) {
                featureBuilder.setGeometry(
                        geobufGeometry.encode((Geometry) feature.getDefaultGeometry()));
            } else {
                featureBuilder.addValues(i, encodeValue(attributeDescriptor, value));
                i++;
            }
        }
        return featureBuilder.build();
    }

    protected Geobuf.Data.Value encodeValue(AttributeDescriptor attributeDescriptor, Object value) {
        Geobuf.Data.Value.Builder builder = Geobuf.Data.Value.newBuilder();
        if (value instanceof String) {
            builder.setStringValue((String) value);
        } else if (value instanceof Integer) {
            builder.setPosIntValue((Integer) value);
        } else if (value instanceof Double) {
            builder.setDoubleValue((Double) value);
        } else if (value instanceof Boolean) {
            builder.setBoolValue((Boolean) value);
        } else {
            builder.setStringValue(value != null ? value.toString() : null);
        }
        return builder.build();
    }

    protected SimpleFeature decode(Geobuf.Data data) throws IOException {
        GeobufFeatureType geobufFeatureType = new GeobufFeatureType();
        return decode(
                data,
                0,
                new SimpleFeatureBuilder(geobufFeatureType.getFeatureType("features", data)));
    }

    protected SimpleFeature decode(Geobuf.Data data, SimpleFeatureBuilder featureBuilder) {
        return decode(data, 0, featureBuilder);
    }

    protected SimpleFeature decode(
            Geobuf.Data data, int index, SimpleFeatureBuilder featureBuilder) {
        if (data.getDataTypeCase() == Geobuf.Data.DataTypeCase.GEOMETRY) {
            if (index > 0) {
                return null;
            }
            featureBuilder.set(
                    featureBuilder.getFeatureType().getGeometryDescriptor().getLocalName(),
                    geobufGeometry.decode(data.getGeometry()));

        } else if (data.getDataTypeCase() == Geobuf.Data.DataTypeCase.FEATURE) {
            if (index > 0) {
                return null;
            }
            featureBuilder.set(
                    featureBuilder.getFeatureType().getGeometryDescriptor().getLocalName(),
                    geobufGeometry.decode(data.getFeature().getGeometry()));
            int keyCount = data.getKeysCount();
            for (int j = 0; j < keyCount; j++) {
                String key = data.getKeys(j);
                Object value = getValue(data.getFeature().getValues(j));
                featureBuilder.set(key, value);
            }
        } else if (data.getDataTypeCase() == Geobuf.Data.DataTypeCase.FEATURE_COLLECTION) {
            if (index >= data.getFeatureCollection().getFeaturesCount()) {
                return null;
            }
            Geobuf.Data.Feature feature = data.getFeatureCollection().getFeatures(index);
            featureBuilder.set(
                    featureBuilder.getFeatureType().getGeometryDescriptor().getLocalName(),
                    geobufGeometry.decode(feature.getGeometry()));
            int keyCount = data.getKeysCount();
            for (int j = 0; j < keyCount; j++) {
                String key = data.getKeys(j);
                Object value = feature.getValuesCount() > j ? getValue(feature.getValues(j)) : null;
                featureBuilder.set(key, value);
            }
        }
        return featureBuilder.buildFeature(String.valueOf(index));
    }

    protected Object getValue(Geobuf.Data.Value value) {
        if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.STRING_VALUE) {
            return value.getStringValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.POS_INT_VALUE) {
            return value.getPosIntValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.NEG_INT_VALUE) {
            return value.getNegIntValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.DOUBLE_VALUE) {
            return value.getDoubleValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.BOOL_VALUE) {
            return value.getBoolValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.JSON_VALUE) {
            return value.getJsonValue();
        } else {
            return null;
        }
    }
}
