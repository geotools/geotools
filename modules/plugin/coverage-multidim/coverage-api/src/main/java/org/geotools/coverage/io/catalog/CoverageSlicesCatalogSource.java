/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import java.io.IOException;
import java.util.List;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * A {@link GranuleSource} implementation wrapping a {@link CoverageSlicesCatalog}.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class CoverageSlicesCatalogSource implements GranuleSource {

    private CoverageSlicesCatalog innerCatalog;

    private final String typeName;

    public CoverageSlicesCatalogSource(CoverageSlicesCatalog innerCatalog, String typeName) {
        this.innerCatalog = innerCatalog;
        this.typeName = typeName;
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        // TODO: optimize this. It's currently "putting" all the features. No iterator is used.

        // Filtering by typeName
        if (q == null) {
            q = new Query(typeName);
        } else {
            q.setTypeName(typeName);
        }

        Filter filter = q.getFilter();
        q.setFilter(filter);
        List<CoverageSlice> granules = innerCatalog.getGranules(q);
        SimpleFeatureCollection collection =
                new ListFeatureCollection(innerCatalog.getSchema(typeName));
        for (CoverageSlice granule : granules) {
            ((ListFeatureCollection) collection).add(granule.getOriginator());
        }
        return collection;
    }

    @Override
    public int getCount(Query q) throws IOException {
        // TODO: quick implementation. think about something less expensive
        return innerCatalog.getGranules(q).size();
    }

    @Override
    public ReferencedEnvelope getBounds(Query q) throws IOException {
        // TODO: quick implementation. think about something less expensive
        return getGranules(q).getBounds();
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return innerCatalog.getSchema(typeName);
    }

    @Override
    public void dispose() throws IOException {
        // do nothing
    }
}
