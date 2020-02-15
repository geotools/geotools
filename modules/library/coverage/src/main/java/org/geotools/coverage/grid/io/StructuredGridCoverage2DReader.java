/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.io.IOException;
import java.util.List;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A {@link GridCoverage2DReader} which exposes the underlying granule structure and allows to
 * create and remove coverages.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public interface StructuredGridCoverage2DReader extends GridCoverage2DReader {

    /**
     * Returns the granule source for the specified coverage (might be null, if there is only one
     * supported coverage)
     *
     * @param coverageName the name of the specified coverage
     * @param readOnly a boolean indicating whether we may want modify the GranuleSource
     * @return the requested {@link GranuleSource}
     */
    GranuleSource getGranules(String coverageName, boolean readOnly)
            throws IOException, UnsupportedOperationException;

    /** Return whether this reader can modify the granule source */
    boolean isReadOnly();

    /** Creates a granule store for a new coverage with the given feature type */
    void createCoverage(String coverageName, SimpleFeatureType schema)
            throws IOException, UnsupportedOperationException;

    /** removes a granule store for the specified coverageName */
    boolean removeCoverage(String coverageName, boolean delete)
            throws IOException, UnsupportedOperationException;

    /**
     * delete all stuff (database content, indexer files, property files, associated auxiliary files
     * and so on).
     *
     * @param deleteData specifies whether data (granules) should be deleted too.
     */
    void delete(boolean deleteData) throws IOException;

    /**
     * Harvests the specified source into the reader. Depending on the implementation, the original
     * source is harvested in place (e.g., image mosaic), or might be copied into the reader
     * persistent storage (e.g., database raster handling)
     *
     * @param defaultTargetCoverage Default target coverage, to be used in case the sources being
     *     harvested are not structured ones. The parameter is optional, in case it's missing the
     *     reader will use the first coverage as the default target.
     * @param source The source can be any kind of object, it's up to the reader implementation to
     *     understand and use it. Commons source types could be a single file, or a folder.
     * @param hints Used to provide implementation specific hints on how to harvest the sources
     */
    List<HarvestedSource> harvest(String defaultTargetCoverage, Object source, Hints hints)
            throws IOException, UnsupportedOperationException;

    /**
     * Describes the dimensions supported by the specified coverage, if any. (coverageName might be
     * null, if there is only one supported coverage)
     */
    List<DimensionDescriptor> getDimensionDescriptors(String coverageName) throws IOException;
}
