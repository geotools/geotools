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
package org.geotools.referencing.factory.epsg;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.Hints;
import org.geotools.referencing.factory.epsg.oracle.OracleOnlineTestCase;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class OracleDialectEpsgMediatorOnlineTest extends OracleOnlineTestCase {

    private OracleDialectEpsgMediator mediator;
    
    protected void connect() throws Exception {
        super.connect();
        Map config = new HashMap();
        config.put( Hints.CACHE_POLICY,"none");
        //config.put( Hints.EPSG_DATA_SOURCE, datasource );
        config.put( Hints.EPSG_DATA_SOURCE, "jdbc/EPSG" );
        Hints hints = new Hints( config );
        mediator = new OracleDialectEpsgMediator( hints );
        
        //mediator = new OracleDialectEpsgMediator(80, hints, datasource);
    }
    
    public void testCreation() throws Exception {
        assertNotNull(mediator);
        CoordinateReferenceSystem epsg4326 = mediator.createCoordinateReferenceSystem("EPSG:4326");
        CoordinateReferenceSystem code4326 = mediator.createCoordinateReferenceSystem("4326");
        
        assertNotNull(epsg4326);
        assertEquals("4326 equals EPSG:4326", code4326, epsg4326);
        assertSame("4326 == EPSG:4326", code4326, epsg4326);       
    }

}
