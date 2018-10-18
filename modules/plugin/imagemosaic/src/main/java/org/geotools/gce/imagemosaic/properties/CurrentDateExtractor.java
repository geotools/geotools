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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.opengis.feature.simple.SimpleFeature;

/** Simple {@link PropertiesCollector} */
class CurrentDateExtractor extends PropertiesCollector {

    private Date date = null;

    public CurrentDateExtractor(PropertiesCollectorSPI spi, List<String> propertyNames) {
        super(spi, propertyNames);
    }

    @Override
    public PropertiesCollector collect(final File file) {
        date = new Date();
        return this;
    }

    @Override
    public void setProperties(SimpleFeature feature) {
        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                feature.setAttribute(propertyName, date);
            }
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                map.put(propertyName, date);
            }
        }
    }
}
