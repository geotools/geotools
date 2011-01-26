/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Visitor;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A visitor that collects features into 
 * a In-Memory FeatureCollection 
 * 
 *
 * @source $URL$
 */
public class FeatureCollectingVisitor implements Visitor {
    SimpleFeatureCollection fc;
    int visited_nodes = 0;

    public FeatureCollectingVisitor(SimpleFeatureType type) {
        fc = new DefaultFeatureCollection("FeatureCollectingVisitor", type);
    }

    /**
     * @param d Must be a SimpleFeature 
     */
    public void visitData(Data<?> d) {
        fc.add((SimpleFeature) d.getData());
    }

    public void visitNode(Node n) {
        visited_nodes++;
    }

    /**
     * @return the collection of features visited
     */
    public SimpleFeatureCollection getCollection() {
        return fc;
    }

    /**
     * @return the number of nodes visited
     */
    public int getVisitedNodes() {
        return visited_nodes;
    }

    /**
     * @returns true as this feature collection does something with the data it visits
     */
    public boolean isDataVisitor() {
        return true;
    }
}
