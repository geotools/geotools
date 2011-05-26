/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Logger;

import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.XmlFeatureTypeMapping;
import org.geotools.data.complex.filter.XPath.StepList;

import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * A Filter visitor that customises UnMappingFilterVisitor to map to xpath expressions.

 * 
 * @author Russell Petty, GSV
 * @version $Id$
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/filter/UnmappingFilterVisitor.java $
 * @since 2.4
 */
public class XmlUnmappingFilterVisitor extends UnmappingFilterVisitor {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(XmlUnmappingFilterVisitor.class.getPackage().getName());

    // private static final FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder
    // .getFilterFactory(null);
    // TODO: once Regfunc stuff pulled up into FilterFactoryImpl
    // the code below can be replaced by the original above.
    private static final FilterFactory2 ff = new FilterFactoryImpl(null);

    /**
     * visit(*Expression) holds the unmapped expression here. Package visible just for unit tests
     */
    public XmlUnmappingFilterVisitor(FeatureTypeMapping mappings) {
        super(mappings);
    }

    
    
}
