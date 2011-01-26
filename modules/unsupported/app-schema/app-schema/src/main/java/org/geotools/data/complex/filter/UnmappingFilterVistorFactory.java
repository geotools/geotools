/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.filter;

import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.XmlFeatureTypeMapping;

/**
 * @author Russell Petty, GSV
 * @version $Id$
 * @source $URL$
 */
public class UnmappingFilterVistorFactory {
    public static UnmappingFilterVisitor getInstance(FeatureTypeMapping mapping) {
        if (mapping instanceof XmlFeatureTypeMapping) {
            return new XmlUnmappingFilterVisitor(mapping);
        }
        return new UnmappingFilterVisitor(mapping);
    }
}
