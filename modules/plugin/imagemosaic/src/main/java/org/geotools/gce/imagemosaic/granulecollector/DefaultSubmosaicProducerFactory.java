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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.gce.imagemosaic.MergeBehavior;
import org.geotools.gce.imagemosaic.RasterLayerRequest;
import org.geotools.gce.imagemosaic.RasterLayerResponse;
import org.geotools.gce.imagemosaic.RasterManager;
import org.opengis.filter.Filter;

/**
 * Create SubmosaicProducer based on whether the request was for a stacked mosaic with additional
 * domains requested
 */
public class DefaultSubmosaicProducerFactory implements SubmosaicProducerFactory {

    @Override
    public List<SubmosaicProducer> createProducers(
            RasterLayerRequest request,
            RasterManager rasterManager,
            RasterLayerResponse response,
            boolean dryRun) {
        // get merge behavior as per request
        List<SubmosaicProducer> defaultSubmosaicProducers = new ArrayList<>();
        MergeBehavior mergeBehavior = request.getMergeBehavior();

        // prepare dimensions management if needed, that is in case we use stacking
        if (mergeBehavior.equals(MergeBehavior.STACK)) {

            // create filter to filter results
            // === Custom Domains Management
            final Map<String, List> requestedAdditionalDomains =
                    request.getRequestedAdditionalDomains();
            if (!requestedAdditionalDomains.isEmpty()) {
                Set<Map.Entry<String, List>> entries = requestedAdditionalDomains.entrySet();

                // Preliminary check on additional domains specification
                // we can't do stack in case there are multiple values selections for more than one
                // domain
                checkMultipleSelection(entries);

                // Prepare filtering
                Map.Entry<String, List> multipleSelectionEntry = null;
                final List<Filter> filters = new ArrayList<Filter>(entries.size());

                // Loop over the additional domains
                for (Map.Entry<String, List> entry : entries) {
                    if (entry.getValue().size() > 1) {
                        // take note of the entry containing multiple values
                        multipleSelectionEntry = entry;
                    } else {
                        // create single value domain filter
                        String domainName =
                                entry.getKey() + RasterManager.DomainDescriptor.DOMAIN_SUFFIX;
                        filters.add(
                                rasterManager
                                        .getDomainsManager()
                                        .createFilter(domainName, Arrays.asList(entry.getValue())));
                    }
                }

                // Anding all filters together
                Filter andFilter =
                        filters.size() > 0
                                ? FeatureUtilities.DEFAULT_FILTER_FACTORY.and(filters)
                                : null;

                if (multipleSelectionEntry == null) {
                    // Simpler case... no multiple selections. All filter have already been combined
                    defaultSubmosaicProducers.add(
                            new DefaultSubmosaicProducer(response, andFilter, dryRun));
                } else {
                    final String domainName =
                            multipleSelectionEntry.getKey()
                                    + RasterManager.DomainDescriptor.DOMAIN_SUFFIX;

                    // Need to loop over the multiple values of a custom domains
                    final List values = multipleSelectionEntry.getValue();
                    for (Object o : values) {

                        // create a filter for this value
                        Filter valueFilter =
                                rasterManager
                                        .getDomainsManager()
                                        .createFilter(domainName, Arrays.asList(o));

                        // combine that filter with the previously merged ones
                        Filter combinedFilter =
                                andFilter == null
                                        ? valueFilter
                                        : FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                                                andFilter, valueFilter);
                        defaultSubmosaicProducers.add(
                                new DefaultSubmosaicProducer(response, combinedFilter, dryRun));
                    }
                }
            }
        }

        // we don't stack them, either because we are not asked to or because we don't need to
        // although
        // we were asked
        // let's use a default marker
        if (defaultSubmosaicProducers.isEmpty()) {
            defaultSubmosaicProducers.add(
                    new DefaultSubmosaicProducer(response, Filter.INCLUDE, dryRun));
        }

        return defaultSubmosaicProducers;
    }

    /**
     * Check whether the specified custom domains contain multiple selection. That case isn't
     * supported so we will throw an exception
     */
    private void checkMultipleSelection(Set<Map.Entry<String, List>> entries) {
        int multipleDimensionsSelections = 0;
        for (Map.Entry<String, List> entry : entries) {
            if (entry.getValue().size() > 1) {
                multipleDimensionsSelections++;
                if (multipleDimensionsSelections > 1) {
                    throw new IllegalStateException(
                            "Unable to handle dimensions stacking for more than 1 dimension");
                }
            }
        }
    }
}
