/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.gce;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.jndi.SharedSessionPool;
import org.geotools.arcsde.raster.info.GatherCoverageMetadataCommand;
import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.raster.io.RasterReaderFactory;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataSourceException;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * An implementation of the ArcSDE Raster Format. Based on the ArcGrid module.
 * 
 * @author Saul Farber (saul.farber)
 * @author jeichar
 * @author Simone Giannecchini (simboss)
 * @author Gabriel Roldan (OpenGeo)
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/gce/ArcSDERasterFormat.java $
 */
@SuppressWarnings({ "nls", "deprecation" })
public final class ArcSDERasterFormat extends AbstractGridFormat implements Format {

    protected static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.gce");

    private final Map<String, ArcSDEConnectionConfig> connectionConfigs = new WeakHashMap<String, ArcSDEConnectionConfig>();

    private static final ArcSDERasterFormat instance = new ArcSDERasterFormat();

    private boolean statisticsMandatory = true;

    /**
     * Creates an instance and sets the metadata.
     */
    private ArcSDERasterFormat() {
        setInfo();
    }

    public static ArcSDERasterFormat getInstance() {
        return instance;
    }

    /**
     * Sets the metadata information.
     */
    private void setInfo() {
        Map<String, String> info = new HashMap<String, String>();

        info.put("name", "ArcSDE Raster");
        info.put("description", "ArcSDE Raster Format");
        info.put("vendor", "Geotools ");
        info.put("docURL", "");
        info.put("version", GeoTools.getVersion().toString());
        mInfo = info;

        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[] { READ_GRIDGEOMETRY2D, OVERVIEW_POLICY }));
    }

    /**
     * @param source
     *            either a {@link String} or {@link File} instance representing the connection URL
     * @see AbstractGridFormat#getReader(Object source)
     */
    @Override
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    private static final WeakHashMap<String, ArcSDEGridCoverage2DReaderJAI> readerCache = new WeakHashMap<String, ArcSDEGridCoverage2DReaderJAI>();

    private static final ReadWriteLock readersLock = new ReentrantReadWriteLock();

    /**
     * @param source
     *            either a {@link String} or {@link File} instance representing the connection URL
     * @see AbstractGridFormat#getReader(Object, Hints)
     */
    @Override
    public AbstractGridCoverage2DReader getReader(final Object source, final Hints hints) {
        try {
            if (source == null) {
                throw new DataSourceException("No source set to read this coverage.");
            }

            // this will be our connection string
            final String coverageUrl = parseCoverageUrl(source);

            readersLock.readLock().lock();

            ArcSDEGridCoverage2DReaderJAI reader = readerCache.get(coverageUrl);

            readersLock.readLock().unlock();

            if (reader == null) {
                readersLock.writeLock().lock();
                try {
                    reader = readerCache.get(coverageUrl);
                    if (reader == null) {
                        final ArcSDEConnectionConfig connectionConfig = getConnectionConfig(coverageUrl);

                        final ISessionPool sessionPool = setupConnectionPool(connectionConfig);

                        final RasterDatasetInfo rasterInfo = loadRasterInfo(coverageUrl,
                                sessionPool);

                        final RasterReaderFactory rasterReaderFactory = new RasterReaderFactory(
                                sessionPool);

                        reader = new ArcSDEGridCoverage2DReaderJAI(this, rasterReaderFactory,
                                rasterInfo, hints);
                        readerCache.put(coverageUrl, reader);
                    }
                } finally {
                    readersLock.writeLock().unlock();
                }
            }
            return reader;
        } catch (IOException dse) {
            LOGGER.log(Level.SEVERE, "Unable to creata ArcSDERasterReader for " + source + ".", dse);
            throw new RuntimeException(dse);
        }
    }

    private RasterDatasetInfo loadRasterInfo(final String coverageUrl, ISessionPool connectionPool)
            throws IOException {

        final String rasterTable;
        {
            String sdeUrl = coverageUrl;
            if (sdeUrl.indexOf(";") != -1) {
                /*
                 * We're not using any extra param anymore. Yet, be cautious cause a client may
                 * still be using urls with some old extra param, so just strip it
                 */
                sdeUrl = sdeUrl.substring(0, sdeUrl.indexOf(";"));
            }
            rasterTable = sdeUrl.substring(sdeUrl.indexOf("#") + 1);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Building ArcSDEGridCoverageReader2D for " + rasterTable);
            }
        }

        ISession scon;
        try {
            scon = connectionPool.getSession(false);
        } catch (UnavailableConnectionException e) {
            throw new RuntimeException(e);
        }

        RasterDatasetInfo rasterInfo;

        try {
            GatherCoverageMetadataCommand command = new GatherCoverageMetadataCommand(rasterTable,
                    statisticsMandatory);
            rasterInfo = scon.issue(command);
        } finally {
            scon.dispose();
        }
        return rasterInfo;
    }

    private ArcSDEConnectionConfig getConnectionConfig(final String coverageUrl) {
        ArcSDEConnectionConfig sdeConfig;
        sdeConfig = connectionConfigs.get(coverageUrl);
        if (sdeConfig == null) {
            synchronized (connectionConfigs) {
                sdeConfig = connectionConfigs.get(coverageUrl);
                if (sdeConfig == null) {
                    sdeConfig = sdeURLToConnectionConfig(new StringBuffer(coverageUrl));
                    connectionConfigs.put(coverageUrl, sdeConfig);
                }
            }
        }
        return sdeConfig;
    }

    /**
     * @see AbstractGridFormat#getWriter(Object)
     */
    @Override
    public GridCoverageWriter getWriter(Object destination) {
        // return new ArcGridWriter(destination);
        return null;
    }

    /**
     * @param source
     *            either a {@link String} or {@link File} instance representing the connection URL
     * @see AbstractGridFormat#accepts(Object input)
     */
    @Override
    public boolean accepts(Object input, Hints hints) {
        StringBuffer url;
        if (input instanceof File) {
            url = new StringBuffer(((File) input).getPath());
        } else if (input instanceof String) {
            url = new StringBuffer((String) input);
        } else {
            return false;
        }
        try {
            sdeURLToConnectionConfig(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @see Format#getName()
     */
    @Override
    public String getName() {
        return this.mInfo.get("name");
    }

    /**
     * @see Format#getDescription()
     */
    @Override
    public String getDescription() {
        return this.mInfo.get("description");
    }

    /**
     * @see Format#getVendor()
     */
    @Override
    public String getVendor() {
        return this.mInfo.get("vendor");
    }

    /**
     * @see Format#getDocURL()
     */
    @Override
    public String getDocURL() {
        return this.mInfo.get("docURL");
    }

    /**
     * @see Format#getVersion()
     */
    @Override
    public String getVersion() {
        return this.mInfo.get("version");
    }

    /**
     * Retrieves the default instance for the {@link ArcSDERasterFormat} of the
     * {@link GeoToolsWriteParams} to control the writing process.
     * 
     * @return a default instance for the {@link ArcSDERasterFormat} of the
     *         {@link GeoToolsWriteParams} to control the writing process.
     * @see AbstractGridFormat#getDefaultImageIOWriteParameters()
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("ArcSDE Rasters are read only for now.");
    }

    // ////////////////

    /**
     * @param input
     *            either a {@link String} or a {@link File} instance representing the connection URL
     *            to a given coverage
     * @return the connection URL as a string
     */
    private String parseCoverageUrl(Object input) {
        String coverageUrl;
        if (input instanceof String) {
            coverageUrl = (String) input;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("connecting to ArcSDE Raster: " + coverageUrl);
            }
        } else if (input instanceof File) {
            String path = ((File) input).getPath();
            while (path.indexOf('\\') > -1) {
                path = path.replace('\\', '/');
            }
            URI uri;
            try {
                uri = new URI(path);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(path);
            }
            coverageUrl = uri.toString();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("connectiong via file-hack to ArcSDE Raster: " + coverageUrl);
            }
        } else {
            throw new IllegalArgumentException("Unsupported input type: " + input.getClass());
        }
        return coverageUrl;
    }

    /**
     * Checks the input provided to this {@link ArcSDERasterGridCoverage2DReader} and sets all the
     * other objects and flags accordingly.
     * 
     * @param sdeUrl
     *            a url representing the connection parameters to an arcsde server instance provied
     *            to this {@link ArcSDERasterGridCoverage2DReader}.
     * @throws IOException
     */
    private ISessionPool setupConnectionPool(ArcSDEConnectionConfig sdeConfig) throws IOException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Getting ArcSDE connection pool for " + sdeConfig);
        }

        ISessionPool sessionPool;
        sessionPool = SharedSessionPool.getInstance(sdeConfig, SessionPoolFactory.getInstance());

        return sessionPool;
    }

    public static String createRasterURL(final ArcSDEConnectionConfig config,
            final String rasterName) {
        StringBuilder sb = new StringBuilder("sde://");
        sb.append(config.getUserName()).append(":").append(config.getPassword()).append("@");
        sb.append(config.getServerName()).append(":");
        sb.append(config.getPortNumber()).append("/");
        sb.append(config.getDatabaseName() == null ? "" : config.getDatabaseName());
        sb.append("#").append(rasterName);
        sb.append(";pool.minConnections=").append(config.getMinConnections());
        sb.append(";pool.maxConnections=").append(config.getMaxConnections());
        return sb.toString();
    }

    /**
     * @param sdeUrl
     *            - A StringBuffer containing a string of form
     *            'sde://user:pass@sdehost:[port]/[dbname]
     * @return a ConnectionConfig object representing these parameters
     */
    public static ArcSDEConnectionConfig sdeURLToConnectionConfig(StringBuffer sdeUrl) {
        // annoyingly, geoserver currently stores the user-entered SDE string as
        // a File, and passes us the
        // File object. The File object strips the 'sde://user...' into a
        // 'sde:/user..'. So we need to check
        // for both forms of the url.
        String sdeHost, sdeUser, sdePass, sdeDBName;
        int sdePort;
        if (sdeUrl.indexOf("sde:/") == -1) {
            throw new IllegalArgumentException(
                    "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName -- Got "
                            + sdeUrl);
        }
        if (sdeUrl.indexOf("sde://") == -1) {
            sdeUrl.delete(0, 5);
        } else {
            sdeUrl.delete(0, 6);
        }

        int idx = sdeUrl.indexOf(":");
        if (idx == -1) {
            throw new IllegalArgumentException(
                    "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName[;pool.minConnections=<int>][;pool.maxConnections=<int>]");
        }
        sdeUser = sdeUrl.substring(0, idx);
        sdeUrl.delete(0, idx);

        idx = sdeUrl.indexOf("@");
        if (idx == -1) {
            throw new IllegalArgumentException(
                    "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName[;pool.minConnections=<int>][;pool.maxConnections=<int>]");
        }
        sdePass = sdeUrl.substring(1, idx);
        sdeUrl.delete(0, idx);

        idx = sdeUrl.indexOf(":");
        if (idx == -1) {
            // there's no "port" specification. Assume 5151;
            sdePort = 5151;

            idx = sdeUrl.indexOf("/");
            if (idx == -1) {
                throw new IllegalArgumentException(
                        "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName");
            }
            sdeHost = sdeUrl.substring(1, idx).toString();
            sdeUrl.delete(0, idx);
        } else {
            sdeHost = sdeUrl.substring(1, idx).toString();
            sdeUrl.delete(0, idx);

            idx = sdeUrl.indexOf("/");
            if (idx == -1) {
                throw new IllegalArgumentException(
                        "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName");
            }
            sdePort = Integer.parseInt(sdeUrl.substring(1, idx).toString());
            sdeUrl.delete(0, idx);
        }

        idx = sdeUrl.indexOf("#");
        if (idx == -1) {
            throw new IllegalArgumentException(
                    "ArcSDE Raster URL must be of the form sde://user:pass@sdehost:port/[dbname]#rasterTableName");
        }
        sdeDBName = sdeUrl.substring(1, idx).toString();
        sdeUrl.delete(0, idx);

        String minConnections = "1";
        String maxConnections = "10";
        if (sdeUrl.indexOf(";") > 0) {
            String optionals = sdeUrl.substring(sdeUrl.indexOf(";") + 1);
            String[] options = optionals.split(";");
            for (String option : options) {
                String[] pair = option.split("=");
                if (pair.length != 2) {
                    LOGGER.info("Ignoring malformed optional param '" + option + "'");
                    continue;
                }
                String name = pair[0];
                String value = pair[1];
                if ("pool.minConnections".equals(name)) {
                    try {
                        minConnections = Integer.valueOf(value).toString();
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Wrong pool.minConnections parameter: " + value);
                    }
                } else if ("pool.maxConnections".equals(name)) {
                    try {
                        maxConnections = Integer.valueOf(value).toString();
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Wrong pool.maxConnections parameter: " + value);
                    }
                } else {
                    LOGGER.info("Ignoring unrecognized optional parameter '" + option
                            + "'. Must be one of [pool.minConnections, pool.maxConnections]");
                }
            }
        }
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME, sdeHost);
        params.put(ArcSDEConnectionConfig.PORT_NUMBER_PARAM_NAME, String.valueOf(sdePort));
        params.put(ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME, sdeDBName);
        params.put(ArcSDEConnectionConfig.USER_NAME_PARAM_NAME, sdeUser);
        params.put(ArcSDEConnectionConfig.PASSWORD_PARAM_NAME, sdePass);
        params.put(ArcSDEConnectionConfig.MIN_CONNECTIONS_PARAM_NAME, minConnections);
        params.put(ArcSDEConnectionConfig.MAX_CONNECTIONS_PARAM_NAME, maxConnections);
        params.put(ArcSDEConnectionConfig.CONNECTION_TIMEOUT_PARAM_NAME, "-1");// do not wait

        ArcSDEConnectionConfig config = ArcSDEConnectionConfig.fromMap(params);
        return config;
    }

    /**
     * Used by test code to indicate wether to fail when a raster lacks statistics, since we can't
     * create statistics with the ArcSDE Java API
     * 
     * @param statisticsMandatory
     */
    void setStatisticsMandatory(final boolean statisticsMandatory) {
        this.statisticsMandatory = statisticsMandatory;
    }

	@Override
	public GridCoverageWriter getWriter(Object destination, Hints hints) {
		return null;
	}
}
