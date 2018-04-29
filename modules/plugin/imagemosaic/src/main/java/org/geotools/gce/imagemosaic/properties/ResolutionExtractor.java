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
package org.geotools.gce.imagemosaic.properties;

import static org.geotools.gce.imagemosaic.properties.ResolutionExtractor.Axis.BOTH;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/** @author Niels Charlier */
class ResolutionExtractor extends PropertiesCollector {

    private static final Logger LOGGER = Logging.getLogger(ResolutionExtractor.class);

    enum Axis {
        X,
        Y,
        BOTH
    };

    Axis axis = BOTH;

    public ResolutionExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, Axis axis) {
        super(spi, propertyNames);
        this.axis = axis;
    }

    @Override
    public PropertiesCollector collect(final GridCoverage2DReader gridCoverageReader) {
        double[][] resolutionLevels;
        try {
            resolutionLevels = gridCoverageReader.getResolutionLevels();

            if (axis == Axis.X) {
                addMatch("" + resolutionLevels[0][0]);
            } else if (axis == Axis.Y) {
                addMatch("" + resolutionLevels[0][1]);
            } else {
                addMatch("" + Math.max(resolutionLevels[0][0], resolutionLevels[0][1]));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return this;
    }

    Double getResolution() {
        // get the the match and convert it to double
        String resolutionStr = getMatches().size() > 0 ? getMatches().get(0) : null;
        if (resolutionStr != null) {
            try {
                return Double.parseDouble(resolutionStr);
            } catch (NumberFormatException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
        }
        return null;
    }

    @Override
    public void setProperties(SimpleFeature feature) {
        Double res = getResolution();
        // set the properties, only if we have a match!
        if (res == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No matches found for this property extractor:");
            }
            throw new IllegalArgumentException("No matches found for this property extractor");
        }
        for (String propertyName : getPropertyNames()) {
            // set the property
            feature.setAttribute(propertyName, res);
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        Double res = getResolution();
        // set the properties, only if we have a match!
        if (res == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No matches found for this property extractor:");
            }
        }
        for (String propertyName : getPropertyNames()) {
            // set the property
            map.put(propertyName, res);
        }
    }
}
