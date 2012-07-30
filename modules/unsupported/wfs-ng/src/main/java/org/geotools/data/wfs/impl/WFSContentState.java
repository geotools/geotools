package org.geotools.data.wfs.impl;

import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.opengis.feature.type.Name;

public class WFSContentState extends ContentState {

    public WFSContentState(ContentEntry entry) {
        super(entry);
        super.transactionState = new WFSLocalTransactionState(this);
    }

    WFSContentState(WFSContentState wfsContentState) {
        super(wfsContentState);
        super.transactionState = new WFSLocalTransactionState(this);
    }

    @Override
    public WFSContentState copy() {
        return new WFSContentState(this);
    }

    WFSLocalTransactionState getLocalTransactionState() {
        return (WFSLocalTransactionState) super.transactionState;
    }

    /**
     * Sets the transaction associated with the state.
     */
    @Override
    public void setTransaction(Transaction tx) {
        super.setTransaction(tx);

        if (tx != Transaction.AUTO_COMMIT) {
            synchronized (WFSRemoteTransactionState.class) {

                WFSContentDataStore dataStore = (WFSContentDataStore) entry.getDataStore();

                WFSRemoteTransactionState remoteStateKeeper;
                remoteStateKeeper = (WFSRemoteTransactionState) tx.getState(dataStore);
                if (remoteStateKeeper == null) {
                    remoteStateKeeper = new WFSRemoteTransactionState(dataStore);
                    tx.putState(dataStore, remoteStateKeeper);
                }
                WFSLocalTransactionState localTransactionState = getLocalTransactionState();
                remoteStateKeeper.watch(localTransactionState);
            }
        }
    }
}
