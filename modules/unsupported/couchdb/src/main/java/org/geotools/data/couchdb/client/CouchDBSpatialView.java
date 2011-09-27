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
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBSpatialView extends CouchDBViewSupport {

    public CouchDBSpatialView(CouchDBClient client, CouchDBConnection connection, String path) {
        super(client, connection, path);
    }
    
    private NameValuePair bbox(double llx,double lly,double urx,double ury) {
        return new NameValuePair("bbox",llx + "," + lly + "," + urx + "," + ury);
    }
    
    public JSONObject get(double llx,double lly,double urx,double ury) throws IOException, CouchDBException {
        return get(bbox(llx, lly, urx, ury));
    }
    
    public long count(double llx,double lly,double urx,double ury) throws IOException, CouchDBException {
        JSONObject count = get(bbox(llx, lly, urx, ury),new NameValuePair("count","true"));
        return (Long) count.get("count");
    }
    
}
