/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the DialogUtils class which don't require a graphics environment.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class DialogUtilsHeadlessTest {
    
    @Test
    public void getStringReturnsNonEmptyInput() {
        assertEquals("Foo", DialogUtils.getString("Foo", "Bar"));
    }
    
    @Test
    public void getStringReturnsFallbackWhenInputNull() {
        assertEquals("Bar", DialogUtils.getString(null, "Bar"));
    }
    
    @Test
    public void getStringReturnsFallbackWhenInputEmpty() {
        assertEquals("Bar", DialogUtils.getString("", "Bar"));
    }
    
    @Test
    public void getStringReturnsFallbackWhenInputAllSpaces() {
        assertEquals("Bar", DialogUtils.getString("     ", "Bar"));
    }
    
    @Test
    public void nullFallbackIsAllowed() {
        assertNull(DialogUtils.getString(null, null));
    }
    
}
