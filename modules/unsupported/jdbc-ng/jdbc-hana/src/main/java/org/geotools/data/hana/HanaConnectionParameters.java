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
package org.geotools.data.hana;

/**
 * SAP HANA connection parameters.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaConnectionParameters {

    /**
     * SAP HANA connection parameters constructor.
     *
     * @param host The database host.
     * @param instance The database instance.
     * @param database The name of the tenant database. Set to null or empty string if the database
     *     is a single-container system. Set to SYSTEMDB to connect to the system database of a
     *     multi-container system.
     */
    public HanaConnectionParameters(String host, int instance, String database) {
        super();
        this.host = host;
        this.instance = instance;
        this.database = database;
    }

    private String host;

    private int instance;

    private String database;

    public String getHost() {
        return host;
    }

    public int getInstance() {
        return instance;
    }

    public String getDatabase() {
        return database;
    }

    /**
     * Builds a JDBC connection URL.
     *
     * @return Returns the JDBC connection URL corresponding to these parameters.
     */
    public String buildUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sap://");
        sb.append(host);
        sb.append("/?instanceNumber=");
        sb.append(Integer.toString(instance));
        if ((database != null) && !database.isEmpty()) {
            sb.append("&databaseName=");
            sb.append(database);
        }
        return sb.toString();
    }
}
