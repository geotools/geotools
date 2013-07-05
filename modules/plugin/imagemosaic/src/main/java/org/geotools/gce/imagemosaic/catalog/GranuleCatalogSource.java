/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.resources.coverage.FeatureUtilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * A {@link GranuleSource} implementation wrapping a {@link GranuleCatalog}.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
public class GranuleCatalogSource implements GranuleSource {

    private final static FilterFactory2 FF = FeatureUtilities.DEFAULT_FILTER_FACTORY;
    
    /** The underlying {@link GranuleCatalog} */
    protected GranuleCatalog catalog;

    protected String typeName;
    
    protected Hints hints;
    
    public GranuleCatalogSource(GranuleCatalog catalog, final String typeName, final Hints hints) {
        super();
        //TODO: once we allow to create different catalogs (based on different featureTypes) 
        // we can stop filtering by name 
        this.catalog = catalog;
        this.typeName = typeName;
        this.hints = hints;
    }
    
    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        Query updatedQuery = setupBaseQuery(q);
        
        // TODO: Optimize me
        Collection<GranuleDescriptor> granules = catalog.getGranules(updatedQuery);
        SimpleFeatureCollection collection = new ListFeatureCollection(catalog.getType(typeName));
        for (GranuleDescriptor granule: granules) {
            ((ListFeatureCollection)collection).add(granule.getOriginator());
        }
        return collection;
    }

    private Query setupBaseQuery(Query q) {
        if (q == null) {
            q = new Query();
        } else  {
            q = new Query(q);
        }
        if (hints != null) {
            q.setHints(hints);
        }
        if(q.getTypeName() == null) {
            q.setTypeName(typeName);
        }
        return q;
    }

    @Override
    public int getCount(Query q) throws IOException {
        q = setupBaseQuery(q);
        //TODO Optimize this call
        Collection<GranuleDescriptor> granules = catalog.getGranules(q);
        return granules.size();
    }

    @Override
    public ReferencedEnvelope getBounds(Query q) throws IOException {
        // TODO Optimize this call
        return getGranules(q).getBounds();
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return catalog.getType(typeName);
    }

    @Override
    public void dispose() throws IOException {
        // TODO: check if we need to dispose it or not
        // Does nothing, the catalog should be disposed by the user
        
    }

}
