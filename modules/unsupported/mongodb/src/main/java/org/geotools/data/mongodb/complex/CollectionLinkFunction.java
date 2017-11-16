/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

/**
 * Function used to chain an entity with a sub collection.
 */
public final class CollectionLinkFunction extends FunctionExpressionImpl {

    private static final FunctionName DEFINITION = new FunctionNameImpl(
            "collectionLink", parameter("value", String.class), parameter("path", String.class));

    public CollectionLinkFunction() {
        super(DEFINITION);
    }

    public Object evaluate(Object object) {
        String path = (String) this.params.get(0).evaluate(object);
        return new LinkCollection(path);
    }

    /**
     * Contains information about a linked collection.
     */
    static final class LinkCollection {

        private final String collectionPath;

        LinkCollection(String collectionPath) {
            this.collectionPath = collectionPath;
        }

        String getCollectionPath() {
            return collectionPath;
        }
    }
}
