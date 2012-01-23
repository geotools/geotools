package org.geotools.jdbc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.function.EnvFunction;
import org.junit.Test;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockStatement;

public class SessionCommandListenerTest {

    static class RecordingConnection extends MockConnection {

        List<String> commands = new ArrayList<String>();

        public java.sql.Statement createStatement() throws java.sql.SQLException {
            return new MockStatement(this) {
                public boolean execute(String sql) throws java.sql.SQLException {
                    commands.add(sql);
                    return false;
                }
            };
        }
    };

    RecordingConnection conn = new RecordingConnection();

    JDBCDataStore store = new JDBCDataStore();

    @Test
    public void testPlain() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("A", "B");

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("A", conn.commands.get(0));
        conn.commands.clear();

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("B", conn.commands.get(0));
    }
    
    @Test
    public void testOnlyBorrow() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("A", null);

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("A", conn.commands.get(0));
        conn.commands.clear();

        // check release
        listener.onRelease(store, conn);
        assertEquals(0, conn.commands.size());
    }
    
    @Test
    public void testOnlyRelease() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, "B");

        // check borrow
        listener.onBorrow(store, conn);
        assertEquals(0, conn.commands.size());

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("B", conn.commands.get(0));
    }

    @Test
    public void testExpandVariables() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("call startSession('${user}')",
                "call endSession('${user,joe}')");
        
        // check borrow
        EnvFunction.setLocalValue("user", "abcde");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('abcde')", conn.commands.get(0));
        conn.commands.clear();
        
        // check release
        EnvFunction.clearLocalValues();
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call endSession('joe')", conn.commands.get(0));
        conn.commands.clear();
    }
    
    @Test
    public void testInvalid() throws Exception {
        try {
            new SessionCommandsListener("startSession('${user')", null);
            fail("This should have failed, the syntax is not valid");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }

}
