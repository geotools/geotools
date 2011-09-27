/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

/**
 * A process clipping the geometries in the input feature collection to a specified rectangle
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 * @source $URL$
 */
@DescribeProcess(title = "rectangularClip", description = "Clips the features to the specified bounding box")
public class RectangularClipProcess implements GSProcess {

    @DescribeResult(name = "result", description = "The feature collection bounds")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "The feature collection to be simplified") SimpleFeatureCollection features,
            @DescribeParameter(name = "clip", description = "The clipping area") ReferencedEnvelope clip)
            throws ProcessException {
        return new ClipProcess().execute(features, JTS.toGeometry(clip));
    }

    
}
