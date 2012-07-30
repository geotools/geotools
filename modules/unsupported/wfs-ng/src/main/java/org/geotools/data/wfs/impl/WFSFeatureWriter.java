package org.geotools.data.wfs.impl;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.DiffContentFeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class WFSFeatureWriter extends DiffContentFeatureWriter {

    final WFSRemoteTransactionState autoCommitState;

    final SimpleFeatureBuilder featureBuilder;

    public WFSFeatureWriter(final WFSContentFeatureStore store,
            final WFSLocalTransactionState localSate,
            final FeatureReader<SimpleFeatureType, SimpleFeature> reader, final boolean autoCommit) {

        super(store, localSate.getDiff(), reader);
        featureBuilder = new SimpleFeatureBuilder(getFeatureType(),
                new MutableIdentifierFeatureFactory());

        if (autoCommit) {
            WFSContentDataStore dataStore = (WFSContentDataStore) store.getDataStore();
            autoCommitState = new WFSRemoteTransactionState(dataStore);
            autoCommitState.watch(localSate);
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
        SimpleFeatureType type = getFeatureType();
        if (hasNext()) {
            return super.next();
        } else {
            // Create new content with mutable fid
            live = null;
            next = null;
            featureBuilder.reset();
            String id = "new" + diff.nextFID;
            current = featureBuilder.buildFeature(id, new Object[type.getAttributeCount()]);
            diff.nextFID++;
            return current;
        }
    }

    private void checkClosed() throws IOException {
        if (reader == null) {
            throw new IOException("FeatureWriter is closed");
        }
    }

}
