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
package org.geotools.data.hana.metadata;

import org.geotools.data.hana.HanaConnectionParameters;

/**
 * Command line arguments for the metadata importer.
 *
 * @author Stefan Uhrig, SAP SE
 */
class CommandLineArguments {

    public static CommandLineArguments parse(String[] args) {
        if ((args.length < 3) || (args.length > 4)) {
            showUsage();
            return null;
        }
        String user = args[0];
        String host = args[1];
        int instance;
        try {
            instance = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            showUsage();
            return null;
        }
        String database = null;
        if (args.length == 4) {
            database = args[3];
        }
        HanaConnectionParameters connectionParameters =
                new HanaConnectionParameters(host, instance, database);
        return new CommandLineArguments(user, connectionParameters);
    }

    private CommandLineArguments(String user, HanaConnectionParameters connectionParameters) {
        super();
        this.user = user;
        this.connectionParameters = connectionParameters;
    }

    private String user;

    private HanaConnectionParameters connectionParameters;

    public String getUser() {
        return user;
    }

    public HanaConnectionParameters getConnectionParameters() {
        return connectionParameters;
    }

    private static void showUsage() {
        System.out.println("HANA Metadata Importer");
        System.out.println();
        System.out.println(
                "This tool import spatial unit of measures and spatial reference systems into a");
        System.out.println("HANA database.");
        System.out.println();
        System.out.println("Usage: import_metadata username host instance [database]");
        System.out.println();
        System.out.println("  username - The database user that is used to connect");
        System.out.println("  host     - The database host to connect to");
        System.out.println("  instance - The number of the instance to connect to");
        System.out.println(
                "  database - The database to connect to. Omit this parameter in case of a");
        System.out.println(
                "             single container database. Otherwise, set this parameter either to");
        System.out.println(
                "             SYSTEMDB to connect to the system database or to the name of the");
        System.out.println("             tenant database");
        System.out.println();
    }
}
