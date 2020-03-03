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
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Helper mapping between geotools and OGR feature types
 *
 * @author Andrea Aime - GeoSolutions
 */
class FeatureTypeMapper {

    OGR ogr;

    public FeatureTypeMapper(OGR ogr) {
        this.ogr = ogr;
    }

    /** Returns the geotools feature type equivalent from the native OGR one */
    SimpleFeatureType getFeatureType(Object layer, String typeName, String namespaceURI)
            throws IOException {
        Object definition = null;
        try {
            // setup the builder
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName(typeName);
            tb.setNamespaceURI(namespaceURI);
            if (tb.getNamespaceURI() == null) {
                tb.setNamespaceURI(BasicFeatureTypes.DEFAULT_NAMESPACE);
            }

            // grab the layer definition
            definition = ogr.LayerGetLayerDefn(layer);

            // figure out the geometry
            Class<? extends Geometry> geometryBinding = getGeometryBinding(definition);
            if (geometryBinding != null) {
                CoordinateReferenceSystem crs = getCRS(layer);
                tb.add("the_geom", geometryBinding, crs);
            }

            // get the non geometric fields
            final int count = ogr.LayerGetFieldCount(definition);
            for (int i = 0; i < count; i++) {
                Object field = ogr.LayerGetFieldDefn(definition, i);
                String name = ogr.FieldGetName(field);
                Class binding = getBinding(field);
                int width = ogr.FieldGetWidth(field);
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
            ogr.LayerReleaseLayerDefn(definition);
        }
    }

    /** Maps the OGR field type to a java class */
    private Class getBinding(Object field) {
        long type = ogr.FieldGetType(field);
        int width = ogr.FieldGetWidth(field);

        // Find the narrowest integer data type which will safely hold the declared width
        if (ogr.FieldIsIntegerType(type)) {
            if (width == 0) {
                return BigInteger.class;
            } else if (width < 3) {
                return Byte.class;
            } else if (width < 5) {
                return Short.class;
            } else if (width < 10) {
                return Integer.class;
            } else if (width < 19) {
                return Long.class;
            } else {
                return BigInteger.class;
            }
        } else if (ogr.FieldIsIntegerListType(type)) {
            return int[].class;
        } else if (ogr.FieldIsRealType(type)) {
            if (width <= 12) {
                return Float.class;
            } else if (width <= 22) {
                return Double.class;
            } else {
                return BigDecimal.class;
            }
        } else if (ogr.FieldIsRealListType(type)) {
            return double[].class;
        } else if (ogr.FieldIsBinaryType(type)) {
            return byte[].class;
        } else if (ogr.FieldIsDateType(type)) {
            return java.sql.Date.class;
        } else if (ogr.FieldIsTimeType(type)) {
            return java.sql.Time.class;
        } else if (ogr.FieldIsDateTimeType(type)) {
            return java.sql.Timestamp.class;
        } else {
            // whatever else we'll map a string
            return String.class;
        }
    }

    /**
     * Returns a OGR field definition compatible with the specified attribute descriptor where:
     *
     * <ul>
     *   <li>width is the number of chars needed to format the strings equivalent of the number
     *   <li>
     *   <li>precision is the number of chars after decimal pont
     *   <li>justification: right or left (in outputs)
     * </ul>
     */
    public Object getOGRFieldDefinition(AttributeDescriptor ad) throws IOException {
        final Class type = ad.getType().getBinding();
        final Object def;
        final String name = ad.getLocalName();
        if (Boolean.class.equals(type)) {
            def = ogr.CreateStringField(name);
            ogr.FieldSetWidth(def, 5);
        } else if (Byte.class.equals(type)) {
            def = ogr.CreateIntegerField(name);
            ogr.FieldSetWidth(def, 3);
            ogr.FieldSetJustifyRight(def);
        } else if (Short.class.equals(type)) {
            def = ogr.CreateIntegerField(name);
            ogr.FieldSetWidth(def, 5);
            ogr.FieldSetJustifyRight(def);
        } else if (Integer.class.equals(type)) {
            def = ogr.CreateIntegerField(name);
            ogr.FieldSetWidth(def, 9);
            ogr.FieldSetJustifyRight(def);
        } else if (Long.class.equals(type)) {
            def = ogr.CreateIntegerField(name);
            ogr.FieldSetWidth(def, 19);
            ogr.FieldSetJustifyRight(def);
        } else if (BigInteger.class.equals(type)) {
            def = ogr.CreateIntegerField(name);
            ogr.FieldSetWidth(def, 32);
            ogr.FieldSetJustifyRight(def);
        } else if (BigDecimal.class.equals(type)) {
            def = ogr.CreateRealField(name);
            ogr.FieldSetWidth(def, 32);
            ogr.FieldSetPrecision(def, 15);
            ogr.FieldSetJustifyRight(def);
        } else if (Float.class.equals(type)) {
            def = ogr.CreateRealField(name);
            ogr.FieldSetWidth(def, 12);
            ogr.FieldSetPrecision(def, 7);
            ogr.FieldSetJustifyRight(def);
        } else if (Double.class.equals(type) || Number.class.isAssignableFrom(type)) {
            def = ogr.CreateRealField(name);
            ogr.FieldSetWidth(def, 22);
            ogr.FieldSetPrecision(def, 16);
            ogr.FieldSetJustifyRight(def);
        } else if (String.class.equals(type)) {
            def = ogr.CreateStringField(name);
            int length = FeatureTypes.getFieldLength(ad);
            if (length <= 0) {
                length = 255;
            }
            ogr.FieldSetWidth(def, length);

        } else if (byte[].class.equals(type)) {
            def = ogr.CreateBinaryField(name);
        } else if (java.sql.Date.class.isAssignableFrom(type)) {
            def = ogr.CreateDateField(name);
        } else if (java.sql.Time.class.isAssignableFrom(type)) {
            def = ogr.CreateTimeField(name);
        } else if (java.util.Date.class.isAssignableFrom(type)) {
            def = ogr.CreateDateTimeField(name);
        } else {
            throw new IOException("Cannot map " + type + " to an OGR type");
        }

        return def;
    }

    /** Returns the OGR geometry type constant given a geometry attribute type */
    public long getOGRGeometryType(GeometryDescriptor descriptor) throws IOException {
        Class binding = descriptor.getType().getBinding();
        if (GeometryCollection.class.isAssignableFrom(binding)) {
            if (MultiPoint.class.isAssignableFrom(binding)) {
                return ogr.GetMultiPointType();
            } else if (MultiLineString.class.isAssignableFrom(binding)) {
                return ogr.GetMultiLineStringType();
            } else if (MultiPolygon.class.isAssignableFrom(binding)) {
                return ogr.GetMultiPolygonType();
            } else {
                return ogr.GetGeometryCollectionType();
            }
        } else {
            if (Point.class.isAssignableFrom(binding)) {
                return ogr.GetPointType();
            } else if (LinearRing.class.isAssignableFrom(binding)) {
                return ogr.GetLinearRingType();
            } else if (LineString.class.isAssignableFrom(binding)) {
                return ogr.GetLineStringType();
            } else if (Polygon.class.isAssignableFrom(binding)) {
                return ogr.GetPolygonType();
            } else {
                return ogr.GetGeometryUnknownType();
            }
        }
    }

    /** Returns the JTS geometry type equivalent to the layer native one */
    private Class<? extends Geometry> getGeometryBinding(Object definition) throws IOException {
        long value = ogr.LayerGetGeometryType(definition);

        // for line and polygon we return multi in any case since OGR will declare simple for
        // multigeom
        // anyways and then return simple or multi in the actual geoemtries depending on
        // what it finds
        if (value == ogr.GetPointType() || value == ogr.GetPoint25DType()) {
            return Point.class;
        } else if (value == ogr.GetLinearRingType()) {
            return LinearRing.class;
        } else if (value == ogr.GetLineStringType()
                || value == ogr.GetLineString25DType()
                || value == ogr.GetMultiLineStringType()
                || value == ogr.GetMultiLineString25DType()) {
            return MultiLineString.class;
        } else if (value == ogr.GetPolygonType()
                || value == ogr.GetPolygon25DType()
                || value == ogr.GetMultiPolygonType()
                || value == ogr.GetMultiPolygon25DType()) {
            return MultiPolygon.class;
        } else if (value == ogr.GetGeometryCollectionType()
                || value == ogr.GetGeometryCollection25DType()) {
            return GeometryCollection.class;
        } else if (value == ogr.GetGeometryNoneType()) {
            return null;
        } else if (value == ogr.GetGeometryUnknownType()) {
            return Geometry.class;
        } else {
            throw new IOException("Unknown geometry type: " + value);
        }
    }

    /** Returns the GeoTools {@link CoordinateReferenceSystem} equivalent to the layer native one */
    private CoordinateReferenceSystem getCRS(Object layer) throws IOException {
        Object spatialReference = null;
        CoordinateReferenceSystem crs = null;
        try {
            spatialReference = ogr.LayerGetSpatialRef(layer);
            if (spatialReference == null) {
                return null;
            }

            try {
                String code = ogr.SpatialRefGetAuthorityCode(spatialReference, "EPSG");
                if (code != null) {
                    String fullCode = "EPSG:" + code;
                    crs = CRS.decode(fullCode);
                }
            } catch (Exception e) {
                // fine, the code might be unknown to out authority
            }
            if (crs == null) {
                try {
                    String wkt = ogr.SpatialRefExportToWkt(spatialReference);
                    crs = CRS.parseWKT(wkt);
                } catch (Exception e) {
                    // the wkt might reference an unsupported projection
                }
            }
            return crs;
        } finally {
            if (spatialReference != null) ogr.SpatialRefRelease(spatialReference);
        }
    }

    /**
     * Returns a Pointer to a OGR spatial reference object equivalent to the specified GeoTools CRS
     */
    public Object getSpatialReference(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        // use tostring to get a lenient wkt translation
        String wkt = crs.toString();
        return ogr.NewSpatialRef(wkt);
    }
}
