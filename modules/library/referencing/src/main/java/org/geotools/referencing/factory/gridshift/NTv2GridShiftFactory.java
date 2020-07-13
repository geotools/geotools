/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import au.com.objectix.jgridshift.GridShiftFile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.URLs;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;

/**
 * Loads and caches NTv2 grid files. Thisthat incorporates a soft cache mechanism to keep grids in
 * memory when first loaded. It also checks NTv2 grid file format in {@link #isNTv2Grid(String)}
 * method.
 *
 * @author Oscar Fonts
 */
public class NTv2GridShiftFactory extends ReferencingFactory implements BufferedFactory {

    /** The number of hard references to hold internally. */
    private static final int GRID_CACHE_HARD_REFERENCES = 10;

    /** Logger. */
    protected static final Logger LOGGER = Logging.getLogger(NTv2GridShiftFactory.class);

    /** The soft cache that holds loaded grids. */
    private SoftValueHashMap<String, GridShiftFile> ntv2GridCache;

    /** Constructs a factory with the default priority. */
    public NTv2GridShiftFactory() {
        super();
        ntv2GridCache = new SoftValueHashMap<String, GridShiftFile>(GRID_CACHE_HARD_REFERENCES);
    }

    /**
     * Constructs an instance using the specified priority level.
     *
     * @param priority The priority for this factory, as a number between {@link
     *     AbstractFactory#MINIMUM_PRIORITY MINIMUM_PRIORITY} and {@link
     *     AbstractFactory#MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    public NTv2GridShiftFactory(final int priority) {
        super(priority);
        ntv2GridCache = new SoftValueHashMap<String, GridShiftFile>(GRID_CACHE_HARD_REFERENCES);
    }

    /**
     * Performs a NTv2 grid file lookup given its name, and checks for file format correctness.
     *
     * @param location The NTv2 grid file location
     * @return {@code true} if file exists and is valid, {@code false} otherwise
     */
    public boolean isNTv2Grid(URL location) {
        if (location != null) {
            return isNTv2GridFileValid(location); // Check
        } else {
            return false;
        }
    }

    /**
     * Creates a NTv2 Grid.
     *
     * @param gridLocation The NTv2 grid file location
     * @return the grid
     * @throws FactoryException if grid cannot be created
     */
    public GridShiftFile createNTv2Grid(URL gridLocation) throws FactoryException {
        if (gridLocation == null) {
            throw new FactoryException("The grid location must be not null");
        }

        synchronized (ntv2GridCache) { // Prevent simultaneous threads trying to load same grid
            GridShiftFile grid = ntv2GridCache.get(gridLocation.toExternalForm());
            if (grid != null) { // Cached:
                return grid; // - Return
            } else { // Not cached:
                if (gridLocation != null) {
                    grid = loadNTv2Grid(gridLocation); // - Load
                    if (grid != null) {
                        ntv2GridCache.put(gridLocation.toExternalForm(), grid); // - Cache
                        return grid; // - Return
                    }
                }
                throw new FactoryException("NTv2 Grid " + gridLocation + " could not be created.");
            }
        }
    }

    /**
     * Checks if a given resource is a valid NTv2 file without fully loading it.
     *
     * <p>If file is not valid, the cause is logged at {@link Level#WARNING warning level}.
     *
     * @param url the NTv2 file absolute path
     * @return true if file has NTv2 format, false otherwise
     */
    protected boolean isNTv2GridFileValid(URL url) {
        try {

            // Loading as RandomAccessFile doesn't load the full grid
            // in memory, but is a quick method to see if file format
            // is NTv2.
            if (url.getProtocol().equals("file")) {
                File file = URLs.urlToFile(url);

                if (!file.exists() || !file.canRead()) {
                    throw new IOException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, file));
                }

                try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                    // will throw an exception if not a valid file
                    new GridShiftFile().loadGridShiftFile(raf);
                }
            } else {
                try (InputStream in =
                        new BufferedInputStream(url.openConnection().getInputStream())) {

                    // will throw an exception if not a valid file
                    new GridShiftFile().loadGridShiftFile(in, false);
                }
            }

            return true; // No exception thrown => valid file.
        } catch (IllegalArgumentException e) {
            // This usually means resource is not a valid NTv2 file.
            // Let exception message describe the cause.
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * Loads the grid in memory.
     *
     * <p>If file cannot be loaded, the cause is logged at {@link Level#SEVERE severe level}.
     *
     * @param location the NTv2 file absolute path
     * @return the grid, or {@code null} on error
     */
    private GridShiftFile loadNTv2Grid(URL location) throws FactoryException {
        InputStream in = null;
        try {
            GridShiftFile grid = new GridShiftFile();
            in = new BufferedInputStream(location.openStream());
            grid.loadGridShiftFile(in, false); // Load full grid in memory
            in.close();
            return grid;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new FactoryException(e.getLocalizedMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // never mind
            }
        }
    }
}
