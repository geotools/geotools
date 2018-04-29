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

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** @author Niels Charlier */
public class ResolutionExtractorSPI implements PropertiesCollectorSPI {

    public String getName() {
        return getClass().getSimpleName();
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    public ResolutionExtractor create(final Object o, final List<String> propertyNames) {
        return new ResolutionExtractor(this, propertyNames, ResolutionExtractor.Axis.BOTH);
    }
}
