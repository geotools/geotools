/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.utilities;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.opengis.feature.simple.SimpleFeature;

/**
 * {@link PropertiesCollector} that is able to collect properties from a file name.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class RuntimeExtractor extends PropertiesCollector {
    Date date;

    enum RuntimeType {
        MODIFY_TIME
    }

    String type = null;

    public RuntimeExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String type) {
        super(spi, propertyNames);
        this.type = type;
    }

    @Override
    public void setProperties(SimpleFeature feature) {

        for (String propertyName : getPropertyNames()) {
            // set the property
            feature.setAttribute(propertyName, date);
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuntimeExtractor collect(File file) {
        super.collect(file);

        // get name of the file
        if (type.equalsIgnoreCase(RuntimeType.MODIFY_TIME.toString())) {

            // TODO Need to take into account locale?
            date = new Date(file.lastModified());
        }
        // final String name = FilenameUtils.getBaseName(file.getAbsolutePath());

        return this;
    }
}
