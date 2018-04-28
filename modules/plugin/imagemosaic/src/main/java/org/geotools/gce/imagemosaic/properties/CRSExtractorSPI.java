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

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** SPI for the CRS extractor */
public class CRSExtractorSPI implements PropertiesCollectorSPI {
    @Override
    public PropertiesCollector create(Object o, List<String> propertyNames) {
        return new CRSExtractor(this, propertyNames);
    }

    @Override
    public String getName() {
        return "CRSExtractorSPI";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
