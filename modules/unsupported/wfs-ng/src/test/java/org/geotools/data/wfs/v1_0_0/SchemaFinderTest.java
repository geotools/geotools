/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import junit.framework.TestCase;

import org.geotools.xml.SchemaFactory;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.wfs.WFSSchema;

/**
 * <p> 
 * DOCUMENT ME!
 * </p>
 * @author dzwiers
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_0_0/SchemaFinderTest.java $
 */
public class SchemaFinderTest extends TestCase {
    public void testFinder(){
        assertNotNull(SchemaFactory.getInstance(GMLSchema.NAMESPACE));
        assertNotNull(SchemaFactory.getInstance(WFSSchema.NAMESPACE));
        assertNotNull(SchemaFactory.getInstance(FilterSchema.NAMESPACE));
    }
}
