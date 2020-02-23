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

/**
 * Information about one of the sources that have been processed by {@link
 * StructuredGridCoverage2DReader#harvest(String, Object, org.geotools.util.factory.Hints)},
 * indicating whether the object was successfully ingested or not.
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface HarvestedSource {

    /** The object that has been processed */
    Object getSource();

    /**
     * If true, the file has been ingested and generated new granules in the reader, false otherwise
     */
    boolean success();

    /** In case the file was not ingested, provides a reason why it was skipped */
    String getMessage();
}
