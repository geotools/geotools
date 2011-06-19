package org.geotools.data.efeature;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.eclipse.emf.query.conditions.booleans.BooleanAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter.ByteAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter.DoubleAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter.FloatAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter.LongAdapter;
import org.eclipse.emf.query.conditions.numbers.NumberAdapter.ShortAdapter;
import org.eclipse.emf.query.conditions.strings.StringAdapter;
import org.geotools.data.efeature.adapters.IntegerAdapter;
import org.geotools.data.efeature.adapters.CharacterAdapter;
import org.geotools.data.efeature.adapters.DateAdapter;
import org.geotools.data.efeature.adapters.GeometryAdapter;
import org.geotools.data.efeature.adapters.ObjectAdapter;
import org.geotools.data.efeature.adapters.WKBAdapter;
import org.geotools.data.efeature.adapters.WKTAdapter;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class DataTypes
{
    
    // ----------------------------------------------------- 
    //  Private static fields
    // -----------------------------------------------------
    
    //private final static Logger LOGGER = Logging.getLogger(DataTypes.class); 
    private final static Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
    private final static Map<Class<?>, String> nameMap = new HashMap<Class<?>, String>();
    private final static Map<Object, Class<?>> uniqueMap = new HashMap<Object, Class<?>>();
    private final static Map<String, IDataTypeAdapter<?>> adapterMap = new HashMap<String, IDataTypeAdapter<?>>();    

    
    // ----------------------------------------------------- 
    //  Public static fields
    // -----------------------------------------------------
    
    public static final String WKT = "WellKnownText";
    public static final String WKT_GEOMETRYCOLLECTION_EMPTY = "GEOMETRYCOLLECTION EMPTY";
    public static final String WKT_MULTIPOLYGON_EMPTY = "MULTIPOLYGON EMPTY";
    public static final String WKT_MULTILINESTRING_EMPTY = "MULTILINESTRING EMPTY";
    public static final String WKT_MULTIPOINT_EMPTY = "MULTIPOINT EMPTY";
    public static final String WKT_POLYGON_EMPTY = "POLYGON EMPTY";
    public static final String WKT_LINESTRING_EMPTY = "LINESTRING EMPTY";
    public static final String WKT_POINT_EMPTY = "POINT EMPTY";
    
    public static final String WKB = "WellKnownBinary";
    public static final byte[] WKB_GEOMETRYCOLLECTION_EMPTY = new byte[]{0, 0, 0, 0, 7, 0, 0, 0, 0};
    public static final byte[] WKB_MULTIPOLYGON_EMPTY = new byte[]{0, 0, 0, 0, 6, 0, 0, 0, 0};
    public static final byte[] WKB_MULTILINESTRING_EMPTY = new byte[]{0, 0, 0, 0, 5, 0, 0, 0, 0};
    public static final byte[] WKB_MULTIPOINT_EMPTY = new byte[]{0, 0, 0, 0, 4, 0, 0, 0, 0};
    public static final byte[] WKB_POLYGON_EMPTY = new byte[]{0, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0};
    public static final byte[] WKB_LINESTRING_EMPTY = new byte[]{0, 0, 0, 0, 2, 0, 0, 0, 0};
    public static final byte[] WKB_POINT_EMPTY = new byte[]{}; // Apparently, there is no WKB convention for "POINT EMPTY"
    
    /**
     * Maximum length of empty WKB definition byte array.
     */
    public static final int WKB_MAX_EMPTY_COUNT = Math.max(WKB_GEOMETRYCOLLECTION_EMPTY.length, 
            Math.max(WKB_MULTIPOLYGON_EMPTY.length,Math.max(WKB_MULTILINESTRING_EMPTY.length,
            Math.max(WKB_MULTIPOINT_EMPTY.length,Math.max(WKB_LINESTRING_EMPTY.length,
                     WKB_POINT_EMPTY.length)))));

    // ----------------------------------------------------- 
    //  Static constructor
    // -----------------------------------------------------
    
    static {

        // ----------------------------------------------------------
        //  Add support for all primitives and their wrapper counterparts
        // ----------------------------------------------------------

        support(int[].class, Integer[].class, IntegerAdapter.DEFAULT,"0",0);
        support(double[].class, Double[].class, DoubleAdapter.DEFAULT,"0.0",0.0);
        support(float[].class, Float[].class, FloatAdapter.DEFAULT,"0.0f",0.0f);
        support(byte[].class, Byte[].class, ByteAdapter.DEFAULT);
        support(short[].class, Short[].class, ShortAdapter.DEFAULT);
        support(long[].class, Long[].class, LongAdapter.DEFAULT,"OL",0L);
        support(char[].class, Character[].class, CharacterAdapter.DEFAULT,Character.toString('\u0000'));

        // ----------------------------------------------------------
        //  Add support for other often used data types
        // ----------------------------------------------------------

        support(Date[].class, DateAdapter.DEFAULT);
        support(String[].class, StringAdapter.DEFAULT,"\"\"","");
        support(boolean[].class, Boolean[].class,BooleanAdapter.DEFAULT,
                "true","false","TRUE","FALSE",Boolean.TRUE,Boolean.FALSE);

        // ----------------------------------------------------------
        //  JTS Geometry data types
        // ----------------------------------------------------------

        support(Geometry[].class, GeometryAdapter.DEFAULT);
        support(Point[].class, GeometryAdapter.DEFAULT,WKT_POINT_EMPTY);
        support(LineString[].class, GeometryAdapter.DEFAULT, WKT_LINESTRING_EMPTY);
        support(Polygon[].class, GeometryAdapter.DEFAULT, WKT_POLYGON_EMPTY);
        support(MultiPoint[].class, GeometryAdapter.DEFAULT, WKT_MULTIPOINT_EMPTY);
        support(MultiLineString[].class, GeometryAdapter.DEFAULT, WKT_MULTILINESTRING_EMPTY);
        support(MultiPolygon[].class, GeometryAdapter.DEFAULT,WKT_MULTIPOLYGON_EMPTY);
        support(GeometryCollection[].class, GeometryAdapter.DEFAULT, WKT_GEOMETRYCOLLECTION_EMPTY);
        
        // ----------------------------------------------------- 
        //  Special data types
        // -----------------------------------------------------

        support(WKT,String.class,WKTAdapter.DEFAULT, 
                WKT_POINT_EMPTY, WKT_LINESTRING_EMPTY, WKT_POLYGON_EMPTY, WKT_MULTIPOINT_EMPTY,
                WKT_MULTILINESTRING_EMPTY, WKT_MULTIPOLYGON_EMPTY, WKT_GEOMETRYCOLLECTION_EMPTY);
        support(WKB,byte[].class,WKBAdapter.DEFAULT, 
                WKB_POINT_EMPTY, WKB_LINESTRING_EMPTY, WKB_POLYGON_EMPTY, WKB_MULTIPOINT_EMPTY,
                WKB_MULTILINESTRING_EMPTY, WKB_MULTIPOLYGON_EMPTY, WKB_GEOMETRYCOLLECTION_EMPTY);

        // ----------------------------------------------------- 
        //  Generic data types
        // -----------------------------------------------------
        //
        // Map Object to all supported data types. 
        //
        support(Object.class,ObjectAdapter.DEFAULT);
        //
        // Map Object to supported data types. Note that the declaration of this default 
        // as type Integer actually doesn't matter at all, because the "cast" to (N), in this 
        // case (Integer), doesn't actually exist (is "erasure" is Number)
        // 
        support(Number.class,NumberAdapter.<Integer>getDefault());
        
    }    
            
    // ----------------------------------------------------- 
    //  Public methods
    // -----------------------------------------------------

    
    public static int getCount() {
        return nameMap.size();
    }
    
    public static int getCount(Class<?> type, boolean subtype) {
        int count = 0;
        if(subtype) {
            for(Class<?> it : typeMap.values()) {
                if(it.isAssignableFrom(type))
                    count++;
            }            
        } else {
            for(Class<?> it : typeMap.values()) {
                if(type.isAssignableFrom(it))
                    count++;
            }            
        }
        return count;
    }
           

    @SuppressWarnings("unchecked")
    public static <T> List<Class<T>> getSubTypes(Class<T> type) {
        List<Class<T>> types = new ArrayList<Class<T>>();
        for(Class<?> it : typeMap.values()) {
            if(!type.equals(it) && type.isAssignableFrom(it))
                types.add((Class<T>)it);
        }            
        return types;
    }
    
    public static String getName(Class<?> type) {
        return nameMap.get(type);
    }
    
    public static Class<?> getType(String name) {
        return typeMap.get(name);
    }            
    
    public static boolean supports(String name)
    {
        return typeMap.containsKey(name);
    }
    
    public static boolean supports(Class<?> type)
    {
        if(!nameMap.containsKey(type)) {
            return getSubTypes(type).size()>0;
        }
        return true;
    }
    
    public static IDataTypeAdapter<?> getAdapter(String name) {
        return adapterMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> IDataTypeAdapter<T> getAdapter(Class<T> type) {
        if(!nameMap.containsKey(type)) {
            List<Class<T>> types = getSubTypes(type);
            if(types.size()==0) {
                return null;
            }            
            type = types.get(0);
        }
        return (IDataTypeAdapter<T>) getAdapter(type.getName());
    }
    
    public static <N extends Number> N getMinValue(Class<N> type) 
        throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = type.getField("MIN_VALUE");
        return type.cast(field.get(null));
    }
    
    public static <N extends Number> N getMaxValue(Class<N> type) 
        throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = type.getField("MAX_VALUE");
        return type.cast(field.get(null));
    }
    
    public static boolean isArray(Literal value) {
        return isArray(value.getValue());
    }
    
    public static boolean isArray(Object value) {
        if(value != null) {
            return value.getClass().isArray();
        }
        return false;
    }
    
    public static Class<?> toType(Literal value) {
        return toType(value.getValue());
    }
    
    public static Class<?> toType(Object value) {
        if(value !=null) {
            Class<?> type = value.getClass();
            return type.isArray() ? type.getComponentType() : type;
        }
        return null;
    }
    
    public static boolean isNumeric(Literal value) {
        return isNumeric(value.getValue());
    }

    public static boolean isNumeric(Object value) {        
        return (value instanceof Number);
    }

    public static boolean isDate(Literal value) {
        return isDate(value.getValue());
    }

    public static boolean isDate(Object value) {
        return (value instanceof Date);
    }
    
    public static boolean isBoolean(Literal value, boolean parse) {
        return isBoolean(value.getValue(),parse);
    }

    public static boolean isBoolean(Object value, boolean parse) {
        if(value instanceof Boolean) {
            return true;
        }
        else if(parse && (value instanceof String) ) {
            String s =(String)value; 
            return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
        }
        return false;
    }
    
    public static boolean isCharacter(Literal value) {
        return isString(value.getValue());
    }

    public static boolean isCharacter(Object value) {
        return (value instanceof Character);
    }
    
    public static boolean isString(Literal value) {
        return isString(value.getValue());
    }

    public static boolean isString(Object value) {
        return (value instanceof String);
    }

    public static boolean isGeometry(Literal value) {
        return isGeometry(value.getValue());
    }

    public static boolean isGeometry(Object value) {
        if(value instanceof Geometry) {
            return true;
        }
        else if(value instanceof Class) {
            Class<?> cls = (Class<?>)value;
            if(Geometry.class.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }
        
        
    
    // ----------------------------------------------------- 
    //  Private helper methods
    // -----------------------------------------------------

    private static void support(String name, Class<?> type, IDataTypeAdapter<?> adapter, Object... unique)
    {
        typeMap.put(name,type);
        nameMap.put(type,name);
        adapterMap.put(name, adapter);
        Class<?> component = type.getComponentType();
        if(component!=null)
        {
            support(component,adapter,unique);
        } 
        else if(!(unique == null  || unique.length==0))
        {
            // Only set nil values for non-array types
            for(Object it : unique)
            {
                uniqueMap.put(it, type);                    
            }
        }            
    }

    private static void support(Class<?> type, IDataTypeAdapter<?> adapter, Object... unique)
    {
        String name = type.getName();
        support(name, type, adapter, unique);
    }
    
    private static void support(Class<?> primitive, Class<?> wrapper, IDataTypeAdapter<?> adapter, Object... unique)
    {
        support(primitive,adapter,unique);
        support(wrapper,adapter,unique);
    }


}