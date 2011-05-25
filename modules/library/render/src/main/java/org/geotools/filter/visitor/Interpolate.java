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

import java.util.List;

import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;


/**
 * Interpolation: Transformation of continuous values by a function defined on a
 * number of nodes. This is used to adjust the value distribution of an attribute to the
 * desired distribution of a continuous symbolization control variable (like size,
 * width, color, etc).
 *
 * In case the Categorize (or Interpolate) function is used inside a RasterSymbolizer as a
 * ColorMap, the LookupValue is set to the fixed value “Rasterdata”.
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Johann Sorel (Geomatys)
 */
@XmlElement("Interpolate")
public interface Interpolate extends Function{

    /**
     * Get lookup value.
     *
     * @return Expression
     */
    @XmlElement("LookupValue")
    Expression getLookupValue();

    /**
     * See {@link InterpolationPoint} for details.
     */
    List<InterpolationPoint> getInterpolationPoints();

    /**
     * Get the interpolation mode.
     *
     * @return LINEAR, COSINE or CUBIC.
     */
    @XmlElement("Mode")
    Mode getMode();

    /**
     * Get the interpolation method.
     *
     * @return NUMERIC or COLOR
     */
    @XmlElement("Method")
    Method getMethod();

}
