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

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gce.imagemosaic.RasterManager;
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

    protected RasterManager manager;

    public GranuleCatalogSource(
            RasterManager manager,
            GranuleCatalog catalog,
            final String typeName,
            final Hints hints) {

        // TODO: once we allow to create different catalogs (based on different featureTypes)
        // we can stop filtering by name
        this.catalog = catalog;
        this.typeName = typeName;
        this.hints = hints;
        this.manager = manager;
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        final Query updatedQuery = setupBaseQuery(q);
        if (Boolean.TRUE.equals(q.getHints().get(GranuleSource.FILE_VIEW))) {
            // FIGURE OUT how to get the paramaters
            return new FileViewCollection(catalog, updatedQuery, manager);
        }
        return catalog.getGranules(updatedQuery);
    }

    private Query setupBaseQuery(Query q) {
        Query baseQuery = new Query();
        baseQuery.setHints(hints);
        baseQuery.setTypeName(typeName);

        if (q == null) {
            return baseQuery;
        } else {
            if (q.getTypeName() != null && !Objects.equals(this.typeName, q.getTypeName())) {
                throw new IllegalArgumentException(
                        "Invalid type name in query "
                                + q.getTypeName()
                                + ", this granule source only returns "
                                + this.typeName);
            }

            return DataUtilities.mixQueries(baseQuery, q, null);
        }
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

    @Override
    public Set<RenderingHints.Key> getSupportedHints() {
        return Collections.singleton(GranuleSource.FILE_VIEW);
    }
}
