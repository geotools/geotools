package org.geotools.data.efeature;

import java.util.Date;

import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

public class DataBuilder implements ConverterFactory, Converter {
        
    public static Object parse(Class<?> cls, Object value) throws IllegalArgumentException {
        return parse(cls.getName(), value);
    }

    public static Object parse(String name, Object value) throws IllegalArgumentException {
        IDataTypeAdapter<?> adapter = getAdapter(name);
        if (adapter != null) {
            return adapter.adapt(value);
        }
        throw new IllegalArgumentException("Data type " + name + " is not supported");
    }

    public static boolean isNumeric(Literal value) {
        return DataTypes.isNumeric(value);
    }

    public static boolean isNumeric(Object value) {
        return DataTypes.isNumeric(value);
    }

    public static boolean isDate(Literal value) {
        return DataTypes.isDate(value);
    }

    public static boolean isDate(Object value) {
        return DataTypes.isDate(value);
    }

    public static boolean isString(Literal value) {
        return DataTypes.isString(value);
    }

    public static boolean isString(Object value) {
        return DataTypes.isString(value);
    }

    public static boolean isGeometry(Literal value) {
        return DataTypes.isGeometry(value);
    }

    public static boolean isGeometry(Object value) {
        return DataTypes.isGeometry(value);
    }

    public static String getName(Class<?> type) {
        return DataTypes.getName(type);
    }
    
    public static Class<?> getType(String name) {
        return DataTypes.getType(name);
    }    

    public static boolean supports(String name) {
        return DataTypes.supports(name);
    }

    public static boolean supports(Class<?> type) {
        return DataTypes.supports(type);
    }

    public static IDataTypeAdapter<?> getAdapter(String name) {
        return DataTypes.getAdapter(name);
    }

    public static <T> IDataTypeAdapter<T> getAdapter(Class<T> type) {
        return DataTypes.getAdapter(type);
    }

    public Integer toInteger(Literal value) {
        return toInteger(value.getValue());
    }

    public Integer toInteger(Object value) {
        return DataTypes.getAdapter(Integer.class).adapt(value);
    }

    public Double toDouble(Literal value) {
        return toDouble(value.getValue());
    }

    public Double toDouble(Object value) {
        return DataTypes.getAdapter(Double.class).adapt(value);
    }

    public Long toLong(Literal value) {
        return toLong(value.getValue());
    }

    public Long toLong(Object value) {
        return DataTypes.getAdapter(Long.class).adapt(value);
    }

    public Short toShort(Literal value) {
        return toShort(value.getValue());
    }

    public Short toShort(Object value) {
        return DataTypes.getAdapter(Short.class).adapt(value);
    }

    public Byte toByte(Literal value) {
        return toByte(value.getValue());
    }

    public Byte toByte(Object value) {
        return DataTypes.getAdapter(Byte.class).adapt(value);
    }
    
    public Float toFloat(Literal value) {
        return toFloat(value.getValue());
    }

    public Float toFloat(Object value) {
        return DataTypes.getAdapter(Float.class).adapt(value);
    }

    public Boolean toBoolean(Literal value) {
        return toBoolean(value.getValue());
    }

    public Boolean toBoolean(Object value) {
        return DataTypes.getAdapter(Boolean.class).adapt(value);
    }

    public Character toCharacter(Literal value) {
        return toCharacter(value.getValue());
    }

    public Character toCharacter(Object value) {
        return DataTypes.getAdapter(Character.class).adapt(value);
    }

    public String toString(Literal value) {
        return toString(value.getValue());
    }

    public String toString(Object value) {
        return DataTypes.getAdapter(String.class).adapt(value);
    }

    public Date toDate(Literal value) {
        return toDate(value.getValue());
    }

    public Date toDate(Object value) {
        return DataTypes.getAdapter(Date.class).adapt(value);
    }

    public Date toDate(String date) {
        return DataTypes.getAdapter(Date.class).adapt(date);
    }

    public static String toWKT(Literal value) {
        return toWKT(value.getValue());
    }

    public static String toWKT(Object value) {
        return (String) DataTypes.getAdapter(DataTypes.WKT).adapt(value);
    }

    public static String toWKT(Geometry geom) {
        return (String) DataTypes.getAdapter(DataTypes.WKT).adapt(geom);
    }
    
    public static String toEmptyWKT(Class<?> type) throws IllegalArgumentException {
        if(Point.class.isAssignableFrom(type)) return DataTypes.WKT_POINT_EMPTY;
        if(LineString.class.isAssignableFrom(type)) return DataTypes.WKT_LINESTRING_EMPTY;
        if(Polygon.class.isAssignableFrom(type)) return DataTypes.WKT_POLYGON_EMPTY;
        if(MultiPoint.class.isAssignableFrom(type)) return DataTypes.WKT_MULTIPOINT_EMPTY;
        if(MultiLineString.class.isAssignableFrom(type)) return DataTypes.WKT_MULTILINESTRING_EMPTY;
        if(MultiPolygon.class.isAssignableFrom(type)) return DataTypes.WKT_MULTIPOLYGON_EMPTY;
        if(GeometryCollection.class.isAssignableFrom(type)) return DataTypes.WKT_GEOMETRYCOLLECTION_EMPTY;
        throw new IllegalArgumentException(type + " is not supported");
    }
    
    public static String toWKB(Literal value) {
        return toWKB(value.getValue());
    }

    public static String toWKB(Object value) {
        return (String) DataTypes.getAdapter(DataTypes.WKB).adapt(value);
    }

    public static byte[] toWKB(Geometry geom) {
        return (byte[]) DataTypes.getAdapter(DataTypes.WKB).adapt(geom);
    }
    
    public static byte[] toEmptyWKB(Class<?> type) throws IllegalArgumentException {
        if(Point.class.isAssignableFrom(type)) return DataTypes.WKB_POINT_EMPTY;
        if(LineString.class.isAssignableFrom(type)) return DataTypes.WKB_LINESTRING_EMPTY;
        if(Polygon.class.isAssignableFrom(type)) return DataTypes.WKB_POLYGON_EMPTY;
        if(MultiPoint.class.isAssignableFrom(type)) return DataTypes.WKB_MULTIPOINT_EMPTY;
        if(MultiLineString.class.isAssignableFrom(type)) return DataTypes.WKB_MULTILINESTRING_EMPTY;
        if(MultiPolygon.class.isAssignableFrom(type)) return DataTypes.WKB_MULTIPOLYGON_EMPTY;
        if(GeometryCollection.class.isAssignableFrom(type)) return DataTypes.WKB_GEOMETRYCOLLECTION_EMPTY;
        throw new IllegalArgumentException(type + " is not supported");
    }
    
    public static Geometry toGeometry(Literal value) throws ParseException {
        return toGeometry(value.getValue());
    }

    public static Geometry toGeometry(Object value) throws ParseException {
        return DataTypes.getAdapter(Geometry.class).adapt(value);
    }

    public static Geometry toGeometry(String wkt) throws ParseException {
        return DataTypes.getAdapter(Geometry.class).adapt(wkt);
    }
    
    public static Geometry toGeometry(byte[] wkb) throws ParseException {
        return DataTypes.getAdapter(Geometry.class).adapt(wkb);
    }
    
    public static Geometry toEmptyGeometry(Class<?> type) throws ParseException {
        //
        // Get adapter
        //
        IDataTypeAdapter<Geometry> adapter = DataTypes.getAdapter(Geometry.class);
        //
        // Workaround for undefined WKB 
        //
        if(Point.class.isAssignableFrom(type)) return adapter.adapt(DataTypes.WKB_POINT_EMPTY);
        //
        // Use WKB because it's faster
        //
        return DataTypes.getAdapter(Geometry.class).adapt(toEmptyWKB(type));
    }

    // -------------------------------------------------- 
    //  ConverterFactory implementation
    // --------------------------------------------------
    
    private static DataBuilder builder;

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if(builder==null) {
            builder = new DataBuilder(hints);
        }
        return builder;
    }

    // -------------------------------------------------- 
    // Converter implementation 
    // --------------------------------------------------

    private final Hints hints;
    
    public DataBuilder() {
        this(new Hints());
    }

    public DataBuilder(Hints hints) {
        this.hints = hints;
    }

    public <T> T convert(Object source, Class<T> target) throws Exception {
        // Use target class name as value type name as default
        //
        String name = target.getName();

        // Is another target value type name supplied?
        //
        if (hints.containsKey(DataTypes.WKT)) {
            name = DataTypes.WKT;
        }

        // Parse it into target class
        //
        return target.cast(parse(name, source));
    }

}
