/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.text.Text;

public class RasterProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<RasterProcess> registry;

    public static BeanFactoryRegistry<RasterProcess> getRegistry() {
        if (registry == null) {
            synchronized (RasterProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<RasterProcess>(RasterProcess.class);
                }
            }
        }
        return registry;
    }

    public RasterProcessFactory() {
        super(Text.text("Raster processes"), "ras", getRegistry().lookupBeanClasses());
    }
}
