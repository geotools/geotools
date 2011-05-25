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
package org.geotools.gml3.bindings;

import com.vividsolutions.jts.geom.LineString;
import org.geotools.gml3.GML3TestSupport;

/**
 *
 * @author Erik van de Pol
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-gml3/src/test/java/org/geotools/gml3/bindings/ArcTypeBindingTest.java $
 */
public class ArcTypeBindingTest extends GML3TestSupport {
    
    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true; 
    }
    
    public void testParse() throws Exception {
        GML3MockData.arcWithPosList(document, document);
        LineString lineString = (LineString) parse();

        assertEquals(65, lineString.getNumPoints());
    }

}
