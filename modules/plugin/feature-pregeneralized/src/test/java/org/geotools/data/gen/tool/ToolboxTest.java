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

package org.geotools.data.gen.tool;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Assert;

/** @source $URL$ */
public class ToolboxTest extends TestCase {

    public void testValidate() {
        Toolbox tb = new Toolbox();
        try {
            tb.parse(new String[] {"validate", "src/test/resources/geninfo_vertical.xml"});
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
