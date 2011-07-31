/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
/**
 * Gathers up raster for the "gs" GeoSpatial factory; most of these have
 * been back ported from GeoServer for wider use. The "gs" namespace allows
 * us to preserve backwards compatibility.
 * <p>
 * It looks as if many of these are simple wrappers around jai-tools work.
 * <p>
 * This uses a "marker" interface that is expected to be used in Spring
 * in order to round up all the implementations.
 * <p>
 * Code example:<pre>
 import org.geotools.process.factory.DescribeParameter;
 import org.geotools.process.factory.DescribeProcess;
 import org.geotools.process.factory.DescribeResult;
 import org.geotools.process.gs.GeoServerProcess;
 </pre>
 * @author Jody Garnett - LISAsoft
 * @author Andrea Aime - OpenGeo
 */
package org.geotools.process.raster.gs;

