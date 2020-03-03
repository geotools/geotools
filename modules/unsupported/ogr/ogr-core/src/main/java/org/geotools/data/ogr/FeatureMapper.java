/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Maps OGR features into Geotools ones, and vice versa. Chances are that if you need to update a
 * decode method a simmetric modification will be needed in the encode method. This class is not
 * thread safe, so each thread should create its own instance.
 *
 * @author Andrea Aime - OpenGeo
 */
class FeatureMapper {

    SimpleFeatureBuilder builder;

    SimpleFeatureType schema;

    GeometryMapper geomMapper;

    GeometryFactory geomFactory;

    /** The date time format used by OGR when getting/setting times using strings */
    DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    HashMap<String, Integer> attributeIndexes;

    OGR ogr;

    /**
     * TODO: this is subscepitble to changes to the Locale in Java that might not affect the C
     * code... we should probably figure out a way to get the OS level locale?
     */
    static final DecimalFormatSymbols DECIMAL_SYMBOLS = new DecimalFormatSymbols();

    public FeatureMapper(
            SimpleFeatureType targetSchema, Object layer, GeometryFactory geomFactory, OGR ogr) {
        this.schema = targetSchema;
        this.builder = new SimpleFeatureBuilder(schema);
        this.geomMapper = new GeometryMapper.WKB(geomFactory, ogr);
        this.geomFactory = geomFactory;
        this.ogr = ogr;

        attributeIndexes = new HashMap<String, Integer>();
        Object layerDefinition = ogr.LayerGetLayerDefn(layer);
        int size = ogr.LayerGetFieldCount(layerDefinition);
        for (int i = 0; i < size; i++) {
            Object field = ogr.LayerGetFieldDefn(layerDefinition, i);
            String name = ogr.FieldGetName(field);
            if (targetSchema.getDescriptor(name) != null) {
                attributeIndexes.put(name, i);
            }
        }
    }

    /** Converts an OGR feature into a GeoTools one */
    SimpleFeature convertOgrFeature(Object ogrFeature) throws IOException {
        // Extract all attributes (do not assume any specific order, the feature
        // type may have been re-ordered by the Query)
        Object[] attributes = new Object[schema.getAttributeCount()];

        // .. then extract each attribute using the attribute type to determine
        // which extraction method to call
        for (int i = 0; i < attributes.length; i++) {
            AttributeDescriptor at = schema.getDescriptor(i);
            builder.add(getOgrField(at, ogrFeature));
        }

        // .. gather the FID
        String fid = convertOGRFID(schema, ogrFeature);

        // .. finally create the feature
        return builder.buildFeature(fid);
    }

    /** Turns a GeoTools feature into an OGR one */
    Object convertGTFeature(Object featureDefinition, SimpleFeature feature) throws IOException {
        // create a new empty OGR feature
        Object ogrFeature = ogr.LayerNewFeature(featureDefinition);

        // go thru GeoTools feature attributes, and convert
        SimpleFeatureType schema = feature.getFeatureType();
        for (int i = 0, j = 0; i < schema.getAttributeCount(); i++) {
            Object attribute = feature.getAttribute(i);
            if (attribute instanceof Geometry) {
                // using setGeoemtryDirectly the feature becomes the owner of the generated
                // OGR geometry and we don't have to .delete() it (it's faster, too)
                Object geometry = geomMapper.parseGTGeometry((Geometry) attribute);
                ogr.FeatureSetGeometryDirectly(ogrFeature, geometry);
            } else {
                setFieldValue(featureDefinition, ogrFeature, j, attribute, ogr);
                j++;
            }
        }

        return ogrFeature;
    }

    static void setFieldValue(
            Object featureDefinition, Object ogrFeature, int fieldIdx, Object value, OGR ogr)
            throws IOException {
        if (value == null) {
            ogr.FeatureUnsetField(ogrFeature, fieldIdx);
        } else {
            Object fieldDefinition = ogr.LayerGetFieldDefn(featureDefinition, fieldIdx);
            long ogrType = ogr.FieldGetType(fieldDefinition);
            if (ogr.FieldIsIntegerType(ogrType)) {
                ogr.FeatureSetFieldInteger(ogrFeature, fieldIdx, ((Number) value).intValue());
            } else if (ogr.FieldIsRealType(ogrType)) {
                ogr.FeatureSetFieldDouble(ogrFeature, fieldIdx, ((Number) value).doubleValue());
            } else if (ogr.FieldIsBinaryType(ogrType)) {
                byte[] attValue = (byte[]) value;
                ogr.FeatureSetFieldBinary(ogrFeature, fieldIdx, attValue.length, attValue);
            } else if (ogr.FieldIsDateType(ogrType)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) value);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                ogr.FeatureSetFieldDateTime(ogrFeature, fieldIdx, year, month, day, 0, 0, 0, 0);
            } else if (ogr.FieldIsTimeType(ogrType)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) value);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                ogr.FeatureSetFieldDateTime(ogrFeature, fieldIdx, 0, 0, 0, hour, minute, second, 0);
            } else if (ogr.FieldIsDateTimeType(ogrType)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) value);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                ogr.FeatureSetFieldDateTime(
                        ogrFeature, fieldIdx, year, month, day, hour, minute, second, 0);
            } else {
                // anything else we treat as a string
                String str = Converters.convert(value, String.class);
                ogr.FeatureSetFieldString(ogrFeature, fieldIdx, str);
            }
        }
    }

    /**
     * Turns line and polygon into multiline and multipolygon. This is a stop-gap measure to make
     * things works against shapefiles, I've asked the GDAL mailing list on how to properly handle
     * this in the meantime
     */
    Geometry fixGeometryType(Geometry ogrGeometry, AttributeDescriptor ad) {
        if (MultiPolygon.class.equals(ad.getType().getBinding())) {
            if (ogrGeometry instanceof MultiPolygon) return ogrGeometry;
            else return geomFactory.createMultiPolygon(new Polygon[] {(Polygon) ogrGeometry});
        } else if (MultiLineString.class.equals(ad.getType().getBinding())) {
            if (ogrGeometry instanceof MultiLineString) return ogrGeometry;
            else
                return geomFactory.createMultiLineString(
                        new LineString[] {(LineString) ogrGeometry});
        }
        return ogrGeometry;
    }

    /**
     * Reads the current feature's specified field using the most appropriate OGR field extraction
     * method
     */
    Object getOgrField(AttributeDescriptor ad, Object ogrFeature) throws IOException {
        if (ad instanceof GeometryDescriptor) {
            // gets the geometry as a reference, we don't own it, we should not deallocate it
            Object ogrGeometry = ogr.FeatureGetGeometry(ogrFeature);
            return fixGeometryType(geomMapper.parseOgrGeometry(ogrGeometry), ad);
        }

        Integer idx = attributeIndexes.get(ad.getLocalName());

        // check for null fields
        if (idx == null || !ogr.FeatureIsFieldSet(ogrFeature, idx)) {
            return null;
        }

        // hum, ok try and parse it
        Class clazz = ad.getType().getBinding();
        if (clazz.equals(String.class)) {
            return ogr.FeatureGetFieldAsString(ogrFeature, idx);
        } else if (clazz.equals(Byte.class)) {
            return (byte) ogr.FeatureGetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Short.class)) {
            // return (short) OGR_F_GetFieldAsInteger(ogrFeature, idx);
            return (short) ogr.FeatureGetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Integer.class)) {
            return ogr.FeatureGetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Long.class)) {
            String value = ogr.FeatureGetFieldAsString(ogrFeature, idx);
            return Long.valueOf(value);
        } else if (clazz.equals(BigInteger.class)) {
            String value = ogr.FeatureGetFieldAsString(ogrFeature, idx);
            return new BigInteger(value);
        } else if (clazz.equals(Double.class)) {
            return ogr.FeatureGetFieldAsDouble(ogrFeature, idx);
        } else if (clazz.equals(Float.class)) {
            return (float) ogr.FeatureGetFieldAsDouble(ogrFeature, idx);
        } else if (clazz.equals(BigDecimal.class)) {
            String value = ogr.FeatureGetFieldAsString(ogrFeature, idx).trim();
            char separator = DECIMAL_SYMBOLS.getDecimalSeparator();
            if (separator != '.') {
                value = value.replace(separator, '.');
            }
            return new BigDecimal(value);
        } else if (clazz.equals(java.sql.Date.class)) {
            Calendar cal = getDateField(ogrFeature, idx);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            return new java.sql.Date(cal.getTimeInMillis());
        } else if (clazz.equals(java.sql.Time.class)) {
            Calendar cal = getDateField(ogrFeature, idx);
            cal.clear(Calendar.YEAR);
            cal.clear(Calendar.MONTH);
            cal.clear(Calendar.DAY_OF_MONTH);
            return new java.sql.Time(cal.getTimeInMillis());
        } else if (clazz.equals(java.sql.Timestamp.class)) {
            Calendar cal = getDateField(ogrFeature, idx);
            return new java.sql.Time(cal.getTimeInMillis());
        } else if (clazz.equals(java.util.Date.class)) {
            Calendar cal = getDateField(ogrFeature, idx);
            return cal.getTime();
        } else {
            throw new IllegalArgumentException(
                    "Don't know how to read " + clazz.getName() + " fields");
        }
    }

    /** Reads a date field from the OGR api */
    private Calendar getDateField(Object ogrFeature, Integer idx) {
        int[] year = new int[1];
        int[] month = new int[1];
        int[] day = new int[1];
        int[] hour = new int[1];
        int[] minute = new int[1];
        int[] second = new int[1];
        int[] timeZone = new int[1];

        ogr.FeatureGetFieldAsDateTime(
                ogrFeature, idx, year, month, day, hour, minute, second, timeZone);

        Calendar cal = Calendar.getInstance();
        // from ogr_core.h
        // 0=unknown, 1=localtime(ambiguous), 100=GMT, 104=GMT+1, 80=GMT-5, etc
        int tz = timeZone[0];
        if (tz != 0 && tz != 1) {
            int offset = tz - 100 / 4;
            if (offset < 0) {
                cal.setTimeZone(TimeZone.getTimeZone("GMT" + offset));
            } else if (offset == 0) {
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            } else {
                cal.setTimeZone(TimeZone.getTimeZone("GMT+" + offset));
            }
        }
        cal.clear();
        cal.set(Calendar.YEAR, year[0]);
        cal.set(Calendar.MONTH, month[0]);
        cal.set(Calendar.DAY_OF_MONTH, day[0]);
        cal.set(Calendar.HOUR_OF_DAY, hour[0]);
        cal.set(Calendar.MINUTE, minute[0]);
        cal.set(Calendar.SECOND, second[0]);
        return cal;
    }

    /** Generates a GT2 feature id given its feature type and an OGR feature */
    String convertOGRFID(SimpleFeatureType schema, Object ogrFeature) {
        long id = ogr.FeatureGetFID(ogrFeature);
        return schema.getTypeName() + "." + id;
    }

    /** Decodes a GT2 feature id into an OGR one */
    long convertGTFID(SimpleFeature feature) {
        String id = feature.getID();
        return Long.parseLong(id.substring(id.indexOf(".") + 1));
    }
}
