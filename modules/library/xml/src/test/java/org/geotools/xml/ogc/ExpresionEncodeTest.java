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
package org.geotools.xml.ogc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Expression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilter;
import org.geotools.xml.DocumentWriter;
import org.geotools.xml.filter.FilterSchema;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 *  For now just writes the expression built.
 *  
 * @author David Zwiers, Refractions Research
 * @source $URL$
 */
public class ExpresionEncodeTest extends TestCase {
    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter");
    /** Constructor with test name. */
    String dataFolder = "";

    public ExpresionEncodeTest(String testName) {
        super(testName);

        //_log.getLoggerRepository().setThreshold(Level.DEBUG);
        LOGGER.finer("running XMLEncoderTests");
        
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            //then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + "tests/unit/testData"; //url.toString();
            LOGGER.finer("data folder is " + dataFolder);
        }
    }
    
    public void testPropBetweenFilter() throws IllegalFilterException, OperationNotSupportedException, IOException{
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter bf = ff.between( ff.property("testDouble"), ff.literal(60000),ff.literal(200000));
        
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(bf,
            FilterSchema.getInstance(), output, null);
        
//        System.out.println(output);
    }
    
    public void testLikeFilter() throws IllegalFilterException, OperationNotSupportedException, IOException{
        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        Expression testAttribute = ff.createAttributeExpression("testString");

        LikeFilter lf = ff.createLikeFilter();
        lf.setValue(testAttribute);
        lf.setPattern(ff.createLiteralExpression("test*"), "*", ".", "!");

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(lf,
            FilterSchema.getInstance(), output, null);
        
        //System.out.println(output);
    }
    
    public void testFidFilter() throws OperationNotSupportedException, IOException{
        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        FidFilter fif = ff.createFidFilter();
        fif.addFid("f1");
        fif.addFid("f2");
        fif.addFid("f3");
        fif.addFid("f4");

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(fif,
            FilterSchema.getInstance(), output, null);
        
        //System.out.println(output);
    }
}
