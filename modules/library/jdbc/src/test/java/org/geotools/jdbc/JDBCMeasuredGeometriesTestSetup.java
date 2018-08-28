/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** Creates the necessary data to exercise measurements coordinates (M) support. */
public abstract class JDBCMeasuredGeometriesTestSetup extends JDBCDelegatingTestSetup {

    protected static final Logger LOGGER = Logging.getLogger(JDBCMeasuredGeometriesTestSetup.class);

    protected JDBCMeasuredGeometriesTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected final void setUpData() throws Exception {
        // quietly drop the tables if they already exist
        dropTables();
        // create the necessary tables as well the data
        createTables();
    }

    @FunctionalInterface
    private interface Statement {
        void execute() throws Exception;
    }

    /** Quietly executes the provided statements, i.e. all exceptions will be ignored. */
    private void quietly(Statement... statements) {
        for (Statement statement : statements) {
            // quietly execute statement
            quietly(statement);
        }
    }

    /** Quietly executes a statement, i.e. all exceptions will be ignored. */
    private void quietly(Statement statement) {
        try {
            statement.execute();
        } catch (Exception exception) {
            // log the exception and move on
            LOGGER.log(Level.FINE, "Ignored error.", exception);
        }
    }

    /** Create the necessary tables, data will also be inserted. */
    private void createTables() {
        try {
            // create the necessary tables
            createTablePointsM();
            createTablePointsZM();
            createTableLinesM();
            createTableLinesZM();
        } catch (Exception exception) {
            // ouch, something bad happen let's clean any created table
            dropTables();
            LOGGER.log(Level.INFO, "Error creating tables.", exception);
        }
    }

    /** Quietly drop the created tables, all exceptions will be ignored. */
    private void dropTables() {
        quietly(
                this::dropTablePointsM,
                this::dropTablePointsZM,
                this::dropTableLinesM,
                this::dropTableLinesZM);
    }

    /**
     * Creates the points_m table that contains points geometries with a measurement coordinate.
     *
     * <pre>{@code
     * INSERT INTO points_m (id, description, geometry) VALUES
     * (1, 'point_m_a', ST_SetSRID(ST_GeomFromEWKT('POINTM(-2 1 0)'), 4326)),
     * (2, 'point_m_b', ST_SetSRID(ST_GeomFromEWKT('POINTM( 3 1 1)'), 4326)),
     * (3, 'point_m_c', ST_SetSRID(ST_GeomFromEWKT('POINTM( 3 5 2)'), 4326)),
     * (4, 'point_m_d', ST_SetSRID(ST_GeomFromEWKT('POINTM(-2 5 3)'), 4326));
     * }</pre>
     */
    protected abstract void createTablePointsM() throws Exception;

    /** Drops points_m table. */
    protected abstract void dropTablePointsM() throws Exception;

    /**
     * Creates the points_m table that contains points geometries with a measurement and elevation
     * coordinate.
     *
     * <pre>{@code
     * INSERT INTO points_zm (id, description, geometry) VALUES
     * (1, 'point_zm_a', ST_SetSRID(ST_GeomFromEWKT('POINTZM(-2 1 10 0)'), 4326)),
     * (2, 'point_zm_b', ST_SetSRID(ST_GeomFromEWKT('POINTZM( 3 1 15 1)'), 4326)),
     * (3, 'point_zm_c', ST_SetSRID(ST_GeomFromEWKT('POINTZM( 3 5 20 2)'), 4326)),
     * (4, 'point_zm_d', ST_SetSRID(ST_GeomFromEWKT('POINTZM(-2 5 25 3)'), 4326));
     * }</pre>
     */
    protected abstract void createTablePointsZM() throws Exception;

    /** Drops points_zm table. */
    protected abstract void dropTablePointsZM() throws Exception;

    /**
     * Creates the lines_m table that contains lines geometries with a measurement coordinate.
     *
     * <pre>{@code
     * INSERT INTO lines_m (id, description, geometry) VALUES
     * (1, 'line_m_a', ST_SetSRID(ST_GeomFromEWKT('LINESTRINGM(-2 1 0, 3 1 1, 3 5 2, -2 5 3)'), 4326));
     * }</pre>
     */
    protected abstract void createTableLinesM() throws Exception;

    /** Drops lines_m table. */
    protected abstract void dropTableLinesM() throws Exception;

    /**
     * Creates the lines_zm table that contains lines geometries with a measurement and elevation
     * coordinate.
     *
     * <pre>{@code
     * INSERT INTO lines_zm (id, description, geometry) VALUES
     * (1, 'line_zm_a', ST_SetSRID(ST_GeomFromEWKT('LINESTRINGZM(-2 1 10 0, 3 1 15 1, 3 5 20 2, -2 5 25 3)'), 4326));
     * }</pre>
     */
    protected abstract void createTableLinesZM() throws Exception;

    /** Drops lines_zm table. */
    protected abstract void dropTableLinesZM() throws Exception;
}
