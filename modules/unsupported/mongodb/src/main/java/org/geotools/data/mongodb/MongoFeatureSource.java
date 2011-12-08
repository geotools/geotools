package org.geotools.data.mongodb;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.mongodb.BasicDBObject;

/**
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
public class MongoFeatureSource implements SimpleFeatureSource
{

    private MongoDataStore    store;
    private MongoLayer        layer     = null;
    private MongoResultSet    results   = null;
    private MongoQueryCaps    queryCaps = new MongoQueryCaps();
    private MongoResourceInfo info      = null;

    public MongoFeatureSource (MongoDataStore store, MongoLayer layer)
    {
        init( store, layer, null );
    }

    public MongoFeatureSource (MongoDataStore store, MongoLayer layer, BasicDBObject dbo)
    {
        init( store, layer, dbo );
    }

    private void init (MongoDataStore store, MongoLayer layer, BasicDBObject dbo)
    {
        this.store = store;
        this.layer = layer;
        BasicDBObject query = dbo;
        if (query == null)
        {
            query = new BasicDBObject();
        }
        results = new MongoResultSet( layer, query );
        info = new MongoResourceInfo( this );
    }

    public MongoLayer getLayer ()
    {
        return layer;
    }

    public ReferencedEnvelope getBounds ()
    {
        return results.getBounds();
    }

    public Set<String> getKeywords ()
    {
        return layer.getKeywords();
    }

    public final Set<RenderingHints.Key> getSupportedHints ()
    {
        return Collections.emptySet();
    }

    public final int getCount (final Query query)
    {
        int res = 0;
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        Filter filter = query.getFilter();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoResultSet rs = new MongoResultSet( layer, dbo );
        res = rs.getCount();
        return res;
    }

    public final ReferencedEnvelope getBounds (final Query query)
    {
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        Filter filter = query.getFilter();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoResultSet rs = new MongoResultSet( layer, dbo );
        return rs.getBounds();
    }

    public final SimpleFeatureCollection getFeatures ()
    {
        return new MongoFeatureCollection( results );
    }

    public final SimpleFeatureCollection getFeatures (final Filter filter)
    {
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoResultSet rs = new MongoResultSet( layer, dbo );
        return new MongoFeatureCollection( rs );
    }

    public final SimpleFeatureCollection getFeatures (final Query query)
    {
        FilterToMongoQuery f2m = new FilterToMongoQuery();
        Filter filter = query.getFilter();
        BasicDBObject dbo = (BasicDBObject) filter.accept( f2m, null );
        MongoResultSet rs = new MongoResultSet( layer, dbo );
        // check for paging; maxFeatures and/or startIndex
        int maxFeatures = query.getMaxFeatures();
        if (maxFeatures > 0)
        {
            int startIndex = 0;
            if (query.getStartIndex() != null)
            {
                startIndex = query.getStartIndex().intValue();
            }
            rs.paginateFeatures( startIndex, maxFeatures );
        }
        return new MongoFeatureCollection( rs );
    }

    public final SimpleFeatureType getSchema ()
    {
        return layer.getSchema();
    }

    public final void addFeatureListener (final FeatureListener listener)
    {
        store.addListener( this, listener );
    }

    public final void removeFeatureListener (final FeatureListener listener)
    {
        store.removeListener( this, listener );
    }

    public final DataStore getDataStore ()
    {
        return store;
    }

    public QueryCapabilities getQueryCapabilities ()
    {
        return queryCaps;
    }

    public ResourceInfo getInfo ()
    {
        return info;
    }

    public Name getName ()
    {
        return layer.getSchema().getName();
    }
}
