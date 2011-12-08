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
import static org.geotools.data.ogr.bridj.OsrLibrary.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRJustification;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Helper mapping between geotools and OGR feature types
 * 
 * @author Andrea Aime - GeoSolutions
 */
class FeatureTypeMapper {

    /**
     * Returns the geotools feature type equivalent from the native OGR one
     * 
     * @param layer
     * @param typeName
     * @param namespaceURI
     * @return
     * @throws IOException
     */
    SimpleFeatureType getFeatureType(Pointer layer, String typeName, String namespaceURI)
            throws IOException {
        Pointer definition = null;
        try {
            // setup the builder
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName(typeName);
            tb.setNamespaceURI(namespaceURI);
            if (tb.getNamespaceURI() == null) {
                tb.setNamespaceURI(BasicFeatureTypes.DEFAULT_NAMESPACE);
            }

            // grab the layer definition
            definition = OGR_L_GetLayerDefn(layer);

            // figure out the geometry
            Class<? extends Geometry> geometryBinding = getGeometryBinding(definition);
            if (geometryBinding != null) {
                CoordinateReferenceSystem crs = getCRS(layer);
                tb.add("the_geom", geometryBinding, crs);
            }

            // get the non geometric fields
            final int count = OGR_FD_GetFieldCount(definition);
            for (int i = 0; i < count; i++) {
                Pointer field = OGR_FD_GetFieldDefn(definition, i);
                String name = OGR_Fld_GetNameRef(field).getCString();
                Class binding = getBinding(field);
                int width = OGR_Fld_GetWidth(field);
                if (width > 0) {
                    tb.length(width);
                }
                tb.add(name, binding);
            }

            // compute a default parent feature type
            if ((geometryBinding == Point.class) || (geometryBinding == MultiPoint.class)) {
                tb.setSuperType(BasicFeatureTypes.POINT);
            } else if ((geometryBinding == Polygon.class)
                    || (geometryBinding == MultiPolygon.class)) {
                tb.setSuperType(BasicFeatureTypes.POLYGON);
            } else if ((geometryBinding == LineString.class)
                    || (geometryBinding == MultiLineString.class)) {
                tb.setSuperType(BasicFeatureTypes.LINE);
            }

            return tb.buildFeatureType();
        } finally {
            OGRUtils.releaseDefinition(definition);
        }
    }

    /**
     * Maps the OGR field type to a java class
     * 
     * @param field
     * @return
     */
    private Class getBinding(Pointer field) {
        ValuedEnum<OGRFieldType> type = OGR_Fld_GetType(field);
        int width = OGR_Fld_GetWidth(field);
        long value = type.value();
        if (value == OGRFieldType.OFTInteger.value()) {
            if (width <= 3) {
                return Byte.class;
            } else if (width <= 5) {
                return Short.class;
            } else if (width <= 9) {
                return Integer.class;
            } else if (width <= 19) {
                return Long.class;
            } else {
                return BigDecimal.class;
            }
        } else if (value == OGRFieldType.OFTIntegerList.value()) {
            return int[].class;
        } else if (value == OGRFieldType.OFTReal.value()) {
            if (width <= 12) {
                return Float.class;
            } else if (width <= 22) {
                return Double.class;
            } else {
                return BigDecimal.class;
            }
        } else if (value == OGRFieldType.OFTRealList.value()) {
            return double[].class;
        } else if (value == OGRFieldType.OFTBinary.value()) {
            return byte[].class;
        } else if (value == OGRFieldType.OFTDate.value()) {
            return java.sql.Date.class;
        } else if (value == OGRFieldType.OFTTime.value()) {
            return java.sql.Time.class;
        } else if (value == OGRFieldType.OFTDateTime.value()) {
            return java.sql.Timestamp.class;
        } else {
            // whatever else we'll map a string
            return String.class;
        }
    }

    /**
     * Returns a OGR field definition compatible with the specified attribute descriptor where:
     * <ul>
     * <li>width is the number of chars needed to format the strings equivalent of the number
     * <li>
     * <li>precision is the number of chars after decimal pont</li>
     * <li>justification: right or left (in outputs)</li>
     * </ul>
     * 
     * @param ad
     * @throws IOException
     */
    public Pointer getOGRFieldDefinition(AttributeDescriptor ad) throws IOException {
        final Class type = ad.getType().getBinding();
        final Pointer def;
        Pointer<Byte> namePtr = pointerToCString(ad.getLocalName());
        if (Boolean.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTString);
            OGR_Fld_SetWidth(def, 5);
        } else if (Byte.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 3);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (Short.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 5);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (Integer.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 9);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (Long.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 19);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (BigInteger.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 32);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (BigDecimal.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTReal);
            OGR_Fld_SetWidth(def, 32);
            OGR_Fld_SetPrecision(def, 15);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (Float.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTReal);
            OGR_Fld_SetWidth(def, 12);
            OGR_Fld_SetPrecision(def, 7);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (Double.class.equals(type) || Number.class.isAssignableFrom(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTInteger);
            OGR_Fld_SetWidth(def, 22);
            OGR_Fld_SetPrecision(def, 16);
            OGR_Fld_SetJustify(def, OGRJustification.OJRight);
        } else if (String.class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTString);
            int length = FeatureTypes.getFieldLength(ad);
            if (length <= 0) {
                length = 255;
            }
            OGR_Fld_SetWidth(def, length);
        } else if (byte[].class.equals(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTBinary);
        } else if (java.sql.Date.class.isAssignableFrom(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTDate);
        } else if (java.sql.Time.class.isAssignableFrom(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTTime);
        } else if (java.util.Date.class.isAssignableFrom(type)) {
            def = OGR_Fld_Create(namePtr, OGRFieldType.OFTDateTime);
        } else {
            throw new IOException("Cannot map " + type + " to an OGR type");
        }

        return def;
    }

    /**
     * Returns the OGR geometry type constant given a geometry attribute type
     * 
     * @param descriptor
     * @return
     * @throws IOException
     */
    public ValuedEnum<OGRwkbGeometryType> getOGRGeometryType(GeometryDescriptor descriptor)
            throws IOException {
        Class binding = descriptor.getType().getBinding();
        if (GeometryCollection.class.isAssignableFrom(binding)) {
            if (MultiPoint.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbMultiPoint;
            } else if (MultiLineString.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbMultiLineString;
            } else if (MultiPolygon.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbMultiPolygon;
            } else {
                return OGRwkbGeometryType.wkbGeometryCollection;
            }
        } else {
            if (Point.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbPoint;
            } else if (LinearRing.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbLinearRing;
            } else if (LineString.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbLineString;
            } else if (Polygon.class.isAssignableFrom(binding)) {
                return OGRwkbGeometryType.wkbPolygon;
            } else {
                return OGRwkbGeometryType.wkbUnknown;
            }
        }
    }

    /**
     * Returns the JTS geometry type equivalent to the layer native one
     * 
     * @param definition
     * @return
     * @throws IOException
     */
    private Class<? extends Geometry> getGeometryBinding(Pointer definition) throws IOException {
        ValuedEnum<OGRwkbGeometryType> gt = OGR_FD_GetGeomType(definition);
        long value = gt.value();
        // for line and polygon we return multi in any case since OGR will declare simple for
        // multigeom
        // anyways and then return simple or multi in the actual geoemtries depending on
        // what it finds
        if (value == OGRwkbGeometryType.wkbPoint.value()
                || value == OGRwkbGeometryType.wkbPoint25D.value()) {
            return Point.class;
        } else if (value == OGRwkbGeometryType.wkbLinearRing.value()) {
            return LinearRing.class;
        } else if (value == OGRwkbGeometryType.wkbLineString.value()
                || value == OGRwkbGeometryType.wkbLineString25D.value()
                || value == OGRwkbGeometryType.wkbMultiLineString.value()
                || value == OGRwkbGeometryType.wkbMultiLineString25D.value()) {
            return MultiLineString.class;
        } else if (value == OGRwkbGeometryType.wkbPolygon.value()
                || value == OGRwkbGeometryType.wkbPolygon25D.value()
                || value == OGRwkbGeometryType.wkbMultiPolygon.value()
                || value == OGRwkbGeometryType.wkbMultiPolygon25D.value()) {
            return MultiPolygon.class;
        } else if (value == OGRwkbGeometryType.wkbGeometryCollection.value()
                || value == OGRwkbGeometryType.wkbGeometryCollection25D.value()) {
            return GeometryCollection.class;
        } else if (value == OGRwkbGeometryType.wkbNone.value()) {
            return null;
        } else if (value == OGRwkbGeometryType.wkbUnknown.value()) {
            return Geometry.class;
        } else {
            throw new IOException("Unknown geometry type: " + value);
        }
    }

    /**
     * Returns the GeoTools {@link CoordinateReferenceSystem} equivalent to the layer native one
     * 
     * @param layer
     * @return
     * @throws IOException
     */
    private CoordinateReferenceSystem getCRS(Pointer layer) throws IOException {
        Pointer spatialReference = null;
        CoordinateReferenceSystem crs = null;
        try {
            spatialReference = OGR_L_GetSpatialRef(layer);
            if (spatialReference == null) {
                return null;
            }

            try {
                Pointer<Byte> code = OSRGetAuthorityCode(spatialReference, pointerToCString("EPSG"));
                if (code != null) {
                    String fullCode = "EPSG:" + code;
                    crs = CRS.decode(fullCode);
                }
            } catch (Exception e) {
                // fine, the code might be unknown to out authority
            }
            if (crs == null) {
                try {
                    Pointer<Pointer<Byte>> wktPtr = allocatePointer(Byte.class);
                    OSRExportToWkt(spatialReference, wktPtr);
                    String wkt = wktPtr.getPointer(Byte.class).getCString();
                    crs = CRS.parseWKT(wkt);
                } catch (Exception e) {
                    // the wkt might reference an unsupported projection
                }
            }
            return crs;
        } finally {
            OGRUtils.releaseSpatialReference(spatialReference);
        }
    }

    /**
     * Returns a Pointer to a OGR spatial reference object equivalent to the specified GeoTools CRS
     * 
     * @param crs
     * @return
     */
    public Pointer getSpatialReference(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        // use tostring to get a lenient wkt translation
        String wkt = crs.toString();
        return OSRNewSpatialReference(pointerToCString(wkt));
    }

}
