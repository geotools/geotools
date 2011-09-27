/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public abstract class CouchDBViewSupport extends CouchDBClient.Component {
    protected final CouchDBConnection connection;
    
    protected CouchDBViewSupport(CouchDBClient client,CouchDBConnection connection, String path) {
        super(path,client);
        this.connection = connection;
    }
    
    protected JSONObject get(NameValuePair... query) throws IOException, CouchDBException {
        // @todo yuck - shouldn't have to get parent to build uri
        CouchDBResponse resp = client.get(connection.uri(root),query);
        resp.checkOK("Error performing query");
        return resp.getBodyAsJSONObject();
    }
    
}
