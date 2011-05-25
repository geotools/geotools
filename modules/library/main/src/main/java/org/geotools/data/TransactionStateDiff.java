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
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.data.Transaction.State;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A Transaction.State that keeps a difference table for use with
 * AbstractDataStore.
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class TransactionStateDiff implements State {
    /**
     * DataStore used to commit() results of this transaction.
     *
     * @see TransactionStateDiff.commit();
     */
    AbstractDataStore store;

    /** Tranasction this State is opperating against. */
    Transaction transaction;

    /**
     * Map of differences by typeName.
     * 
     * <p>
     * Differences are stored as a Map of Feature by fid, and are reset during
     * a commit() or rollback().
     * </p>
     */
    Map typeNameDiff = new HashMap();

    public TransactionStateDiff(AbstractDataStore dataStore) {
        store = dataStore;
    }

    public synchronized void setTransaction(Transaction transaction) {
        if (transaction != null) {
            // configure
            this.transaction = transaction;
        } else {
            this.transaction = null;

            if (typeNameDiff != null) {
                for (Iterator i = typeNameDiff.values().iterator();
                        i.hasNext();) {
                    Diff diff = (Diff) i.next();
                    diff.clear();
                }

                typeNameDiff.clear();
            }

            store = null;
        }
    }

    public synchronized Diff diff(String typeName) throws IOException {
        if (!exists(typeName)) {
            throw new IOException(typeName + " not defined");
        }

        if (typeNameDiff.containsKey(typeName)) {
            return (Diff) typeNameDiff.get(typeName);
        } else {
            Diff diff = new Diff();
            typeNameDiff.put(typeName, diff);

            return diff;
        }
    }
    
    boolean exists(String typeName) {
        String[] types;
        try {
            types = store.getTypeNames();
        } catch (IOException e) {
            return false;
        }
        Arrays.sort(types);

        return Arrays.binarySearch(types, typeName) != -1;
    }

    /**
     * @see org.geotools.data.Transaction.State#addAuthorization(java.lang.String)
     */
    public synchronized void addAuthorization(String AuthID)
        throws IOException {
        // not required for TransactionStateDiff
    }

    /**
     * Will apply differences to store.
     *
     * @see org.geotools.data.Transaction.State#commit()
     */
    public synchronized void commit() throws IOException {
        Map.Entry entry;

        for (Iterator i = typeNameDiff.entrySet().iterator(); i.hasNext();) {
            entry = (Entry) i.next();

            String typeName = (String) entry.getKey();
            Diff diff = (Diff) entry.getValue();
            applyDiff(typeName, diff);
        }
    }

    /**
     * Called by commit() to apply one set of diff
     * 
     * <p>
     * The provided <code> will be modified as the differences are applied,
     * If the operations are all successful diff will be empty at
     * the end of this process.
     * </p>
     * 
     * <p>
     * diff can be used to represent the following operations:
     * </p>
     * 
     * <ul>
     * <li>
     * fid|null: represents a fid being removed
     * </li>
     * 
     * <li>
     * fid|feature: where fid exists, represents feature modification
     * </li>
     * <li>
     * fid|feature: where fid does not exist, represents feature being modified
     * </li>
     * </ul>
     * 
     *
     * @param typeName typeName being updated
     * @param diff differences to apply to FeatureWriter
     *
     * @throws IOException If the entire diff cannot be writen out
     * @throws DataSourceException If the entire diff cannot be writen out
     */
    void applyDiff(String typeName, Diff diff) throws IOException {
        if (diff.isEmpty()) {
            return;
        }
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
		try{
        	writer = store.createFeatureWriter(typeName, transaction);
        }catch (UnsupportedOperationException e) {
			// backwards compatibility
        	try {
        		writer = store.getFeatureWriter(typeName);
        	}
        	catch( UnsupportedOperationException eek){
        		throw e; // throw original - our fallback did not work
        	}
		}
        SimpleFeature feature;
        SimpleFeature update;
        String fid;

        try {
            while (writer.hasNext()) {
                feature = (SimpleFeature)writer.next();
                fid = feature.getID();

                if (diff.modified2.containsKey(fid)) {
                    update = (SimpleFeature) diff.modified2.get(fid);

                    if (update == NULL) {
                        writer.remove();

                        // notify
                        store.listenerManager.fireFeaturesRemoved(typeName,
                            transaction, ReferencedEnvelope.reference(feature.getBounds()), true);
                    } else {
                        try {
                            feature.setAttributes(update.getAttributes());
                            writer.write();

                            // notify                        
                            ReferencedEnvelope bounds = new ReferencedEnvelope((CoordinateReferenceSystem)null);
                            bounds.include(feature.getBounds());
                            bounds.include(update.getBounds());
                            store.listenerManager.fireFeaturesChanged(typeName,
                                transaction, bounds, true);
                        } catch (IllegalAttributeException e) {
                            throw new DataSourceException("Could update " + fid,
                                e);
                        }
                    }
                }
            }

            SimpleFeature addedFeature;
            SimpleFeature nextFeature;
            
            synchronized (diff) {
            	for (Iterator i = diff.added.values().iterator(); i.hasNext();) {
            		addedFeature = (SimpleFeature) i.next();
            		
            		fid = addedFeature.getID();
            		
            		nextFeature = (SimpleFeature)writer.next();
            		
            		if (nextFeature == null) {
            			throw new DataSourceException("Could not add " + fid);
            		} else {
            			try {
            				nextFeature.setAttributes(addedFeature
            						.getAttributes());
            				writer.write();
            				
            				// notify                        
            				store.listenerManager.fireFeaturesAdded(typeName,
            						transaction, ReferencedEnvelope.reference(nextFeature.getBounds()), true);
            			} catch (IllegalAttributeException e) {
            				throw new DataSourceException("Could update " + fid,
            						e);
            			}
            		}
            	}
            }
        } finally {
            writer.close();
            store.listenerManager.fireChanged(typeName, transaction, true);
            diff.clear();
        }
    }

    /**
     * @see org.geotools.data.Transaction.State#rollback()
     */
    public synchronized void rollback() throws IOException {
        Map.Entry entry;

        for (Iterator i = typeNameDiff.entrySet().iterator(); i.hasNext();) {
            entry = (Entry) i.next();

            String typeName = (String) entry.getKey();
            Diff diff = (Diff) entry.getValue();

            diff.clear(); // rollback differences
            store.listenerManager.fireChanged(typeName, transaction, false);
        }
    }

    /**
     * Convience Method for a Transaction based FeatureReader.
     * 
     * <p>
     * Constructs a DiffFeatureReader that works against this Transaction.
     * </p>
     *
     * @param typeName TypeName to aquire a Reader on
     *
     * @return  FeatureReader<SimpleFeatureType, SimpleFeature> the mask orgional contents with against the
     *         current Differences recorded by the Tansasction State
     *
     * @throws IOException If typeName is not Manged by this Tansaction State
     */
    public synchronized  FeatureReader<SimpleFeatureType, SimpleFeature> reader(String typeName)
        throws IOException {
        Diff diff = diff(typeName);
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(typeName);

        return new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(reader, diff);
    }

    /**
     * Convience Method for a Transaction based FeatureWriter
     * 
     * <p>
     * Constructs a DiffFeatureWriter that works against this Transaction.
     * </p>
     *
     * @param typeName Type Name to record differences against
     * @param filter 
     *
     * @return A FeatureWriter that records Differences against a FeatureReader
     *
     * @throws IOException If a FeatureRader could not be constucted to record
     *         differences against
     */
    public synchronized FeatureWriter<SimpleFeatureType, SimpleFeature> writer(final String typeName, Filter filter)
        throws IOException {
        Diff diff = diff(typeName);
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(store.getFeatureReader(typeName, new DefaultQuery(typeName, filter)), filter);

        return new DiffFeatureWriter(reader, diff, filter) {
                public void fireNotification(int eventType, ReferencedEnvelope bounds) {
                    switch (eventType) {
                    case FeatureEvent.FEATURES_ADDED:
                        store.listenerManager.fireFeaturesAdded(typeName,
                            transaction, bounds, false);

                        break;

                    case FeatureEvent.FEATURES_CHANGED:
                        store.listenerManager.fireFeaturesChanged(typeName,
                            transaction, bounds, false);

                        break;

                    case FeatureEvent.FEATURES_REMOVED:
                        store.listenerManager.fireFeaturesRemoved(typeName,
                            transaction, bounds, false);

                        break;
                    }
                }
                public String toString() {
                    return "<DiffFeatureWriter>("+reader.toString()+")";
                }
            };
    }
    /**
     * A NullObject used to represent the absence of a SimpleFeature.
     * <p>
     * This class is used by TransactionStateDiff as a placeholder
     * to represent features that have been removed. The concept
     * is generally useful and may wish to be taken out as a separate
     * class (used for example to represent deleted rows in a shapefile).
     */
    public static final SimpleFeature NULL=new SimpleFeature( ){
        public Object getAttribute(String path) {
            return null;
        }

        public Object getAttribute(int index) {
            return null;
        }

        public Object[] getAttributes(Object[] attributes) {
            return null;
        }

        public ReferencedEnvelope getBounds() {
            return null;
        }

        public Geometry getDefaultGeometry() {
            return null;
        }

        public SimpleFeatureType getFeatureType() {
            return null;
        }

        public String getID() {
            return null;
        }
        public FeatureId getIdentifier() {
        	return null;
        }
        public int getNumberOfAttributes() {
            return 0;
        }

        public void setAttribute(int position, Object val) {
        }

        public void setAttribute(String path, Object attribute)
                throws IllegalAttributeException {
        }

        public void setDefaultGeometry(Geometry geometry)
                throws IllegalAttributeException {
        }

        public Object getAttribute(Name name) {
            return null;
        }

        public int getAttributeCount() {
            return 0;
        }

        public List<Object> getAttributes() {
            return null;
        }

        public SimpleFeatureType getType() {
            return null;
        }

        public void setAttribute(Name name, Object value) {
        }

        public void setAttributes(List<Object> values) {
        }

        public void setAttributes(Object[] values) {
        }

        public void setDefaultGeometry(Object geometry) {
        }

        public GeometryAttribute getDefaultGeometryProperty() {
            return null;
        }

        public void setDefaultGeometryProperty(
                GeometryAttribute geometryAttribute) {
        }

        public Collection<Property> getProperties(Name name) {
            return null;
        }

        public Collection<Property> getProperties() {
            return null;
        }
        
        public Collection<Property> getProperties(String name) {
            return null;
        }

        public Property getProperty(Name name) {
            return null;
        }

        public Property getProperty(String name) {
            return null;
        }

        public Collection<? extends Property> getValue() {
            return null;
        }

        public void setValue(Collection<Property> values) {
        }

        public AttributeDescriptor getDescriptor() {
            return null;
        }

        public Name getName() {
            return null;
        }

        public Map<Object, Object> getUserData() {
            return null;
        }

        public boolean isNillable() {
            return false;
        }

        public void setValue(Object newValue) {
        }
        public String toString() {
            return "<NullFeature>";
        }
        public int hashCode() {
            return 0;
        }
		public boolean equals( Object arg0 ) {
		    return arg0 == this;
		}
		public void validate() {
		}
    };


}
