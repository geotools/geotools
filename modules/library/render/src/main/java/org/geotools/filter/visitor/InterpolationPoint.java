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
package org.geotools.filter.visitor;

import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * The InterpolationPoints have to be specified in ascending order of Data. They define a
 * graph of points. LookupValues less than the Data value of the first InterpolationPoint
 * are mapped to its corresponding Value. Accordingly, LookupValues greater than the
 * Data value of the last InterpolationPoint are mapped to the Value of this one.
 * LookupValues between two InterpolationPoints are interpolated between the
 * corresponding Values.
 *
 * Only numeric quantities are allowed for LookupValue and Data. Values are usually
 * numeric as well. The interpolation of color-values requires the attribute mode="color" at
 * the Interpolate element.s
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Johann Sorel (Geomatys)
 */
@XmlElement("InterpolationPoint")
public interface InterpolationPoint {

    /**
     * Get value.
     *
     * The Values can be of any type, dependent on which symbolization context the function is
     * employed. Color values (like #00ffff) or numeric values are typical.
     *
     * @return Expression
     */
    @XmlElement("Value")
    Expression getValue();

    /**
     * Get data.
     *
     * @return double
     */
    @XmlElement("Data")
    double getData();

}
