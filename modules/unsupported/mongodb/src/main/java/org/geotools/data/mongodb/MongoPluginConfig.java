package org.geotools.data.mongodb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.logging.Logging;

/**
 * Stores config info for mongo geoserver plugin; mongo host, port, DB name etc.
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
public class MongoPluginConfig
{

    private final static String MONGO_HOST_PARAM    = "mongo_host";
    private final static String MONGO_PORT_PARAM    = "mongo_port";
    private final static String MONGO_DB_NAME_PARAM = "mongo_db_name";
    private final static String NAMESPACE_PARAM     = "namespace";
    private final static String MONGO_DB_USERNAME   = "username";
    private final static String MONGO_DB_PASSWORD   = "password";
    private String              mongoHost;
    private int                 mongoPort;
    private String              mongoDB;
    private String              namespace;
    /** Log instance to be shared among package classes */
    private final static Logger log                 = Logging
                                                            .getLogger( "org.geotools.data.mongodb" );
    // requires proper config of data/logging.xml, and addition of org.geotools.data.mongodb package
    // name
    // in log config/props file referenced by logging.xml

    private static List<Param>  mongoParams         = null;

    public static List<Param> getPluginParams ()
    {
        if (mongoParams == null)
        {
            mongoParams = new ArrayList<Param>();
            mongoParams.add( new Param( NAMESPACE_PARAM, String.class,
                                        "Namespace associated with this data store", false ) );
            mongoParams.add( new Param( MONGO_HOST_PARAM, String.class, "MongoDB Server", true,
                                        "localhost" ) );
            mongoParams.add( new Param( MONGO_PORT_PARAM, Integer.class, "MongoDB Port", true,
                                        Integer.valueOf( 27017 ) ) );
            mongoParams.add( new Param( MONGO_DB_NAME_PARAM, String.class, "MongoDB Database",
                                        true, "db" ) );
        }
        return mongoParams;
    }

    public MongoPluginConfig (Map<String, Serializable> params) throws MongoPluginException
    {
        String msg = "Mongo Plugin Configuration Error";

        try
        {
            String param = params.get( NAMESPACE_PARAM ).toString();
            if (param == null)
            {
                msg = "Mongo Plugin: Missing namespace param";
                throw new Exception();
            }
            namespace = param;
            param = params.get( MONGO_HOST_PARAM ).toString();
            if (param == null)
            {
                msg = "Mongo Plugin: Missing server name param";
                throw new Exception();
            }
            mongoHost = param;
            param = params.get( MONGO_PORT_PARAM ).toString();
            if (param == null)
            {
                msg = "Mongo Plugin: Missing port param";
                throw new Exception();
            }
            msg = "Mongo Plugin: Error parsing port param";
            mongoPort = Integer.parseInt( param );
            param = params.get( MONGO_DB_NAME_PARAM ).toString();
            if (param == null)
            {
                msg = "Mongo Plugin: Missing database name param";
                throw new Exception();
            }
            mongoDB = param;
        }
        catch (Throwable t)
        {
            throw new MongoPluginException( msg );
        }
    }

    public String getHost ()
    {
        return mongoHost;
    }

    public int getPort ()
    {
        return mongoPort;
    }

    public String getDB ()
    {
        return mongoDB;
    }

    public String getNamespace ()
    {
        return namespace;
    }

    /**
     * Get logger for use with this package
     * 
     * @return package logger
     */
    public static Logger getLog ()
    {
        return log;
    }

}
