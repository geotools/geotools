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

/**
 * Used by Categorize function.<br>
 * Whether the Threshold values themselves belong to the preceding or the succeeding interval can be
 * controlled by the attribute thresholdsBelongTo= with the possible values "preceding" and
 * "succeeding" the latter being the default.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Johann Sorel (Geomatys)
 */
@XmlElement("ThreshholdsBelongToType")
public enum ThreshholdsBelongTo {
    SUCCEEDING,
    PRECEDING
}
