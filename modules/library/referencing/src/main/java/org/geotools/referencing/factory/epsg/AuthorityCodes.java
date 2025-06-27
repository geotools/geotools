/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.geotools.api.referencing.operation.Projection;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.util.logging.Logging;

/**
 * A set of EPSG authority codes. This set requires a living connection to the EPSG database. All {@link #iterator}
 * method call creates a new {@link ResultSet} holding the codes. However, call to {@link #contains} map directly to a
 * SQL call.
 *
 * <p>Serialization of this class store a copy of all authority codes. The serialization do not preserve any connection
 * to the database.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class AuthorityCodes extends AbstractSet<String> implements Serializable {
    /** For compatibility with different versions. */
    private static final long serialVersionUID = 7105664579449680562L;

    /**
     * The factory which is the owner of this set. One purpose of this field (even if it were not used directly by this
     * class) is to avoid garbage collection of the factory as long as this set is in use. This is required because
     * {@link DirectEpsgFactory#finalize} closes the JDBC connections.
     */
    private final DirectEpsgFactory factory;

    /**
     * The type for this code set. This is translated to the most appropriate interface type even if the user supplied
     * an implementation type.
     */
    public final Class<?> type;

    /** {@code true} if {@link #type} is assignable to {@link Projection}. */
    private final boolean isProjection;

    /**
     * A view of this set as a map with object's name as values, or {@code null} if none. Will be created only when
     * first needed.
     */
    private transient java.util.Map<String, String> asMap;

    /** The SQL command to use for creating the {@code queryAll} statement. Used for iteration over all codes. */
    final String sqlAll;

    /**
     * The SQL command to use for creating the {@code querySingle} statement. Used for fetching the description from a
     * code.
     */
    private final String sqlSingle;

    /** The statement to use for querying all codes. Will be created only when first needed. */
    private transient PreparedStatement queryAll;

    /** The statement to use for querying a single code. Will be created only when first needed. */
    private transient PreparedStatement querySingle;

    /**
     * The collection's size, or a negative value if not yet computed. The records will be counted only when first
     * needed. The special value -2 if set by {@link #isEmpty} if the size has not yet been computed, but we know that
     * the set is not empty.
     */
    private int size = -1;

    /**
     * Creates a new set of authority codes for the specified type.
     *
     * @param table The table to query.
     * @param type The type to query.
     * @param factory The factory originator.
     */
    public AuthorityCodes(final TableInfo table, final Class<?> type, final DirectEpsgFactory factory) {
        this.factory = factory;
        final StringBuilder buffer = new StringBuilder("SELECT ");
        buffer.append(table.codeColumn);
        if (table.nameColumn != null) {
            buffer.append(", ").append(table.nameColumn);
        }
        buffer.append(" FROM ").append(table.table);
        boolean hasWhere = false;
        Class<?> tableType = table.type;
        if (table.typeColumn != null) {
            for (int i = 0; i < table.subTypes.length; i++) {
                final Class<?> candidate = table.subTypes[i];
                if (candidate.isAssignableFrom(type)) {
                    buffer.append(" WHERE (")
                            .append(table.typeColumn)
                            .append(" LIKE '")
                            .append(table.typeNames[i])
                            .append("%'");
                    hasWhere = true;
                    tableType = candidate;
                    break;
                }
            }
            if (hasWhere) {
                buffer.append(')');
            }
        }
        this.type = tableType;
        isProjection = Projection.class.isAssignableFrom(tableType);
        final int length = buffer.length();
        buffer.append(" ORDER BY ").append(table.codeColumn);
        sqlAll = factory.adaptSQL(buffer.toString());
        buffer.setLength(length);
        buffer.append(hasWhere ? " AND " : " WHERE ").append(table.codeColumn).append(" = ?");
        sqlSingle = factory.adaptSQL(buffer.toString());
    }

    @SuppressWarnings("PMD.CloseResource")
    PreparedStatement validateStatement(PreparedStatement stmt, String sql) throws SQLException {
        Connection conn = null;
        if (stmt != null) {
            try {
                conn = stmt.getConnection();
            } catch (SQLException sqle) {
                // mark this invalid
                stmt = null;
            }
        }
        if (conn != null && !factory.isConnectionValid(conn)) {
            stmt = null;
        }
        if (stmt == null) {
            stmt = factory.getConnection().prepareStatement(sql);
        }
        return stmt;
    }

    /** Returns all codes. */
    private ResultSet getAll() throws SQLException {
        assert Thread.holdsLock(this);
        queryAll = validateStatement(queryAll, sqlAll);
        try {
            return queryAll.executeQuery();
        } catch (SQLException ignore) {
            /*
             * Failed to reuse an existing statement. This problem occurs in some occasions
             * with the JDBC-ODBC bridge in Java 6 (the error message is "Invalid handle").
             * I'm not sure where the bug come from (didn't noticed it when using HSQL). We
             * will try again with a new statement created in the code after this 'catch'
             * clause. Note that we set 'queryAll' to null first in case of failure during
             * the 'prepareStatement(...)' execution.
             */
            queryAll.close();
            queryAll = null;
            recoverableException("getAll", ignore);
        }
        queryAll = validateStatement(queryAll, sqlAll);
        return queryAll.executeQuery();
    }

    /** Returns a single code. */
    private ResultSet getSingle(final Object code) throws SQLException {
        assert Thread.holdsLock(this);
        querySingle = validateStatement(querySingle, sqlSingle);
        querySingle.setString(1, code.toString());
        return querySingle.executeQuery();
    }

    /**
     * Returns {@code true} if the code in the specified result set is acceptable. This method handle projections in a
     * special way.
     */
    private boolean isAcceptable(final ResultSet results) throws SQLException {
        if (!isProjection) {
            return true;
        }
        final String code = results.getString(1);
        synchronized (factory) {
            return factory.isProjection(code);
        }
    }

    /**
     * Returns {@code true} if the code in the specified code is acceptable. This method handle projections in a special
     * way.
     */
    private boolean isAcceptable(final String code) throws SQLException {
        if (!isProjection) {
            return true;
        }
        synchronized (factory) {
            return factory.isProjection(code);
        }
    }

    /**
     * Returns {@code true} if this collection contains no elements. This method fetch at most one row instead of
     * counting all rows.
     */
    @Override
    public synchronized boolean isEmpty() {
        if (size != -1) {
            return size == 0;
        }
        boolean empty = true;
        try (ResultSet results = getAll()) {
            while (results.next()) {
                if (isAcceptable(results)) {
                    empty = false;
                    break;
                }
            }
        } catch (SQLException exception) {
            unexpectedException("isEmpty", exception);
        }
        size = empty ? 0 : -2;
        return empty;
    }

    /** Count the number of elements in the underlying result set. */
    @Override
    public synchronized int size() {
        if (size >= 0) {
            return size;
        }
        int count = 0;
        try {
            try (ResultSet results = getAll()) {
                while (results.next()) {
                    if (isAcceptable(results)) {
                        count++;
                    }
                }
            }
        } catch (SQLException exception) {
            unexpectedException("size", exception);
        }
        size = count; // Stores only on success.
        return count;
    }

    /** Returns {@code true} if this collection contains the specified element. */
    @Override
    public synchronized boolean contains(final Object code) {
        boolean exists = false;
        if (code != null)
            try {
                try (ResultSet results = getSingle(code)) {
                    while (results.next()) {
                        if (isAcceptable(results)) {
                            exists = true;
                            break;
                        }
                    }
                }
            } catch (SQLException exception) {
                unexpectedException("contains", exception);
            }
        return exists;
    }

    /**
     * Returns an iterator over the codes. The iterator is backed by a living {@link ResultSet}, which will be closed as
     * soon as the iterator reach the last element.
     */
    @Override
    public synchronized java.util.Iterator<String> iterator() {
        try {
            final Iterator iterator = new Iterator(getAll());
            /*
             * Set the statement to null without closing it, in order to force a new statement
             * creation if getAll() is invoked before the iterator finish its iteration.  This
             * is needed because only one ResultSet is allowed for each Statement.
             */
            queryAll = null;
            return iterator;
        } catch (SQLException exception) {
            unexpectedException("iterator", exception);
            final Set<String> empty = Collections.emptySet();
            return empty.iterator();
        }
    }

    /**
     * Returns a serializable copy of this set. This method is invoked automatically during serialization. The
     * serialised set of authority code is disconnected from the underlying database.
     */
    Object writeReplace() throws ObjectStreamException {
        return new LinkedHashSet<>(this);
    }

    /**
     * Closes the underlying statements. Note: this method is also invoked directly by
     * {@link DirectEpsgFactory#dispose}, which is okay in this particular case since the implementation of this method
     * can be executed an arbitrary amount of times.
     */
    @Override
    @SuppressWarnings({"deprecation", "Finalize"}) // finalize is deprecated in Java 9
    protected synchronized void finalize() throws SQLException {
        if (querySingle != null) {
            querySingle.close();
            querySingle = null;
        }
        if (queryAll != null) {
            queryAll.close();
            queryAll = null;
        }
    }

    /** Invoked when an exception occured. This method just log a warning. */
    private static void unexpectedException(final String method, final SQLException exception) {
        unexpectedException(AuthorityCodes.class, method, exception);
    }

    /** Invoked when an exception occured. This method just log a warning. */
    static void unexpectedException(final Class<?> classe, final String method, final SQLException exception) {
        Logging.unexpectedException(classe, method, exception);
    }

    /** Invoked when a recoverable exception occured. */
    private static void recoverableException(final String method, final SQLException exception) {
        // Uses the FINE level instead of WARNING because it may be a recoverable error.
        LogRecord record = Loggings.format(Level.FINE, LoggingKeys.UNEXPECTED_EXCEPTION);
        record.setSourceClassName(AuthorityCodes.class.getName());
        record.setSourceMethodName(method);
        record.setThrown(exception);
        final Logger logger = Logging.getLogger(AuthorityCodes.class);
        record.setLoggerName(logger.getName());
        logger.log(record);
    }

    /**
     * The iterator over the codes. This inner class must kept a reference toward the enclosing {@link AuthorityCodes}
     * in order to prevent a call to {@link AuthorityCodes#finalize} before the iteration is finished.
     */
    private final class Iterator implements java.util.Iterator<String> {
        /** The result set, or {@code null} if there is no more elements. */
        private ResultSet results;

        /** The next code. */
        private transient String next;

        /** Creates a new iterator for the specified result set. */
        Iterator(final ResultSet results) throws SQLException {
            assert Thread.holdsLock(AuthorityCodes.this);
            this.results = results;
            toNext();
        }

        /** Moves to the next element. */
        private void toNext() throws SQLException {
            while (results.next()) {
                next = results.getString(1);
                if (isAcceptable(next)) {
                    return;
                }
            }
            finalize();
        }

        /** Returns {@code true} if there is more elements. */
        @Override
        public boolean hasNext() {
            return results != null;
        }

        /** Returns the next element. */
        @Override
        public String next() {
            if (results == null) {
                throw new NoSuchElementException();
            }
            final String current = next;
            try {
                toNext();
            } catch (SQLException exception) {
                results = null;
                unexpectedException(Iterator.class, "next", exception);
            }
            return current;
        }

        /** Always throws an exception, since this iterator is read-only. */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Closes the underlying result set. */
        @Override
        @SuppressWarnings({"deprecation", "Finalize", "PMD.CloseResource"}) // finalize is deprecated in Java 9
        protected void finalize() throws SQLException {
            next = null;
            if (results != null) {
                final PreparedStatement owner = (PreparedStatement) results.getStatement();
                results.close();
                results = null;
                synchronized (AuthorityCodes.this) {
                    /*
                     * We don't need the statement anymore. Gives it back to the enclosing class
                     * in order to avoid creating a new one when AuthorityCodes.getAll() will be
                     * invoked again,  or closes the statement if getAll() already created a new
                     * statement anyway.
                     */
                    assert owner != queryAll;
                    if (queryAll == null) {
                        queryAll = owner;
                    } else {
                        owner.close();
                    }
                }
            }
        }
    }

    /** Returns a view of this set as a map with object's name as value, or {@code null} if none. */
    final java.util.Map<String, String> asMap() {
        if (asMap == null) {
            asMap = new Map();
        }
        return asMap;
    }

    /** A view of {@link AuthorityCodes} as a map, with authority codes as key and object names as values. */
    private final class Map extends AbstractMap<String, String> {
        /** Returns the number of key-value mappings in this map. */
        @Override
        public int size() {
            return AuthorityCodes.this.size();
        }

        /** Returns {@code true} if this map contains no key-value mappings. */
        @Override
        public boolean isEmpty() {
            return AuthorityCodes.this.isEmpty();
        }

        /** Returns the description to which this map maps the specified EPSG code. */
        @Override
        public String get(final Object code) {
            String value = null;
            if (code != null)
                try {
                    synchronized (AuthorityCodes.this) {
                        try (ResultSet results = getSingle(code)) {
                            while (results.next()) {
                                if (isAcceptable(results)) {
                                    value = results.getString(2);
                                    break;
                                }
                            }
                        }
                    }
                } catch (SQLException exception) {
                    unexpectedException("get", exception);
                }
            return value;
        }

        /** Returns {@code true} if this map contains a mapping for the specified EPSG code. */
        @Override
        public boolean containsKey(final Object key) {
            return contains(key);
        }

        /** Returns a set view of the keys contained in this map. */
        @Override
        public Set<String> keySet() {
            return AuthorityCodes.this;
        }

        /**
         * Returns a set view of the mappings contained in this map.
         *
         * @todo Not yet implemented.
         */
        @Override
        public Set<java.util.Map.Entry<String, String>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }
}
