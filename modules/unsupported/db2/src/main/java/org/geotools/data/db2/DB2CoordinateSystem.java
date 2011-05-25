/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.factory.FactoryRegistryException;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Represent the coordinate system information maintained in the DB2 Spatial
 * Extender catalog tables.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2CoordinateSystem {
    private int srsId;
    private String coordsysName;
    private CoordinateReferenceSystem crs = null;
    private String organization;
    private int coordsysId;
    private String definition;

    /**
     * Constructs a DB2CoordinateSystem.
     *
     * @param conn an open database connection.
     * @param srsId the DB2 spatial reference system identifier.
     *
     * @throws SQLException if there was a database error attempting to load
     *         the coordinate system information.
     */
    public DB2CoordinateSystem(Connection conn, int srsId)
        throws SQLException {
        super();
        this.srsId = srsId;
        this.loadFromDB(conn);
    }

    /**
     * Selects the coordinate system information from the DB2 spatial catalog
     * table db2gse.ST_Spatial_Reference_Systems.
     * 
     * <p></p>
     *
     * @param conn an open database connection
     *
     * @throws SQLException if there was a database error attempting to load
     *         the coordinate system information.
     */
    private void loadFromDB(Connection conn) throws SQLException {
        String querySRS =
            "SELECT coordsys_name, organization, organization_coordsys_id, "
            + " definition" + " FROM db2gse.st_spatial_reference_systems"
            + " WHERE srs_id = " + this.srsId;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(querySRS);

        if (rs.next()) {
            this.coordsysName = rs.getString(1);
            this.organization = rs.getString(2);
            this.coordsysId = rs.getInt(3);
            this.definition = rs.getString(4);
        } else {
            throw new SQLException("Unrecognized srid '" + this.srsId + "'");
        }

        rs.close();
        stmt.close();
    }

    /**
     * Returns the coordinate system name, organization and coordinate system
     * identifier.
     *
     * @return the coordinate system name, organization and coordinate system
     *         identifier  as a String.
     */
    public String toString() {
        return this.coordsysName + "=" + this.organization + ":"
        + this.coordsysId;
    }

    /**
     * Gets the coordinate system identifier, usually an EPSG or similar value.
     *
     * @return coordinate system identifier.
     */
    int getCsId() {
        return this.coordsysId;
    }

    /**
     * Gets the OpenGIS CoordinateReferenceSystem for this DB2CoordinateSystem.
     *
     * @return a CoordinateReferenceSystem
     *
     * @throws FactoryRegistryException
     * @throws FactoryException
     */
    CoordinateReferenceSystem getCRS()
        throws FactoryRegistryException, FactoryException {
        if (this.crs == null) {
            this.crs = ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(this.definition);
        }

        return this.crs;
    }
}
