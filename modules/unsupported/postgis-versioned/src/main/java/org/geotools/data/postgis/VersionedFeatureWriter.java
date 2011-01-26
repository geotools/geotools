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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.MutableFIDFeature;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A feature writer that handles versioning using two slave feature writers to expire old features
 * and create new revisions of the features
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class VersionedFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    private static Long NON_EXPIRED = new Long(Long.MAX_VALUE);

    private FeatureWriter<SimpleFeatureType, SimpleFeature> updateWriter;

    private FeatureWriter<SimpleFeatureType, SimpleFeature> appendWriter;

    private SimpleFeatureType featureType;

    private SimpleFeature oldFeature;

    private SimpleFeature newFeature;

    private SimpleFeature liveFeature;

    private VersionedJdbcTransactionState state;

    private VersionedFIDMapper mapper;

    private FeatureListenerManager listenerManager;

    private boolean autoCommit;

    /**
     * Builds a new feature writer
     * 
     * @param updateWriter
     * @param appendWriter
     * @param featureType
     *            the outside visible feature type
     * @param mapper
     * @param autoCommit
     *            if true, the transaction need to be committed once the writer is closed
     */
    public VersionedFeatureWriter(FeatureWriter<SimpleFeatureType, SimpleFeature> updateWriter,
            FeatureWriter<SimpleFeatureType, SimpleFeature> appendWriter,
            SimpleFeatureType featureType, VersionedJdbcTransactionState state,
            VersionedFIDMapper mapper, boolean autoCommit) {
        this.updateWriter = updateWriter;
        this.appendWriter = appendWriter;
        this.featureType = featureType;
        this.state = state;
        this.mapper = mapper;
        this.autoCommit = autoCommit;
    }

    public void setFeatureListenerManager(FeatureListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    public void close() throws IOException {
        if (updateWriter != null)
            updateWriter.close();
        appendWriter.close();

        // double check, state.getTransaction() will return null if the transaction
        // has already been closed
        if (autoCommit && state.getTransaction() != null) {
            state.getTransaction().commit();
            state.getTransaction().close();
        }
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public boolean hasNext() throws IOException {
        appendWriter.hasNext();
        if (updateWriter != null)
            return updateWriter.hasNext();
        else
            return false;
    }

    public SimpleFeature next() throws IOException {
        SimpleFeature original = null;
        if (updateWriter != null && updateWriter.hasNext()) {
            oldFeature = updateWriter.next();
            newFeature = appendWriter.next();
            original = oldFeature;
            state.expandDirtyBounds(getWgs84FeatureEnvelope(oldFeature));
        } else {
            oldFeature = null;
            newFeature = appendWriter.next();
            original = newFeature;
        }

        try {
            liveFeature = DataUtilities.reType(featureType, original);
            // if the feature it brand new, it'll have a random fid, not a
            // proper one, keep using
            // it, we cannot un-version it
            String unversionedId = liveFeature.getID();
            if (oldFeature != null)
                unversionedId = mapper.getUnversionedFid(liveFeature.getID());
            liveFeature = new MutableFIDFeature((List) liveFeature
                    .getAttributes(), featureType, unversionedId);
            return liveFeature;
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Error casting versioned feature to external one. "
                    + "Should not happen, there's a bug at work", e);
        }
    }

    /**
     * Computes a feature's envelope, using all geometry attributes, and returns an envelop in WGS84
     * 
     * @param oldFeature
     * @return
     * @throws TransformException
     */
    public Envelope getWgs84FeatureEnvelope(SimpleFeature feature) throws IOException {
        try {
            Envelope result = new Envelope();
            SimpleFeatureType ft = feature.getFeatureType();
            for (int i = 0; i < ft.getAttributeCount(); i++) {
                AttributeDescriptor at = ft.getDescriptor(i);
                if (at instanceof GeometryDescriptor) {
                	GeometryDescriptor gat = (GeometryDescriptor) at;
                    CoordinateReferenceSystem crs = gat.getCoordinateReferenceSystem();

                    Geometry geom = (Geometry) feature.getAttribute(i);
                    if (geom != null) {
                        Envelope env = geom.getEnvelopeInternal();
                        if (crs != null)
                            env = JTS.toGeographic(env, crs);
                        result.expandToInclude(env);
                    }
                }
            }
            return result;
        } catch (TransformException e) {
            throw new DataSourceException(
                    "Error computing lat/long envelope of the current feature. "
                            + "This is needed to update the changeset bbox", e);
        }
    }

    public void remove() throws IOException {
        // if the feature is new, we have nothing to remove
        if (oldFeature == null) {
            throw new IOException("No feature available to remove");
        }

        listenerManager.fireFeaturesRemoved(getFeatureType().getTypeName(), state.getTransaction(),
                ReferencedEnvelope.reference(oldFeature.getBounds()), false);
        writeOldFeature(true);
    }

    private void writeOldFeature(boolean expire) throws IOException, DataSourceException {
        try {
            if(expire)
                oldFeature.setAttribute("expired", new Long(state.getRevision()));
            updateWriter.write();
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Error writing expiration tag on old feature. "
                    + "Should not happen, there's a bug at work.", e);
        } catch (FeatureLockException fle) {
            // we have to mangle the id here too
            String unversionedFid = mapper.getUnversionedFid(fle.getFeatureID());
            FeatureLockException mangled = new FeatureLockException(fle.getMessage(),
                    unversionedFid, fle.getCause());
            throw mangled;
        }
    }

    public void write() throws IOException {
        Statement st = null;
        try {
            /*
             Ok, this is complex. We have to deal with four separate cases:
             1) the old feature is not there, meaning we're inserting a new feature
             2) the old feature is there, the new feature is equal to the old one -> no changes, 
                let's just move on
             3) the old feature is there, and it's the first time we modify that feature
                in this transactions, meaning we need to expire the old feature, and 
                create a new, non expired one
             4) the old feature is there, but we already modified it during this transaction. This
                means we have to update the old feature 
             */ 
            
            boolean dirtyFeature = false;
            if (oldFeature != null) {
                // if there is an old feature, make sure to write a new revision only if the
                // feauture was modified
                boolean dirty = false;
                for (int i = 0; i < liveFeature.getAttributeCount(); i++) {
                    AttributeDescriptor at = liveFeature.getFeatureType().getDescriptor(i);
                    Object newValue = liveFeature.getAttribute(at.getLocalName());
                    Object oldValue = oldFeature.getAttribute(at.getLocalName());
                    newFeature.setAttribute(at.getLocalName(), newValue);
                    if (!DataUtilities.attributesEqual(newValue, oldValue)) {
                        dirty = true;
                    }
                }
                if (!dirty)
                    return;
            }
            
            // check if the feature is dirty. The live feature has the right external id
            String typeName = liveFeature.getFeatureType().getTypeName();
            String fid = liveFeature.getID();
            dirtyFeature = state.isFidDirty(typeName, fid);
            
            SimpleFeature writtenFeature = null;
            if(dirtyFeature) {
                // we're updating again a feature we already touched, so we have to move
                // attributes from the live to the old, and make sure the old is not expired
                // (we may have deleted and then re-inserted that feature, if FID are the user
                // assigned kind we can get into troubles with duplicated primary keys)
                
                // copy attributes from live to new
                for (int i = 0; i < liveFeature.getAttributeCount(); i++) {
                    AttributeDescriptor at = liveFeature.getFeatureType().getDescriptor(i);
                    oldFeature.setAttribute(at.getLocalName(), liveFeature.getAttribute(at.getLocalName()));
                }
                
                // write the old one
                writeOldFeature(false);
                
                writtenFeature = oldFeature;
            } else {
                // expire if needed
                if(oldFeature != null)
                    writeOldFeature(true);
                
                // copy attributes from live to new
                for (int i = 0; i < liveFeature.getAttributeCount(); i++) {
                    AttributeDescriptor at = liveFeature.getFeatureType().getDescriptor(i);
                    newFeature.setAttribute(at.getLocalName(), liveFeature.getAttribute(at.getLocalName()));
                }
    
                //set revision and expired,
                newFeature.setAttribute("expired", NON_EXPIRED);
                newFeature.setAttribute("revision", new Long(state.getRevision()));
                
                // mark the feature creation
                if (oldFeature != null) {
                    newFeature.setAttribute("created", oldFeature.getAttribute("created"));
                } else {
                    newFeature.setAttribute("created", new Long(state.getRevision()));
                }
    
                // set FID to the old one
                // TODO: check this, I'm not sure this is the proper handling
                String id = null;
                if (oldFeature != null) {
                    id = mapper.createVersionedFid(liveFeature.getID(), state.getRevision()); 
                    newFeature.setAttribute("created", oldFeature.getAttribute("created"));
                } else if (!mapper.hasAutoIncrementColumns()) {
                    // preserve the outer id for UUID insertions
                    ((MutableFIDFeature) newFeature).setID(liveFeature.getID());
                    id = mapper.createID(state.getConnection(), newFeature, null);
                    newFeature.setAttribute("created", new Long(state.getRevision()));
                }
                
                // transfer generated id values to the primary key attributes
                if (id != null) {
                    ((MutableFIDFeature) newFeature).setID(id);
    
                    Object[] pkatts = mapper.getPKAttributes(id);
                    for (int i = 0; i < pkatts.length; i++) {
                        newFeature.setAttribute(mapper.getColumnName(i), pkatts[i]);
                    }
                } else {
                    
                }
                
                // write
                appendWriter.write();
                
                // if the id is auto-generated, gather it from the db
                if (oldFeature == null && mapper.hasAutoIncrementColumns()) {
                    st = state.getConnection().createStatement();
                    id = mapper.createID(state.getConnection(), newFeature, st);
                }

                // make sure the newly generated id is set into the live
                // feature, and that it's typed, too
                ((MutableFIDFeature) newFeature).setID(id);
                ((MutableFIDFeature) liveFeature).setID(mapper.getUnversionedFid(id));
                
                // mark the fid as dirty
                state.setFidDirty(liveFeature.getFeatureType().getTypeName(), liveFeature.getID());
                
                writtenFeature = newFeature;
            }

            // update dirty bounds
            state.expandDirtyBounds(getWgs84FeatureEnvelope(writtenFeature));

            // and finally notify the user
            if (oldFeature != null) {
                ReferencedEnvelope bounds = ReferencedEnvelope.reference(oldFeature.getBounds());
                bounds.include(liveFeature.getBounds());
                listenerManager.fireFeaturesChanged(getFeatureType().getTypeName(), state
                        .getTransaction(), bounds, false);
            } else {
                listenerManager.fireFeaturesAdded(getFeatureType().getTypeName(), state
                        .getTransaction(), ReferencedEnvelope.reference(liveFeature.getBounds()), false);
            }
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Error writing expiration tag on old feature. "
                    + "Should not happen, there's a bug at work.", e);
        } catch (SQLException e) {
            throw new DataSourceException(
                    "Error creating a new statement for primary key generation", e);
        } finally {
            JDBCUtils.close(st);
        }
    }

}
