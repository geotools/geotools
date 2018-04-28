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
package org.geotools.gce.imagemosaic.namecollector;

import java.util.Map;

/**
 * CoverageNameCollector SPI interface. Allows to instantiate {@link CoverageNameCollector}s based
 * on optional input source object and customization properties.
 *
 * <p>Implementations are pluggable. In addition to implementing this interface, this service file
 * should be defined:
 *
 * <p><code>META-INF/services/org.geotools.gce.imagemosaic.namecollector.CoverageNameCollectorSPI
 * </code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>example:<br>
 * <code>e.g.
 * org.geotools.gce.imagemosaic.namecollector.DefaultCoverageNameCollectorSPI</code>
 */
public interface CoverageNameCollectorSPI {

    /** Create a {@link CoverageNameCollector} instance */
    public CoverageNameCollector create(Object object, Map<String, String> properties);
}
