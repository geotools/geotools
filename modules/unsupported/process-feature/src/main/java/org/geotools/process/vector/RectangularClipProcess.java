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
package org.geotools.process.vector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * A process clipping the geometries in the input feature collection to a specified rectangle
 *
 * @author Andrea Aime - OpenGeo
 */
@DescribeProcess(
        title = "Rectangular Clip",
        description = "Clips (crops) features to the specified rectangular extent")
public class RectangularClipProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "Clipped feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(name = "clip", description = "Bounds of clipping rectangle")
                    ReferencedEnvelope clip,
            @DescribeParameter(
                            name = "preserveZ",
                            min = 0,
                            description =
                                    "Attempt to preserve Z values from the original geometry (interpolate value for new points)")
                    Boolean preserveZ)
            throws ProcessException {
        CoordinateReferenceSystem featuresCRS = features.getSchema().getCoordinateReferenceSystem();
        if (featuresCRS != null
                && clip.getCoordinateReferenceSystem() != null
                && !CRS.equalsIgnoreMetadata(featuresCRS, clip.getCoordinateReferenceSystem())) {
            boolean lenient = true;
            try {
                clip = clip.transform(featuresCRS, lenient);
            } catch (TransformException | FactoryException e) {
                throw new ProcessException(e);
            }
        }
        return new ClipProcess().execute(features, JTS.toGeometry(clip), preserveZ);
    }
}
