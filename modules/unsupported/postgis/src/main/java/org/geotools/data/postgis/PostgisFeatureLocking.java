/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureLocking;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;


/**
 * Extends PostgisFeatureLocking with support for Locking.
 * 
 * <p>
 * This class will be, horror, modey. While the are plenty of Object Oriented
 * ways to fix this I have a deadline right now.
 * </p>
 * 
 * <p>
 * When the DataStore is constructed it will create a LockingManager only if
 * the Postgis implementation does not support database locking. If the
 * lockingManger is present it will be used.
 * </p>
 * 
 * <p>
 * If the lockingManger is not present, the this class will use Database
 * locking
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc
 *
 * @source $URL$
 * @version $Id$
 */
public class PostgisFeatureLocking extends PostgisFeatureStore
    implements FeatureLocking<SimpleFeatureType, SimpleFeature> {
    /** The logger for the postgis module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.postgis");
    FeatureLock featureLock = FeatureLock.TRANSACTION;

    public PostgisFeatureLocking(PostgisDataStore postgisDataStore,
        SimpleFeatureType featureType) throws IOException {
        super(postgisDataStore, featureType);
    }

    /**
     * Provide a FeatureLock for locking operations to operate against.
     * 
     * <p>
     * Initial Transactional duration locks can be restored with
     * setFeatureLock( FetaureLock.TRANSACTION )
     * </p>
     *
     * @param lock FeatureLock (or FeatureLock.TRANSACTION );
     *
     * @throws NullPointerException If lock was <code>null</code>
     *
     * @see org.geotools.data.FeatureLocking#setFeatureLock(org.geotools.data.FeatureLock)
     */
    public void setFeatureLock(FeatureLock lock) {
        if (lock == null) {
            throw new NullPointerException(
                "A FeatureLock is required - did you mean FeatureLock.TRANSACTION?");
        }

        featureLock = lock;
    }

    /**
     * Lock all Features
     *
     * @return Number of Locked features
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureLocking#lockFeatures()
     */
    public int lockFeatures() throws IOException {
        return lockFeatures(Filter.INCLUDE);
    }

    /**
     * Lock features matching <code>filter</code>.
     *
     * @param filter
     *
     * @return Number of locked Features
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureLocking#lockFeatures(org.geotools.filter.Filter)
     */
    public int lockFeatures(Filter filter) throws IOException {
        return lockFeatures(new DefaultQuery(getSchema().getTypeName(), filter));
    }
    public int lockFeatures( org.geotools.filter.Filter filter ) throws IOException {
        return lockFeatures(new DefaultQuery(getSchema().getTypeName(), filter));
    }
    /**
     * Lock features matching Query.
     * 
     * <p>
     * FeatureStores that have provided their own locking to will need to
     * override this method.
     * </p>
     *
     * @param query
     *
     * @return Number of locked Features
     *
     * @throws IOException If we could not determine which feature to lock
     *         based on Query
     * @throws UnsupportedOperationException When DataStore does not provide a
     *         LockingManager
     * @throws DataSourceException If feature to be locked does not exist
     *
     * @see org.geotools.data.FeatureLocking#lockFeatures(org.geotools.data.Query)
     */
    public int lockFeatures(Query query) throws IOException {
        LockingManager lockingManager = getDataStore().getLockingManager();

        if (lockingManager == null) {
            throw new UnsupportedOperationException(
                "DataStore not using lockingManager, must provide alternate implementation");
        }

        String typeName = getSchema().getTypeName();

        if ((query.getTypeName() != null)
                && !typeName.equals(query.getTypeName())) {
            throw new IOException("Query typeName does not match "
                + getSchema().getTypeName() + ":" + query);
        }

        // Reduce the Query to only return the FetureID here?
        // Good idea, but it's not working right now, so we're just using the query passed in.
        Query optimizedQuery = new DefaultQuery(typeName, query.getFilter(),
                query.getMaxFeatures(), Query.NO_NAMES, query.getHandle());
        SimpleFeatureIterator reader = getFeatures(query).features();
        SimpleFeature feature;
        int count = 0;
        LOGGER.info("got reader from query " + optimizedQuery
            + ", reader has next " + reader.hasNext());

        try {
            while (reader.hasNext()) {
                try {
                    feature = (SimpleFeature) reader.next();
                    lockingManager.lockFeatureID(typeName, feature.getID(),
                        transaction, featureLock);
                    count++;
                    LOGGER.info("locked feature " + feature);
                } catch (FeatureLockException locked) {
                    LOGGER.info("feature lock exception");

                    // could not acquire - don't increment count                
                } catch (NoSuchElementException nosuch) {
                    throw new DataSourceException("Problem with "
                        + query.getHandle() + " while locking", nosuch);
                }
            }
        } finally {
            reader.close();
        }

        return count;
    }

    /**
     * HACK HACK HACK!!!  Don't use unless you're working on geoserver. just
     * using ints for return now, to easily swap out with what we've got going
     * on right now.
     *
     * @param feature DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public int lockFeature(SimpleFeature feature) throws IOException {
        LockingManager lockingManager = getDataStore().getLockingManager();

        if (lockingManager == null) {
            throw new UnsupportedOperationException(
                "DataStore not using lockingManager, must provide alternate implementation");
        }

        try {
            //TODO: more checking here, check feature.typename == this.typename
            //perhaps even feature.getFeatureType == this.featureType.
            lockingManager.lockFeatureID(tableName, feature.getID(),
                getTransaction(), featureLock);

            return 1;
        } catch (FeatureLockException locked) {
            return 0;
        }
    }

    /**
     * Unlock all Features.
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureLocking#unLockFeatures()
     */
    public void unLockFeatures() throws IOException {
        unLockFeatures(Filter.INCLUDE);
    }

    /**
     * Unlock Features specified by <code>filter</code>.
     *
     * @param filter
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureLocking#unLockFeatures(org.geotools.filter.Filter)
     */
    public void unLockFeatures(Filter filter) throws IOException {
        unLockFeatures(new DefaultQuery(getSchema().getTypeName(), filter));
    }

    /**
     * Unlock features specified by the <code>query</code>.
     * 
     * <p>
     * FeatureStores that have provided their own locking to will need to
     * override this method.
     * </p>
     *
     * @param query
     *
     * @throws IOException
     * @throws UnsupportedOperationException If lockingManager is not provided
     *         by DataStore subclass
     * @throws DataSourceException Filter describes an unlocked Feature, or
     *         authorization not held
     *
     * @see org.geotools.data.FeatureLocking#unLockFeatures(org.geotools.data.Query)
     */
    public void unLockFeatures(Query query) throws IOException {
        LockingManager lockingManager = getDataStore().getLockingManager();

        if (lockingManager == null) {
            throw new UnsupportedOperationException(
                "DataStore not using lockingManager, must provide alternate implementation");
        }

        // Could we reduce the Query to only return the FetureID here?
        //
        SimpleFeatureIterator reader = getFeatures(query).features();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature;

        try {
            while (reader.hasNext()) {
                try {
                    feature = (SimpleFeature) reader.next();
                    lockingManager.unLockFeatureID(typeName, feature.getID(),
                        getTransaction(), featureLock);
                } catch (NoSuchElementException nosuch) {
                    throw new DataSourceException("Problem with "
                        + query.getHandle() + " while locking", nosuch);
                }
            }
        } finally {
            reader.close();
        }
    }
}
