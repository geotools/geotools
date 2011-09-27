/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.v3_2.bindings;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 *
 * @source $URL$
 */
public class LinearRingTypeBinding extends org.geotools.gml3.bindings.LinearRingTypeBinding {

    public LinearRingTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory) {
        super(gFactory, csFactory);
    }

    @Override
    /**
     * In gml 3.2 LinearRing does not extend from AbstractGeometryType... so we change BEFORE to 
     * OVERRIDE
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

}
