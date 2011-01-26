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

package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.geotools.data.AbstractFeatureStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.jdbc.MutableFIDFeature;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * A cheap implementation of a feature locking.
 * <p>
 * Implementation wise, for all locking needs, tries to leverage the wrapped datastore feature
 * locking. If an optimization is possible (mass updates come to mind), we try to use the feature
 * locking, otherwiser we fall back on the implementation inherited from AbstractFeatureSource.
 * <p>
 * {@link #modifyFeatures(AttributeDescriptor[], Object[], Filter)} is an example of things that cannot be
 * optimized. Theoretically, one could mass expire current feature, but he should have first read
 * into memory all of them to rewrite them as new (which may not be possible).
 * 
 * @author aaime
 * @since 2.4
 * 
 *
 * @source $URL$
 */
public class VersionedPostgisFeatureStore extends AbstractFeatureStore implements VersioningFeatureStore {

    private VersionedPostgisDataStore store;

    private FeatureLocking<SimpleFeatureType, SimpleFeature> locking;

    private SimpleFeatureType schema;

    public VersionedPostgisFeatureStore(SimpleFeatureType schema, VersionedPostgisDataStore store)
            throws IOException {
        this.store = store;
        this.schema = schema;
        this.locking = (FeatureLocking<SimpleFeatureType, SimpleFeature>) store.wrapped.getFeatureSource(schema.getTypeName());
    }
    
    // -----------------------------------------------------------------------------------------------
    // STANDARD FEATURE STORE METHODS 
    // -----------------------------------------------------------------------------------------------

    public Transaction getTransaction() {
        return locking.getTransaction();
    }

    public void setTransaction(Transaction transaction) {
        locking.setTransaction(transaction);
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        DefaultQuery versionedQuery = store.buildVersionedQuery(getTypedQuery(query));
        return locking.getBounds(versionedQuery);
    }

    public int getCount(Query query) throws IOException {
        DefaultQuery versionedQuery = store.buildVersionedQuery(getTypedQuery(query));
        return locking.getCount(versionedQuery);
    }

    public DataStore getDataStore() {
        return store;
    }

    public void addFeatureListener(FeatureListener listener) {
        store.listenerManager.addFeatureListener(this, listener);
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    public void removeFeatureListener(FeatureListener listener) {
        store.listenerManager.removeFeatureListener(this, listener);
    }

    public void removeFeatures(Filter filter) throws IOException {
        // this we can optimize, it's a matter of mass updating the last
        // revisions (and before that, we have to compute the modified envelope)
        Filter versionedFilter = (Filter) store.buildVersionedFilter(schema.getTypeName(), filter,
                new RevisionInfo());
        
        // deal with the transaction, are we playing with auto commit or long running?
        Transaction t = getTransaction();
        boolean autoCommit = false;
        if (Transaction.AUTO_COMMIT.equals(t)) {
            t = new DefaultTransaction();
            autoCommit = true;
        }
        VersionedJdbcTransactionState state = store.wrapped.getVersionedJdbcTransactionState(t);
        
        // we need to mark the modified bounds and store their wgs84 version into the transaction
        ReferencedEnvelope bounds = locking.getBounds(new DefaultQuery(schema.getTypeName(), versionedFilter));
        if(bounds != null) { 
            if(bounds.getCoordinateReferenceSystem() == null) {
                bounds = new ReferencedEnvelope(bounds, getSchema().getCoordinateReferenceSystem());
            }
            try {
                ReferencedEnvelope wgsBounds = null;
                if(bounds.getCoordinateReferenceSystem() != null)
                    wgsBounds = bounds.transform(DefaultGeographicCRS.WGS84, true);
                else
                    wgsBounds = bounds;
                state.expandDirtyBounds(wgsBounds);
                state.setTypeNameDirty(schema.getTypeName());
            } catch(Exception e) {
                throw new DataSourceException("Problems computing and storing the " +
                		"bounds affected by this feature removal", e);
            }
        }
        
        // now we can run the update
        locking.modifyFeatures(locking.getSchema().getDescriptor("expired"), new Long(state
                .getRevision()), versionedFilter);
        
        // if it's auto commit, don't forget to actually commit
        if (autoCommit) {
            t.commit();
            t.close();
        }
        store.listenerManager.fireFeaturesRemoved(schema.getTypeName(), t, bounds, false);
    }

    public void setFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        // remove everything, then add back
        removeFeatures(Filter.INCLUDE);
        addFeatures(reader);
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        // feature collection is writable unfortunately, we have to rely on the
        // default behaviour otherwise writes won't be versioned
        // TODO: build a versioned feature collection that can do better, if possible at all
        return super.getFeatures(query);
    }

    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        // feature collection is writable unfortunately, we have to rely on the
        // default behaviour otherwise writes won't be versioned
        return super.getFeatures(filter);
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        // feature collection is writable unfortunately, we have to rely on the
        // default behaviour otherwise writes won't be versioned
        return super.getFeatures();
    }
    
    public SimpleFeatureCollection getVersionedFeatures(Query query) throws IOException {
        final SimpleFeatureType ft = getSchema();
        
        // check the feature type is the right one 
        final String typeName = ft.getTypeName();
        if(query.getTypeName() != null && !query.getTypeName().equals(typeName))
            throw new IOException("Incompatible type, this class can access only " + typeName);
        
        // make sure the view is around
        if(!Arrays.asList(store.wrapped.getTypeNames()).contains(store.getVFCViewName(typeName)))
            store.createVersionedFeatureCollectionView(typeName);
        
        // we have to hit the view
        DefaultQuery vq = new DefaultQuery(query);
        vq.setTypeName(VersionedPostgisDataStore.getVFCViewName(typeName));
        vq = store.buildVersionedQuery(vq);
        SimpleFeatureCollection fc = store.wrapped.getFeatureSource(VersionedPostgisDataStore.getVFCViewName(typeName)).getFeatures(vq);
        final SimpleFeatureType fcSchema = fc.getSchema();
        // build a renamed feature type with the same attributes as the feature collection
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(ft);
        builder.setAttributes(fc.getSchema().getAttributeDescriptors());
        SimpleFeatureType renamedFt = builder.buildFeatureType();
        return new ReTypingFeatureCollection(fc, renamedFt);
    }

    public SimpleFeatureCollection getVersionedFeatures(Filter filter) throws IOException {
        return getVersionedFeatures(new DefaultQuery(null, filter));
    }

    public SimpleFeatureCollection getVersionedFeatures() throws IOException {
        return getVersionedFeatures(new DefaultQuery(getSchema().getTypeName()));
    }
    
    public List<FeatureId> addFeatures(FeatureCollection<SimpleFeatureType,SimpleFeature> collection) throws IOException {
        List<FeatureId> addedFids = new LinkedList<FeatureId>();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature = null;
        SimpleFeature newFeature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore()
                .getFeatureWriterAppend(typeName, getTransaction());

        Iterator iterator = collection.iterator();
        try {

            while (iterator.hasNext()) {
                feature = (SimpleFeature) iterator.next();
                newFeature = (SimpleFeature) writer.next();
                try {
                    newFeature.setAttributes(feature.getAttributes());
                } catch (Exception writeProblem) {
                    throw new DataSourceException("Could not create " + typeName
                            + " out of provided feature: " + feature.getID(), writeProblem);
                }
                
                // preserve the FID, it could come from another node
                ((MutableFIDFeature) newFeature).setID(feature.getID());

                writer.write();
                addedFids.add(newFeature.getIdentifier());
            }
        } finally {
            collection.close(iterator);
            writer.close();
        }
        return addedFids;
    }
    
    // ---------------------------------------------------------------------------------------------
    // VERSIONING EXTENSIONS
    // ---------------------------------------------------------------------------------------------
    
    public String getVersion() throws IOException {
        Transaction t = getTransaction();
        if(t == Transaction.AUTO_COMMIT) {
            return null;
        } else {
            return String.valueOf(store.wrapped.getVersionedJdbcTransactionState(t).getRevision());
        }
    }

    public void rollback(String toVersion, Filter filter, String[] userIds) throws IOException {
        // TODO: build an optimized version of this that can do the same work with a couple
        // of queries assuming the filter is fully encodable
        
        Transaction t = getTransaction();
        boolean autoCommit = false;
        if (Transaction.AUTO_COMMIT.equals(t)) {
            t = new DefaultTransaction();
            autoCommit = true;
        }

        // Gather feature modified after toVersion
        ModifiedFeatureIds mfids = store.getModifiedFeatureFIDs(schema.getTypeName(), toVersion,
                null, filter, userIds, t);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        
        // grab the state, we need to mark as dirty all the features we are going to modify/re-insert
        VersionedJdbcTransactionState state = store.wrapped.getVersionedJdbcTransactionState(t);

        // remove all features that have been created and not deleted
        Set fidsToRemove = new HashSet(mfids.getCreated());
        fidsToRemove.removeAll(mfids.getDeleted());
        if (!fidsToRemove.isEmpty()) {
            removeFeatures(store.buildFidFilter(fidsToRemove));
            state.setTypeNameDirty(getSchema().getTypeName());
        }

        // reinstate all features that were there before toVersion and that
        // have been deleted after it. Notice this is an insertion, so to preserve
        // the fids I have to use low level writers where I can set all attributes manually
        // (we work on the assumption the wrapped data store maps all attributes of the primary
        // key in the feature itself)
        Set fidsToRecreate = new HashSet(mfids.getDeleted());
        fidsToRecreate.removeAll(mfids.getCreated());
        if (!fidsToRecreate.isEmpty()) {
            state.setTypeNameDirty(getSchema().getTypeName());
            state.setFidsDirty(getSchema().getTypeName(), fidsToRecreate);
            
            long revision = store.wrapped.getVersionedJdbcTransactionState(t).getRevision();
            Filter recreateFilter = store.buildVersionedFilter(schema.getTypeName(), store
                    .buildFidFilter(fidsToRecreate), mfids.fromRevision);
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = null;
            try {
                DefaultQuery q = new DefaultQuery(schema.getTypeName(), recreateFilter);
                fr = store.wrapped.getFeatureReader(q, t);
                fw = store.wrapped.getFeatureWriterAppend(schema.getTypeName(), t);
                while (fr.hasNext()) {
                    SimpleFeature original = fr.next();
                    SimpleFeature restored = fw.next();
                    for (int i = 0; i < original.getFeatureType().getAttributeCount(); i++) {
                        restored.setAttribute(i, original.getAttribute(i));
                    }
                    restored.setAttribute("revision", new Long(revision));
                    restored.setAttribute("expired", new Long(Long.MAX_VALUE));
                    fw.write();
                }
            } catch (IllegalAttributeException iae) {
                throw new DataSourceException("Unexpected error occurred while "
                        + "restoring deleted featues", iae);
            } finally {
                if (fr != null)
                    fr.close();
                if (fw != null)
                    fw.close();
            }
        }

        // Now onto the modified features, that were there, and still are there.
        // Since we cannot get a sorted writer we have to do a kind of inner loop scan
        // (note, a parellel scan of similarly sorted reader and writer would be more
        // efficient, but writer sorting is not there...)
        // Here it's possible to work against the external API, thought it would be more
        // efficient (but more complex) to work against the wrapped one.
        if (!mfids.getModified().isEmpty()) {
            state.setTypeNameDirty(getSchema().getTypeName());
            state.setFidsDirty(getSchema().getTypeName(), mfids.getModified());
            
            Filter modifiedIdFilter = store.buildFidFilter(mfids.getModified());
            Filter mifCurrent = store.buildVersionedFilter(schema.getTypeName(), modifiedIdFilter,
                    new RevisionInfo());
             FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = null;
            try {
                fw = store.getFeatureWriter(schema.getTypeName(), mifCurrent, t);
                while (fw.hasNext()) {
                    SimpleFeature current = fw.next();
                    Filter currIdFilter = ff.id(Collections
                            .singleton(ff.featureId(current.getID())));
                    Filter cidToVersion = store.buildVersionedFilter(schema.getTypeName(),
                            currIdFilter, mfids.fromRevision);
                    DefaultQuery q = new DefaultQuery(schema.getTypeName(), cidToVersion);
                    q.setVersion(mfids.fromRevision.toString());
                    fr = store.getFeatureReader(q, t);
                    SimpleFeature original = fr.next();
                    for (int i = 0; i < original.getFeatureType().getAttributeCount(); i++) {
                        current.setAttribute(i, original.getAttribute(i));
                    }
                    fr.close();
                    fw.write();
                }
            } catch (IllegalAttributeException iae) {
                throw new DataSourceException("Unexpected error occurred while "
                        + "restoring deleted featues", iae);
            } finally {
                if (fr != null)
                    fr.close();
                if (fw != null)
                    fw.close();
            }
        }
        
        // if it's auto commit, don't forget to actually commit
        if (autoCommit) {
            t.commit();
            t.close();
        }

    }

    public SimpleFeatureCollection getLog(String fromVersion, String toVersion, Filter filter, String[] userIds, int maxRows)
            throws IOException {
        if(filter == null)
            filter = Filter.INCLUDE;
        RevisionInfo r1 = new RevisionInfo(fromVersion);
        RevisionInfo r2 = new RevisionInfo(toVersion);

        boolean swapped = false;
        if (r1.revision > r2.revision) {
            // swap them
            RevisionInfo tmpr = r1;
            r1 = r2;
            r2 = tmpr;
            String tmps = toVersion;
            toVersion = fromVersion;
            fromVersion = tmps;
            swapped = true;
        }

        // We implement this exactly as described. Happily, it seems Postgis does not have
        // sql lentgh limitations. Yet, if would be a lot better if we could encode this
        // as a single sql query with subqueries... (but not all filters are encodable...)
        ModifiedFeatureIds mfids = store.getModifiedFeatureFIDs(schema.getTypeName(), fromVersion,
                toVersion, filter, userIds, getTransaction());
        Set ids = new HashSet(mfids.getCreated());
        ids.addAll(mfids.getDeleted());
        ids.addAll(mfids.getModified());
        
        // grab the eventually modified revisions from mfids
        r1 = mfids.fromRevision;
        r2 = mfids.toRevision;

        // no changes?
        if (ids.isEmpty())
            return new EmptyFeatureCollection(schema);

        // Create a filter that sounds like:
        // (revision > r1 and revision <= r2) or (expired > r1 and expired <= r2) and fid in
        // (fidlist)
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter fidFilter = store.buildFidFilter(ids);
        Filter transformedFidFilter = store.transformFidFilter(schema.getTypeName(), fidFilter);
        Filter revGrR1 = ff.greater(ff.property("revision"), ff.literal(r1.revision));
        Filter revLeR2 = ff.lessOrEqual(ff.property("revision"), ff.literal(r2.revision));
        Filter expGrR1 = ff.greater(ff.property("expired"), ff.literal(r1.revision));
        Filter expLeR2 = ff.lessOrEqual(ff.property("expired"), ff.literal(r2.revision));
        Filter versionFilter = ff.and(transformedFidFilter, ff.or(ff.and(revGrR1, revLeR2), ff.and(
                expGrR1, expLeR2)));

        // We just want the revision and expired, build a query against the real feature type
        DefaultQuery q = new DefaultQuery(schema.getTypeName(), versionFilter, new String[] {
                "revision", "expired" });
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        SortedSet revisions = new TreeSet();
        try {
            fr = store.wrapped.getFeatureReader(q, getTransaction());
            while (fr.hasNext()) {
                SimpleFeature f = fr.next();
                Long revision = (Long) f.getAttribute(0);
                if (revision.longValue() > r1.revision)
                    revisions.add(revision);
                Long expired = (Long) f.getAttribute(1);
                if (expired.longValue() != Long.MAX_VALUE && expired.longValue() > r1.revision)
                    revisions.add(expired);
            }
        } catch (Exception e) {
            throw new DataSourceException("Error reading modified revisions from datastore", e);
        } finally {
            if (fr != null)
                fr.close();
        }

        // now, we have a list of revisions between a min and a max
        // let's try to build a fid filter with revisions from the biggest to the smallest
        Set revisionIdSet = new HashSet();
        for (Iterator it = revisions.iterator(); it.hasNext();) {
            Long rev = (Long) it.next();
            revisionIdSet.add(ff.featureId(VersionedPostgisDataStore.TBL_CHANGESETS + "." + rev.toString()));
        }
        if(revisionIdSet.isEmpty())
            return new EmptyFeatureCollection(schema);
        Filter revisionFilter = ff.id(revisionIdSet);

        // return the changelog
        // TODO: sort on revision descending. Unfortunately, to do so we have to fix fid mappers,
        // so that auto-increment can return revision among the attributes, and at the same
        // time simply allow not include fid attributes in the insert queries (or provide a
        // "default"
        // value for them).
        SimpleFeatureSource changesets = (SimpleFeatureSource) store
                .getFeatureSource(VersionedPostgisDataStore.TBL_CHANGESETS);
        DefaultQuery sq = new DefaultQuery();
        sq.setFilter(revisionFilter);
        final SortOrder order = swapped ? SortOrder.ASCENDING : SortOrder.DESCENDING;
        sq.setSortBy(new SortBy[] { ff.sort("revision", order) });
        if(maxRows > 0)
            sq.setMaxFeatures(maxRows);
        return changesets.getFeatures(sq);
    }

    public FeatureDiffReaderImpl getDifferences(String fromVersion, String toVersion, Filter filter, String[] userIds)
            throws IOException {
        if(filter == null)
            filter = Filter.INCLUDE;
        
        RevisionInfo r1 = new RevisionInfo(fromVersion);
        RevisionInfo r2 = new RevisionInfo(toVersion);

        // gather modified ids
        ModifiedFeatureIds mfids = store.getModifiedFeatureFIDs(schema.getTypeName(), fromVersion,
                toVersion, filter, userIds, getTransaction());

        // build all the filters to gather created, deleted and modified features at the appropriate
        // revisions, depending also on wheter creation/deletion should be swapped or not
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        VersionedFIDMapper mapper = (VersionedFIDMapper) store.getFIDMapper(schema.getTypeName());

        return new FeatureDiffReaderImpl(store, getTransaction(), schema, r1, r2, mapper, mfids);
    }
    
    // ----------------------------------------------------------------------------------------------
    // INTERNAL SUPPORT METHODS
    // ----------------------------------------------------------------------------------------------
    
   
    /**
     * Clones the query and sets the proper type name into it
     * 
     * @param query
     * @return
     */
    private Query getTypedQuery(Query query) {
        DefaultQuery q = new DefaultQuery(query);
        q.setTypeName(schema.getTypeName());
        return q;
    }
    
    public Set getSupportedHints() {
    	VersionedPostgisDataStore ds = (VersionedPostgisDataStore) getDataStore();
        if(ds.wrapped.isWKBEnabled()) {
            HashSet set = new HashSet();
            set.add(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
            set.add(Hints.JTS_GEOMETRY_FACTORY);
            return set;
        } else {
            return Collections.EMPTY_SET;
        }
    }
    
    @Override
    public QueryCapabilities getQueryCapabilities() {
        try {
            VersionedPostgisDataStore ds = (VersionedPostgisDataStore) getDataStore();
            return ds.wrapped.getFeatureSource(schema.getTypeName()).getQueryCapabilities();
        } catch(Exception e) {
            throw new RuntimeException("This error should never happen", e);
        }
    }

}
