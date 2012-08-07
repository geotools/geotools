package org.geotools.data.wfs.impl;

import static org.geotools.data.wfs.internal.Loggers.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;
import org.geotools.data.TransactionStateDiff;
import org.geotools.data.wfs.impl.WFSDiff.BatchUpdate;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionRequest.Delete;
import org.geotools.data.wfs.internal.TransactionRequest.Insert;
import org.geotools.data.wfs.internal.TransactionRequest.Update;
import org.geotools.data.wfs.internal.TransactionResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;

class WFSRemoteTransactionState implements State {

    private final WFSContentDataStore dataStore;

    private Transaction transaction;

    private Map<Name, WFSContentState> localStates;

    public WFSRemoteTransactionState(WFSContentDataStore dataStore) {
        this.dataStore = dataStore;
        this.localStates = new HashMap<Name, WFSContentState>();
    }

    public synchronized WFSDiff getDiff(final Name typeName) {
        WFSContentState localState = localStates.get(typeName);
        if (null == localState) {
            throw new IllegalStateException("Not watching " + typeName);
        }
        WFSDiff diff = localState.getLocalTransactionState().getDiff();
        return diff;
    }

    @Override
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void rollback() throws IOException {
        clear(); // rollback differences
        // state.fireBatchFeatureEvent(false);
    }

    @Override
    public void addAuthorization(String AuthID) throws IOException {
        // not needed?
    }

    @Override
    public synchronized void commit() throws IOException {
        try {
            commitInternal();
        } finally {
            // If the commit fails or succeeds, state is reset
            clear();
        }
    }

    /**
     * This state takes ownership of each type's diff, so lets clear them all
     */
    private void clear() {
        for (WFSContentState localState : localStates.values()) {
            WFSLocalTransactionState localTransactionState = localState.getLocalTransactionState();
            WFSDiff diff = localTransactionState.getDiff();
            diff.clear();
        }
    }

    private void commitInternal() throws IOException {
        if (this.localStates.isEmpty()) {
            return;
        }
        WFSClient wfs = dataStore.getWfsClient();
        TransactionRequest transactionRequest = wfs.createTransaction();

        List<MutableFeatureId> requestedInsertFids = new ArrayList<MutableFeatureId>();

        for (Name typeName : localStates.keySet()) {
            List<MutableFeatureId> addedFids = applyDiff(typeName, transactionRequest);
            requestedInsertFids.addAll(addedFids);
        }

        TransactionResponse transactionResponse = wfs.issueTransaction(transactionRequest);
        List<FeatureId> insertedFids = transactionResponse.getInsertedFids();
        int deleteCount = transactionResponse.getDeleteCount();
        int updatedCount = transactionResponse.getUpdatedCount();
        info(getClass().getSimpleName(), "::commit(): Updated: ", updatedCount, ", Deleted: ",
                deleteCount, ", Inserted: ", insertedFids);

        if (requestedInsertFids.size() != insertedFids.size()) {
            throw new IllegalStateException("Asked to add " + requestedInsertFids.size()
                    + " Features but got " + insertedFids.size() + " insert results");
        }

        for (int i = 0; i < requestedInsertFids.size(); i++) {
            MutableFeatureId local = requestedInsertFids.get(i);
            FeatureId inserted = insertedFids.get(i);
            local.setID(inserted.getID());
            local.setFeatureVersion(inserted.getFeatureVersion());
        }
    }

    private List<MutableFeatureId> applyDiff(final Name localTypeName,
            TransactionRequest transactionRequest) throws IOException {

        final WFSContentState localState = localStates.get(localTypeName);
        final WFSLocalTransactionState localTransactionState = localState
                .getLocalTransactionState();
        final WFSDiff diff = localTransactionState.getDiff();

        List<MutableFeatureId> addedFeatureIds = new LinkedList<MutableFeatureId>();

        final QName remoteTypeName = dataStore.getRemoteTypeName(localTypeName);

        applyBatchUpdates(remoteTypeName, diff, transactionRequest);

        final Set<String> ignored = diff.getBatchModified();

        final SimpleFeatureType remoteType = dataStore.getRemoteSimpleFeatureType(remoteTypeName);

        // Create a single insert element with all the inserts for this type
        final Map<String, SimpleFeature> added = diff.getAdded();
        if (added.size() > 0) {
            Insert insert = transactionRequest.createInsert(remoteTypeName);

            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(remoteType);
            for (String fid : diff.getAddedOrder()) {
                if (ignored.contains(fid)) {
                    continue;
                }
                SimpleFeature localFeature = added.get(fid);
                //TODO: revisit
                //localState.fireFeatureAdded(source, feature);
                MutableFeatureId addedFid = (MutableFeatureId) localFeature.getIdentifier();
                addedFeatureIds.add(addedFid);

                SimpleFeature remoteFeature = SimpleFeatureBuilder.retype(localFeature, builder);

                insert.add(remoteFeature);
            }
            transactionRequest.add(insert);
        }

        final Map<String, SimpleFeature> modified = diff.getModified();

        // Create a single delete element with all the deletes for this type
        Set<Identifier> ids = new LinkedHashSet<Identifier>();
        for (Map.Entry<String, SimpleFeature> entry : modified.entrySet()) {
            if (!(TransactionStateDiff.NULL == entry.getValue())) {
                continue;// not a delete
            }
            String rid = entry.getKey();
            if (ignored.contains(rid)) {
                continue;
            }
            Identifier featureId = featureId(rid);
            ids.add(featureId);
        }
        if (!ids.isEmpty()) {
            Filter deleteFilter = dataStore.getFilterFactory().id(ids);
            Delete delete = transactionRequest.createDelete(remoteTypeName, deleteFilter);
            transactionRequest.add(delete);
        }

        // Create one update element per modified feature. Batch modified ones should have been
        // added as a single update element at applyBatchUpdate before
        for (Map.Entry<String, SimpleFeature> entry : modified.entrySet()) {
            String fid = entry.getKey();
            SimpleFeature feature = entry.getValue();

            if (TransactionStateDiff.NULL == feature) {
                continue;// not an update
            }
            if (ignored.contains(fid)) {
                continue;
            }
            applySingleUpdate(remoteTypeName, feature, transactionRequest);
        }

        return addedFeatureIds;
    }

    private void applySingleUpdate(QName remoteTypeName, SimpleFeature feature,
            TransactionRequest transactionRequest) throws IOException {

        // so bad, this is going to update a lot of unnecessary properties. We'd need to make
        // DiffContentFeatureWriter's live and current attributes protected and extend write so that
        // it records the truly modified attributes instead

        Collection<Property> properties = feature.getProperties();

        List<QName> propertyNames = new ArrayList<QName>();
        List<Object> newValues = new ArrayList<Object>();

        for (Property p : properties) {
            QName attName = new QName(remoteTypeName.getNamespaceURI(), p.getName().getLocalPart());
            Object attValue = p.getValue();
            propertyNames.add(attName);
            newValues.add(attValue);
        }

        Filter updateFilter = dataStore.getFilterFactory().id(
                Collections.singleton(feature.getIdentifier()));

        Update update = transactionRequest.createUpdate(remoteTypeName, propertyNames, newValues,
                updateFilter);
        transactionRequest.add(update);
    }

    private void applyBatchUpdates(QName remoteTypeName, WFSDiff diff,
            TransactionRequest transactionRequest) {

        List<BatchUpdate> batchUpdates = diff.getBatchUpdates();

        for (BatchUpdate batch : batchUpdates) {

            List<QName> propertyNames = new ArrayList<QName>(batch.properties.length);
            for (Name attName : batch.properties) {
                propertyNames.add(new QName(remoteTypeName.getNamespaceURI(), attName
                        .getLocalPart()));
            }
            List<Object> newValues = Arrays.asList(batch.values);
            Filter updateFilter = batch.filter;

            Update update = transactionRequest.createUpdate(remoteTypeName, propertyNames,
                    newValues, updateFilter);

            transactionRequest.add(update);
        }

    }

    private Identifier featureId(final String rid) {
        final FilterFactory ff = dataStore.getFilterFactory();
        String fid = rid;
        String featureVersion = null;
        int versionSeparatorIdx = rid.indexOf(FeatureId.VERSION_SEPARATOR);
        if (-1 != versionSeparatorIdx) {
            fid = rid.substring(0, versionSeparatorIdx);
            featureVersion = rid.substring(versionSeparatorIdx + 1);
        }
        FeatureId featureId = ff.featureId(fid, featureVersion);
        return featureId;
    }

    public void watch(WFSContentState localState) {
        Name typeName = localState.getEntry().getName();
        localStates.put(typeName, localState);
    }
}
