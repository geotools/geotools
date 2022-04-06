/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.File;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Configuration for a {@link PGRasterReader}.
 *
 * <p>Configuration is stored as XML with the following basic format:
 *
 * <pre>
 *     &lt;pgraster>
 *       &lt;name//>        // coverage name
 *       &lt;database>     // database connection
 *         &lt;host//>      // database host
 *         &lt;port//>      // database port
 *         &lt;name//>      // database name
 *         &lt;user//>      // database username
 *         &lt;pass//>      // database user password
 *       &lt;/database>
 *       &lt;raster>       // raster column config
 *         &lt;column//>      // column name
 *         &lt;table//>     // table name
 *         &lt;schema//>    // table schema
 *       &lt;/raster>
 *       &lt;time>        // time column config
 *         &lt;enabled//>  // enable / disable time
 *         &lt;column//>     // column name
 *       &lt;/time>
 *     &lt;/pgraster>
 *   </pre>
 */
class PGRasterConfig implements Closeable {

    static final Logger LOG = Logging.getLogger(PGRasterConfig.class);

    String name;
    DataSource dataSource;
    String schema;
    String table;
    String column;
    String enableDrivers;

    TimeConfig time = new TimeConfig();

    static Document parse(File cfgfile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Error creating XML parser");
        }

        try {
            return db.parse(cfgfile);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing pgraster config", e);
        }
    }

    PGRasterConfig(File configFile) {
        this(parse(configFile));
    }

    PGRasterConfig(Document config) {
        Element root = config.getDocumentElement();
        if (!"pgraster".equalsIgnoreCase(root.getNodeName())) {
            throw new IllegalArgumentException(
                    "Not a postgis raster configuration, root element must be 'pgraster'");
        }

        this.name = first(root, "name").map(this::nodeValue).orElse(null);
        this.enableDrivers = first(root, "enableDrivers").map(this::nodeValue).orElse(null);

        Element db =
                first(config.getDocumentElement(), "database")
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Config has no database element"));

        DataSource dataSource = null;

        String jndi = first(db, "jndi").map(this::nodeValue).orElse(null);
        if (jndi != null) {
            try {
                dataSource = (DataSource) GeoTools.jndiLookup(jndi);
            } catch (NamingException e) {
                throw new IllegalArgumentException("Error performing JNDI lookup for: " + jndi, e);
            }
        }

        if (dataSource == null) {
            BasicDataSource source = new BasicDataSource();
            source.setDriverClassName("org.postgresql.Driver");

            String host = first(db, "host").map(this::nodeValue).orElse("localhost");

            Integer port =
                    first(db, "port").map(this::nodeValue).map(Integer::parseInt).orElse(5432);

            String name =
                    first(db, "name")
                            .map(this::nodeValue)
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "database 'name' not specified"));

            source.setUrl("jdbc:postgresql://" + host + ":" + port + "/" + name);

            first(db, "user").map(this::nodeValue).ifPresent(source::setUsername);

            first(db, "passwd").map(this::nodeValue).ifPresent(source::setPassword);

            first(db, "pool")
                    .ifPresent(
                            p -> {
                                first(p, "min")
                                        .map(this::nodeValue)
                                        .map(Integer::parseInt)
                                        .ifPresent(source::setMinIdle);
                                first(p, "max")
                                        .map(this::nodeValue)
                                        .map(Integer::parseInt)
                                        .ifPresent(source::setMaxActive);
                            });

            dataSource = new PGRasterDataSource(source);
        }

        this.dataSource = dataSource;

        Element ras =
                first(config.getDocumentElement(), "raster")
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Config has no 'raster' element"));

        this.schema = first(ras, "schema").map(this::nodeValue).orElse("public");
        this.table =
                first(ras, "table")
                        .map(this::nodeValue)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "column must specify a 'table' element"));
        this.column = first(ras, "column").map(this::nodeValue).orElse(null);

        // time
        first(config.getDocumentElement(), "time")
                .ifPresent(
                        el -> {
                            first(el, "enabled")
                                    .map(this::nodeValue)
                                    .map(Boolean::parseBoolean)
                                    .ifPresent(it -> time.enabled = it);
                            first(el, "column")
                                    .map(this::nodeValue)
                                    .ifPresent(it -> time.column = it);
                        });
    }

    @VisibleForTesting
    PGRasterConfig() {}

    Optional<Element> first(Element el, String name) {
        NodeList matches = el.getElementsByTagName(name);
        if (matches.getLength() > 0) {
            return Optional.of((Element) matches.item(0));
        }
        return Optional.empty();
    }

    String nodeValue(Element el) {
        return el.getFirstChild().getNodeValue();
    }

    @Override
    public void close() {
        if (dataSource instanceof PGRasterDataSource) {
            try {
                ((PGRasterDataSource) dataSource).close();
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Error closing data source", e);
            }
        }
    }

    static class TimeConfig {
        boolean enabled = true;
        String column;
    }

    static class PGRasterDataSource extends DBCPDataSource {

        PGRasterDataSource(BasicDataSource wrapped) {
            super(wrapped);
        }
    }
}
