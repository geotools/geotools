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
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A {@link GranuleSource} implementation wrapping a {@link GranuleCatalog}.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class GranuleCatalogSource implements GranuleSource {

    /** The underlying {@link GranuleCatalog} */
    protected GranuleCatalog catalog;

    protected String typeName;

    protected Hints hints;

    public GranuleCatalogSource(GranuleCatalog catalog, final String typeName, final Hints hints) {

        // TODO: once we allow to create different catalogs (based on different featureTypes)
        // we can stop filtering by name
        this.catalog = catalog;
        this.typeName = typeName;
        this.hints = hints;
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        final Query updatedQuery = setupBaseQuery(q);
        return catalog.getGranules(updatedQuery);
    }

    private Query setupBaseQuery(Query q) {
        if (q == null) {
            q = new Query();
        } else {
            q = new Query(q);
        }
        if (hints != null) {
            q.setHints(hints);
        }
        if (q.getTypeName() == null) {
            q.setTypeName(typeName);
        }
        return q;
    }

    @Override
    public int getCount(Query q) throws IOException {
        q = setupBaseQuery(q);
        return catalog.getGranulesCount(q);
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
