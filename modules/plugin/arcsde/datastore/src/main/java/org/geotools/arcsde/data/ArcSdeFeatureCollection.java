/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.data.store.NoContentIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureReaderIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.hsqldb.Session;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * FeatureCollection implementation that works over an {@link ArcSDEFeatureReader} or one of the
 * decorators over it returned by {@link ArcSDEDataStore#getFeatureReader(Query, Session, boolean)}.
 * <p>
 * Note this class and the iterators it returns are thread safe.
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcSdeFeatureCollection.java $
 * @see FeatureCollection
 */
public class ArcSdeFeatureCollection extends DataFeatureCollection {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.data");

    private final ArcSdeFeatureSource featureSource;

    private final Query query;

    private final SimpleFeatureType childrenSchema;

    // private Session session;

    public ArcSdeFeatureCollection(final ArcSdeFeatureSource featureSource,
            SimpleFeatureType queryType, final Query namedQuery) throws IOException {
        this.featureSource = featureSource;
        this.query = namedQuery;
        this.childrenSchema = queryType;
    }

    /**
     * @see FeatureCollection#getSchema()
     */
    @Override
    public final synchronized SimpleFeatureType getSchema() {
        return childrenSchema;
    }

    /**
     * @see FeatureCollection#getBounds()
     */
    @Override
    public final ReferencedEnvelope getBounds() {
        ReferencedEnvelope bounds;

        LOGGER.info("Getting collection bounds");
        try {
            bounds = featureSource.getBounds(query);
            if (bounds == null) {
                LOGGER.info("FeatureSource returned null bounds, going to return an empty one");
                bounds = new ReferencedEnvelope(getCRS());
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error getting collection bounts", e);
            bounds = new ReferencedEnvelope(getCRS());
        }
        return bounds;
    }

    private CoordinateReferenceSystem getCRS() {
        GeometryDescriptor defaultGeometry = this.featureSource.getSchema().getGeometryDescriptor();
        return defaultGeometry == null ? null : defaultGeometry.getCoordinateReferenceSystem();
    }

    @Override
    public final int getCount() throws IOException {
        return featureSource.getCount(query);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        final FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = featureSource.getfeatureReader(childrenSchema, query);

        return reader;
    }

    /**
     * Overrides to avoid the superclass' call to {@link #writer()} and it's
     * {@code UnsupportedOperationException}
     * 
     * @return Iterator, should be closed closeIterator
     */
    @Override
    protected Iterator<SimpleFeature> openIterator() throws IOException {
        try {
            return new FeatureReaderIterator<SimpleFeature>(reader());
        } catch (IOException e) {
            return new NoContentIterator(e);
        }
    }

    /**
     * Overrides to deal with closing the {@link FeatureReaderIterator}s created at
     * {@link #openIterator()}, as superclass uses another class that does the same but its package
     * visible (actually I don't see the point on two versions of FeatureReaderIterator?)
     */
    @Override
    protected void closeIterator(Iterator<SimpleFeature> close) throws IOException {
        if (close == null) {
            // iterator probably failed during consturction !
        } else if (close instanceof FeatureReaderIterator) {
            FeatureReaderIterator<SimpleFeature> iterator = (FeatureReaderIterator<SimpleFeature>) close;
            iterator.close(); // only needs package visability
        }
    }

}
