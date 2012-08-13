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
package org.geotools.process.geometry.gs;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Will reproject a geometry to another CRS. 
 * 
 * @author Andrea Aime
 *
 * @source $URL$
 */
@DescribeProcess(title = "Reproject Geometry", description = "Reprojects a given geometry into a supplied coordinate reference system.")
public class ReprojectGeometry implements GSProcess {

    @DescribeResult(name = "result", description = "Reprojected geometry")
    public Geometry execute(
            @DescribeParameter(name = "geometry", description = "Input geometry") Geometry geometry,
            @DescribeParameter(name = "sourceCRS", min = 0, description = "Coordinate reference system of input geometry") CoordinateReferenceSystem sourceCRS,
            @DescribeParameter(name = "targetCRS", min = 0, description = "Target coordinate reference system to use for reprojection") CoordinateReferenceSystem targetCRS)
            throws Exception {

        return JTS.transform(geometry, CRS.findMathTransform(sourceCRS, targetCRS, true));
    }

}
