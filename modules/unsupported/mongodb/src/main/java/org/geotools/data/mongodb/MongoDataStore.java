package org.geotools.data.mongodb;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.types.BasicBSONList;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.EmptyFeatureWriter;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.mongodb.MongoLayer.GeometryType;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Represents a mongoDB-backed GeoServer Data Store; manages layers representing mongoDB collections
 * containing GeoJSON-encoded geospatial data
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
public class MongoDataStore implements DataStore
{

    /** List of mongo layers for this mongo store */
    private ArrayList<MongoLayer>     layers = null;
    private CoordinateReferenceSystem crs    = null;
    /** Config for this mongo plugin */
    private MongoPluginConfig         config = null;
    private FeatureListenerManager    lsnMgr = null;

    /** Package logger */
    private final static Logger       log    = MongoPluginConfig.getLog();

    public MongoDataStore (MongoPluginConfig config)
    {
        this.config = config;
        lsnMgr = new FeatureListenerManager();
        layers = new ArrayList<MongoLayer>();
        log.info( "MongoDataStore; layers=" + layers );
        try
        {
            crs = CRS.decode( "EPSG:4326" );
        }
        catch (Throwable t)
        {
            crs = DefaultGeographicCRS.WGS84;
        }
        // TODO when to look for and detect changes to layers
        if (layers.size() == 0)
        {
            getLayers();
        }
    }

    /**
     * Get list of valid layers for this mongo DB; those containing at least one valid, non-null
     * GeoJSON geometry
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void getLayers ()
    {
        Mongo mongo = null;
        try
        {
            // Get the list of collections from Mongo...
            mongo = new Mongo( config.getHost(), config.getPort() );
            DB db = mongo.getDB( config.getDB() ); // TODO add authentication
            Set<String> colls = db.getCollectionNames();
            for (String s : colls)
            {
                DBCollection dbc = db.getCollection( s );
                log.info( "getLayers; collection=" + dbc );
                // find distinct non-null geometry to determine if valid layer
                // TODO branch point for separate geometry-specific layers per collection
                List geoList = dbc.distinct( "geometry.type" );
                // distinct returns single BSON List, may barf if results large, > max doc. size
                // trap exception on props distinct and assume it's valid since there's obviously
                // something there (http://www.mongodb.org/display/DOCS/Aggregation)
                List propList = null;
                try
                {
                    propList = dbc.distinct( "properties" );
                }
                catch (IllegalArgumentException ex)
                {
                    propList = new BasicBSONList();
                    propList.add( "ex nihilo" );
                }
                catch (MongoException ex)
                {
                    propList = new BasicBSONList();
                    propList.add( "ex nihilo" );
                }
                // check that layer has valid geometry and some properties defined
                if (geoList != null && propList != null && propList.size() > 0)
                {
                    boolean hasValidGeo = false;
                    for (GeometryType type : GeometryType.values())
                    {
                        if (geoList.contains( type.toString() ))
                        {
                            hasValidGeo = true;
                            break;
                        }
                    }
                    if (hasValidGeo)
                    {
                        layers.add( new MongoLayer( dbc, config ) );
                    }
                }
            }
        }
        catch (Throwable t)
        {
            log.severe( "getLayers error; " + t.getLocalizedMessage() );
        }
        if (mongo != null)
        {
            mongo.close();
        }
    }

    public CoordinateReferenceSystem getCRS ()
    {
        return crs;
    }

    public MongoPluginConfig getConfig ()
    {
        return config;
    }

    public void addListener (FeatureSource<?, ?> src, FeatureListener listener)
    {
        lsnMgr.addFeatureListener( src, listener );
    }

    public void removeListener (FeatureSource<?, ?> src, FeatureListener listener)
    {
        lsnMgr.removeFeatureListener( src, listener );
    }

    public Set<String> getKeywords (String typeName)
    {
        Set<String> result = null;

        for (MongoLayer ml : layers)
        {
            if (ml.getName().equals( typeName ))
            {
                result = ml.getKeywords();
                break;
            }
        }

        return result;
    }

    public LockingManager getLockingManager ()
    {
        // returning null as per DataStore.getLockingManager() contract
        return null;
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend (final String typeName,
                                                                                   final Transaction transaction)
    {
        return new EmptyFeatureWriter( new SimpleFeatureTypeBuilder().buildFeatureType() );
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter (final String typeName,
                                                                             final Transaction transaction)
    {
        return new EmptyFeatureWriter( new SimpleFeatureTypeBuilder().buildFeatureType() );
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter (final String typeName,
                                                                             final Filter filter,
                                                                             final Transaction transaction)
    {
        return new EmptyFeatureWriter( new SimpleFeatureTypeBuilder().buildFeatureType() );
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader (final Query query,
                                                                             final Transaction transaction)
    {
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        Filter filter = query.getFilter();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoLayer layer = getMongoLayer( query.getTypeName() );
        MongoResultSet rs = new MongoResultSet( layer, dbo );
        return new MongoFeatureReader( rs );
    }

    public SimpleFeatureSource getFeatureSource (final String typeName) throws IOException
    {
        MongoLayer layer = getMongoLayer( typeName );
        return new MongoFeatureSource( this, layer );
    }

    public FeatureSource<SimpleFeatureType, SimpleFeature> getView (final Query query)
    {
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        Filter filter = query.getFilter();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoLayer layer = getMongoLayer( query.getTypeName() );
        return new MongoFeatureSource( this, layer, dbo );
    }

    public SimpleFeatureType getSchema (final String typeName)
    {
        SimpleFeatureType sft = null;

        for (MongoLayer ml : layers)
        {
            if (ml.getName().equals( typeName ))
            {
                sft = ml.getSchema();
            }
        }

        return sft;
    }

    public String[] getTypeNames ()
    {
        String[] names = new String[layers.size()];
        int idx = 0;
        for (MongoLayer ml : layers)
        {
            names[idx++] = ml.getName();
        }
        return names;
    }

    public void updateSchema (final String typeName, final SimpleFeatureType featureType)
                                                                                         throws IOException
    {
        throw new UnsupportedOperationException( "Schema modification not supported" );
    }

    public void dispose ()
    {

    }

    public SimpleFeatureSource getFeatureSource (Name name) throws IOException
    {
        return getFeatureSource( name.getLocalPart() );
    }

    public SimpleFeatureType getSchema (Name name) throws IOException
    {
        return getSchema( name.getLocalPart() );
    }

    public List<Name> getNames () throws IOException
    {
        List<Name> names = new ArrayList<Name>( layers.size() );
        for (MongoLayer ml : layers)
        {
            names.add( new NameImpl( ml.getName() ) );
        }
        return names;
    }

    public void updateSchema (Name typeName, SimpleFeatureType featureType) throws IOException
    {
        updateSchema( typeName.getLocalPart(), featureType );
    }

    public void createSchema (final SimpleFeatureType featureType) throws IOException,
                                                                  IllegalArgumentException
    {

    }

    public ServiceInfo getInfo ()
    {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setTitle( "MongoDB Data Store" );
        info.setDescription( "Features from MongoDB" );
        try
        {
            info.setSchema( new URI( config.getNamespace() ) );
        }
        catch (Throwable t)
        {
        }
        return info;
    }

    public MongoLayer getMongoLayer (String typeName)
    {
        MongoLayer layer = null;
        for (MongoLayer ml : layers)
        {
            if (ml.getName().equals( typeName ))
            {
                layer = ml;
                break;
            }
        }
        return layer;
    }

}
