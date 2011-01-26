/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties.numeric;

import java.util.List;

import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;

public final class LongFileNameExtractorSPI extends NumericFileNameExtractorSPI implements PropertiesCollectorSPI{

    public LongFileNameExtractorSPI() {
        super("LongFileNameExtractorSPI");
    }

    @Override
    protected PropertiesCollector createInternal(
            NumericFileNameExtractorSPI spi,
            List<String> propertyNames, String regex) {
        return new NumericFileNameExtractor.LongFileNameExtractor(spi, propertyNames, regex);
    }
    
}