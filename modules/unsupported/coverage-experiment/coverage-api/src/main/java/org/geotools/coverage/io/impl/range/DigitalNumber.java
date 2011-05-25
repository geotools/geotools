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
package org.geotools.coverage.io.impl.range;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Quantity;

/**
 * Stub implementation of a {@link Dimensionless} {@link Quantity} which can be
 * used to represent digital numbers as usde in simple image processing or like
 * with the numbers we get from remote sensing image prior to going back to
 * radiance or reflectance.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/impl/range/DigitalNumber.java $
 */
public class DigitalNumber implements Quantity, Dimensionless {

}
