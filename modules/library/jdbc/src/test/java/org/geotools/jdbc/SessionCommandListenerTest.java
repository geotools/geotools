package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockStatement;
import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.function.EnvFunction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class SessionCommandListenerTest {

    static class RecordingConnection extends MockConnection {

        List<String> commands = new ArrayList<>();

        @Override
        public java.sql.Statement createStatement() throws java.sql.SQLException {
            return new MockStatement(this) {
                @Override
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
    public void testTwoExpandVariablesAreReplaced() throws Exception {
        SessionCommandsListener listener =
                new SessionCommandsListener(
                        "call startSession('select ${user}, ${version} from 1')", null);

        // check borrow
        EnvFunction.setLocalValue("user", "user1");
        EnvFunction.setLocalValue("version", "x.x");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('select user1, x.x from 1')", conn.commands.get(0));
    }

    @Test
    public void testValidSqlWhenNoVariable() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener("select true from 1", null);
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("select true from 1", conn.commands.get(0));
    }

    @Test
    public void testValidSqlUsingCurlyBracketsOutOfAVariableDefinition() throws Exception {
        SessionCommandsListener listener =
                new SessionCommandsListener(
                        "select set_config('my.userString', substring('${GSUSER}', 'user#\"[0-9]{5}#\"%', '#'), false) ",
                        null);

        EnvFunction.setLocalValue("GSUSER", "user11111");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals(
                "select set_config('my.userString', substring('user11111', 'user#\"[0-9]{5}#\"%', '#'), false) ",
                conn.commands.get(0));
    }

    @Test
    public void testOneExpandVariablesIsReplaced() throws Exception {
        SessionCommandsListener listener =
                new SessionCommandsListener("call startSession('${user}')", null);

        // check borrow
        EnvFunction.setLocalValue("user", "abcde");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('abcde')", conn.commands.get(0));
    }

    // this tests checks the same of previos but for the release connection sql script
    @Test
    public void testReleaseConnectionsScript() throws Exception {
        SessionCommandsListener listener =
                new SessionCommandsListener(null, "call endSession('${user,joe}')");

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call endSession('joe')", conn.commands.get(0));
    }

    @Test
    public void testInvalidVariableDefinitionMissingClosingBracket() throws Exception {
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("select ${user from 1)", null));
    }

    @After
    public void clearResources() {
        EnvFunction.clearLocalValues();
        conn.commands.clear();
    }
}
