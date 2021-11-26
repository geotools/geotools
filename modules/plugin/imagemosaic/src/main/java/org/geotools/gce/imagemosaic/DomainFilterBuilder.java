/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.util.Range;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/** Helper class to build filters matching the provided dimension values */
public class DomainFilterBuilder {

    static final Logger LOGGER = Logging.getLogger(DomainFilterBuilder.class);

    private final String identifier;
    private final String property;
    private final String endProperty;

    public DomainFilterBuilder(String identifier, String property) {
        this.identifier = identifier;
        this.property = property;
        this.endProperty = null;
    }

    public DomainFilterBuilder(String identifier, String property, String endProperty) {
        this.identifier = identifier;
        this.property = property;
        this.endProperty = endProperty;
    }

    /**
     * This method is responsible for creating {@link Filter} that match the provided {@link List}
     * of values
     *
     * @param values the {@link List} of values to use for building the containment {@link Filter}.
     * @return a {@link Filter} that matches the provided value {@link List}
     */
    @SuppressWarnings("unchecked")
    public Filter createFilter(List values) {

        // === create the filter
        // loop values and AND them
        final List<Filter> filters = new ArrayList<>();
        FilterFactory2 ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
        for (Object value : values) {
            // checks
            if (value == null) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Ignoring null date for the filter:" + this.identifier);
                }
                continue;
            }
            if (endProperty == null) {
                // Domain made of single values
                if (value instanceof Range) {
                    // RANGE
                    final Range range = (Range) value;
                    filters.add(
                            ff.and(
                                    ff.lessOrEqual(
                                            ff.property(property), ff.literal(range.getMaxValue())),
                                    ff.greaterOrEqual(
                                            ff.property(property),
                                            ff.literal(range.getMinValue()))));
                } else {
                    // SINGLE value
                    filters.add(ff.equal(ff.property(property), ff.literal(value), true));
                }
            } else {
                // Domain made of ranges such as (beginTime,endTime) ,
                // (beginElevation,endElevation) , ...
                if (value instanceof Range) {
                    // RANGE
                    final Range range = (Range) value;
                    final Comparable maxValue = range.getMaxValue();
                    final Comparable minValue = range.getMinValue();
                    if (maxValue.compareTo(minValue) != 0) {
                        // logic comes from Range.intersectsNC(Range)
                        // in summary, requestedMax > min && requestedMin < max
                        Filter maxCondition =
                                ff.greaterOrEqual(ff.literal(maxValue), ff.property(property));
                        Filter minCondition =
                                ff.lessOrEqual(ff.literal(minValue), ff.property(endProperty));

                        filters.add(ff.and(Arrays.asList(maxCondition, minCondition)));
                        continue;
                    } else {
                        value = maxValue;
                    }
                }
                filters.add(
                        ff.and(
                                ff.lessOrEqual(ff.property(property), ff.literal(value)),
                                ff.greaterOrEqual(ff.property(endProperty), ff.literal(value))));
            }
        }
        if (filters.size() == 1) return filters.get(0);
        return ff.or(filters);
    }
}
