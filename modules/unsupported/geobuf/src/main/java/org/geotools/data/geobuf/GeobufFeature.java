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
import org.locationtech.jts.geom.Geometry;
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
        featureBuilder.setId(feature.getID());
        int i = 0, n = 0;
        featureBuilder.setGeometry(geobufGeometry.encode((Geometry) feature.getDefaultGeometry()));
        for (AttributeDescriptor attributeDescriptor :
                feature.getFeatureType().getAttributeDescriptors()) {
            Object value = feature.getAttribute(attributeDescriptor.getName());
            if (!(attributeDescriptor instanceof GeometryDescriptor)) {
                Geobuf.Data.Value geobufDataValue = encodeValue(attributeDescriptor, value);
                if (geobufDataValue != null) {
                    featureBuilder.addValues(i, geobufDataValue);
                    featureBuilder.addProperties(n);
                    featureBuilder.addProperties(i);
                    i++;
                }
                n++;
            }
        }
        return featureBuilder.build();
    }

    protected Geobuf.Data.Value encodeValue(AttributeDescriptor attributeDescriptor, Object value) {
        Geobuf.Data.Value.Builder builder = Geobuf.Data.Value.newBuilder();
        if (value instanceof String) {
            builder.setStringValue((String) value);
        } else if (value instanceof Integer) {
            int val = (Integer) value;
            if (val >= 0) {
                builder.setPosIntValue(val);
            } else {
                builder.setNegIntValue(-val);
            }
        } else if (value instanceof Long) {
            long val = (Long) value;
            if (val >= 0) {
                builder.setPosIntValue(val);
            } else {
                builder.setNegIntValue(-val);
            }
        } else if (value instanceof Double) {
            builder.setDoubleValue((Double) value);
        } else if (value instanceof Boolean) {
            builder.setBoolValue((Boolean) value);
        } else {
            // cannot pass null here, will NPE
            if (value != null) {
                builder.setStringValue(value.toString());
            } else {
                return null;
            }
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
            return featureBuilder.buildFeature(String.valueOf(index));
        } else if (data.getDataTypeCase() == Geobuf.Data.DataTypeCase.FEATURE) {
            if (index > 0) {
                return null;
            }
            return decodeFeature(data, data.getFeature(), featureBuilder);
        } else if (data.getDataTypeCase() == Geobuf.Data.DataTypeCase.FEATURE_COLLECTION) {
            if (index >= data.getFeatureCollection().getFeaturesCount()) {
                return null;
            }
            Geobuf.Data.Feature feature = data.getFeatureCollection().getFeatures(index);
            return decodeFeature(data, feature, featureBuilder);
        } else {
            throw new IllegalStateException("unrecognized data_type: " + data.getDataTypeCase());
        }
    }

    protected SimpleFeature decodeFeature(
            Geobuf.Data data, Geobuf.Data.Feature feature, SimpleFeatureBuilder featureBuilder) {
        featureBuilder.set(
                featureBuilder.getFeatureType().getGeometryDescriptor().getLocalName(),
                geobufGeometry.decode(feature.getGeometry()));
        int propertiesCount = feature.getPropertiesCount();
        if (propertiesCount == 0 && feature.getValuesCount() > 0) {
            // Geobuf feature witten by legacy gt-geobuf
            int keyCount = data.getKeysCount();
            for (int j = 0; j < keyCount; j++) {
                String key = data.getKeys(j);
                Object value = feature.getValuesCount() > j ? getValue(feature.getValues(j)) : null;
                featureBuilder.set(key, value);
            }
        } else {
            if ((propertiesCount & 0x01) != 0) {
                throw new IllegalStateException(
                        "number of properties (pairs of key/value indexes) is odd");
            }
            int valueCount = propertiesCount / 2;
            for (int j = 0; j < valueCount; j++) {
                int attrOffset = feature.getProperties(j * 2);
                int valOffset = feature.getProperties(j * 2 + 1);
                Object value =
                        feature.getValuesCount() > valOffset
                                ? getValue(feature.getValues(valOffset))
                                : null;
                String key = data.getKeys(attrOffset);
                featureBuilder.set(key, value);
            }
        }
        return featureBuilder.buildFeature(feature.getId());
    }

    protected Object getValue(Geobuf.Data.Value value) {
        if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.STRING_VALUE) {
            return value.getStringValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.POS_INT_VALUE) {
            return value.getPosIntValue();
        } else if (value.getValueTypeCase() == Geobuf.Data.Value.ValueTypeCase.NEG_INT_VALUE) {
            return -value.getNegIntValue();
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
