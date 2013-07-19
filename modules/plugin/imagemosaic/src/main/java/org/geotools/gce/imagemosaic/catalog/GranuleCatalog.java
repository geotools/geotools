/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

/**
 * 
 *
 * @source $URL$
 */
public abstract class GranuleCatalog {
    
    /**
     * @param hints
     */
    public GranuleCatalog(Hints hints) {
        this.hints = hints;
    }

    protected final Hints hints;

    public void addGranule(final String typeName, final SimpleFeature granule, final Transaction transaction) throws IOException {
            addGranules(typeName, Collections.singleton(granule),transaction);
    }

    public abstract void addGranules(final String typeName, Collection<SimpleFeature> granules, Transaction transaction) throws IOException;
    
    public abstract void computeAggregateFunction(Query q, FeatureCalc function) throws IOException ;

    public abstract void createType(String namespace, String typeName, String typeSpec) throws IOException, SchemaException ;

    public abstract void createType(SimpleFeatureType featureType) throws IOException;

    public abstract void createType(String identification, String typeSpec) throws SchemaException, IOException ;

    public abstract void dispose() ;

    public abstract BoundingBox getBounds(final String typeName) ;

    public abstract SimpleFeatureCollection getGranules(Query q) throws IOException;
    
    public abstract int getGranulesCount(Query q) throws IOException;
    
    public abstract  void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor) throws IOException;
    
    public abstract QueryCapabilities getQueryCapabilities(final String typeName);
    
    public abstract SimpleFeatureType getType(final String typeName) throws IOException ;

    public abstract int removeGranules(Query query);
    
    public abstract String[] getTypeNames() ;


    /**
     * Merges the wrapper hints with the query ones, making sure not to overwrite the query ones
     * 
     * @param q
     * @return
     */
    protected Query mergeHints(Query q) {
        if(this.hints==null||this.hints.isEmpty()){
            return q;
        }
        Query clone = new Query(q);
        Hints hints = clone.getHints();
        if (hints == null || hints.isEmpty()) {
            clone.setHints(this.hints);
        } else {
            for (Entry<Object, Object> entry : this.hints.entrySet()) {
                if (!hints.containsKey(entry.getKey())) {
                    hints.put(entry.getKey(), entry.getValue());
                }
            }
        }
    
        return clone;
    }
}
