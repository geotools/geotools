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

import java.util.HashMap;
import org.geotools.data.hana.HanaConnectionParameters;

/**
 * Command line arguments for the metadata importer.
 *
 * @author Stefan Uhrig, SAP SE
 */
class CommandLineArguments {

    private static enum ConnectionType {
        USING_PORT,
        SINGLE_CONTAINER,
        MULTI_CONTAINER
    }

    public static CommandLineArguments parse(String[] args) {

        ConnectionType connType;
        int port = 0;
        int instance = 0;
        String database = null;
        HashMap<String, String> options = new HashMap<>();

        int aidx = 0;
        if (args.length < 2) {
            showUsage();
            return null;
        }
        String user = args[aidx++];
        String host = args[aidx++];
        int colonIdx = host.indexOf(':');
        if (colonIdx != -1) {
            connType = ConnectionType.USING_PORT;
            String sport = host.substring(colonIdx + 1);
            try {
                port = Integer.parseInt(sport);
            } catch (NumberFormatException e) {
                showUsage();
                return null;
            }
            host = host.substring(0, colonIdx);
        } else {
            if (aidx == args.length) {
                showUsage();
                return null;
            }
            try {
                instance = Integer.parseInt(args[aidx++]);
            } catch (NumberFormatException e) {
                showUsage();
                return null;
            }
            if (aidx < args.length && !args[aidx].startsWith("--")) {
                database = args[aidx++];
                connType = ConnectionType.MULTI_CONTAINER;
            } else {
                connType = ConnectionType.SINGLE_CONTAINER;
            }
        }
        if (aidx < args.length) {
            String option = args[aidx++];
            if (!option.equals("--ssl")) {
                showUsage();
                return null;
            }
            options.put("encrypt", "true");
        }
        if (aidx != args.length) {
            showUsage();
            return null;
        }

        switch (connType) {
            case USING_PORT:
                return new CommandLineArguments(user, HanaConnectionParameters.forPort(host, port, options));
            case SINGLE_CONTAINER:
                return new CommandLineArguments(
                        user, HanaConnectionParameters.forSingleContainer(host, instance, options));
            case MULTI_CONTAINER:
                return new CommandLineArguments(
                        user, HanaConnectionParameters.forMultiContainer(host, instance, database, options));
            default:
                throw new AssertionError();
        }
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

    @SuppressWarnings("PMD.SystemPrintln")
    private static void showUsage() {
        System.out.println("HANA Metadata Importer");
        System.out.println();
        System.out.println("This tool imports spatial unit of measures and spatial reference systems into a");
        System.out.println("HANA database.");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  import_metadata <username> <host>:<port> [--ssl]");
        System.out.println("  import_metadata <username> <host> <instance> [<database>] [--ssl]");
        System.out.println();
        System.out.println("  username - The database user that is used to connect");
        System.out.println("  port     - The port to connect to");
        System.out.println("  host     - The database host to connect to");
        System.out.println("  instance - The number of the instance to connect to");
        System.out.println("  database - The database to connect to. Omit this parameter in case of a");
        System.out.println("             single container database. Otherwise, set this parameter either to");
        System.out.println("             SYSTEMDB to connect to the system database or to the name of the");
        System.out.println("             tenant database");
        System.out.println("  --ssl    - Use SSL to connect to the database");
        System.out.println();
    }
}
