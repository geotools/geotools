/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.geotools.data.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for {@link JDBCTransactionState}.
 *
 * @author awaterme
 */
public class JDBCTransactionStateTest {

    private Connection mockConnection = mock(Connection.class);

    private Transaction mockTransaction = mock(Transaction.class);

    private Handler mockLogHandler = mock(Handler.class);

    private JDBCDataStore dataStore;

    private int warningsCount;

    /** Setup a log handler counting {@link LogRecord} having {@link Level#WARNING}. */
    @Before
    public void setUp() {
        // when(mockLogHandler.publish(any(LogRecord.class)));
        doAnswer(
                        new Answer<Object>() {
                            public Object answer(InvocationOnMock invocation) {
                                Object[] arguments = invocation.getArguments();
                                LogRecord logRecord = (LogRecord) arguments[0];
                                if (logRecord.getLevel() == Level.WARNING
                                        && !logRecord.getSourceMethodName().equals("finalize")) {
                                    warningsCount++;
                                }
                                return null;
                            }
                        })
                .when(mockLogHandler)
                .publish(any(LogRecord.class));
        dataStore = new JDBCDataStore();
        dataStore.getLogger().addHandler(mockLogHandler);
    }

    @After
    public void tearDown() {
        dataStore.getLogger().removeHandler(mockLogHandler);
        dataStore.dispose();
    }

    /**
     * Tests if connection gets closed on internally managed connections and creation of log
     * statements.
     */
    @Test
    public void testSetTransactionNullWithInternalConnection() throws IOException, SQLException {
        JDBCTransactionState state = new JDBCTransactionState(mockConnection, dataStore);
        // init state
        state.setTransaction(mockTransaction);

        // clear state
        state.setTransaction(null);

        // make sure connection is closed
        verify(mockConnection, times(1)).close();
        testLogWarnings(state);
    }

    @Test
    public void testSetTransactionNullWithExternalConnection() throws Exception {
        JDBCTransactionState state = new JDBCTransactionState(mockConnection, dataStore, true);
        // init state
        state.setTransaction(mockTransaction);

        // clear state
        state.setTransaction(null);

        // make sure connection is closed
        verify(mockConnection, times(0)).close();
        testLogWarnings(state);
    }

    void testLogWarnings(JDBCTransactionState state) {
        Assert.assertEquals("Regular close, no warnings expected.", 0, warningsCount);
        state.setTransaction(null);
        Assert.assertEquals("Duplicate close, warning expected.", 1, warningsCount);
    }
}
