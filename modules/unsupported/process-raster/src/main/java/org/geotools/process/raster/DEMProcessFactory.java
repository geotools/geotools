/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.util.SimpleInternationalString;

/**
 * ProcessFactory for several digital elevation model processes.
 * 
 * @since 2.7
 * @version 8.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/process/src/main/java/org/geotools/process/dem/DEMProcessFactory.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/process/src/main/java/org/geotools
 *         /process/raster/RasterToVectorFactory.java $
 */
public class DEMProcessFactory extends StaticMethodsProcessFactory<DEMTools> {
    public DEMProcessFactory() {
        super(new SimpleInternationalString("Digitial Elevation Model"), "http://localhost/dem/",
                DEMTools.class);
    }

}
