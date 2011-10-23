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

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.bridj.Pointer;
import org.geotools.data.DataSourceException;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Maps OGR features into Geotools ones, and vice versa. Chances are that if you need to update a
 * decode method a simmetric modification will be needed in the encode method. This class is not
 * thread safe, so each thread should create its own instance.
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
class FeatureMapper {

    SimpleFeatureBuilder builder;
    
    SimpleFeatureType schema;
    
    GeometryMapper geomMapper;
    
    GeometryFactory geomFactory;

    /**
     * The date time format used by OGR when getting/setting times using strings
     */
    DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    HashMap<String, Integer> attributeIndexes;
    
    /**
     * TODO: this is subscepitble to changes to the Locale in Java that might not affect
     * the C code... we should probably figure out a way to get the OS level locale?
     */
    static final DecimalFormatSymbols DECIMAL_SYMBOLS = new DecimalFormatSymbols(); 

    public FeatureMapper(SimpleFeatureType targetSchema, Pointer layer, GeometryFactory geomFactory) {
        this.schema = targetSchema;
        this.builder = new SimpleFeatureBuilder(schema);
        this.geomMapper = new GeometryMapper.WKB(geomFactory);
        this.geomFactory = geomFactory;
        
        attributeIndexes = new HashMap<String, Integer>();
        Pointer layerDefinition = OGR_L_GetLayerDefn(layer);
        int size = OGR_FD_GetFieldCount(layerDefinition);
        for(int i = 0; i < size; i++) {
            Pointer  field = OGR_FD_GetFieldDefn(layerDefinition, i);
            Pointer<Byte> namePtr = OGR_Fld_GetNameRef(field);
            String name = namePtr.getCString();
            if(targetSchema.getDescriptor(name) != null) {
                attributeIndexes.put(name, i);
            }
        }
    }

    /**
     * Converts an OGR feature into a GeoTools one
     * 
     * @param schema
     * @param ogrFeature
     * @return
     * @throws IOException
     */
    SimpleFeature convertOgrFeature(Pointer<?> ogrFeature)
            throws IOException {
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

    /**
     * Turns a GeoTools feature into an OGR one
     * 
     * @param feature
     * @return
     * @throws DataSourceException
     */
    Pointer convertGTFeature(Pointer featureDefinition, SimpleFeature feature)
            throws IOException {
        // create a new empty OGR feature
        Pointer result = OGR_F_Create(featureDefinition);

        // go thru GeoTools feature attributes, and convert
        SimpleFeatureType schema = feature.getFeatureType();
        for (int i = 0, j = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor at = schema.getDescriptor(i);
            Object attribute = feature.getAttribute(i);
            if (at instanceof GeometryDescriptor) {
                // using setGeoemtryDirectly the feature becomes the owner of the generated
                // OGR geometry and we don't have to .delete() it (it's faster, too)
                Pointer geometry = geomMapper.parseGTGeometry((Geometry) attribute);
                OGR_F_SetGeometryDirectly(result, geometry);
                continue;
            }

            if (attribute == null) {
                OGR_F_UnsetField(result, j);
            } else {
                Pointer fieldDefinition = OGR_FD_GetFieldDefn(featureDefinition, j);
                long ogrType = OGR_Fld_GetType(fieldDefinition).value();
                if (ogrType == OGRFieldType.OFTInteger.value()) {
                    OGR_F_SetFieldInteger(result, j, ((Number) attribute).intValue());
                } else if (ogrType == OGRFieldType.OFTReal.value()) {
                    OGR_F_SetFieldDouble(result, j, ((Number) attribute).doubleValue());
                } else if (ogrType == OGRFieldType.OFTBinary.value()) {
                    byte[] attValue = (byte[]) attribute;
                    OGR_F_SetFieldBinary(result, j, attValue.length, pointerToBytes(attValue));
                } else if (ogrType == OGRFieldType.OFTDate.value()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) attribute);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    OGR_F_SetFieldDateTime(result, j, year, month, day, 0, 0, 0, 0);
                } else if (ogrType == OGRFieldType.OFTTime.value()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) attribute);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    int second = cal.get(Calendar.SECOND);
                    OGR_F_SetFieldDateTime(result, j, 0, 0, 0, hour, minute, second, 0);
                } else if (ogrType == OGRFieldType.OFTDateTime.value()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) attribute);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    int second = cal.get(Calendar.SECOND);
                    OGR_F_SetFieldDateTime(result, j, year, month, day, hour, minute, second, 0);
                } else {
                    // anything else we treat as a string
                    String str = Converters.convert(attribute, String.class);
                    OGR_F_SetFieldString(result, j, pointerToCString(str));
                }
            }
            j++;
        }

        return result;
    }

    /**
     * Turns line and polygon into multiline and multipolygon. This is a stop-gap measure to make
     * things works against shapefiles, I've asked the GDAL mailing list on how to properly handle
     * this in the meantime
     * 
     * @param ogrGeometry
     * @param ad
     * @return
     */
    Geometry fixGeometryType(Geometry ogrGeometry, AttributeDescriptor ad) {
        if (MultiPolygon.class.equals(ad.getType())) {
            if (ogrGeometry instanceof MultiPolygon)
                return ogrGeometry;
            else
                return geomFactory.createMultiPolygon(new Polygon[] { (Polygon) ogrGeometry });
        } else if (MultiLineString.class.equals(ad.getType())) {
            if (ogrGeometry instanceof MultiLineString)
                return ogrGeometry;
            else
                return geomFactory
                        .createMultiLineString(new LineString[] { (LineString) ogrGeometry });
        }
        return ogrGeometry;

    }


    /**
     * Reads the current feature's specified field using the most appropriate OGR field extraction
     * method
     * 
     * @param ad
     * @return
     */
    Object getOgrField(AttributeDescriptor ad, Pointer<?> ogrFeature) throws IOException {
        if(ad instanceof GeometryDescriptor) {
            // gets the geometry as a reference, we don't own it, we should not deallocate it
            Pointer<?> ogrGeometry = OGR_F_GetGeometryRef(ogrFeature);
            return fixGeometryType(geomMapper.parseOgrGeometry(ogrGeometry), ad);
        }
        
        Integer idx = attributeIndexes.get(ad.getLocalName());

        // check for null fields
        if (idx == null || OGR_F_IsFieldSet(ogrFeature, idx) == 0) {
            return null;
        }

        // hum, ok try and parse it
        Class clazz = ad.getType().getBinding();
        if (clazz.equals(String.class)) {
            return  OGR_F_GetFieldAsString(ogrFeature, idx).getCString();
        } else if (clazz.equals(Byte.class)) {
            return (byte) OGR_F_GetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Short.class)) {
            return (short) OGR_F_GetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Integer.class)) {
            return OGR_F_GetFieldAsInteger(ogrFeature, idx);
        } else if (clazz.equals(Long.class)) {
            String value = OGR_F_GetFieldAsString(ogrFeature, idx).getCString();
            return new Long(value);
        } else if (clazz.equals(BigInteger.class)) {
            String value = OGR_F_GetFieldAsString(ogrFeature, idx).getCString();
            return new BigInteger(value);
        } else if (clazz.equals(Double.class)) {
            return OGR_F_GetFieldAsDouble(ogrFeature, idx);
        } else if (clazz.equals(Float.class)) {
            return (float) OGR_F_GetFieldAsDouble(ogrFeature, idx);
        } else if (clazz.equals(BigDecimal.class)) {
            String value = OGR_F_GetFieldAsString(ogrFeature, idx).getCString().trim();
            char separator = DECIMAL_SYMBOLS.getDecimalSeparator();
            if(separator != '.') {
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
            throw new IllegalArgumentException("Don't know how to read " + clazz.getName()
                    + " fields");
        }
    }

    /**
     * Reads a date field from the OGR api
     * @param ogrFeature
     * @param idx
     * @return
     */
    private Calendar getDateField(Pointer<?> ogrFeature, Integer idx) {
        Pointer<Integer> year = allocateInt();
        Pointer<Integer> month = allocateInt();
        Pointer<Integer> day = allocateInt();
        Pointer<Integer> hour = allocateInt();
        Pointer<Integer> minute = allocateInt();
        Pointer<Integer> second = allocateInt();
        Pointer<Integer> timeZone = allocateInt();
        
        OGR_F_GetFieldAsDateTime(ogrFeature, idx, year, month, day, hour, minute, second, timeZone);
        
        Calendar cal = Calendar.getInstance();
        // from ogr_core.h 
        // 0=unknown, 1=localtime(ambiguous), 100=GMT, 104=GMT+1, 80=GMT-5, etc
        int tz = timeZone.getInt();
        if(tz != 0 && tz != 1) {
            int offset = tz - 100 / 4;
            if(offset < 0) {
                cal.setTimeZone(TimeZone.getTimeZone("GMT" + offset));
            } else if(offset == 0) {
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            } else {
                cal.setTimeZone(TimeZone.getTimeZone("GMT+" + offset));
            }               
        }
        cal.clear();
        cal.set(Calendar.YEAR, year.getInt());
        cal.set(Calendar.MONTH, month.getInt());
        cal.set(Calendar.DAY_OF_MONTH, day.getInt());
        cal.set(Calendar.HOUR_OF_DAY, hour.getInt());
        cal.set(Calendar.MINUTE, minute.getInt());
        cal.set(Calendar.SECOND, second.getInt());
        return cal;
    }

    /**
     * Generates a GT2 feature id given its feature type and an OGR feature
     * 
     * @param schema
     * @param ogrFeature
     * @return
     */
    String convertOGRFID(SimpleFeatureType schema, Pointer<?> ogrFeature) {
        long id = OGR_F_GetFID(ogrFeature);
        return schema.getTypeName() + "." + id;
    }

    /**
     * Decodes a GT2 feature id into an OGR one
     * 
     * @param feature
     * @return
     */
    long convertGTFID(SimpleFeature feature) {
        String id = feature.getID();
        return Long.parseLong(id.substring(id.indexOf(".") + 1));
    }

}
