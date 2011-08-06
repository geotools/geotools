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
package org.geotools.process.feature.gs;

import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

/**
 * Simple process with a {@link ReferencedEnvelope} as the output
 * 
 * @author Andrea Aime
 */
@DescribeProcess(title = "bounds", description = "Computes the overlall bounds of the input features")
public class BoundsProcess implements GSProcess {

    @DescribeResult(name = "bounds", description = "The feature collection bounds")
    public ReferencedEnvelope execute(
            @DescribeParameter(name = "features", description = "The feature collection whose bounds will be computed") FeatureCollection features) {
        return features.getBounds();
    }

}
