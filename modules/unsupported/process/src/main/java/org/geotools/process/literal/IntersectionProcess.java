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
package org.geotools.process.literal;


import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.SimpleProcess;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Process to intersect 2 geometries.
 * <p>
 * We are treating this as a SimpleProcess (intersection is so quick it is hard
 * to report progress).
 * 
 * @author gdavis
 */
class IntersectionProcess extends SimpleProcess {
    public IntersectionProcess( IntersectionFactory intersectsFactory ) {
        super( intersectsFactory );
    }

    public ProcessFactory getFactory() {
        return factory;
    }

    public void process() {
	    Geometry geom1 = (Geometry) get( IntersectionFactory.GEOM1.key );
        Geometry geom2 = (Geometry) get( IntersectionFactory.GEOM2.key );
        
        result.put( IntersectionFactory.RESULT.key, geom1.intersection( geom2 ) );
    }    

}
