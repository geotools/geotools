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

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * A FeatureWriter for auto commit mode.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/AutoCommitFeatureWriter.java $
 */
class AutoCommitFeatureWriter extends ArcSdeFeatureWriter {

    public AutoCommitFeatureWriter(final FIDReader fidReader, final SimpleFeatureType featureType,
            final FeatureReader<SimpleFeatureType, SimpleFeature> filteredContent,
            final ISession session, final FeatureListenerManager listenerManager,
            final ArcSdeVersionHandler versionHandler) throws NoSuchElementException, IOException {

        super(fidReader, featureType, filteredContent, session, listenerManager, versionHandler);
    }

    @Override
    protected void doFireFeaturesAdded(String typeName, ReferencedEnvelope bounds, Filter filter) {
        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.ADDED, bounds, filter);
        listenerManager.fireEvent(typeName, Transaction.AUTO_COMMIT, event);
    }

    @Override
    protected void doFireFeaturesChanged(String typeName, ReferencedEnvelope bounds, Filter filter) {
        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.CHANGED, bounds, filter);
        listenerManager.fireEvent(typeName, Transaction.AUTO_COMMIT, event);
    }

    @Override
    protected void doFireFeaturesRemoved(String typeName, ReferencedEnvelope bounds, Filter filter) {
        FeatureEvent event = new FeatureEvent(this, FeatureEvent.Type.REMOVED, bounds, filter);
        listenerManager.fireEvent(typeName, Transaction.AUTO_COMMIT, event);
    }

}
