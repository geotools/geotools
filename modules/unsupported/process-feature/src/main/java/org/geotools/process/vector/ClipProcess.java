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
package org.geotools.process.vector;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.ClippedFeatureCollection;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

/**
 * Modified version that can preserve Z values after the clip
 *
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(title = "Clip", description = "Clips (crops) features to a given geometry")
public class ClipProcess implements VectorProcess {

    static final FilterFactory ff =
            CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

    @DescribeResult(name = "result", description = "Clipped feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "clip",
                            description =
                                    "Geometry to use for clipping (in same CRS as input features)")
                    Geometry clip,
            @DescribeParameter(
                            name = "preserveZ",
                            min = 0,
                            description =
                                    "Attempt to preserve Z values from the original geometry (interpolate value for new points)")
                    Boolean preserveZ)
            throws ProcessException {
        // only get the geometries in the bbox of the clip
        Envelope box = clip.getEnvelopeInternal();
        String srs = null;
        if (features.getSchema().getCoordinateReferenceSystem() != null) {
            srs = CRS.toSRS(features.getSchema().getCoordinateReferenceSystem());
        }
        BBOX bboxFilter =
                ff.bbox("", box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(), srs);

        // default value for preserve Z
        if (preserveZ == null) {
            preserveZ = false;
        }

        // return dynamic collection clipping geometries on the fly
        return new ClippedFeatureCollection(features.subCollection(bboxFilter), clip, preserveZ);
    }
}
