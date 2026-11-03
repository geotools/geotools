/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.filter.spatial.DefaultCRSFilterVisitor;
import org.geotools.filter.spatial.ReprojectingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;

/** Moves queries and granules of a granule catalog type between its index CRS and the mosaic CRS. */
final class IndexReprojection {

    /** No reprojection: the index is in the mosaic CRS, or either CRS is unknown. Every method returns its input. */
    static final IndexReprojection NONE = new IndexReprojection(null, null, null);

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private final SimpleFeatureType indexSchema;
    private final CoordinateReferenceSystem mosaicCRS;
    private final SimpleFeatureType mosaicSchema;

    private IndexReprojection(
            SimpleFeatureType indexSchema, CoordinateReferenceSystem mosaicCRS, SimpleFeatureType mosaicSchema) {
        this.indexSchema = indexSchema;
        this.mosaicCRS = mosaicCRS;
        this.mosaicSchema = mosaicSchema;
    }

    /** Reprojection from the declared CRS of the index schema to the mosaic CRS, {@link #NONE} if not needed. */
    static IndexReprojection create(CoordinateReferenceSystem mosaicCRS, SimpleFeatureType indexSchema)
            throws IOException {
        CoordinateReferenceSystem indexCRS = indexSchema.getCoordinateReferenceSystem();
        if (mosaicCRS == null || indexCRS == null) return NONE;
        try {
            // same code in another axis order: read the index as stored, as mosaics always did
            Integer indexCode = CRS.lookupEpsgCode(indexCRS, false);
            if (indexCode != null && indexCode.equals(CRS.lookupEpsgCode(mosaicCRS, false))) return NONE;
            // also catches equivalent CRS with different metadata (e.g. ESRI vs EPSG WKT) via an identity transform
            if (!CRS.isTransformationRequired(indexCRS, mosaicCRS)) return NONE;
            return new IndexReprojection(indexSchema, mosaicCRS, FeatureTypes.transform(indexSchema, mosaicCRS));
        } catch (FactoryException | SchemaException e) {
            throw new IOException(
                    "Unable to compare the index CRS of " + indexSchema.getTypeName() + " with the mosaic one", e);
        }
    }

    /** Moves the spatial filters of a query expressed in the mosaic CRS to the index CRS. */
    Query toIndex(Query query) {
        if (this == NONE) return query;
        // geometries without a CRS are expressed in the mosaic CRS, the one granules are exposed in
        Filter filter = (Filter) query.getFilter().accept(new DefaultCRSFilterVisitor(FF, mosaicCRS), null);
        Query result = new Query(query);
        result.setFilter((Filter) filter.accept(new ReprojectingFilterVisitor(FF, indexSchema), null));
        return result;
    }

    /** Moves granules built in the mosaic CRS to the index CRS, for writing. */
    SimpleFeatureCollection toIndex(SimpleFeatureCollection granules) {
        if (this == NONE) return granules;
        return new ReprojectingFeatureCollection(granules, mosaicCRS, indexSchema.getCoordinateReferenceSystem());
    }

    /** Exposes granules read from the index in the mosaic CRS. */
    SimpleFeatureCollection toMosaic(SimpleFeatureCollection granules) {
        // property selections may leave out the geometry, nothing to reproject then
        if (this == NONE || granules.getSchema().getGeometryDescriptor() == null) return granules;
        return new ReprojectingFeatureCollection(granules, mosaicCRS);
    }

    /** Moves index bounds to the mosaic CRS, null and empty bounds are returned as they are. */
    ReferencedEnvelope toMosaic(ReferencedEnvelope bounds) throws IOException {
        if (this == NONE || bounds == null || bounds.isEmpty()) return bounds;
        try {
            // stores may return bounds without a CRS
            return new ReferencedEnvelope(bounds, indexSchema.getCoordinateReferenceSystem())
                    .transform(mosaicCRS, true);
        } catch (TransformException | FactoryException e) {
            throw new IOException("Unable to transform the index bounds to the mosaic CRS", e);
        }
    }

    /** Schema of the granules returned by {@link #toMosaic(SimpleFeatureCollection)}. */
    SimpleFeatureType mosaicSchema(SimpleFeatureType schema) {
        return this == NONE ? schema : mosaicSchema;
    }
}
