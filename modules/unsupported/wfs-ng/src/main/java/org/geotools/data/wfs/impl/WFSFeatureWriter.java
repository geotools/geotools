package org.geotools.data.wfs.impl;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.DiffContentFeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class WFSFeatureWriter extends DiffContentFeatureWriter {

    final WFSRemoteTransactionState autoCommitState;

    public WFSFeatureWriter(final WFSContentFeatureStore store,
            final WFSLocalTransactionState localSate,
            final FeatureReader<SimpleFeatureType, SimpleFeature> reader, final boolean autoCommit) {

        super(store, localSate.getDiff(), reader, new SimpleFeatureBuilder(reader.getFeatureType(), new MutableIdentifierFeatureFactory()));
        
        if (autoCommit) {
            WFSContentDataStore dataStore = (WFSContentDataStore) store.getDataStore();
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
