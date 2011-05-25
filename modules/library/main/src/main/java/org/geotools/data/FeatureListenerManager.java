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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * This class is used by DataStore implementations to provide FeatureListener
 * support for the FeatureSources they create.
 * 
 * <p>
 * FeatureWriters created by the DataStore will need to make use of this class
 * to provide the required FeatureEvents.
 * </p>
 * This class has been updated to store listeners using weak references
 * in order to cut down on memory leaks.
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class FeatureListenerManager {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data");	
    /**
     * Hold on to provided FeatureListener using a weak reference.
     * <p>
     * If I was really smart I could do this with the DynamicProxy class; I am not 
     * that smart...
     * <p>
     * @author Jody Garnett
     */
    class WeakFeatureListener implements FeatureListener {
        WeakReference<FeatureListener> reference; 
        public WeakFeatureListener( FeatureListener listener ){
            reference = new WeakReference<FeatureListener>( listener );
        }
        
        public void changed( FeatureEvent featureEvent ) {
            FeatureListener listener = reference.get(); 
            if(listener==null){ 
                removeFeatureListener( this ); 
            } else { 
                listener.changed(featureEvent);
            }
        }        
    }
    
    /**
     * EvenListenerLists by FeatureSource, using a WeakHashMap to allow
     * listener lists to be cleaned up after their FeatureSource is no longer referenced.
     */
    Map<FeatureSource<? extends FeatureType, ? extends Feature>, EventListenerList> listenerMap = new WeakHashMap<FeatureSource<? extends FeatureType, ? extends Feature>, EventListenerList>();

    /**
     * Used by FeaureSource implementations to provide listener support.
     *
     * @param featureSource
     * @param featureListener
     */
    public void addFeatureListener(FeatureSource<? extends FeatureType, ? extends Feature> featureSource,
        FeatureListener featureListener) {
        eventListenerList(featureSource).add(FeatureListener.class,
            featureListener);
    }

    /**
     * Used to clean up a weak reference to a feature listener after
     * it is no longer in use.
     * 
     * @param listener
     */
    void removeFeatureListener( WeakFeatureListener listener ){
        for( EventListenerList list : listenerMap.values() ){
            list.remove(FeatureListener.class, listener);
        }
    }
            
    /**
     * Used by SimpleFeatureSource implementations to provide listener support.
     *
     * @param featureSource
     * @param featureListener
     */
    public void removeFeatureListener(FeatureSource<? extends FeatureType, ? extends Feature> featureSource,
        FeatureListener featureListener) {
        EventListenerList list = eventListenerList(featureSource);
        list.remove(FeatureListener.class, featureListener);
        // don't keep references to feature sources if we have no
        // more any listener. Since there's no way to know a feature source
        // has ceased its existance, better remove references as soon as possible
        if(list.getListenerCount() == 0){
            cleanListenerList(featureSource);
        }
    }

    /**
     * Retrieve the EvenListenerList for the provided FeatureSource.
     * 
     * @param featureSource
     * @return
     */
    private EventListenerList eventListenerList(FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        synchronized (listenerMap) {
            if (listenerMap.containsKey(featureSource)) {
                return listenerMap.get(featureSource);
            } else {
                EventListenerList listenerList = new EventListenerList();
                listenerMap.put(featureSource, listenerList);

                return listenerList;
            }
        }
    }
    
    public void cleanListenerList(FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        synchronized (listenerMap) {
            listenerMap.remove(featureSource);
        }
    }

    /**
     * Returns a Map of FeatureListener[] by SimpleFeatureSource for all matches with
     * featureType and transaction.
     * 
     * <p>
     * A SimpleFeatureSource is considered a match when typeName and Transaction
     * agree.  Transaction.AUTO_COMMIT will match with any change.
     * </p>
     *
     * @param typeName typeName to match against
     * @param transaction Transaction to match against (may be AUTO_COMMIT)
     *
     */
    Map<SimpleFeatureSource,FeatureListener[]> getListeners(String typeName, Transaction transaction) {
        Map<SimpleFeatureSource,FeatureListener[]> map = new HashMap<SimpleFeatureSource,FeatureListener[]>();
        //Map.Entry<SimpleFeatureSource,FeatureListener[]> entry;
        SimpleFeatureSource featureSource;
        EventListenerList listenerList;
        FeatureListener[] listeners;

        synchronized (listenerMap) {
            for (Map.Entry entry : listenerMap.entrySet()) {
                featureSource = (SimpleFeatureSource) entry.getKey();

                if (!featureSource.getName().getLocalPart().equals(typeName)) {
                    continue; // skip as typeName does not match
                }

                if ((transaction != Transaction.AUTO_COMMIT)
                        && hasTransaction(featureSource)) {
                    // need to ensure Transactions match
                    if (transaction != getTransaction(featureSource)) {
                        continue; // skip as transactions do not match        
                    }
                }

                listenerList = (EventListenerList) entry.getValue();
                listeners = (FeatureListener[]) listenerList.getListeners(FeatureListener.class);

                if (listeners.length != 0) {
                    map.put(featureSource, listeners);
                }
            }
        }

        return map;
    }

    private static boolean hasTransaction(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        return featureSource instanceof FeatureStore
                && (((FeatureStore<? extends FeatureType, ? extends Feature>) featureSource)
                        .getTransaction() != null);
    }

    private static Transaction getTransaction(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        if (hasTransaction(featureSource)) {
            return ((FeatureStore<? extends FeatureType, ? extends Feature>) featureSource)
                    .getTransaction();
        }

        return Transaction.AUTO_COMMIT;
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * <p>
     * This method is called by:
     * </p>
     * 
     * <ul>
     * <li>
     * FeatureWriter.next() with FeatureWriter.hasNext() == false<br>
     * - when an existing Feature is removed with Tranasaction.AUTO_COMMIT all
     * listeners registered with SimpleFeatureSource of typeName will be notified.
     * </li>
     * <li>
     * FeatureWriter.next()with FeatureWriter.hasNext() == false<br>
     * - when an existing Feature is removed with a Transaction all listeners
     * registered with SimpleFeatureSource of typeName and with the same Transaction
     * will be notified.
     * </li>
     * </ul>
     * <p>
     * <b>NOTE</b> requiring to fire this event at FeatureWriter.next() is quite
     * a gap inherited from an old API when {@link FeatureWriter#write()} didn't
     * exist yet. It's a good idea though to fire the event at FeatureWriter.write()
     * instead of FeatureWriter.next() so there are actually changes to notify for.
     * </p>
     *
     * @param typeName typeName being modified
     * @param transaction Transaction used for change
     * @param bounds BoundingBox of changes (may be <code>null</code> if unknown)
     * @param commit true if
     */
    public void fireFeaturesAdded(String typeName, Transaction transaction,
        ReferencedEnvelope bounds, boolean commit) {
        if (commit) {
            fireCommit(typeName, transaction, FeatureEvent.FEATURES_ADDED, bounds );
        } else {
            fireEvent(typeName, transaction, FeatureEvent.FEATURES_ADDED, bounds );
        }
    }
    
    /**
     * Provided event will be used as a template for notifying all FeatureSources
     * for the provided typeName.
     * 
     * @param typeName
     * @param transaction
     * @param event
     */
    public void fireEvent(String typeName, Transaction transaction, FeatureEvent event ){
    	if( event.getType() == FeatureEvent.Type.COMMIT ||
    	    event.getType() == FeatureEvent.Type.ROLLBACK ){

    		// This is a commit event; it needs to go out to everyone
    		// Listeners on the Transaction need to be told about any feature ids that were changed
    		// Listeners on AUTO_COMMIT need to be told that something happened            
            Map<SimpleFeatureSource,FeatureListener[]> map = getListeners(typeName, Transaction.AUTO_COMMIT);
            for (Map.Entry entry : map.entrySet()) {
                FeatureSource featureSource = (FeatureSource) entry.getKey();
                FeatureListener[] listeners = (FeatureListener[]) entry.getValue();
                event.setFeatureSource( featureSource );
                for (FeatureListener listener : listeners ){
                	try {
                		listener.changed(event);
                	}
                	catch( Throwable t ){
                		LOGGER.log( Level.FINE, "Could not deliver "+event+" to "+listener+":"+t.getMessage(), t );
                	}
                }
            }
    	}
    	else {
    		// This is a commit event; it needs to go out to everyone
    		// Listeners on the Transaction need to be told about any feature ids that were changed
    		// Listeners on AUTO_COMMIT need to be told that something happened            
    		Map<SimpleFeatureSource,FeatureListener[]> map = getListeners(typeName, transaction);
            for (Map.Entry entry : map.entrySet()) {
                FeatureSource featureSource = (FeatureSource) entry.getKey();
                FeatureListener[] listeners = (FeatureListener[]) entry.getValue();
                event.setFeatureSource( featureSource );
                for (FeatureListener listener : listeners ){
                	try {
                		listener.changed(event);
                	}
                	catch( Throwable t ){
                		LOGGER.log( Level.FINE, "Could not deliver "+event+" to "+listener+":"+t.getMessage(), t );
                	}
                }
            }
    	}
    }
    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * <p>
     * This method is called by:
     * </p>
     * 
     * <ul>
     * <li>
     * FeatureWriter.next() with FeatureWriter.hasNext() == true <br>
     * - when an existing Feature is modified with Tranasaction.AUTO_COMMIT
     * all listeners registered with SimpleFeatureSource of typeName will be
     * notified.
     * </li>
     * <li>
     * FeatureWriter.next()with FeatureWriter.hasNext() == true <br>
     * - when an existing Feature is modified, with a Transaction all
     * listeners registered with SimpleFeatureSource of typeName and with the same
     * Transaction will be notified.
     * </li>
     * </ul>
     * <p>
     * <b>NOTE</b> requiring to fire this event at FeatureWriter.next() is quite
     * a gap inherited from an old API when {@link FeatureWriter#write()} didn't
     * exist yet. It's a good idea though to fire the event at FeatureWriter.write()
     * instead of FeatureWriter.next() so there are actually changes to notify for.
     * </p>
     * 
     *
     * @param typeName typeName being modified
     * @param transaction Transaction used for change
     * @param bounds BoundingBox of changes (may be <code>null</code> if
     *        unknown)
     */
    public void fireFeaturesChanged(String typeName, Transaction transaction,
        ReferencedEnvelope bounds, boolean commit) {
        if (commit) {
            fireCommit(typeName, transaction, FeatureEvent.FEATURES_CHANGED, bounds );
        } else {
            fireEvent(typeName, transaction, FeatureEvent.FEATURES_CHANGED, bounds );
        }

    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * <p>
     * This method is called by:
     * </p>
     * 
     * <ul>
     * <li>
     * Transaction.commit()<br> - when changes have occured on a Transaction
     * all listeners registered with SimpleFeatureSource of typeName will be
     * notified except those with the Same Transaction
     * </li>
     * <li>
     * Transaction.rollback()<br> - when changes have been reverted only those
     * listeners registered with SimpleFeatureSource of typeName and with the same
     * Transaction will be notified.
     * </li>
     * </ul>
     * 
     *
     * @param typeName typeName being modified
     * @param transaction Transaction used for change
     * @param commit <code>true</code> for <code>commit</code>,
     *        <code>false</code> for <code>rollback</code>
     */
    public void fireChanged(String typeName, Transaction transaction,
        boolean commit) {
        if (commit) {
            fireCommit(typeName, transaction, FeatureEvent.FEATURES_CHANGED, null );
        } else {
            fireEvent(typeName, transaction, FeatureEvent.FEATURES_CHANGED, null );
        }
    }

    /**
     * Fire notifications out to everyone.
     * 
     * @param typeName
     * @param transaction
     */
    private void fireCommit( String typeName, Transaction transaction, int type, ReferencedEnvelope bounds) {
        FeatureSource<? extends FeatureType, ? extends Feature> featureSource;
        FeatureListener[] listeners;
        FeatureEvent event;
        Map<SimpleFeatureSource,FeatureListener[]> map = getListeners(typeName, Transaction.AUTO_COMMIT);

        for (Map.Entry entry : map.entrySet()) {
            featureSource = (FeatureSource<? extends FeatureType, ? extends Feature>) entry.getKey();
            listeners = (FeatureListener[]) entry.getValue();

            if (hasTransaction(featureSource)
                    && (getTransaction(featureSource) == transaction)) {
                continue; // skip notify members of the same transaction
            }

            event = new FeatureEvent(featureSource, type, bounds);

            for (int l = 0; l < listeners.length; l++) {
                listeners[l].changed(event);
            }
        }
    }
    /**
     * Fire notifications out to those listing on this transaction.
     * @param typeName
     * @param transaction
     * @param type
     * @param bounds
     */
    private void fireEvent( String typeName, Transaction transaction, int type, ReferencedEnvelope bounds) {        
        FeatureSource<? extends FeatureType, ? extends Feature> featureSource;
        FeatureListener[] listeners;
        FeatureEvent event;
        Map<SimpleFeatureSource,FeatureListener[]> map = getListeners(typeName, transaction);

        for (Map.Entry entry : map.entrySet()) {
            featureSource = (FeatureSource) entry.getKey();
            listeners = (FeatureListener[]) entry.getValue();
            
            event = new FeatureEvent(featureSource,type, bounds);

            for (int l = 0; l < listeners.length; l++) {
                listeners[l].changed(event);
            }
        }
    }
    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     * 
     * <p>
     * This method is called by:
     * </p>
     * 
     * <ul>
     * <li>
     * FeatureWrtier.remove() - when an existing Feature is removed with
     * Tranasaction.AUTO_COMMIT all listeners registered with SimpleFeatureSource of
     * typeName will be notified.
     * </li>
     * <li>
     * FeatureWrtier.remove() - when an existing Feature is removed with a
     * Transaction all listeners registered with SimpleFeatureSource of typeName and
     * with the same Transaction will be notified.
     * </li>
     * </ul>
     * 
     *
     * @param typeName typeName being modified
     * @param transaction Transaction used for change
     * @param bounds BoundingBox of changes (may be <code>null</code> if
     *        unknown)
     */
    public void fireFeaturesRemoved(String typeName, Transaction transaction,
        ReferencedEnvelope bounds, boolean commit ) {
        if (commit) {
            fireCommit(typeName, transaction, FeatureEvent.FEATURES_REMOVED, bounds );
        } else {
            fireEvent(typeName, transaction, FeatureEvent.FEATURES_REMOVED, bounds );
        }
    }
}
