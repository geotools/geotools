/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.opengis.feature.simple.SimpleFeature;

/** @source $URL$ */
public abstract class PropertiesCollector {

    private List<String> propertyNames;

    private PropertiesCollectorSPI spi;

    private List<String> matches = new ArrayList<String>();

    public PropertiesCollector(final PropertiesCollectorSPI spi, final List<String> propertyNames) {
        this.spi = spi;
        this.propertyNames = new ArrayList<String>(propertyNames);
    }

    public PropertiesCollectorSPI getSpi() {
        return spi;
    }

    public PropertiesCollector collect(final File file) {
        return this;
    }

    public PropertiesCollector collect(final ImageReader imageReader) {
        return this;
    }

    public PropertiesCollector collect(final GridCoverage2DReader gridCoverageReader) {
        return this;
    }

    public abstract void setProperties(final SimpleFeature feature);

    public abstract void setProperties(final Map<String, Object> map);

    public void reset() {
        matches = new ArrayList<String>();
    }

    public List<String> getPropertyNames() {
        return Collections.unmodifiableList(propertyNames);
    }

    protected void addMatch(String match) {
        matches.add(match);
    }

    protected List<String> getMatches() {
        return Collections.unmodifiableList(matches);
    }
}
