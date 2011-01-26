/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.geotools.data.Query;

/**
 * @author Christian Mueller
 * 
 * This invocation handler is used for a query proxy passed to the backend feature sources.
 * 
 * The proxy object returns typeName and propertyNames fitting for the backendstore, leaving all
 * other query data unchanged
 * 
 *
 * @source $URL$
 */
public class QueryInvocationHandler implements InvocationHandler {

    String typeName;

    String[] propertyNames;

    Query query;

    public QueryInvocationHandler(Query query, String typeName, String[] propertyNames) {
        super();
        this.query = query;
        this.typeName = typeName;
        this.propertyNames = propertyNames;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("getTypeName".equals(method.getName()) && method.getParameterTypes().length == 0)
            return typeName;

        if ("getPropertyNames".equals(method.getName()) && method.getParameterTypes().length == 0)
            return propertyNames;

        return method.invoke(query, args);

    }

}
