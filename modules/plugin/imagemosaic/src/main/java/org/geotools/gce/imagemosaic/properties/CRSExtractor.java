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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Extract the CRS from the coverage in order to store in the index */
public class CRSExtractor extends PropertiesCollector {

    public static final String DEFAULT_ATTRIBUTE_NAME = "crs";

    private String crsCode;

    public CRSExtractor() {
        this(new CRSExtractorSPI(), Collections.singletonList(DEFAULT_ATTRIBUTE_NAME));
    }

    public CRSExtractor(PropertiesCollectorSPI spi, List<String> propertyNames) {
        super(spi, propertyNames);
    }

    @Override
    public void setProperties(SimpleFeature feature) {
        feature.setAttribute(this.getPropertyNames().get(0), this.crsCode);
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        getPropertyNames().forEach(propName -> map.put(propName, this.crsCode));
    }

    @Override
    public PropertiesCollector collect(GridCoverage2DReader gridCoverageReader) {

        CoordinateReferenceSystem crs = gridCoverageReader.getCoordinateReferenceSystem();
        crsCode = CRS.toSRS(crs, false);

        return this;
    }
}
