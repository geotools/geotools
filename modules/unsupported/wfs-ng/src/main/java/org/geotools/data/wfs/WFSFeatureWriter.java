/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.store.DiffContentFeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class WFSFeatureWriter extends DiffContentFeatureWriter {

    final WFSRemoteTransactionState autoCommitState;

    public WFSFeatureWriter(
            final WFSFeatureStore store,
            final WFSLocalTransactionState localSate,
            final FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            final boolean autoCommit) {

        super(
                store,
                localSate.getDiff(),
                reader,
                new SimpleFeatureBuilder(
                        reader.getFeatureType(), new MutableIdentifierFeatureFactory()));

        if (autoCommit) {
            WFSDataStore dataStore = (WFSDataStore) store.getDataStore();
            autoCommitState = new WFSRemoteTransactionState(dataStore);
            autoCommitState.watch(localSate.getState());
        } else {
            autoCommitState = null;
        }
    }

    @Override
    public void write() throws IOException {
        checkClosed();
        super.write();
        if (autoCommitState != null) {
            autoCommitState.commit();
        }
    }

    @Override
    public void remove() throws IOException {
        checkClosed();
        super.remove();
        if (autoCommitState != null) {
            autoCommitState.commit();
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        checkClosed();
        return super.hasNext();
    }

    @Override
    public synchronized SimpleFeature next() throws IOException {
        checkClosed();
        return super.next();
    }

    private void checkClosed() throws IOException {
        if (reader == null) {
            throw new IOException("FeatureWriter is closed");
        }
    }
}
