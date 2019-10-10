/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.tools;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.logging.Logging;

/**
 * Command line utility to migrate an existing mosaic of NetCDF files from sidecar H2 slice indexes
 * to a centralized index
 */
public class H2Migrate {

    static final Logger LOGGER = Logging.getLogger(H2Migrate.class);

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(
                Option.builder("m").desc("Path to the mosaic folder").required().hasArg().build());
        options.addOption(
                Option.builder("ms")
                        .desc(
                                "Path to the store configuration for the mosaic index (to be used when the mosaic reader cannot connect to the target store by itself, e.g., the store is managed by GeoServer and referred by name)")
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("mit")
                        .desc(
                                "Mosaic index tables containing the location property, used to find the NetCDF files to migrate. If missing, assumed to be the same as the coverage names. To be used in combination with -ms")
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("cl")
                        .desc(
                                "List of coverage names (to be used when the mosaic worker cannot work stand alone, if not provided the tool will try to guess the names from config files)")
                        .type(List.class)
                        .valueSeparator(',')
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("ndd").desc("Path to the NetCDF data dir").hasArg().build());
        options.addOption(
                Option.builder("is")
                        .desc(
                                "Path to the index store configuration property file. Will be turned into netcdf_datastore.properties at the migration end, unless -isn is used")
                        .required()
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("isn")
                        .desc(
                                "Index store name. If specified, the migration will create a netcdf_datastore.properties using a StoreName instead of the connection parameters provided in -is")
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("if")
                        .desc("Ignore failures (no value needed, just add the flag)")
                        .build());
        options.addOption(
                Option.builder("c")
                        .desc(
                                "Concurrency level (positive number). If not provided, will use one core")
                        .hasArg()
                        .build());
        options.addOption(
                Option.builder("ld")
                        .desc(
                                "Directory for the migrated file logs, migrated.txt, h2.txt (the current folder will be used if not specified)")
                        .hasArg()
                        .build());

        options.addOption(Option.builder("v").desc("Verbose output").build());

        CommandLine cmd;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);

            H2MigrateConfiguration configuration = new H2MigrateConfiguration();
            configuration.setMosaicPath(cmd.getOptionValue("m"));
            configuration.setTargetStoreConfiguration(cmd.getOptionValue("is"));
            if (cmd.hasOption("ms")) {
                configuration.setSourceStoreConfiguration(cmd.getOptionValue("ms"));
                if (!cmd.hasOption("cl")) {
                    throw new H2MigrateConfiguration.ConfigurationException(
                            "When using -ms also provide a list of coverages to migrate with -cl");
                }
                final String[] coverageNames = cmd.getOptionValue("cl").split(",");
                configuration.setCoverageNames(coverageNames);
                if (cmd.hasOption("mit")) {
                    final String[] mosaicIndexTables = cmd.getOptionValue("mit").split(",");
                    configuration.setIndexTables(mosaicIndexTables);
                }
            }
            if (cmd.hasOption("ndd")) {
                final String netcdfDataDir = cmd.getOptionValue("ndd");
                final File file = new File(netcdfDataDir);
                if (!file.exists() && !file.isDirectory()) {
                    throw new H2MigrateConfiguration.ConfigurationException(
                            "NetCDF data directory does not exist or is not a directory "
                                    + netcdfDataDir);
                }
                System.setProperty(NetCDFUtilities.NETCDF_DATA_DIR, netcdfDataDir);
            }
            if (cmd.hasOption("c")) {
                configuration.setConcurrency(cmd.getOptionValue("c"));
            }
            if (cmd.hasOption("if")) {
                configuration.setFailureIgnored(true);
            }
            if (cmd.hasOption("ld")) {
                configuration.setLogDirectory(cmd.getOptionValue("ld"));
            }
            if (cmd.hasOption("isn")) {
                configuration.setIndexStoreName(cmd.getOptionValue("isn"));
            }

            if (cmd.hasOption("v")) {
                LOGGER.setLevel(Level.INFO);
                H2Migrator.LOGGER.setLevel(Level.INFO);
            } else {
                LOGGER.setLevel(Level.INFO);
                H2Migrator.LOGGER.setLevel(Level.WARNING);
            }

            H2Migrator migrator = new H2Migrator(configuration);
            migrator.migrate();
        } catch (ParseException e) {
            optionErrorAndExit(options, e.getMessage());
        } catch (H2MigrateConfiguration.ConfigurationException e) {
            LOGGER.log(Level.INFO, "Failed to validate inputs", e);
            optionErrorAndExit(options, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Migration failed", e);
            System.out.println("Migration failed: " + e.getMessage());
            System.exit(2);
        }
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void optionErrorAndExit(Options options, String message) {
        System.out.println(message);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(140);
        formatter.printHelp(
                "H2Migrate",
                "\nMigrates NetCDF sidecar H2 indexes to  the target datastore. Typical usages:\n"
                        + "* Stand alone mosaic: java -cp <allJarsNeeded> "
                        + H2Migrate.class.getName()
                        + " -m <mosaicDir> -is <targetStorePropertyFile>\n"
                        + "* GeoServer OpenSearch mosaic: java -cp <allJarsNeeded> "
                        + H2Migrate.class.getName()
                        + " -m <mosaicDir> \n  -ms <openSearchPostgisPropertyFile> -is <targetStorePropertyFile> -isn <theFinalIndexStoreName>\n\nThe full list of options for the tool are:",
                options,
                null,
                true);
        System.exit(1);
    }
}
