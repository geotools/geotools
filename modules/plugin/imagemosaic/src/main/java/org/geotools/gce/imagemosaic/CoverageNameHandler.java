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
package org.geotools.gce.imagemosaic;

import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.namecollector.CoverageNameCollector;
import org.geotools.gce.imagemosaic.namecollector.CoverageNameCollectorSPI;
import org.geotools.gce.imagemosaic.namecollector.CoverageNameCollectorSpiFinder;

/**
 * Class delegated to parse coverageNameCollectors property and setup proper {@link
 * CoverageNameCollector} machinery to be used to return a target coverageName.
 */
class CoverageNameHandler {

    private CoverageNameCollectorSPI spi;

    private Map<String, String> properties;

    CoverageNameHandler(CoverageNameCollectorSPI spi) {
        this.spi = spi;
    }

    CoverageNameHandler(String coverageNameCollectors) {
        if (coverageNameCollectors != null && coverageNameCollectors.length() > 0) {
            if (coverageNameCollectors.contains(":")) {
                int indexOf = coverageNameCollectors.indexOf(":");
                String properties = coverageNameCollectors.substring(indexOf + 1);
                coverageNameCollectors = coverageNameCollectors.substring(0, indexOf);
                initializeProperties(properties);
            }

            CoverageNameCollectorSPI spi =
                    CoverageNameCollectorSpiFinder.getCoverageNameCollectorSPI()
                            .get(coverageNameCollectors);
            if (spi != null) {
                this.spi = spi;
            }
        }
    }

    CoverageNameHandler(CoverageNameCollectorSPI spi, Map<String, String> properties) {
        this.spi = spi;
        this.properties = properties;
    }

    private void initializeProperties(String propertiesString) {
        if (propertiesString != null && !propertiesString.trim().isEmpty()) {
            String[] propertiesKVP = propertiesString.split(Utils.PROPERTIES_SEPARATOR);
            properties = new HashMap<String, String>();
            for (String property : propertiesKVP) {
                String[] kvp = property.split("=");
                properties.put(kvp[0], kvp[1]);
            }
        }
    }

    String getTargetCoverageName(
            GridCoverage2DReader inputCoverageReader, Map<String, String> map) {
        CoverageNameCollector collector = spi.create(inputCoverageReader, properties);
        return collector.getName(inputCoverageReader, map);
    }
}
