package org.geotools.data.mongodb;

import java.awt.RenderingHints;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

/**
 * 
 * @author Gerald Gay, Data Tactics Corp.
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
public class MongoDataStoreFactory implements DataStoreFactorySpi
{

    private static final String FACTORY_DESCRIPTION  = "MongoDB GeoServer Plugin";
    private static final String FACTORY_DISPLAY_NAME = "MongoDB";
    /** Package logger */
    static private final Logger log                  = MongoPluginConfig.getLog();

    public DataStore createNewDataStore (Map<String, Serializable> map)
    {
        return createDataStore( map );
    }

    public DataStore createDataStore (Map<String, Serializable> params)
    {
        DataStore theStore = null;
        log.info( "DataStore.createDataStore()" );
        try
        {
            MongoPluginConfig config = new MongoPluginConfig( params );
            theStore = new MongoDataStore( config );
            log.info( "DataStore.createDataStore(); theStore=" + theStore );
        }
        catch (Throwable t)
        {
            log.severe( t.getLocalizedMessage() );
        }
        return theStore;
    }

    public boolean isAvailable ()
    {
        boolean result = false;
        try
        {
            // basic check to ensure mongo jar available
            Class.forName( "com.mongodb.BasicDBObject" );
            result = true;
        }
        catch (Throwable t)
        {
            log.severe( "Mongo Plugin: The MongoDB JAR file was not found on the class path." );
        }
        return result;
    }

    public boolean canProcess (Map<String, Serializable> params)
    {

        boolean result = true;

        try
        {
            new MongoPluginConfig( params );
        }
        catch (MongoPluginException e)
        {
            result = false;
        }

        return result;
    }

    public DataStoreFactorySpi.Param[] getParametersInfo ()
    {
        List<Param> params = MongoPluginConfig.getPluginParams();
        return params.toArray( new Param[params.size()] );
    }

    public String getDescription ()
    {
        return FACTORY_DESCRIPTION;
    }

    public String getDisplayName ()
    {
        return FACTORY_DISPLAY_NAME;
    }

    public Map<RenderingHints.Key, ?> getImplementationHints ()
    {
        return Collections.emptyMap();
    }
}
