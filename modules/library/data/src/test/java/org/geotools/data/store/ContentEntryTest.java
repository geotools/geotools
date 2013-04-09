/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.feature.NameImpl;
import org.junit.Test;
import org.opengis.feature.type.Name;

/**
 * Test the behaviour of {@link ContentEntry}.
 * @author Mauro Bartolomeoli (mauro.bartolomeoli@geo-solutions.it)
 */
public class ContentEntryTest {

    /**
     * Test that the a Transaction is removed from state when the Transaction is
     * closed.
     */
    @Test
    public void transactionCacheClearedOnTransactionClose() {
        ContentDataStore dataStore = new ContentDataStore() {
    
            @Override
            protected List<Name> createTypeNames() throws IOException {                
                return null;
            }
    
            @Override
            protected ContentFeatureSource createFeatureSource(ContentEntry entry)
                    throws IOException {
                return null;
            }
        };
    
        Transaction transaction = new DefaultTransaction();
    
        ContentEntry entry = new ContentEntry(dataStore, new NameImpl("test"));
        ContentState state = entry.getState(transaction);
        new DiffTransactionState(state);
    
        // state is extracted from state cache
        assertSame(state, entry.getState(transaction));
        // and contains our transaction
        assertSame(state.getTransaction(), transaction);
    
        try {
            transaction.close();
            
            // after transaction closing, the old state has been cleared, so 
            // a new one is built and returned
            ContentState stateForClosedTransaction = entry.getState(transaction);
            assertNotSame(stateForClosedTransaction, state);
        } catch (IOException e) {
            fail("Cannot close transaction: " + e.getLocalizedMessage());
        }
    }

}
