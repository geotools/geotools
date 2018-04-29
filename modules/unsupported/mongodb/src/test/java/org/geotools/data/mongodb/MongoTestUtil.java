/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mongodb.DBObject;

/** @author tkunicki@boundlessgeo.com */
public class MongoTestUtil {

    public static String prettyPrint(DBObject dbo) {
        return prettyPrint(dbo.toString());
    }

    public static String prettyPrint(String json) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(json));
    }
}
