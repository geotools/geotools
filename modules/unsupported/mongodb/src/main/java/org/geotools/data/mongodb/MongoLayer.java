package org.geotools.data.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Represents a GeoServer layer consisting of valid GeoJSON-encoded data from a mongoDB collection.
 * (A single collection containing different geometry types (Point, Polygon etc.) may be represented
 * by multiple layers.)
 * 
 * @author Gerald Gay, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 * @source $URL$
 * 
 *         (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * 
 * @see The GNU Lesser General Public License (LGPL)
 */
/* This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA */
public class MongoLayer
{

    private MongoPluginConfig         config    = null;
    private String                    layerName = null;
    private SimpleFeatureType         schema    = null;
    private Set<String>               keywords  = null;
    private CoordinateReferenceSystem crs       = null;
    /** meta data for layer defining geometry type, property field names and types */
    private DBObject                  metaData  = null;

    /** Supported GeoJSON geometry types */
    static public enum GeometryType
    {
        GeometryCollection, LineString, Point, Polygon, MultiLineString, MultiPoint, MultiPolygon, Unknown;
    }

    /** Geometry type for this layer */
    private GeometryType geometryType = null;

    /**
     * How to calculate collection record fields and types Majority: for same named fields with
     * different types use major instance to determine which type to assign String: if same named
     * fields with different types exist; store them as Strings
     */
    static public enum RecordBuilder
    {
        MAJORITY, STRING;
    }

    /** How to build records with potentially different types for this layer */
    private RecordBuilder                  buildRule       = RecordBuilder.MAJORITY;

    /** Metadata map function (ensure no comments) */
    private String                         metaMapFunc     = "function() { mapfields_recursive (\"\", this);}";
    /** Metadata reduce function (ensure no comments) */
    private String                         metaReduceFunc  = "function (key, vals) {"
                                                                   + "  sum = 0;"
                                                                   + "  for (var i in vals) sum += vals[i];"
                                                                   + "  return sum;" + "}";
    /** Name of collection holding metadata results */
    private String                         metaResultsColl = "FieldsAndTypes";

    /**
     * Mapping from class string names from mongo map-reduce to corresponding Java Class NB needs to
     * be synced with MetaDataCompute.js javascript file
     */
    static private HashMap<String, String> classNameMap    = new HashMap<String, String>();
    static
    {
        classNameMap.put( "array", BasicDBList.class.getCanonicalName() );
        classNameMap.put( "boolean", Boolean.class.getCanonicalName() );
        classNameMap.put( "date", Date.class.getCanonicalName() );
        classNameMap.put( "double", Double.class.getCanonicalName() );
        classNameMap.put( "long", Long.class.getCanonicalName() );
        classNameMap.put( "object", BasicDBObject.class.getCanonicalName() );
        classNameMap.put( "string", String.class.getCanonicalName() );
    }

    /** Package logger */
    static private final Logger            log             = MongoPluginConfig.getLog();

    public MongoLayer (DBCollection coll, MongoPluginConfig config)
    {
        this.config = config;
        layerName = coll.getName();
        log.fine( "MongoLayer; layerName " + layerName );
        keywords = new HashSet<String>();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName( layerName );
        builder.setNamespaceURI( config.getNamespace() );
        // Always add _id...
        AttributeTypeBuilder b = new AttributeTypeBuilder();
        b.setBinding( String.class );
        b.setName( "_id" );
        b.setNillable( false );
        b.setDefaultValue( null );
        b.setLength( 1024 );
        AttributeDescriptor a = b.buildDescriptor( "_id" );
        builder.add( a );

        // We could get this out of the table, exercise for the reader... TODO
        try
        {
            crs = CRS.decode( "EPSG:4326" );
        }
        catch (Throwable t)
        {
            crs = DefaultGeographicCRS.WGS84;
        }

        b = new AttributeTypeBuilder();
        b.setName( "geometry" );
        b.setNillable( false );
        b.setDefaultValue( null );
        b.setCRS( crs );
        // determine metadata for this collection
        metaData = getCollectionModel( coll, buildRule );
        // determine geometry type
        setGeometryType( metaData );

        switch (geometryType)
        {
        case GeometryCollection:
            b.setBinding( GeometryCollection.class );
            break;
        case LineString:
            b.setBinding( LineString.class );
            break;
        case Point:
            b.setBinding( Point.class );
            break;
        case Polygon:
            b.setBinding( Polygon.class );
            break;
        case MultiLineString:
            b.setBinding( MultiLineString.class );
            break;
        case MultiPoint:
            b.setBinding( MultiPoint.class );
            break;
        case MultiPolygon:
            b.setBinding( MultiPolygon.class );
            break;
        case Unknown:
            log.warning( "Unknown geometry for layer " + layerName
                    + " (but has valid distinct geometry.type)" );
            return;
        }

        a = b.buildDescriptor( "geometry" );
        builder.add( a );

        // Add the 2 known keywords...
        keywords.add( "_id" );
        keywords.add( "geometry" );

        // Now get all the properties...
        DBObject props = (DBObject) metaData.get( "properties" );
        addAttributes( builder, props, "properties" );
        schema = builder.buildFeatureType();
    }

    /**
     * Add JSON attributes to geo tools schema
     * 
     * @param builder geo tools feature builder
     * @param dbo base object; either a JSON object or Array
     * @param baseProp base property name, e.g. "properties" or nested properties objects and/or
     *            arrays
     */
    private void addAttributes (SimpleFeatureTypeBuilder builder, BSONObject dbo, String baseProp)
    {
        Set<String> cols = dbo.keySet();
        for (String col : cols)
        {
            Object dbcol = dbo.get( col );
            String propName = baseProp + "." + col;
            keywords.add( propName );
            // cannot bind to nulls; only handle non-nulls
            if (dbcol != null)
            {
                // handle as native types
                AttributeTypeBuilder b = new AttributeTypeBuilder();
                b.setName( propName );
                b.setBinding( dbcol.getClass() );
                b.setNillable( true );
                b.setDefaultValue( null );
                b.setLength( 1024 );
                AttributeDescriptor a = b.buildDescriptor( propName );
                builder.add( a );

                // add attrs for nested JSON or Array objects
                if (dbcol instanceof BasicDBObject || dbcol instanceof BasicBSONList)
                {
                    addAttributes( builder, (BSONObject) dbcol, propName );
                }
            }
        }
    }

    public String getName ()
    {
        return layerName;
    }

    public SimpleFeatureType getSchema ()
    {
        return schema;
    }

    public Set<String> getKeywords ()
    {
        return keywords;
    }

    public CoordinateReferenceSystem getCRS ()
    {
        return crs;
    }

    public MongoPluginConfig getConfig ()
    {
        return config;
    }

    /**
     * Get GeometryType
     * 
     * @return GeometryType, may be null if not set to valid and supported GeoJSON geometry
     */
    public GeometryType getGeometryType ()
    {
        return geometryType;
    }

    /**
     * Generate model of collection records' data fields and types
     * 
     * @param coll mongo collection
     * @param buildRule which rule to apply if same named fields with different types exist
     * @return JSON object describing collection record
     */
    private DBObject getCollectionModel (DBCollection coll, RecordBuilder buildRule)
    {
        // call map-reduce job to generate metadata
        // mongo java driver calls mapReduce with the functions rather than the name of the
        // functions
        // function prototypes from scripts/mrscripts/MetaDataCompute.js
        // (do not include comments in quoted javascript functions below-gives mongo error)
        coll.mapReduce( metaMapFunc, metaReduceFunc, metaResultsColl, new BasicDBObject() );

        // get mapping of field names and types, and counts for different types
        DBCollection metaColl = coll.getDB().getCollection( metaResultsColl );
        HashMap<String, ClassCount> fieldMap = getFieldMap( metaColl );
        log.finest( "fieldMap=" + fieldMap );

        // resulting collection may have dupes for fields of different types
        // use build rule to determine final type
        HashMap<String, String> finalMap = finalizeMajorityRule( fieldMap, buildRule );
        log.finest( "finalMap=" + finalMap );

        // convert map of field names with types and associated counts to a JSON DBObject
        DBObject metaData = convertMapToJson( finalMap );
        log.finest( "metaData=" + metaData );

        return metaData;
    }

    /**
     * Get mapping of field names and types, and counts for different types
     * 
     * @param collection where metadata from map-reduce job stored, in format: { "_id" : {
     *            "fieldname" : "geometry.type", "type" : "Point"}, "value" : 2 } { "_id" : {
     *            "fieldname" : "properties.ActivityDescription", "type" : "number" }, "value" : 1 }
     *            { "_id" : { "fieldname" : "properties.ActivityDescription", "type" : "string" },
     *            "value" : 3 } where value is number of occurrences for given type
     * @return mapping of field names to ClassCount holding type and count info
     */
    private HashMap<String, ClassCount> getFieldMap (DBCollection metaResultsColl)
    {
        // cursor over collection
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = metaResultsColl.find( query );
        // avoid cursor timeout
        cursor.addOption( Bytes.QUERYOPTION_NOTIMEOUT );
        // map to store fieldname and ClasCount object holding type and type-count info
        HashMap<String, ClassCount> fieldMap = new HashMap<String, ClassCount>();

        try
        {
            // iterate over each record
            while (cursor.hasNext())
            {
                // check type found for current field
                DBObject currRec = cursor.next();
                DBObject currField = (DBObject) currRec.get( "_id" );
                String fieldName = (String) currField.get( "fieldname" );
                String fieldType = (String) currField.get( "type" );
                int typeCount = ((Double) currRec.get( "value" )).intValue();
                // if first occurrence of field name instantiate counter
                if (!fieldMap.containsKey( fieldName ))
                {
                    fieldMap.put( fieldName, new ClassCount( fieldType, typeCount ) );
                }
                // else increment count for given type
                else
                {
                    ClassCount currCount = fieldMap.get( fieldName );
                    currCount.add( fieldType, typeCount );
                    fieldMap.put( fieldName, currCount );
                }
            }
        }
        finally
        {
            // need to explicitly release cursor since notimeout option set
            cursor.close();
        }

        return fieldMap;
    }

    /**
     * Apply build rule to determine final type
     * 
     * @param fieldMap map holding field name and type data
     * @param buildRule build rule to apply; convert conflicts to String, use
     * @return mapping of field names to Java Classes
     */
    private HashMap<String, String> finalizeMajorityRule (HashMap<String, ClassCount> fieldMap,
                                                          RecordBuilder buildRule)
    {
        HashMap<String, String> finalMap = new HashMap<String, String>();

        for (String field : fieldMap.keySet())
        {
            String finalClass = fieldMap.get( field ).getMajorityClass( field, buildRule );
            finalMap.put( field, finalClass );
        }

        return finalMap;
    }

    /**
     * Convert map of field names and Java Classes to JSON representation
     * 
     * @param finalMap map with field names and types
     * @return metadata GeoJSON representation as DBObject
     */
    private DBObject convertMapToJson (HashMap<String, String> finalMap)
    {
        BasicDBObject metaData = new BasicDBObject();

        // add geometry type
        BasicDBObject geometry = new BasicDBObject();
        geometry.append( "type", finalMap.get( "geometry.type" ) );
        metaData.append( "geometry", geometry );

        // add properties
        BasicDBObject properties = new BasicDBObject();
        properties = (BasicDBObject) recreateJson( "properties", properties, finalMap );
        metaData.append( "properties", properties );

        return metaData;
    }

    /**
     * Build JSON object from map of property names and types
     * 
     * @param baseName base name, e.g. "properties"
     * @param base base object to store results, either BasicDBObject or BasicBSONList
     * @param fieldMap map of Java Class names indexed by field name
     * @return BSONObject object, either JSON or Array
     */
    private BSONObject recreateJson (String baseName, Object base, HashMap<String, String> fieldMap)
    {
        // strip relevant field names corresponding to required property
        HashMap<String, String> propMap = new HashMap<String, String>();
        for (String key : fieldMap.keySet())
        {
            if (key.startsWith( baseName + "." ))
            {
                String propKey = key.substring( baseName.length() + 1 );
                propMap.put( propKey, fieldMap.get( key ) );
            }
        }

        // convert propMap to appropriate object; either BasicDBObject (JSON) or BasicBSONList
        // (Array)
        BSONObject json = null;
        if (base instanceof BasicDBObject)
        {
            json = new BasicDBObject();
        }
        else if (base instanceof BasicBSONList)
        {
            json = new BasicBSONList();
        }
        else
        {
            log.warning( "Error, can only process BasicDBObject (JSON) or BasicBSONList (Array), base is a "
                    + base.getClass() );
            return new BasicBSONObject();
        }

        for (String propKey : propMap.keySet())
        {
            if (!propKey.contains( "." ))
            {
                // ignore nulls
                if (propMap.get( propKey ) != null)
                {
                    // check for nested JSON or Array (BasicDBObject or BasicBSONList)
                    if (propMap.get( propKey ).equals( "com.mongodb.BasicDBObject" ))
                    {
                        BasicDBObject subJSON = new BasicDBObject();
                        json.put( propKey, recreateJson( propKey, subJSON, propMap ) );
                    }
                    else if (propMap.get( propKey ).equals( "com.mongodb.BasicDBList" ))
                    {
                        BasicBSONList subArray = new BasicBSONList();
                        json.put( propKey, recreateJson( propKey, subArray, propMap ) );
                    }
                    else
                    {
                        try
                        {
                            json.put( propKey, Class.forName( propMap.get( propKey ) )
                                    .newInstance() );
                        }
                        catch (InstantiationException ie)
                        {
                            // Number subclasses no nullary cons; use constructor that takes String
                            // arg.
                            try
                            {
                                json.put( propKey, (Class.forName( propMap.get( propKey ) )
                                        .getConstructor( String.class )).newInstance( "0" ) );
                            }
                            catch (Exception e)
                            {
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            }
        }

        return json;
    }

    /**
     * Simple object to keep count of Classes and associated counts when merging existing metadata
     * with new, incoming metadata from collection's current record
     * 
     * @author Alan Mangan
     */
    private class ClassCount
    {
        /** Map to track counts of given classes */
        private HashMap<String, Integer> classMap = new HashMap<String, Integer>();

        /**
         * Initialize ClassCount with given initial Class
         * 
         * @param initClass
         */
        public ClassCount (String initClass, int initCount)
        {
            classMap.put( initClass, initCount );
        }

        /**
         * Add count for given Class
         * 
         * @param newClass
         */
        public void add (String newClass, int newCount)
        {
            if (classMap.containsKey( newClass ))
            {
                int currCount = classMap.get( newClass );
                classMap.put( newClass, currCount + newCount );
            }
            else
            {
                classMap.put( newClass, newCount );
            }
        }

        /**
         * Return name of Class with max number occurrences
         * 
         * @param propKey original property key, "geometry.type" needs special handling
         * @param buildRule build rule to apply; use majority, or convert all to Strings
         * @return name of Class with max occurrences
         */
        public String getMajorityClass (String propKey, RecordBuilder buildRule)
        {
            int max = -1;
            String maxClass = null;

            Set<String> keys = classMap.keySet();

            // if more than one type, and build rule is String just return String type
            if (keys.size() > 1 && buildRule.equals( RecordBuilder.STRING )
                    && !keys.contains( "geometry.type" ))
            {
                maxClass = String.class.getCanonicalName();
            }
            else
            {
                for (String currClass : keys)
                {
                    if (classMap.get( currClass ) > max)
                    {
                        max = classMap.get( currClass );
                        maxClass = currClass;
                    }
                }

                // determine class for "normal" property, preserve actual type (Point etc.) for
                // geometry.type
                if (!propKey.equals( "geometry.type" ))
                {
                    maxClass = classNameMap.get( maxClass );
                }
            }

            return maxClass;
        }

        @Override
        public String toString ()
        {
            return classMap.toString();
        }
    }

    /**
     * Set geo type for this layer based on metadata JSON obj (GeometryType.Unknown if cannot
     * determine)
     * 
     * @param metaData JSON object with geometry.type defined
     */
    private void setGeometryType (DBObject metaData)
    {
        try
        {
            // determine geometry type
            String geoTypeStr = (String) ((DBObject) metaData.get( "geometry" )).get( "type" );
            geometryType = GeometryType.valueOf( geoTypeStr );
        }
        catch (Throwable t)
        {
            geometryType = GeometryType.Unknown;
        }
    }

}
