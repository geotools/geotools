/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.ResultSet;
import java.sql.Statement;

/** Callback for {@link JDBCFeatureReader}. */
public interface JDBCReaderCallback {

    /** Null callback. */
    JDBCReaderCallback NULL = new JDBCReaderCallback() {};

    /**
     * Called when the reader is created.
     *
     * @param reader The feature reader.
     */
    default void init(JDBCFeatureReader reader) {}

    /**
     * Called directly before the reader makes it's initial query to the database.
     *
     * @param st The query statement being executed.
     */
    default void beforeQuery(Statement st) {}

    /**
     * Called directly after the reader makes it's initial query to the database.
     *
     * @param st The query statement that was executed.
     */
    default void afterQuery(Statement st) {}

    /** Called when an error occurs making the initial query to the database. */
    default void queryError(Exception e) {}

    /**
     * Called before the reader makes a call to {@link java.sql.ResultSet#next()}.
     *
     * @param rs The result set.
     */
    default void beforeNext(ResultSet rs) {}

    /**
     * Called after the reader makes a call to {@link java.sql.ResultSet#next()}.
     *
     * @param rs The result set.
     * @param hasMore Whether or not any more rows exist in the {@link java.sql.ResultSet}
     */
    default void afterNext(ResultSet rs, boolean hasMore) {}

    /**
     * Called when an error occurs fetching the next row in the result set.
     *
     * @param e The error thrown.
     */
    default void rowError(Exception e) {}

    /**
     * Called after the last row from the reader {@link java.sql.ResultSet} is read.
     *
     * @param reader The feature reader.
     */
    default void finish(JDBCFeatureReader reader) {}
}
