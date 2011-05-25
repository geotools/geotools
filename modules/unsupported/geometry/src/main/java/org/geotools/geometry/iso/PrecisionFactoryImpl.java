/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso;

import org.opengis.geometry.Precision;
import org.opengis.geometry.PrecisionFactory;
import org.opengis.geometry.PrecisionType;

/**
 * Implementation set up to create PrecisionModel
 *
 *
 * @source $URL$
 */
public class PrecisionFactoryImpl implements PrecisionFactory {

    public Precision createFixedPrecision( PrecisionType code, double scale ) {
        if( code == PrecisionType.FLOAT){
            return new PrecisionModel( scale  );    
        }
        return new PrecisionModel( code );
    }

}
