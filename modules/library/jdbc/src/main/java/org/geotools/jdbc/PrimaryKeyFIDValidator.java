/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.filter.visitor.SimplifyingFilterVisitor.FIDValidator;

/**
 * Fid validator which validates with respect to a primary key.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public class PrimaryKeyFIDValidator implements FIDValidator {

    JDBCFeatureSource featureSource;
    
    public PrimaryKeyFIDValidator( JDBCFeatureSource featureSource ) {
        this.featureSource = featureSource;
    }
    public boolean isValid(String fid) {
        PrimaryKey key = featureSource.getPrimaryKey();
        try {
            featureSource.getDataStore().decodeFID( key, fid, true );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

}
