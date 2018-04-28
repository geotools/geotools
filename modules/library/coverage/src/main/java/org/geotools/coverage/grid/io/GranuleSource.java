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
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * API to operate on Granules data.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public interface GranuleSource {

    /**
     * Retrieves granules, in the form of a {@code SimpleFeatureCollection}, based on a {@code
     * Query}.
     *
     * @param q the {@link Query} to select granules
     * @return the resulting granules.
     * @throws IOException
     */
    public SimpleFeatureCollection getGranules(Query q) throws IOException;

    /**
     * Gets the number of the granules that would be returned by the given {@code Query}, taking
     * into account any settings for max features and start index set on the {@code Query}.
     *
     * @param q the {@link Query} to select granules
     * @return the number of granules
     * @throws IOException
     */
    public int getCount(Query q) throws IOException;

    /**
     * Get the spatial bounds of the granules that would be returned by the given {@code Query}.
     *
     * @param q the {@link Query} to select granules
     * @return The bounding envelope of the requested data
     * @throws IOException
     */
    public ReferencedEnvelope getBounds(Query q) throws IOException;

    /**
     * Retrieves the schema (feature type) that will apply to granules retrieved from this {@code
     * GranuleSource}.
     *
     * @return
     * @throws IOException
     */
    public SimpleFeatureType getSchema() throws IOException;

    /**
     * This will free/release any resource (cached granules, ...).
     *
     * @throws IOException
     */
    public void dispose() throws IOException;
}
