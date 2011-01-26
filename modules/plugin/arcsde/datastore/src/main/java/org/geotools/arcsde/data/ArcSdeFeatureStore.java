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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeTable;

public class ArcSdeFeatureStore extends ArcSdeFeatureSource implements SimpleFeatureStore {

    private static final Logger LOGGER = Logging.getLogger(ArcSdeFeatureStore.class.getName());

    public ArcSdeFeatureStore(final FeatureTypeInfo typeInfo, final ArcSDEDataStore arcSDEDataStore) {
        super(typeInfo, arcSDEDataStore);
    }

    /**
     * @see FeatureStore#getTransaction()
     */
    public synchronized Transaction getTransaction() {
        return transaction;
    }

    /**
     * Sets this FeatureStore transaction.
     * <p>
     * If transaction is not auto commit, initiates an {@link ArcTransactionState} with the
     * dataStore's connection pool as key.
     * </p>
     * 
     * @see FeatureStore#setTransaction(Transaction)
     */
    public synchronized void setTransaction(final Transaction transaction) {
        // System.err.println(">>setTransaction called at " +
        // Thread.currentThread().getName());
        if (transaction == null) {
            throw new NullPointerException("mean Transaction.AUTO_COMMIT?");
        }

        // this is hacky as a simple reference check should be enough but
        // there
        // seem to be some class loader issues with udig so it does not work
        boolean isAutoCommit = Transaction.AUTO_COMMIT.equals(transaction);
        if (!isAutoCommit) {
            try {
                transaction.getState(new Object());
            } catch (UnsupportedOperationException e) {
                isAutoCommit = true;
            }
        }
        this.transaction = transaction;
    }

    /**
     * @see FeatureStore#addFeatures(SimpleFeatureCollection)
     */
    public List<FeatureId> addFeatures(
            final FeatureCollection<SimpleFeatureType, SimpleFeature> collection)
            throws IOException {
        // System.err.println(">>addFeatures called at " +
        // Thread.currentThread().getName());
        final String typeName = typeInfo.getFeatureTypeName();

        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = dataStore.getFeatureWriterAppend(typeName, transaction);
        final FeatureIterator<SimpleFeature> iterator = collection.features();
        List<FeatureId> featureIds = new LinkedList<FeatureId>();
        try {
            SimpleFeature toAdd;
            SimpleFeature newFeature;
            while (iterator.hasNext()) {
                toAdd = iterator.next();
                newFeature = writer.next();
                newFeature.setAttributes(toAdd.getAttributes());
                writer.write();
                featureIds.add(newFeature.getIdentifier());
            }
        } finally {
            iterator.close();
            writer.close();
        }
        return featureIds;
    }

    /**
     * @see FeatureStore#modifyFeatures(AttributeDescriptor[], Object[], Filter)
     */
    public final void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {

        Name attributeNames[] = new Name[type.length];
        for (int i = 0; i < type.length; i++) {
            attributeNames[i] = type[i].getName();
        }
        modifyFeatures(attributeNames, value, filter);
    }

    public void modifyFeatures(final Name[] attributes, final Object[] values, final Filter filter)
            throws IOException {
        final ISession session = getSession();
        try {
            final String typeName = typeInfo.getFeatureTypeName();
            final Transaction currTransaction = getTransaction();
            final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
            writer = dataStore.getFeatureWriter(typeName, filter, currTransaction);

            try {
                SimpleFeature feature;
                while (writer.hasNext()) {
                    feature = writer.next();
                    for (int i = 0; i < values.length; i++) {
                        feature.setAttribute(attributes[i].getLocalPart(), values[i]);
                    }
                    writer.write();
                }
            } finally {
                writer.close();
            }
        } finally {
            session.dispose();
        }
    }

    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(new Name[] { new NameImpl(name), }, new Object[] { attributeValue, }, filter);
    }

    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        Name attributeNames[] = new Name[names.length];
        for (int i = 0; i < names.length; i++) {
            attributeNames[i] = new NameImpl(names[i]);
        }
        modifyFeatures(attributeNames, values, filter);
    }

    /**
     * @see FeatureStore#modifyFeatures(AttributeDescriptor, Object, Filter)
     */
    public final void modifyFeatures(final AttributeDescriptor type, final Object value,
            final Filter filter) throws IOException {
        modifyFeatures(new Name[] { type.getName(), }, new Object[] { value, }, filter);
    }

    public final void modifyFeatures(final Name name, final Object value, final Filter filter)
            throws IOException {
        modifyFeatures(new Name[] { name, }, new Object[] { value, }, filter);
    }

    /**
     * @see FeatureStore#removeFeatures(Filter)
     */
    public void removeFeatures(final Filter filter) throws IOException {
        final Transaction currTransaction = getTransaction();
        final String typeName = typeInfo.getFeatureTypeName();
        // short circuit cut if needed to remove all features
        // if (Filter.INCLUDE == filter) {
        // truncate(typeName, connection);
        // return;
        // }
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = dataStore.getFeatureWriter(typeName, filter, currTransaction);
        try {
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * @see FeatureStore#setFeatures(FeatureReader)
     */
    public void setFeatures(final FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        final SimpleFeatureType readerType = reader.getFeatureType();
        if (!getSchema().equals(readerType)) {
            throw new IllegalArgumentException("Type mismatch: " + readerType);
        }

        final String typeName = typeInfo.getFeatureTypeName();
        final ISession session = getSession();
        try {
            // truncate using this connection to apply or not depending on
            // whether a transaction is in progress
            truncate(typeName, session);
        } finally {
            session.dispose();
        }

        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = dataStore.getFeatureWriterAppend(typeName, transaction);
        try {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                SimpleFeature newFeature = writer.next();
                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Truncates (removes all the features in) the ArcSDE table named <code>typeName</code> by using
     * an SeTable with the provided <code>connection</code>. This means if the connection has a
     * transaction in progress, the truncation takes effect upon commit, otherwise it takes effect
     * immediately.
     * 
     * @param typeName
     * @param session
     * @throws DataSourceException
     */
    private void truncate(final String typeName, final ISession session) throws IOException {
        final boolean transactionInProgress = session.isTransactionActive();
        final SeTable table = session.getTable(typeName);
        if (transactionInProgress) {
            // need to do actual deletes, as SeTable.truncate does not respects
            // transactions and would delete all content
            LOGGER.fine("deleting all table records for " + typeName);
            final FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore
                    .getFeatureWriter(typeName, transaction);
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
        } else {
            // we're in auto commit mode, lets truncate the table the fast way
            LOGGER.fine("truncating table " + typeName);
            session.issue(new Command<Void>() {
                @Override
                public Void execute(ISession session, SeConnection connection) throws SeException,
                        IOException {
                    table.truncate();
                    return null;
                }
            });
        }
    }
}
