/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.FileSetManager;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.impl.DefaultFileCoverageAccess;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.Parameter;
import org.geotools.data.Repository;
import org.geotools.data.ServiceInfo;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.imageio.GeoSpatialImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * {@link CoverageAccess} implementation for NetCDF Data format.
 *
 * @author Romagnoli Daniele, GeoSolutions SAS
 */
public class NetCDFAccess extends DefaultFileCoverageAccess
        implements CoverageAccess, FileSetManager {

    private static final Logger LOGGER = Logging.getLogger(NetCDFAccess.class);
    private Exception tracer;

    GeoSpatialImageReader reader = null;

    /** Constructor */
    @SuppressWarnings("serial")
    NetCDFAccess(
            Driver driver,
            URL source,
            Map<String, Serializable> additionalParameters,
            Hints hints,
            ProgressListener listener)
            throws DataSourceException {

        super(
                driver,
                EnumSet.of(AccessType.READ_ONLY),
                new HashMap<String, Parameter<?>>() {
                    {
                        put(DefaultFileDriver.URL.key, DefaultFileDriver.URL);
                    }
                },
                source,
                additionalParameters);

        // get the protocol
        final String protocol = source.getProtocol();

        // file
        if (!(protocol.equalsIgnoreCase("file")
                || protocol.equalsIgnoreCase("http")
                || protocol.equalsIgnoreCase("dods"))) {
            throw new DataSourceException(
                    "Wrong protocol for URL:" + source.toExternalForm().toString());
        }
        File sourceFile = null;
        if (protocol.equalsIgnoreCase("file")) {
            // convert to file
            sourceFile = URLs.urlToFile(source);

            // check that it is a file,exists and can be at least read
            if (!sourceFile.exists() || !sourceFile.isFile() || !sourceFile.canRead()) {
                throw new DataSourceException("Invalid source");
            }
        }

        // initialize
        // get the needed info from them to set the extent
        try {
            reader = (NetCDFImageReader) NetCDFDriver.SPI.createReaderInstance();

            // Look for auxiliary paths from Hints and set them into the reader
            setAuxiliaryEntries(hints);
            reader.setInput(this.source);

            if (names == null) {
                names = new ArrayList<Name>();
                final Collection<Name> originalNames = reader.getCoveragesNames();
                for (Name name : originalNames) {
                    Name coverageName = new NameImpl(/*namePrefix + */ name.toString());
                    names.add(coverageName);
                }
            }
        } catch (Exception e) {
            throw new DataSourceException(e);
        }

        if (NetCDFUtilities.TRACE_ENABLED) {
            tracer = new Exception();
            tracer.fillInStackTrace();
        }
    }

    /**
     * Scan the provided hints (if any) and look for auxiliary entries to be set into the reader.
     */
    private void setAuxiliaryEntries(Hints hints) {
        String prefix = "";
        if (hints != null) {
            if (hints.containsKey(Utils.PARENT_DIR)) {
                prefix = (String) hints.get(Utils.PARENT_DIR) + File.separatorChar;
            }
            if (hints.containsKey(Utils.AUXILIARY_FILES_PATH)) {
                String filePath = (String) hints.get(Utils.AUXILIARY_FILES_PATH);
                filePath = makeAbsolute(prefix, filePath);
                reader.setAuxiliaryFilesPath(filePath);
            }
            if (hints.containsKey(Utils.AUXILIARY_DATASTORE_PATH)) {
                String filePath = (String) hints.get(Utils.AUXILIARY_DATASTORE_PATH);
                filePath = makeAbsolute(prefix, filePath);
                reader.setAuxiliaryDatastorePath(filePath);
            }
            if (hints.containsKey(Hints.REPOSITORY)) {
                reader.setRepository((Repository) hints.get(Hints.REPOSITORY));
            }
        }
    }

    private String makeAbsolute(String prefix, String filePath) {
        if (!Paths.get(filePath).isAbsolute()) {
            filePath = prefix + filePath;
        }
        return filePath;
    }

    @Override
    public boolean delete(Name name, Map<String, Serializable> params, Hints hints)
            throws IOException {
        // Right now, simply delete the name
        return names.remove(name);
    }

    public CoverageSource access(
            Name name,
            Map<String, Serializable> params,
            AccessType accessType,
            Hints hints,
            ProgressListener listener)
            throws IOException {
        if (listener == null) {
            listener = new NullProgressListener();
        }
        listener.started();
        try {
            return new NetCDFSource((NetCDFImageReader) reader, name);
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Failed to access the NetCDF source", e);
            listener.exceptionOccurred(e);
            return null;
        } finally {
            listener.complete();
        }
    }

    @Override
    public ServiceInfo getInfo(ProgressListener listener) {
        if (listener == null) {
            listener = new NullProgressListener();
        }
        listener.started();
        final DefaultServiceInfo info = new DefaultServiceInfo();

        // Fix that
        Collection<Name> coverageNames = getNames(listener);
        Iterator<Name> namesIterator = coverageNames.iterator();
        if (namesIterator.hasNext()) {
            info.setTitle(namesIterator.next().toString());
        }
        try {
            info.setSource(source.toURI());
        } catch (URISyntaxException e1) {

        } finally {
            listener.complete();
        }
        return info;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (reader != null) {
            try {
                reader.dispose();
                reader = null;
            } catch (Throwable t) {

            }
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        if (reader != null) {
            LOGGER.warning(
                    "There is code leaving netcdf readers open, this might cause "
                            + "issues with file deletion on Windows!");
            if (NetCDFUtilities.TRACE_ENABLED) {
                LOGGER.log(
                        Level.WARNING,
                        "The unclosed reader originated on this stack trace",
                        tracer);
            }
            dispose();
        }
    }

    @Override
    public void addFile(String filePath) {
        reader.addFile(filePath);
    }

    @Override
    public List<String> list() {
        return reader.list();
    }

    @Override
    public void removeFile(String filePath) {
        reader.removeFile(filePath);
    }

    @Override
    public void purge() {
        reader.purge();
    }
}
