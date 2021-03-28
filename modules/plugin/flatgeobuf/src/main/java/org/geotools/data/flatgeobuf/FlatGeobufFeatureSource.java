/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.wololo.flatgeobuf.geotools.ColumnMeta;
import org.wololo.flatgeobuf.geotools.FeatureTypeConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

/**
 * A {@link FeatureSource} for FlatGeobuf based on {@link ContentFeatureSource}
 *
 * @author Björn Harrtell
 */
class FlatGeobufFeatureSource extends ContentFeatureSource {

    static final Logger LOGGER = Logging.getLogger(FlatGeobufFeatureSource.class);

    final URL url;
    final HeaderMeta headerMeta;

    public FlatGeobufFeatureSource(ContentEntry entry, URL url, HeaderMeta headerMeta) {
        super(entry, Query.ALL);
        this.url = url;
        this.headerMeta = headerMeta;
    }

    @Override
    public FlatGeobufDataStore getDataStore() {
        return (FlatGeobufDataStore) super.getDataStore();
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE) return null;
        Envelope env = headerMeta.envelope;
        if (env == null) return null;
        CoordinateReferenceSystem crs;
        try {
            crs = headerMeta.srid > 0 ? CRS.decode("EPSG:" + headerMeta.srid) : null;
        } catch (NoSuchAuthorityCodeException e) {
            throw new IOException(e);
        } catch (FactoryException e) {
            throw new IOException(e);
        }
        return new ReferencedEnvelope(env, crs);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE) return -1;
        return (int) headerMeta.featuresCount;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query q)
            throws IOException {

        // grab the target bbox, if any
        Envelope bbox = new ReferencedEnvelope();
        if (q != null && q.getFilter() != null) {
            bbox = (Envelope) q.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
            if (bbox == null) {
                bbox = new ReferencedEnvelope();
            }
        }

        FlatGeobufFeatureReader reader;

        Filter filter = q != null ? q.getFilter() : null;
        if (filter instanceof Id) {
            // TODO: can be done but need upstream java ref impl.
            // Id fidFilter = (Id) filter;
            reader = new FlatGeobufFeatureReader(getSchema(), url, headerMeta, null);
        } else if (!bbox.isNull()
                && !Double.isInfinite(bbox.getWidth())
                && !Double.isInfinite(bbox.getHeight())) {
            LOGGER.info("Filtering with bbox " + bbox);
            reader = new FlatGeobufFeatureReader(getSchema(), url, headerMeta, bbox);
        } else {
            reader = new FlatGeobufFeatureReader(getSchema(), url, headerMeta, null);
        }

        return (FeatureReader<SimpleFeatureType, SimpleFeature>) reader;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        Name name = getEntry().getName();
        LOGGER.info("Building feature type for " + name);
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(name);
        ftb.setAbstract(false);
        ftb.add("geometry", FeatureTypeConversions.getGeometryClass(headerMeta.geometryType));
        for (ColumnMeta columnMeta : headerMeta.columns)
            ftb.add(columnMeta.name, columnMeta.getBinding());
        SimpleFeatureType ft = ftb.buildFeatureType();
        return ft;
    }
}
