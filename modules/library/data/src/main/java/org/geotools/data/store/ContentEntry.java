/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.Transaction;
import org.opengis.feature.type.Name;


/**
 * An entry for a type or feature source provided by a datastore.
 * <p>
 * This class is only of concern to subclasses, client code should never see
 * this class.
 * </p>
 * <p>
 * An entry maintains state on a per-transaction basis. The {@link #getState(Transaction)}
 * method is used to get at this state.
 * <pre>
 *   <code>
 *   ContentEntry entry = ...;
 *   
 *   Transaction tx1 = new Transaction();
 *   Transaction tx2 = new Transaction();
 *   
 *   ContentState s1 = entry.getState( tx1 );
 *   ContentState s2 = entry.getState( tx2 );
 *   
 *   s1 != s2;
 *   </code>
 * </pre>
 * </p>
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public final class ContentEntry {
    /**
     * Qualified name of the entry.
     */
    Name typeName;

    /**
     * Map<Transaction,ContentState> state according to Transaction.
     */
    Map<Transaction,ContentState> state;

    /**
     * backpointer to datastore
     */
    ContentDataStore dataStore;

    /**
     * Creates the entry.
     * 
     * @param dataStore The datastore of the entry.
     * @param typeName The name of the entry.
     */
    public ContentEntry(ContentDataStore dataStore, Name typeName) {
        this.typeName = typeName;
        this.dataStore = dataStore;

        this.state = new HashMap<Transaction, ContentState>();

        //create a state for the auto commit transaction
        ContentState autoState = dataStore.createContentState(this);
        autoState.setTransaction(Transaction.AUTO_COMMIT);
        this.state.put(Transaction.AUTO_COMMIT, autoState);
    }

    /**
     * Qualified name of the entry.
     */
    public Name getName() {
        return typeName;
    }

    /**
     * Unqualified name of the entry.
     * <p>
     * Equivalent to: <code>getName().getLocalPart()</code>.
     * </p>
     */
    public String getTypeName() {
        return typeName.getLocalPart();
    }

    /**
     * Backpointer to datastore.
     */
    public ContentDataStore getDataStore() {
        return dataStore;
    }

    /**
     * Returns state for the entry for a particular transaction.
     * <p>
     * In the event that no state exists for the supplied transaction one will
     * be created by copying the state of {@link Transaction#AUTO_COMMIT}.
     * </p>
     * @param transaction A transaction.
     *
     * @return The state for the transaction.
     */
    public ContentState getState(Transaction transaction) {
        if (state.containsKey(transaction)) {
            return state.get(transaction);
        } else {
            ContentState auto = state.get(Transaction.AUTO_COMMIT);
            ContentState copy = (ContentState) auto.copy();
            copy.setTransaction(transaction != null ? transaction : Transaction.AUTO_COMMIT);
            state.put(transaction, copy);

            return copy;
        }
    }

    /**
     * Called by a ContentState to let others transactions know of a modification.
     * <p>
     * Transaction.AUTO_COMMIT state will call this method for everything; others
     * mostly use this to broadcast the BatchFeatureEvents issued during commit
     * and rollback.
     */
    void notifiyFeatureEvent( ContentState source, FeatureEvent notification){
        for(ContentState entry : state.values() ){
           if( entry == source ) {
               continue;  // no notificaiton required               
           }
           for( FeatureListener listener : source.listeners ){
               try {
                   listener.changed( notification );
               }
               catch (Throwable t ){
                   // problem issuing notification to an interested party
                   dataStore.LOGGER.log( Level.WARNING, "Problem issuing feature event "+notification, t );
               }
           }
        }
    }
    
    /**
     * Disposes the entry by disposing all maintained state.
     */
    public void dispose() {
        //kill all state
        for (ContentState s : state.values() ) {
            s.close();
        }
    }
    
    public String toString() {
        return getTypeName();
    }
}
