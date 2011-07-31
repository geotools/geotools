/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.process.feature.gs;

import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GeoServerProcess;

/**
 * Simple process with a {@link ReferencedEnvelope} as the output
 * 
 * @author Andrea Aime
 */
@DescribeProcess(title = "bounds", description = "Computes the overlall bounds of the input features")
public class BoundsProcess implements GeoServerProcess {

    @DescribeResult(name = "bounds", description = "The feature collection bounds")
    public ReferencedEnvelope execute(
            @DescribeParameter(name = "features", description = "The feature collection whose bounds will be computed") FeatureCollection features) {
        return features.getBounds();
    }

}
