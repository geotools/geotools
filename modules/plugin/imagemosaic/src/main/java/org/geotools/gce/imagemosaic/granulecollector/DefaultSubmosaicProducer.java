/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic.granulecollector;

import java.util.logging.Level;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.RasterLayerResponse;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 * Class responsible for loading granules, mosaicking a group of granules, pre-processing them
 * before handling, etc.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class DefaultSubmosaicProducer extends BaseSubmosaicProducer {

    /**
     * Constructor.
     *
     * @param granuleFilter the {@link Filter} we are supposed to use to select granules for this
     *     {@link DefaultSubmosaicProducer}.
     * @param dryRun whether we need to make
     */
    public DefaultSubmosaicProducer(
            RasterLayerResponse rasterLayerResponse, Filter granuleFilter, boolean dryRun) {
        super(rasterLayerResponse, dryRun);
        this.granuleFilter = granuleFilter;
    }

    /** {@link Filter} instance used to collect granule. */
    private final Filter granuleFilter;

    /**
     * This method is responsible for collecting all the granules accepting a certain {@link
     * Filter}.
     *
     * <p>The method return <code>true</code> when a {@link GranuleDescriptor} for which the {@link
     * GranuleDescriptor#originator} {@link SimpleFeature} is evaluated positively by the internal
     * filter and retain the granule, or <code>false</code> otherwise so that the caller can keep
     * trying with a different {@link DefaultSubmosaicProducer}
     *
     * @param granuleDescriptor the {@link GranuleDescriptor} to test with the internal {@link
     *     Filter}
     * @return <code>true</code> in case the {@link GranuleDescriptor} is added, <code>false</code>
     *     otherwise.
     */
    @Override
    public boolean accept(GranuleDescriptor granuleDescriptor) {
        Utilities.ensureNonNull("granuleDescriptor", granuleDescriptor);

        if (granuleFilter.evaluate(granuleDescriptor.getOriginator())) {
            return acceptGranule(granuleDescriptor);
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("We filtered out the granule " + granuleDescriptor.toString());
            }
        }
        return false;
    }
}
