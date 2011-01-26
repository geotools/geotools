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
 * Recoding: Transformation of discrete values to any other values. This is needed
 * when integers have to be translated into text or, reversely, text contents into other
 * texts or numeric values or colors.
 *
 * This function recodes values from a property or expression into corresponding values of
 * arbitrary type. The comparisons are performed checking for identical values.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Johann Sorel (Geomatys)
 */
@XmlElement("Recode")
public interface Recode extends Function{

    /**
     * Get lookup value.
     *
     * @return Expression
     */
    @XmlElement("LookupValue")
    Expression getLookupValue();

    /**
     * See {@link MapItem} for details.
     */
    List<MapItem> getMapItems();

}