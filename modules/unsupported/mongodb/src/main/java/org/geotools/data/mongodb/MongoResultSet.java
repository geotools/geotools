package org.geotools.data.mongodb;

import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.BSONObject;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.geotools.data.mongodb.MongoLayer.GeometryType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Handles conversion of GeoServer query results from mongo GeoJSON format back to GeoServer
 * compatible features
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
public class MongoResultSet
{

    private MongoLayer                   layer      = null;
    private ArrayList<SimpleFeature>     features   = null;
    private ReferencedEnvelope           bounds     = null;
    double                               minX       = 180;
    double                               maxX       = -180;
    double                               minY       = 90;
    double                               maxY       = -90;
    /** Package logger */
    static private final Logger          log        = MongoPluginConfig.getLog();

    static private final PrecisionModel  pm         = new PrecisionModel();
    /** GeometryFactory with given precision model */
    static private final GeometryFactory geoFactory = new GeometryFactory( pm, -1 );

    public MongoResultSet (MongoLayer layer, BasicDBObject query)
    {
        this.layer = layer;
        bounds = new ReferencedEnvelope( 0, 0, 0, 0, layer.getCRS() );
        features = new ArrayList<SimpleFeature>();
        if (query != null)
        {
            buildFeatures( query );
        }
    }

    /**
     * Build features for given layer; convert mongo collection records to equivalent geoTools
     * SimpleFeatureBuilder
     * 
     * @param query mongoDB query (empty to find all)
     */
    private void buildFeatures (BasicDBObject query)
    {
        if (layer == null)
        {
            log.warning( "buildFeatures called, but layer is null" );
            return;
        }
        Mongo mongo = null;
        try
        {
            if (layer.getGeometryType() == null)
            {
                return;
            }
            mongo = new Mongo( layer.getConfig().getHost(), layer.getConfig().getPort() );
            DB db = mongo.getDB( layer.getConfig().getDB() );
            DBCollection coll = db.getCollection( layer.getName() );
            DBCursor cur = coll.find( query );
            minX = 180;
            maxX = -180;
            minY = 90;
            maxY = -90;
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder( layer.getSchema() );
            // use SimpleFeatureBuilder.set(name, value) rather than add(value) since
            // attributes not in guaranteed order
            log.finer( "cur.count()=" + cur.count() );

            while (cur.hasNext())
            {
                DBObject dbo = cur.next();
                if (dbo == null)
                {
                    continue;
                }

                // get mongo id and ensure valid
                if (dbo.get( "_id" ) instanceof ObjectId)
                {
                    ObjectId oid = (ObjectId) dbo.get( "_id" );
                    fb.set( "_id", oid.toString() );
                }
                else if (dbo.get( "_id" ) instanceof String)
                {
                    fb.set( "_id", dbo.get( "_id" ) );
                }
                else
                {
                    throw new MongoPluginException( "_id is invalid type: "
                            + dbo.get( "_id" ).getClass() );
                }

                // ensure geometry defined
                DBObject geo = (DBObject) dbo.get( "geometry" );
                if (geo == null || geo.get( "type" ) == null
                        || (geo.get( "coordinates" ) == null && geo.get( "geometries" ) == null))
                {
                    continue;
                }

                // GeometryType of current record
                GeometryType recordGeoType = GeometryType.valueOf( (String) geo.get( "type" ) );
                // skip record if its geo type does not match layer geo type
                if (!layer.getGeometryType().equals( recordGeoType ))
                {
                    continue;
                }

                // create Geometry for given type
                Geometry recordGeometry = createGeometry( recordGeoType, geo );
                if (recordGeometry != null)
                {
                    fb.set( "geometry", recordGeometry );
                    // set non-geometry properties for feature (GeoJSON.properties)
                    DBObject props = (DBObject) dbo.get( "properties" );
                    setProperties( fb, "properties", props );
                    features.add( fb.buildFeature( null ) );
                    bounds = new ReferencedEnvelope( minX, maxX, minY, maxY, layer.getCRS() );
                }
                else
                {
                    fb.reset();
                }
            }
        }
        catch (Throwable t)
        {
            log.severe( "Error building layer " + layer.getName() + "; " + t.getLocalizedMessage() );
        }
        if (mongo != null)
        {
            mongo.close();
        }
    }

    /**
     * Set non-geometry properties for feature (GeoJSON.properties)
     * 
     * @param fb SimpleFeatureBuilder, properties defined in dotted notation, e.g.
     *            "properties.name", \ "properties.nested.attr" etc.
     * @param base property name (called recursively, "properties" first time through)
     * @param dbo JSON (BasicDBObject) or Array (BasicBSONList) object
     */
    @SuppressWarnings("rawtypes")
    private void setProperties (SimpleFeatureBuilder fb, String base, BSONObject dbo)
    {
        Set<String> cols = dbo.keySet();

        for (String col : cols)
        {
            Object dbcol = dbo.get( col );
            // recurse for nested JSON objects and arrays
            if (dbcol instanceof BasicDBObject || dbcol instanceof BasicBSONList)
            {
                setProperties( fb, base + "." + col, (BSONObject) dbcol );
            }
            else
            {
                Class featureBinding = fb.getFeatureType().getType( base + "." + col ).getBinding();
                Class dboBinding = dbo.get( col ).getClass();
                // set if bindings match
                if (dboBinding.equals( featureBinding ))
                {
                    fb.set( base + "." + col, dbo.get( col ) );
                }
                // if bindings mismatch, but feature binding is String then set using toString()
                // or if bindings subclass Number then cast (possibly lossy)
                else if (featureBinding.equals( String.class )
                        || (featureBinding.getSuperclass().equals( Number.class ) && dboBinding
                                .getSuperclass().equals( Number.class )))
                {
                    try
                    {
                        fb.set( base + "." + col, dbo.get( col ).toString() );
                    }
                    // ignore nfe if unable to convert
                    catch (NumberFormatException ne)
                    {
                    }
                }
            }
        }
    }

    public SimpleFeatureType getSchema ()
    {
        return layer.getSchema();
    }

    /**
     * Get Feature references by index
     * @param idx
     * @return SimpleFeature, null if idx out of bounds
     */
    public SimpleFeature getFeature (int idx) throws IndexOutOfBoundsException
    {
        if (idx < 0 || idx >= features.size())
            throw new IndexOutOfBoundsException( "Index " + idx + " exceeds features size of "
                    + features.size() );
        return features.get( idx );
    }

    public int getCount ()
    {
        return features.size();
    }

    public ReferencedEnvelope getBounds ()
    {
        return bounds;
    }

    /**
     * Paginate result features using startIndex and maxFeatures
     * 
     * @param startIndex starting index (>= 0)
     * @param maxFeatures max features to return (> 0)
     */
    public void paginateFeatures (int startIndex, int maxFeatures)
    {
        int endIndex = startIndex + maxFeatures;
        if (startIndex >= 0 && maxFeatures > 0 && endIndex < features.size())
        {
            features = new ArrayList<SimpleFeature>( features.subList( startIndex, endIndex ) );
        }
    }

    /**
     * Create a Coordinate from given coordinates list
     * 
     * @param coords list of coords
     * @return Coordinate, may be null if coords invalid
     */
    private Coordinate createCoordinate (BasicDBList coords)
    {
        double x = 0.0;
        double y = 0.0;
        boolean success = true;
        Coordinate coord = null;
        try
        {
            x = Double.parseDouble( coords.get( 0 ).toString() );
            y = Double.parseDouble( coords.get( 1 ).toString() );
            if ((x < -180) || (x > 180))
                success = false;
            if ((y < -90) || (y > 90))
                success = false;
            if (success)
            {
                if (x < minX)
                    minX = x;
                if (x > maxX)
                    maxX = x;
                if (y < minY)
                    minY = y;
                if (y > maxY)
                    maxY = y;
            }
            coord = new Coordinate( x, y );
        }
        catch (Throwable t)
        {
            log.log( Level.SEVERE, t.getLocalizedMessage(), t );
            coord = null;
        }
        return coord;
    }

    /**
     * Create a Point from given coordinates
     * 
     * @param coords list of coords
     * @return Point, may be null if coords invalid
     */
    private Point createPoint (BasicDBList coords)
    {
        Coordinate coord = createCoordinate( coords );
        Point pt = null;
        if (coord != null)
        {
            pt = geoFactory.createPoint( coord );
        }
        return pt;
    }

    /**
     * Create a Polygon from given coordinates
     * 
     * @param polyCoords as mongo BasicDBList, 1st list is outer shell, any subsequent lists inner
     *            holes
     * @return Polygon, may be null if coordinates invalid
     */
    private Polygon createPolygon (BasicDBList polyCoords)
    {
        Vector<ArrayList<Coordinate>> rings = new Vector<ArrayList<Coordinate>>();
        boolean success = true;
        for (Object polys : polyCoords)
        {
            BasicDBList inner = (BasicDBList) polys;
            ArrayList<Coordinate> ring = new ArrayList<Coordinate>();
            for (Object obj : inner)
            {
                BasicDBList aPoint = (BasicDBList) obj;
                Coordinate coord = createCoordinate( aPoint );
                ring.add( coord );
            }
            rings.add( ring );
        } // end outer loop

        // have vector of rings; 1st is outer ring/shell, rest are innner rings/holes
        Polygon poly = null;
        if (success && rings.size() > 0)
        {
            Coordinate[] shellCoords = new Coordinate[rings.get( 0 ).size()];
            shellCoords = rings.get( 0 ).toArray( shellCoords );
            LinearRing shell = geoFactory.createLinearRing( shellCoords );
            LinearRing[] holes = null;
            // construct holes if any present
            if (rings.size() > 1)
            {
                holes = new LinearRing[rings.size() - 1];
                for (int i = 1; i < rings.size(); i++)
                {
                    Coordinate[] holeCoords = new Coordinate[rings.get( i ).size()];
                    holeCoords = rings.get( i ).toArray( holeCoords );
                    holes[i - 1] = geoFactory.createLinearRing( holeCoords );
                }
            }
            poly = geoFactory.createPolygon( shell, holes );
        }
        return poly;
    }

    /**
     * Create a LineString from given coordinates list
     * 
     * @param coords list of coords
     * @return LineString, may be null if coords invalid
     */
    private LineString createLineString (BasicDBList outer)
    {
        Coordinate[] coords = new Coordinate[outer.size()];
        int i = 0;
        for (Object lineCoords : outer)
        {
            coords[i++] = createCoordinate( (BasicDBList) lineCoords );
        }
        LineString lineString = geoFactory.createLineString( coords );
        return lineString;
    }

    /**
     * Create a Geometry object; GeometryCollection, Point, MultiPoint, Polygon etc.
     * 
     * @param type Geometry type to create
     * @param geoElement coordinates
     * @return Geometry, may be null if coordinates null/invalid, or type invalid
     */
    private Geometry createGeometry (GeometryType type, DBObject coordinates)
    {
        Geometry geometryObj = null;

        // GeometryCollection different; has geometries field rather than coordinates
        if (type.equals( GeometryType.GeometryCollection ))
        {
            if (!coordinates.containsField( "geometries" ))
            {
                log.warning( "No geometries detected for GeometryCollection, skipping." );
                return geometryObj;
            }
            BasicDBList geometryList = (BasicDBList) coordinates.get( "geometries" );
            int i = 0;
            Geometry[] geometries = new Geometry[geometryList.size()];
            for (Object geoElement : geometryList)
            {
                String subType = (String) ((BasicDBList) geoElement).get( "type" );
                GeometryType geoType = GeometryType.valueOf( subType );
                geometries[i++] = createGeometry( geoType, (DBObject) geoElement );
            }
            geometryObj = geoFactory.createGeometryCollection( geometries );
        }

        // all other geometry types; Point, Polygon etc.
        else
        {
            if (!coordinates.containsField( "coordinates" ))
            {
                return geoFactory.createPoint( (Coordinate) null );
            }

            BasicDBList coords = (BasicDBList) coordinates.get( "coordinates" );
            int i = 0;

            switch (type)
            {
            case LineString:
                geometryObj = createLineString( coords );
                break;

            case Point:
                geometryObj = createPoint( coords );
                break;

            case Polygon:
                geometryObj = createPolygon( coords );
                break;

            case MultiLineString:
                LineString[] lines = new LineString[coords.size()];
                for (Object lineCoords : coords)
                {
                    lines[i++] = createLineString( (BasicDBList) lineCoords );
                }
                geometryObj = geoFactory.createMultiLineString( lines );
                break;

            case MultiPoint:
                Point[] points = new Point[coords.size()];
                for (Object obj : coords)
                {
                    BasicDBList aPoint = (BasicDBList) obj;
                    points[i++] = createPoint( aPoint );
                }
                geometryObj = geoFactory.createMultiPoint( points );
                break;

            case MultiPolygon:
                Polygon[] polys = new Polygon[coords.size()];
                for (Object polyCoords : coords)
                {
                    polys[i++] = createPolygon( (BasicDBList) polyCoords );
                }
                geometryObj = geoFactory.createMultiPolygon( polys );
                break;
            }
        }

        return geometryObj;
    }

}
