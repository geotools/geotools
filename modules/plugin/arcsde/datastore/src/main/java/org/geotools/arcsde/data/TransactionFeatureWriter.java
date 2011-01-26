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
import java.util.NoSuchElementException;

import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.hsqldb.Session;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * A FeatureWriter aware of transactions.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/TransactionFeatureWriter.java $
 */
class TransactionFeatureWriter extends ArcSdeFeatureWriter {
    private ArcTransactionState state;

    /**
     * <p>
     * </p>
     * 
     * @param fidReader
     * @param featureType
     * @param filteredContent
     * @param listenerManager
     * @param transactionalConnection
     *            the {@link Session} to work over, with a {@link Session#isTransactionActive()
     *            transaction active}
     * @param transaction
     *            a transaction <b>already configured</b> with the {@link ArcTransactionState}
     *            needed for this writer to work.
     * @throws NoSuchElementException
     * @throws IOException
     */
    public TransactionFeatureWriter(final FIDReader fidReader, final SimpleFeatureType featureType,
            final FeatureReader<SimpleFeatureType, SimpleFeature> filteredContent,
            final ArcTransactionState state, final ArcSdeVersionHandler versionHandler,
            final FeatureListenerManager listenerManager) throws NoSuchElementException,
            IOException {

        super(fidReader, featureType, filteredContent, state.getConnection(), listenerManager,
                versionHandler);
        this.state = state;
        assert state.getConnection().isTransactionActive();

    }

    /**
     * Overrides to not close the connection as it's the transaction responsibility.
     * 
     * @see FeatureWriter#close()
     */
    @Override
    public void close() throws IOException {
        // we're inside a transaction, so we don't
        // close the connection. Neither filteredContent should do.
        if (filteredContent != null) {
            filteredContent.close();
            filteredContent = null;
        }
    }

    @Override
    public void write() throws IOException {
        try {
            super.write();
            String typeName = feature.getFeatureType().getTypeName();
            state.addChange(typeName);
        } catch (IOException e) {
            state.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void remove() throws IOException {
        try {
            super.remove();
        } catch (IOException e) {
            state.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    protected void doFireFeaturesAdded(String typeName, ReferencedEnvelope bounds, Filter filter) {
        Transaction transaction = state.getTransaction();

        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.ADDED, bounds, filter);
        listenerManager.fireEvent(typeName, transaction, event);
    }

    @Override
    protected void doFireFeaturesChanged(String typeName, ReferencedEnvelope bounds, Filter filter) {
        Transaction transaction = state.getTransaction();

        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.CHANGED, bounds, filter);
        listenerManager.fireEvent(typeName, transaction, event);
    }

    @Override
    protected void doFireFeaturesRemoved(String typeName, ReferencedEnvelope bounds, Filter filter) {
        Transaction transaction = state.getTransaction();

        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.REMOVED, bounds, filter);
        listenerManager.fireEvent(typeName, transaction, event);
    }

}
