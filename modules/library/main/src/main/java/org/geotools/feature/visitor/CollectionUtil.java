/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 * @source $URL$
 */
public class CollectionUtil {
    /**
     * Navigate the collection and call vistor.visit( Feature ) for each
     * element in the collection.
     *
     * @param collection the FeatureCollection<SimpleFeatureType, SimpleFeature> containing the features we want to visit
     * @param visitor the visitor which already knows which attributes it wants to meet
     */
    static void accept(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, FeatureVisitor visitor) {
        Iterator iterator;

        for (iterator = collection.iterator(); iterator.hasNext();) {
            SimpleFeature feature = (SimpleFeature) iterator.next();
            visitor.visit(feature);
        }

        collection.close(iterator);
    }

    static void accept(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, FeatureVisitor[] visitors) {
        Iterator iterator;

        for (iterator = collection.iterator(); iterator.hasNext();) {
        	SimpleFeature feature = (SimpleFeature) iterator.next();

            for (int i = 0; i < visitors.length; i++) {
                FeatureVisitor visitor = visitors[i];
                visitor.visit(feature);
            }
        }

        collection.close(iterator);
    }

    public static Object calc(FeatureCollection<SimpleFeatureType, SimpleFeature> collection,
        FeatureCalc calculator) {
        accept(collection, calculator);

        return calculator.getResult();
    }
}
