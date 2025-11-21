/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.imagemosaic.properties;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A Bean containing the 3 elements defining a PropertiesCollector, the spi, the collector config and the name. */
public class PropertiesCollectorBean {

    static final Set<PropertiesCollectorSPI> PC_SPIS = PropertiesCollectorFinder.getPropertiesCollectorSPI();
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(PropertiesCollectorBean.class);

    public final String spi;
    public final String config;
    public final String propertyName;

    public PropertiesCollectorBean(String spi, String config, String propertyName) {
        this.spi = spi;
        this.config = config;
        this.propertyName = propertyName;
    }

    public PropertiesCollector getCollector() {
        final String spiName = spi;
        PropertiesCollectorSPI selectedSPI = null;
        for (PropertiesCollectorSPI spi : PC_SPIS) {
            if (spi.isAvailable() && spi.getName().equalsIgnoreCase(spiName)) {
                selectedSPI = spi;
                break;
            }
        }

        if (selectedSPI == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Unable to find a PropertyCollector for this definition: " + spiName);
            }
            return null;
        }

        // property names
        String pcConfig = null;
        String collectorValue = config;
        if (collectorValue != null) {
            if (!collectorValue.startsWith(DefaultPropertiesCollectorSPI.REGEX_PREFIX)) {
                pcConfig = DefaultPropertiesCollectorSPI.REGEX_PREFIX + config;
            } else {
                pcConfig = config;
            }
        }

        // create the PropertiesCollector
        return selectedSPI.create(pcConfig, Collections.singletonList(propertyName));
    }
}
