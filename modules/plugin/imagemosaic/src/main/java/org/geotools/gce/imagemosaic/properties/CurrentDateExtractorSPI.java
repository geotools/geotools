/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Factory for a properties extractor collecting the current date */
public class CurrentDateExtractorSPI implements PropertiesCollectorSPI {

    @Override
    public String getName() {
        return "CurrentDateExtractorSPI";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public PropertiesCollector create(final Object o, final List<String> propertyNames) {
        return new CurrentDateExtractor(this, propertyNames);
    }
}
