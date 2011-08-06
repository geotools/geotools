/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.factory;

import org.geotools.process.gs.GSProcess;

/**
 * Simple process used to verify AnnotatedBeanProcessFactory is working.
 * 
 * @author Jody Garnett (LISAsoft)
 */
@DescribeProcess(title = "Identity", description = "identiy process used for tesing")
public class IdentityProcess implements GSProcess {

    @DescribeResult(name = "value", description = "the value provided as input")
    public Object execute(
            @DescribeParameter(name = "input", description = "input object") Object input) {
        return input;
    }

}
