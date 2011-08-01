/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
