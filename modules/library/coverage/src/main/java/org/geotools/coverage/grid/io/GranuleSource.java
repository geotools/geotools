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

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
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
     * Asks a {@link GranuleSource} to return a file based view of the granules instead of a slice
     * based view. In case a granule file contains more than one slice (e.g., NetCDF). The returned
     * features will also miss an eventual location attribute, and include a full {@link
     * org.geotools.data.FileGroupProvider.FileGroup} as the feature metadata under the {@link
     * #FILES} key.
     */
    public static final Hints.Key FILE_VIEW = new Hints.Key(Boolean.class);

    /**
     * Used as key in the granule feature user data, pointing to a {@link
     * org.geotools.data.FileGroupProvider.FileGroup} with the infos about the group of files
     * composing
     */
    public static final String FILES = "GranuleFiles";

    /**
     * Retrieves granules, in the form of a {@code SimpleFeatureCollection}, based on a {@code
     * Query}.
     *
     * @param q the {@link Query} to select granules
     * @return the resulting granules.
     */
    public SimpleFeatureCollection getGranules(Query q) throws IOException;

    /**
     * Gets the number of the granules that would be returned by the given {@code Query}, taking
     * into account any settings for max features and start index set on the {@code Query}.
     *
     * @param q the {@link Query} to select granules
     * @return the number of granules
     */
    public int getCount(Query q) throws IOException;

    /**
     * Get the spatial bounds of the granules that would be returned by the given {@code Query}.
     *
     * @param q the {@link Query} to select granules
     * @return The bounding envelope of the requested data
     */
    public ReferencedEnvelope getBounds(Query q) throws IOException;

    /**
     * Retrieves the schema (feature type) that will apply to granules retrieved from this {@code
     * GranuleSource}.
     */
    public SimpleFeatureType getSchema() throws IOException;

    /** This will free/release any resource (cached granules, ...). */
    public void dispose() throws IOException;

    /**
     * Returns the set of hints that this {@code GranuleSource} supports via {@code Query} requests.
     *
     * <p>Note: the existence of a specific hint does not guarantee that it will always be honored
     * by the implementing class.
     *
     * @see Hints#SGCR_FILE_VIEW
     * @return a set of {@code RenderingHints#Key} objects; may be empty but never {@code null}
     */
    public default Set<RenderingHints.Key> getSupportedHints() {
        return Collections.emptySet();
    }
}
