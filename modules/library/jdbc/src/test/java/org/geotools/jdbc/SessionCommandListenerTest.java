package org.geotools.jdbc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockStatement;
import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.function.EnvFunction;
import org.junit.After;
import org.junit.Before;
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
    }

    RecordingConnection conn = new RecordingConnection();

    JDBCDataStore store = new JDBCDataStore();

    @Before
    public void setUp() {
        System.setProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY, "true");
        System.clearProperty(SessionCommandsListener.ALLOWED_DBTYPES_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_STARTUP_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_CLOSEUP_KEY);
        System.setProperty(SessionCommandsListener.ALLOWED_VALUES_KEY, "^.*$");
    }

    @After
    public void clearResources() {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_DBTYPES_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_STARTUP_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_CLOSEUP_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_VALUES_KEY);
        EnvFunction.clearLocalValues();
        conn.commands.clear();
    }

    @Test
    public void testBlankStrings() {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        new SessionCommandsListener(null, null, null);
        new SessionCommandsListener("", "", "");
        new SessionCommandsListener(" ", " ", " ");
    }

    @Test
    public void testDbtypeValidation() {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        new SessionCommandsListener("postgis", null, "RESET SESSION AUTHORIZATION");

        IllegalArgumentException e1 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("geopkg", null, "RESET SESSION AUTHORIZATION"));
        assertEquals("Session startup/close-up SQL not allowed for database type", e1.getMessage());

        System.setProperty(SessionCommandsListener.ALLOWED_DBTYPES_KEY, "db2");
        new SessionCommandsListener("db2", null, "RESET SESSION AUTHORIZATION");

        System.setProperty(SessionCommandsListener.ALLOWED_DBTYPES_KEY, "oracle");
        IllegalArgumentException e2 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("mysql", null, "RESET SESSION AUTHORIZATION"));
        assertEquals("Session startup/close-up SQL not allowed for database type", e2.getMessage());
    }

    @Test
    public void testStartupSqlValidation() {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${GSUSER}", null);
        new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${GSUSER,geoserver}", null);

        IllegalArgumentException e1 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${FOO}", null));
        assertEquals("Session startup SQL does not match allowed pattern", e1.getMessage());
        IllegalArgumentException e2 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${FOO,geoserver}", null));
        assertEquals("Session startup SQL does not match allowed pattern", e2.getMessage());

        System.setProperty(SessionCommandsListener.ALLOWED_STARTUP_KEY, "^.*$");
        new SessionCommandsListener("postgis", "SELECT 1", null);

        System.setProperty(SessionCommandsListener.ALLOWED_STARTUP_KEY, "^$");
        IllegalArgumentException e3 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${GSUSER}", null));
        assertEquals("Session startup SQL does not match allowed pattern", e3.getMessage());
        IllegalArgumentException e4 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("postgis", "SET SESSION AUTHORIZATION ${GSUSER,geoserver}", null));
        assertEquals("Session startup SQL does not match allowed pattern", e4.getMessage());
    }

    @Test
    public void testCloseupSqlValidation() {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        new SessionCommandsListener("postgis", null, "RESET SESSION AUTHORIZATION");

        IllegalArgumentException e1 = assertThrows(
                IllegalArgumentException.class, () -> new SessionCommandsListener("postgis", null, "SELECT 1"));
        assertEquals("Session close-up SQL does not match allowed pattern", e1.getMessage());

        System.setProperty(SessionCommandsListener.ALLOWED_CLOSEUP_KEY, "^.*$");
        new SessionCommandsListener("postgis", null, "SELECT 1");

        System.setProperty(SessionCommandsListener.ALLOWED_CLOSEUP_KEY, "^$");
        IllegalArgumentException e2 = assertThrows(
                IllegalArgumentException.class,
                () -> new SessionCommandsListener("postgis", null, "RESET SESSION AUTHORIZATION"));
        assertEquals("Session close-up SQL does not match allowed pattern", e2.getMessage());
    }

    @Test
    public void testEnvironmentVariables() throws Exception {
        System.clearProperty(SessionCommandsListener.UNRESTRICTED_SQL_KEY);
        System.clearProperty(SessionCommandsListener.ALLOWED_VALUES_KEY);
        System.setProperty(SessionCommandsListener.ALLOWED_STARTUP_KEY, "^.*$");
        SessionCommandsListener listener = new SessionCommandsListener("postgis", "${foo}", null);
        listener.onBorrow(store, conn);
        assertEquals(List.of(), conn.commands);
        conn.commands.clear();

        EnvFunction.setLocalValue("foo", "bar");
        listener.onBorrow(store, conn);
        assertEquals(List.of("bar"), conn.commands);
        conn.commands.clear();

        EnvFunction.setLocalValue("foo", "~!@#$");
        IllegalArgumentException e1 =
                assertThrows(IllegalArgumentException.class, () -> listener.onBorrow(store, conn));
        assertEquals("Environment variable value '~!@#$' does not match allowed pattern", e1.getMessage());
        assertEquals(List.of(), conn.commands);
        conn.commands.clear();

        System.setProperty(SessionCommandsListener.ALLOWED_VALUES_KEY, "^.*$");
        EnvFunction.setLocalValue("foo", "~!@#$");
        listener.onBorrow(store, conn);
        assertEquals(List.of("~!@#$"), conn.commands);
        conn.commands.clear();

        System.setProperty(SessionCommandsListener.ALLOWED_VALUES_KEY, "^$");
        EnvFunction.setLocalValue("foo", "bar");
        IllegalArgumentException e2 =
                assertThrows(IllegalArgumentException.class, () -> listener.onBorrow(store, conn));
        assertEquals("Environment variable value 'bar' does not match allowed pattern", e2.getMessage());
        assertEquals(List.of(), conn.commands);
        conn.commands.clear();
    }

    @Test
    public void testPlain() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, "A", "B");

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
        SessionCommandsListener listener = new SessionCommandsListener(null, "A", null);

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
        SessionCommandsListener listener = new SessionCommandsListener(null, null, "B");

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
                new SessionCommandsListener(null, "call startSession('select ${user}, ${version} from 1')", null);

        // check borrow
        EnvFunction.setLocalValue("user", "user1");
        EnvFunction.setLocalValue("version", "x.x");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('select user1, x.x from 1')", conn.commands.get(0));
    }

    @Test
    public void testValidSqlWhenNoVariable() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, "select true from 1", null);
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("select true from 1", conn.commands.get(0));
    }

    @Test
    public void testValidSqlUsingCurlyBracketsOutOfAVariableDefinition() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(
                null,
                "select set_config('my.userString', substring('${GSUSER}', 'user#\"[0-9]{5}#\"%', '#'), false) ",
                null);

        EnvFunction.setLocalValue("GSUSER", "user11111");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals(
                "select set_config('my.userString', substring('user11111', 'user#\"[0-9]{5}#\"%', '#'), false)",
                conn.commands.get(0));
    }

    @Test
    public void testOneExpandVariablesIsReplaced() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, "call startSession('${user}')", null);

        // check borrow
        EnvFunction.setLocalValue("user", "abcde");
        listener.onBorrow(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call startSession('abcde')", conn.commands.get(0));
    }

    // this tests checks the same of previos but for the release connection sql script
    @Test
    public void testReleaseConnectionsScript() throws Exception {
        SessionCommandsListener listener = new SessionCommandsListener(null, null, "call endSession('${user,joe}')");

        // check release
        listener.onRelease(store, conn);
        assertEquals(1, conn.commands.size());
        assertEquals("call endSession('joe')", conn.commands.get(0));
    }

    @Test
    public void testInvalidVariableDefinitionMissingClosingBracket() throws Exception {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new SessionCommandsListener(null, "select ${user from 1)", null));
        assertThat(exception.getMessage(), startsWith("Unclosed environment variable reference"));
    }
}
