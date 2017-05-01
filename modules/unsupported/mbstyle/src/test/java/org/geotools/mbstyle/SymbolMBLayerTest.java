/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle;

import static org.junit.Assert.*;

import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.junit.Test;

public class SymbolMBLayerTest {

    @Test
    public void testTextAnchorEnum(){
        // cannot use valueOf directly
        assertEquals( TextAnchor.CENTER, TextAnchor.parse("center"));
        assertEquals( TextAnchor.BOTTOM_LEFT, TextAnchor.parse("bottom-left"));
        
        // json
        assertEquals( "bottom-left", TextAnchor.BOTTOM_LEFT.json() );
        
        // justification
        assertEquals( 0.5, TextAnchor.getAnchorX("center"), 0.0 );
        assertEquals( 0.5, TextAnchor.getAnchorY("center"), 0.0 );
    }
}
