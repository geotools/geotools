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
 * @source $URL:
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

    /**
     * Looks up for attribute mappings matching the xpath expression <code>propertyName</code>.
     * <p>
     * If any step in <code>propertyName</code> has index greater than 1, any mapping for the same
     * property applies, regardless of the mapping. For example, if there are mappings for
     * <code>gml:name[1]</code>, <code>gml:name[2]</code> and <code>gml:name[3]</code>, but
     * propertyName is just <code>gml:name</code>, all three mappings apply.
     * </p>
     * 
     * @param mappings
     *            Feature type mapping to search for
     * @param simplifiedSteps
     * @return
     */
    @Override
    protected List<Expression> findMappingsFor(FeatureTypeMapping mappings,
            final StepList propertyName) {
        XmlFeatureTypeMapping xmlMapping = (XmlFeatureTypeMapping) mappings;
        // collect all the mappings for the given property
        List<String> candidates;

        // get all matching mappings if index is not specified, otherwise
        // get the specified mapping
        if (!propertyName.toString().contains("[")) {
            candidates = xmlMapping.getStringMappingsIgnoreIndex(propertyName);
        } else {
            candidates = new ArrayList<String>();
            String mapping = xmlMapping.getStringMapping(propertyName);
            if (mapping != null) {
                candidates.add(mapping);
            }
        }
        List<Expression> expressions = getExpressions(candidates);

        return expressions;
    }

    private List<Expression> getExpressions(List<String> candidates) {
        List<Expression> ls = new ArrayList<Expression>(candidates.size());
        Iterator<String> itr = candidates.iterator();
        while (itr.hasNext()) {
            String element = itr.next();
            Expression ex = ff.property(element);
            ls.add(ex);
        }
        return ls;
    }
    
}
