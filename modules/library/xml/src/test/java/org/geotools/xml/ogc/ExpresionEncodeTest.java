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
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.xml.DocumentWriter;
import org.geotools.xml.filter.FilterSchema;
import org.junit.Test;

/**
 * For now just writes the expression built.
 *
 * @author David Zwiers, Refractions Research
 */
public class ExpresionEncodeTest {
    /** Standard logging instance */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ExpresionEncodeTest.class);
    /** Constructor with test name. */
    String dataFolder = "";

    public ExpresionEncodeTest() {
        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
        LOGGER.finer("running XMLEncoderTests");

        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            // then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + "tests/unit/testData"; // url.toString();
            LOGGER.finer("data folder is " + dataFolder);
        }
    }

    @Test
    public void testPropBetweenFilter()
            throws IllegalFilterException, OperationNotSupportedException, IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter bf = ff.between(ff.property("testDouble"), ff.literal(60000), ff.literal(200000));

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(bf, FilterSchema.getInstance(), output, null);

        //        System.out.println(output);
    }

    @Test
    public void testLikeFilter()
            throws IllegalFilterException, OperationNotSupportedException, IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        PropertyIsLike lf = ff.like(ff.property("testString"), "test*", "*", ".", "!");

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(lf, FilterSchema.getInstance(), output, null);

        // System.out.println(output);
    }

    @Test
    public void testFidFilter() throws OperationNotSupportedException, IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Id fif =
                ff.id(
                        ff.featureId("f1"),
                        ff.featureId("f2"),
                        ff.featureId("f3"),
                        ff.featureId("f4"));

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(fif, FilterSchema.getInstance(), output, null);

        // System.out.println(output);
    }
}
