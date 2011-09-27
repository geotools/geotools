/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.string;

import java.util.Arrays;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.opengis.filter.FilterFactory;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class StringInFunctionTest extends TestCase {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    public void test() throws Exception {
        StringInFunction f = new StringInFunction();
        
        List params = Arrays.asList(ff.literal("foo"), ff.literal(true), ff.literal("foo"), 
            ff.literal("bar"), ff.literal("baz"));
        ((FunctionImpl)f).setParameters(params);
        
        assertEquals(Boolean.TRUE, f.evaluate(null));
        
        params = Arrays.asList(ff.literal("foo"), ff.literal(true), ff.literal("FOO"), 
                ff.literal("bar"), ff.literal("baz"));
        ((FunctionImpl)f).setParameters(params);
        assertEquals(Boolean.FALSE, f.evaluate(null));
    }
    
    public void testTooFewArguments() throws Exception {
        StringInFunction f = new StringInFunction();
        
        List params = Arrays.asList(ff.literal("foo"), ff.literal(true));
        ((FunctionImpl)f).setParameters(params);
        
        try {
            f.evaluate(null);
            fail();
        }
        catch(IllegalArgumentException e) {}
    }
}
